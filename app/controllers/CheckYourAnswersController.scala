/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import com.google.inject.Inject
import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions.{AuthAction, DataRequiredAction, DataRetrievalAction}
import models.templates.RobotXML
import models.{Metadata, SubmissionSuccessful, _}
import org.joda.time.LocalDateTime
import play.api.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import services.SubmissionService
import uk.gov.hmrc.auth.core.retrieve.{ItmpAddress, ItmpName}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import uk.gov.hmrc.play.partials.FormPartialRetriever
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.{CheckYourAnswersHelper, CheckYourAnswersSections, ReferenceGenerator, SubmissionMark}
import views.html.{check_your_answers, pdf_check_your_answers}

import scala.concurrent.Future

class CheckYourAnswersController @Inject()(appConfig: FrontendAppConfig,
                                           override val messagesApi: MessagesApi,
                                           dataCacheConnector: DataCacheConnector,
                                           authenticate: AuthAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           submissionService: SubmissionService,
                                           referenceGenerator: ReferenceGenerator,
                                           implicit val formPartialRetriever: FormPartialRetriever,
                                           implicit val templateRenderer: TemplateRenderer
                                          ) extends FrontendController with I18nSupport {

  def onPageLoad(): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val cyaHelper = new CheckYourAnswersHelper(request.userAnswers)
      val cyaSections = new CheckYourAnswersSections(cyaHelper, request.userAnswers)
      Ok(check_your_answers(appConfig, cyaSections.sections))
  }

  def onSubmit(): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val cyaHelper: CheckYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)
      val cyaSections: CheckYourAnswersSections = new CheckYourAnswersSections(cyaHelper, request.userAnswers)
      val itmpName: ItmpName = request.name.getOrElse(ItmpName(Some("No name returned from ITMP"), None, None))
      val itmpAddress: ItmpAddress = request.address.getOrElse(ItmpAddress(Some("No address returned from ITMP"), None, None, None, None, None, None, None))
      val nino: String = request.nino

      val pdfHtml: String = pdf_check_your_answers(
        appConfig = appConfig,
        answerSections = cyaSections.sections,
        nino = nino,
        name = itmpName,
        address = itmpAddress,
        telephone = request.userAnswers.anyTelephoneNumber
      ).toString.replaceAll("\t|\n", "")

      val submissionReference = referenceGenerator.generateSubmissionNumber
      val timeStamp = LocalDateTime.now

      val robotXml = new RobotXML
      val xml: String = robotXml.generateXml(request.userAnswers, submissionReference, timeStamp.toString("ssMMyyddmmHH"), nino, itmpName, itmpAddress).toString

      val submissionMark = SubmissionMark.getSfMark(xml)
      val metadata: Metadata = new Metadata(nino, submissionReference, submissionMark, timeStamp)

      val futureSubmission: Future[Submission] = for {
        _ <- dataCacheConnector.save[String](request.externalId, key = "pdf", pdfHtml)
        _ <- dataCacheConnector.save[String](request.externalId, key = "xml", xml)
        _ <- dataCacheConnector.save[String](request.externalId, key = "metadata", Metadata.toXml(metadata).toString)
      } yield new Submission(pdfHtml, Metadata.toXml(metadata).toString, xml)

      futureSubmission.onFailure {
        case e =>
          Logger.error("[CheckYourAnswersController][onSubmit] failed", e)
      }

      println(s"\n\n\n\n\n\n ${Metadata.toXml(metadata)} \n\n\n\n")

      futureSubmission.flatMap {
        submission =>
          submissionService.ctrSubmission(submission) map {
            case SubmissionSuccessful => Redirect(routes.ConfirmationController.onPageLoad(metadata.submissionRef))
            case _ => throw new Exception("[Check your answers][Submission failed]")
          }
      }
  }
}

