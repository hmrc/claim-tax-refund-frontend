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
import forms.HowMuchJobseekersAllowanceForm
import identifiers.HowMuchJobseekersAllowanceId
import javax.inject.Inject
import models.Mode
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import utils.{Navigator, UserAnswers}
import views.html.howMuchJobseekersAllowance

import scala.concurrent.{ExecutionContext, Future}

class HowMuchJobseekersAllowanceController @Inject()(
                                                      appConfig: FrontendAppConfig,
                                                      override val messagesApi: MessagesApi,
                                                      dataCacheConnector: DataCacheConnector,
                                                      navigator: Navigator,
                                                      authenticate: AuthAction,
                                                      getData: DataRetrievalAction,
                                                      requireData: DataRequiredAction,
                                                      howMuchJobseekersAllowance: howMuchJobseekersAllowance,
cc: MessagesControllerComponents,
                                                      formBuilder: HowMuchJobseekersAllowanceForm
                                                    )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  private val form: Form[String] = formBuilder()

  def onPageLoad(mode: Mode) = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.howMuchJobseekersAllowance match {
        case None => form
        case Some(value) => form.fill(value)
      }
      request.userAnswers.selectTaxYear.map {
        taxYear =>
          Ok(howMuchJobseekersAllowance(appConfig, preparedForm, mode, taxYear))
      }.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad)
      }
  }

  def onSubmit(mode: Mode) = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      request.userAnswers.selectTaxYear.map {
        taxYear =>
          form.bindFromRequest().fold(
            (formWithErrors: Form[_]) =>
              Future.successful(BadRequest(howMuchJobseekersAllowance(appConfig, formWithErrors, mode, taxYear))),
            (value) =>
              dataCacheConnector.save[String](request.externalId, HowMuchJobseekersAllowanceId.toString, value).map(cacheMap =>
                Redirect(navigator.nextPage(HowMuchJobseekersAllowanceId, mode)(new UserAnswers(cacheMap))))
          )
      }.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad))
      }
  }
}
