/*
 * Copyright 2023 HM Revenue & Customs
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

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.BooleanForm
import identifiers.AnyOtherBenefitsId
import javax.inject.Inject
import models.{CheckMode, Mode, NormalMode, SelectTaxYear}
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, Result}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import utils.{CheckYourAnswersHelper, CheckYourAnswersSections, Navigator, UserAnswers}
import viewmodels.AnswerSection
import views.html.anyOtherBenefits

import scala.concurrent.{ExecutionContext, Future}

class AnyOtherBenefitsController @Inject()(appConfig: FrontendAppConfig,
                                           dataCacheConnector: DataCacheConnector,
                                           navigator: Navigator,
                                           authenticate: AuthAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           formProvider: BooleanForm,
                                           anyOtherBenefits: anyOtherBenefits,
                                           cc: MessagesControllerComponents
                                          )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  private val errorKey = "anyOtherBenefits.blank"

  def onPageLoad(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val form: Form[Boolean] = formProvider(cc.messagesApi.preferred(request).messages.apply(errorKey))

      val result: Option[Result] = for {
        taxYear: SelectTaxYear <- request.userAnswers.selectTaxYear
        cyaHelper: CheckYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)
        otherBenefitsAnswers: AnswerSection = mode match {
          case NormalMode => new CheckYourAnswersSections(cyaHelper, request.userAnswers).otherBenefitsAddToListNormalMode
          case CheckMode => new CheckYourAnswersSections(cyaHelper, request.userAnswers).otherBenefitsAddToListCheckMode
        }
      } yield {
        Ok(anyOtherBenefits(appConfig, form, mode, taxYear, otherBenefitsAnswers))
      }

      result.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad)
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val form: Form[Boolean] = formProvider(cc.messagesApi.preferred(request).messages.apply(errorKey))

      val result: Option[Future[Result]] = for {
        taxYear: SelectTaxYear <- request.userAnswers.selectTaxYear
        cyaHelper: CheckYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)
        otherBenefitsAnswers: AnswerSection = mode match {
          case NormalMode => new CheckYourAnswersSections(cyaHelper, request.userAnswers).otherBenefitsAddToListNormalMode
          case CheckMode => new CheckYourAnswersSections(cyaHelper, request.userAnswers).otherBenefitsAddToListCheckMode
        }
      } yield {
        form.bindFromRequest().fold(
          (formWithErrors: Form[_]) =>
            Future.successful(BadRequest(anyOtherBenefits(appConfig, formWithErrors, mode, taxYear, otherBenefitsAnswers))),
          value =>
            dataCacheConnector.save[Boolean](request.externalId, AnyOtherBenefitsId.toString, value).map(cacheMap =>
              Redirect(navigator.nextPage(AnyOtherBenefitsId, mode)(new UserAnswers(cacheMap))))
        )
      }

      result.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad))
      }
  }
}
