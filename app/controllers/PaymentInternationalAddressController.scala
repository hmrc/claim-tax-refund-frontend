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

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.PaymentInternationalAddressForm
import identifiers.PaymentInternationalAddressId
import javax.inject.Inject

import models.{InternationalAddress, Mode}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import utils.{Navigator, UserAnswers}
import views.html.paymentInternationalAddress

import scala.concurrent.Future


class PaymentInternationalAddressController @Inject()(appConfig: FrontendAppConfig,
                                                      override val messagesApi: MessagesApi,
                                                      dataCacheConnector: DataCacheConnector,
                                                      navigator: Navigator,
                                                      authenticate: AuthAction,
                                                      getData: DataRetrievalAction,
                                                      requireData: DataRequiredAction,
                                                      formBuilder: PaymentInternationalAddressForm) extends FrontendController with I18nSupport {

  private val form: Form[InternationalAddress] = formBuilder()

  def onPageLoad(mode: Mode) = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.paymentInternationalAddress match {
        case None => form
        case Some(value) => form.fill(value)
      }
      Ok(paymentInternationalAddress(appConfig, preparedForm, mode))
  }

  def onSubmit(mode: Mode) = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      form.bindFromRequest().fold(
        (formWithErrors: Form[InternationalAddress]) =>
          Future.successful(BadRequest(paymentInternationalAddress(appConfig, formWithErrors, mode))),
        (value) =>
          dataCacheConnector.save[InternationalAddress](request.externalId, PaymentInternationalAddressId.toString, value).map(cacheMap =>
            Redirect(navigator.nextPage(PaymentInternationalAddressId, mode)(new UserAnswers(cacheMap))))
      )
  }
}