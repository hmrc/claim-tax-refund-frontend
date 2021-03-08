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
import javax.inject.Inject
import models._
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Request}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.play.partials.FormPartialRetriever
import utils.{Navigator, UserAnswers}
import views.html.removeOtherSelectedOption

import scala.concurrent.{ExecutionContext, Future}

class RemoveOtherSelectedOptionController @Inject()(appConfig: FrontendAppConfig,
                                                    override val messagesApi: MessagesApi,
                                                    dataCacheConnector: DataCacheConnector,
                                                    navigator: Navigator,
                                                    authenticate: AuthAction,
                                                    getData: DataRetrievalAction,
                                                    requireData: DataRequiredAction,
                                                    removeOtherSelectedOption: removeOtherSelectedOption,
                                                    cc: MessagesControllerComponents,
                                                    formProvider: BooleanForm,
                                                    implicit val formPartialRetriever: FormPartialRetriever,
                                                    implicit val templateRenderer: LocalTemplateRenderer
                                                   )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  private val errorKey = "RemoveOtherSelectedOption.blank"
  private def otherBenefitsKey(implicit request: Request[_]) =
    cc.messagesApi.preferred(request).messages("RemoveOtherSelectedOption.otherBenefits")
  private def otherCompanyBenefitsKey(implicit request: Request[_]) =
    cc.messagesApi.preferred(request).messages("RemoveOtherSelectedOption.companyBenefits")
  private def otherTaxableIncomeKey(implicit request: Request[_]) =
    cc.messagesApi.preferred(request).messages("RemoveOtherSelectedOption.otherIncome")

  def onPageLoad(mode: Mode, collectionId: String): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      request.userAnswers.selectTaxYear.map {
        selectedTaxYear =>
          val collectionName = collectionId match {
            case OtherBenefit.collectionId => otherBenefitsKey
            case OtherCompanyBenefit.collectionId => otherCompanyBenefitsKey
            case OtherTaxableIncome.collectionId => otherTaxableIncomeKey
          }
          val form: Form[Boolean] = formProvider(cc.messagesApi.preferred(request).messages(errorKey, collectionName))
          Ok(removeOtherSelectedOption(appConfig, form, mode, selectedTaxYear, collectionId))
      }.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }

  def onSubmit(mode: Mode, collectionId: String): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      request.userAnswers.selectTaxYear.map {
        selectedTaxYear =>
          val taxYear = selectedTaxYear
          val collectionName = collectionId match {
            case OtherBenefit.collectionId => otherBenefitsKey
            case OtherCompanyBenefit.collectionId => otherCompanyBenefitsKey
            case OtherTaxableIncome.collectionId => otherTaxableIncomeKey
          }
          val form: Form[Boolean] = formProvider(cc.messagesApi.preferred(request).messages(errorKey, collectionName))

          form.bindFromRequest().fold(
            (formWithErrors: Form[_]) =>
              Future.successful(BadRequest(removeOtherSelectedOption(appConfig, formWithErrors, mode, taxYear, collectionId))),
            (value: Boolean) => {
              dataCacheConnector.save[Boolean](request.externalId, collectionId.toString, value).map(cacheMap =>
                Redirect(navigator.nextPageWithCollectionId(collectionId, mode)(new UserAnswers(cacheMap))))
            }
          )
      }.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }
}
