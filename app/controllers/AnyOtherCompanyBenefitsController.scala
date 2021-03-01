/*
 * Copyright 2021 HM Revenue & Customs
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

import config.{FrontendAppConfig, LocalTemplateRenderer}
import connectors.DataCacheConnector
import controllers.actions._
import forms.BooleanForm
import identifiers.AnyOtherCompanyBenefitsId
import javax.inject.Inject
import models._
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Result}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.play.partials.FormPartialRetriever
import utils.{CheckYourAnswersHelper, CheckYourAnswersSections, Navigator, UserAnswers}
import viewmodels.AnswerSection
import views.html.anyOtherCompanyBenefits

import scala.concurrent.{ExecutionContext, Future}

class AnyOtherCompanyBenefitsController @Inject()(appConfig: FrontendAppConfig,
                                                  override val messagesApi: MessagesApi,
                                                  dataCacheConnector: DataCacheConnector,
                                                  navigator: Navigator,
                                                  authenticate: AuthAction,
                                                  getData: DataRetrievalAction,
                                                  requireData: DataRequiredAction,
                                                  anyOtherCompanyBenefits: anyOtherCompanyBenefits,
cc: MessagesControllerComponents,
                                                  formProvider: BooleanForm,
                                                  implicit val formPartialRetriever: FormPartialRetriever,
                                                  implicit val templateRenderer: LocalTemplateRenderer
                                                 )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  private val errorKey = "anyOtherCompanyBenefits.blank"

  def onPageLoad(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val form: Form[Boolean] = formProvider(cc.messagesApi.preferred(request).messages.apply(errorKey))

      val result: Option[Result] = for {
        taxYear: SelectTaxYear <- request.userAnswers.selectTaxYear
        cyaHelper: CheckYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)
        otherCompanyBenefits: AnswerSection = mode match {
          case NormalMode => new CheckYourAnswersSections(cyaHelper, request.userAnswers).otherCompanyBenefitsAddToListNormalMode
          case CheckMode => new CheckYourAnswersSections(cyaHelper, request.userAnswers).otherCompanyBenefitsAddToListCheckMode
        }
      } yield {
        Ok(anyOtherCompanyBenefits(appConfig, form, mode, taxYear, otherCompanyBenefits))
      }

      result.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val form: Form[Boolean] = formProvider(cc.messagesApi.preferred(request).messages.apply(errorKey))

      val result: Option[Future[Result]] = for {
        taxYear: SelectTaxYear <- request.userAnswers.selectTaxYear
        cyaHelper: CheckYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)
        otherCompanyBenefits: AnswerSection = mode match {
          case NormalMode => new CheckYourAnswersSections(cyaHelper, request.userAnswers).otherCompanyBenefitsAddToListNormalMode
          case CheckMode => new CheckYourAnswersSections(cyaHelper, request.userAnswers).otherCompanyBenefitsAddToListCheckMode
        }
      } yield {
        form.bindFromRequest().fold(
          (formWithErrors: Form[_]) =>
            Future.successful(BadRequest(anyOtherCompanyBenefits(appConfig, formWithErrors, mode, taxYear, otherCompanyBenefits))),
          value =>
            dataCacheConnector.save[Boolean](request.externalId, AnyOtherCompanyBenefitsId.toString, value).map(cacheMap =>
              Redirect(navigator.nextPage(AnyOtherCompanyBenefitsId, mode)(new UserAnswers(cacheMap))))
        )
      }

      result.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }
}