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
import forms.PaymentUKAddressForm
import identifiers.PaymentUKAddressId
import javax.inject.Inject
import models.{Mode, UkAddress}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import utils.{Navigator, UserAnswers}
import views.html.paymentUKAddress

import scala.concurrent.{ExecutionContext, Future}

class PaymentUKAddressController @Inject()(appConfig: FrontendAppConfig,
                                           override val messagesApi: MessagesApi,
                                           dataCacheConnector: DataCacheConnector,
                                           navigator: Navigator,
                                           authenticate: AuthAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           paymentUKAddress: paymentUKAddress,
cc: MessagesControllerComponents,
                                           formBuilder: PaymentUKAddressForm
                                          )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  private val form: Form[UkAddress] = formBuilder()

  def onPageLoad(mode: Mode) = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.paymentUKAddress match {
        case None => form
        case Some(value) => form.fill(value)
      }
      request.userAnswers.selectTaxYear.map {
        taxYear =>
          Ok(paymentUKAddress(appConfig, preparedForm, mode, taxYear))
      }.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad)
      }
  }

  def onSubmit(mode: Mode) = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      request.userAnswers.selectTaxYear.map {
        taxYear =>
          form.bindFromRequest().fold(
            (formWithErrors: Form[UkAddress]) =>
              Future.successful(BadRequest(paymentUKAddress(appConfig, formWithErrors, mode, taxYear))),
            value =>
              dataCacheConnector.save[UkAddress](request.externalId, PaymentUKAddressId.toString, value).map(cacheMap =>
                Redirect(navigator.nextPage(PaymentUKAddressId, mode)(new UserAnswers(cacheMap))))
          )
      }.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad))
      }
  }
}
