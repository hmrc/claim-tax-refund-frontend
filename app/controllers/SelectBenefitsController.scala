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

import javax.inject.Inject
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import connectors.DataCacheConnector
import controllers.actions._
import config.FrontendAppConfig
import forms.SelectBenefitsForm
import identifiers.SelectBenefitsId
import models.{Benefits, Mode}
import play.api.mvc.{Action, AnyContent}
import utils.{Navigator, UserAnswers}
import views.html.selectBenefits

import scala.concurrent.Future

class SelectBenefitsController @Inject()(
                                          appConfig: FrontendAppConfig,
                                          override val messagesApi: MessagesApi,
                                          dataCacheConnector: DataCacheConnector,
                                          navigator: Navigator,
                                          authenticate: AuthAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction) extends FrontendController with I18nSupport {

  def onPageLoad(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.selectBenefits match {
        case None => SelectBenefitsForm()
        case Some(value) => SelectBenefitsForm().fill(value)
      }

      request.userAnswers.selectTaxYear.map {
        taxYear =>
          Ok(selectBenefits(appConfig, preparedForm, mode, taxYear))
      }.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      request.userAnswers.selectTaxYear.map {
        taxYear =>
          SelectBenefitsForm().bindFromRequest().fold(
            (formWithErrors: Form[_]) =>
              Future.successful(BadRequest(selectBenefits(appConfig, formWithErrors, mode, taxYear))),
            value =>
              dataCacheConnector.save[Set[Benefits.Value]](request.externalId, SelectBenefitsId.toString, value).map(cacheMap =>
                Redirect(navigator.nextPage(SelectBenefitsId, mode)(new UserAnswers(cacheMap))))
          )
      }.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }
}

