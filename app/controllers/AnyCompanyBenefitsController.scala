/*
 * Copyright 2020 HM Revenue & Customs
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

import com.github.tototoshi.play2.scalate._
import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.BooleanForm
import identifiers.AnyCompanyBenefitsId
import javax.inject.Inject
import models.Mode
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.play.partials.FormPartialRetriever
import utils.{Navigator, UserAnswers}
import views.html.anyCompanyBenefits

import scala.concurrent.{ExecutionContext, Future}

class AnyCompanyBenefitsController @Inject()(
                                              appConfig: FrontendAppConfig,
                                              override val messagesApi: MessagesApi,
                                              dataCacheConnector: DataCacheConnector,
                                              navigator: Navigator,
                                              authenticate: AuthAction,
                                              getData: DataRetrievalAction,
                                              requireData: DataRequiredAction,
cc: MessagesControllerComponents,
                                              formProvider: BooleanForm,
                                              implicit val formPartialRetriever: FormPartialRetriever,
                                              implicit val scalate: Scalate
                                            )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  private val errorKey = "anyCompanyBenefits.blank"


  def onPageLoad(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val form: Form[Boolean] = formProvider(cc.messagesApi.preferred(request).messages.apply(errorKey))

      val preparedForm = request.userAnswers.anyCompanyBenefits match {
        case None => form
        case Some(value) => form.fill(value)
      }

      request.userAnswers.selectTaxYear.map {
        selectedTaxYear =>
          val taxYear = selectedTaxYear
          Ok(anyCompanyBenefits(appConfig, preparedForm, mode, taxYear))
      }.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val form: Form[Boolean] = formProvider(cc.messagesApi.preferred(request).messages.apply(errorKey))

      request.userAnswers.selectTaxYear.map {
        selectedTaxYear =>
          val taxYear = selectedTaxYear
          form.bindFromRequest().fold(
            (formWithErrors: Form[_]) =>
              Future.successful(BadRequest(anyCompanyBenefits(appConfig, formWithErrors, mode, taxYear))),
            (value) =>
              dataCacheConnector.save[Boolean](request.externalId, AnyCompanyBenefitsId.toString, value).map(cacheMap =>
                Redirect(navigator.nextPage(AnyCompanyBenefitsId, mode)(new UserAnswers(cacheMap))))
          )
      }.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }
}
