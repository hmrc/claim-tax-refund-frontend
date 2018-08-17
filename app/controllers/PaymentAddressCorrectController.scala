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
import forms.BooleanForm
import identifiers.{ItmpAddressId, PaymentAddressCorrectId}
import javax.inject.Inject
import models.{ItmpAddressFormat, Mode, NormalMode}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Format
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.auth.core.retrieve.ItmpAddress
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import uk.gov.hmrc.play.partials.FormPartialRetriever
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.{Navigator, UserAnswers}
import views.html.paymentAddressCorrect

import scala.concurrent.Future

class PaymentAddressCorrectController @Inject()(appConfig: FrontendAppConfig,
                                                override val messagesApi: MessagesApi,
                                                dataCacheConnector: DataCacheConnector,
                                                navigator: Navigator,
                                                authenticate: AuthAction,
                                                getData: DataRetrievalAction,
                                                requireData: DataRequiredAction,
                                                formProvider: BooleanForm,
                                                implicit val formPartialRetriever: FormPartialRetriever,
                                                implicit val templateRenderer: TemplateRenderer) extends FrontendController with I18nSupport {

  private val errorKey = "paymentAddressCorrect.blank"
  val form: Form[Boolean] = formProvider(errorKey)
  implicit val format: Format[ItmpAddress] = ItmpAddressFormat.format

  def onPageLoad(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.paymentAddressCorrect match {
        case None => form
        case Some(value) => form.fill(value)
      }

      request.address match {
        case Some(address)
          if address.line1.exists(_.trim.nonEmpty) && (address.postCode.exists(_.trim.nonEmpty) || address.countryName.exists(_.trim.nonEmpty)) =>
            Ok(paymentAddressCorrect(appConfig, preparedForm, mode, address))
        case _ =>
          Redirect(routes.IsPaymentAddressInTheUKController.onPageLoad(mode))
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      request.address match {
        case Some(address)
          if address.line1.exists(_.trim.nonEmpty) && (address.postCode.exists(_.trim.nonEmpty) || address.countryName.exists(_.trim.nonEmpty)) =>
            form.bindFromRequest().fold(
              (formWithErrors: Form[_]) =>
                Future.successful(BadRequest(paymentAddressCorrect(appConfig, formWithErrors, mode, address))),
              value => {
                if (value) {
                  for {
                    _ <- dataCacheConnector.save[Boolean](request.externalId, PaymentAddressCorrectId.toString, value)
                    updatedCacheMap <- dataCacheConnector.save[ItmpAddress](request.externalId, ItmpAddressId.toString, address)
                  } yield
                    Redirect(navigator.nextPage(PaymentAddressCorrectId, mode)(new UserAnswers(updatedCacheMap)))
                } else {
                  dataCacheConnector.save[Boolean](request.externalId, PaymentAddressCorrectId.toString, value).map(cacheMap =>
                    Redirect(navigator.nextPage(PaymentAddressCorrectId, mode)(new UserAnswers(cacheMap)))
                  )
                }
              }
            )
        case _ =>
          Future.successful(Redirect(routes.IsPaymentAddressInTheUKController.onPageLoad(NormalMode)))
      }
  }
}
