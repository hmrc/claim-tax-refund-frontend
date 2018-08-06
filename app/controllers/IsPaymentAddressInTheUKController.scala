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
import connectors.{AddressLookupConnector, DataCacheConnector}
import controllers.actions._
import forms.BooleanForm
import identifiers.{IsPaymentAddressInTheUKId, PaymentLookupAddressId}
import javax.inject.Inject
import models.Mode
import identifiers.IsPaymentAddressInTheUKId
import models.Mode
import models.{CheckMode, Mode, NormalMode, SelectTaxYear}
import play.api.Logger
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import uk.gov.hmrc.play.partials.FormPartialRetriever
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.{Navigator, UserAnswers}
import views.html.isPaymentAddressInTheUK

import scala.concurrent.Future

class IsPaymentAddressInTheUKController @Inject()(appConfig: FrontendAppConfig,
                                                  override val messagesApi: MessagesApi,
                                                  dataCacheConnector: DataCacheConnector,
                                                  navigator: Navigator,
                                                  authenticate: AuthAction,
                                                  getData: DataRetrievalAction,
                                                  requireData: DataRequiredAction,
                                                  formProvider: BooleanForm,
                                                  addressLookup: AddressLookupConnector
                                                 ) extends FrontendController with I18nSupport {
                                                  formProvider: BooleanForm,
                                                  implicit val formPartialRetriever: FormPartialRetriever,
                                                  implicit val templateRenderer: TemplateRenderer) extends FrontendController with I18nSupport {

  private val errorKey = "isPaymentAddressInTheUK.blank"

  val form: Form[Boolean] = formProvider(errorKey)

  def onPageLoad(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val preparedForm = request.userAnswers.isPaymentAddressInTheUK match {
        case None => form
        case Some(value) => form.fill(value)
      }

//      val continueUrl = routes.PaymentAddressCorrectController.onPageLoad(mode).absoluteURL()
      val continueUrl = navigator.nextPage(PaymentLookupAddressId, mode)(new UserAnswers(request.userAnswers.cacheMap)).absoluteURL()

      val addressInit = for {
        result: Option[String] <- addressLookup.initialise(continueUrl = continueUrl)
      } yield {
        result map (
          url => Redirect(url)
        )
      }

      addressInit.map(_.getOrElse(
        Ok(isPaymentAddressInTheUK(appConfig, preparedForm, mode))
      ))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      form.bindFromRequest().fold(
        (formWithErrors: Form[_]) =>
          Future.successful(BadRequest(isPaymentAddressInTheUK(appConfig, formWithErrors, mode))),
        value =>
          dataCacheConnector.save[Boolean](request.externalId, IsPaymentAddressInTheUKId.toString, value).map(cacheMap =>
            Redirect(navigator.nextPage(IsPaymentAddressInTheUKId, mode)(new UserAnswers(cacheMap)))
          )
      )
  }
}
