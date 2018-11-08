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
import connectors.{CasConnector, DataCacheConnector}
import controllers.actions.{AuthAction, DataRequiredAction, DataRetrievalAction}
import models.templates.RobotXML
import models.{Metadata, SubmissionSuccessful, _}
import org.apache.commons.codec.digest.DigestUtils
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

import scala.concurrent.{ExecutionContext, Future}

class CheckYourAnswersController @Inject()(appConfig: FrontendAppConfig,
                                           override val messagesApi: MessagesApi,
                                           dataCacheConnector: DataCacheConnector,
                                           casConnector: CasConnector,
                                           authenticate: AuthAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           submissionService: SubmissionService,
                                           referenceGenerator: ReferenceGenerator,
                                           implicit val formPartialRetriever: FormPartialRetriever,
                                           implicit val templateRenderer: TemplateRenderer
                                          )(implicit ec: ExecutionContext) extends FrontendController with I18nSupport {

  def onPageLoad(): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val cyaHelper = new CheckYourAnswersHelper(request.userAnswers)
      val cyaSections = new CheckYourAnswersSections(cyaHelper, request.userAnswers)

      request.userAnswers.selectTaxYear.map {
        _ =>
          Ok(check_your_answers(appConfig, cyaSections.sections))
      }.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }

  def onSubmit(): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val cyaHelper: CheckYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)
      val cyaSections: CheckYourAnswersSections = new CheckYourAnswersSections(cyaHelper, request.userAnswers)
      val itmpName: ItmpName = request.name.getOrElse(ItmpName(Some("No name returned from ITMP"), None, None))
      val itmpAddress: ItmpAddress = request.address.getOrElse(ItmpAddress(Some("No address returned from ITMP"), None, None, None, None, None, None, None))
      val nino: String = request.nino

      val submissionReference = referenceGenerator.generateSubmissionNumber
      val timeStamp = LocalDateTime.now

      val pdfHtml: String = pdf_check_your_answers(
        appConfig = appConfig,
        answerSections = cyaSections.sections,
        nino = nino,
        name = itmpName,
        address = itmpAddress,
        telephone = request.userAnswers.anyTelephoneNumber,
        date = timeStamp
      ).toString

      val robotXml = new RobotXML

      val xml: String =
        robotXml.generateXml(
          userAnswers = request.userAnswers,
          submissionReference = submissionReference,
          dateCreated = timeStamp.toString("dd/MM/yyyy HH:mm:ss"),
          nino = nino,
          itmpName = itmpName,
          itmpAddress = itmpAddress
        ).toString

      val submissionMark = SubmissionMark.getSfMark(xml)

      val submissionArchiveRequest = SubmissionArchiveRequest(
        checksum = DigestUtils.sha1Hex(xml.getBytes("UTF-8")),
        submissionRef = submissionReference,
        submissionMark = submissionMark,
        submissionData = xml
      )

      val futureMetadata: Future[Metadata] =
        casConnector.archiveSubmission(submissionReference, submissionArchiveRequest).map {
          submissionResponse =>
            new Metadata(
              customerId = nino,
              submissionRef = submissionReference,
              submissionMark = submissionMark,
              timeStamp = timeStamp,
              casKey = submissionResponse.casKey
            )
        }
      
      futureMetadata.onFailure {
        case e =>
          Logger.error(s"[CasConnector][archiveSubmission][Submission Reference: $submissionReference][Submission Mark: $submissionMark] failed", e)
      }

      val futureSubmission: Future[Submission] = for {
        metadata <- futureMetadata
        _ <- dataCacheConnector.save[String](request.externalId, key = "submissionReference", submissionReference)
        _ <- dataCacheConnector.save[String](request.externalId, key = "pdf", pdfHtml)
        _ <- dataCacheConnector.save[String](request.externalId, key = "xml", xml)
        _ <- dataCacheConnector.save[String](request.externalId, key = "metadata", Metadata.toXml(metadata).toString)
      } yield new Submission(pdfHtml, Metadata.toXml(metadata).toString, xml)

      futureSubmission.onFailure {
        case e =>
          Logger.error("[CheckYourAnswersController][onSubmit] failed", e)
      }

      futureSubmission.flatMap {
        submission =>
          submissionService.ctrSubmission(submission) map {
            case SubmissionSuccessful =>
              Redirect(routes.ConfirmationController.onPageLoad())
            case _ =>
              throw new Exception("[Check your answers][Submission failed]")
          }
      }
  }
}

