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
import forms.TelephoneNumberForm
import identifiers.{AnyTelephoneId, PaymentLookupAddressId, TelephoneNumberId}
import javax.inject.Inject
import models._
import models.requests.DataRequest
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Result}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import uk.gov.hmrc.play.partials.FormPartialRetriever
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.{Navigator, UserAnswers}
import views.html.telephoneNumber

import scala.concurrent.Future

class TelephoneNumberController @Inject()(
                                           appConfig: FrontendAppConfig,
                                           override val messagesApi: MessagesApi,
                                           dataCacheConnector: DataCacheConnector,
                                           navigator: Navigator,
                                           authenticate: AuthAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           formBuilder: TelephoneNumberForm,
                                           implicit val formPartialRetriever: FormPartialRetriever,
                                           implicit val templateRenderer: TemplateRenderer) extends FrontendController with I18nSupport {
                                           formBuilder: TelephoneNumberForm,
                                           addressLookupConnector: AddressLookupConnector
                                         ) extends FrontendController with I18nSupport {

  private val form: Form[TelephoneOption] = formBuilder()

  def forwardRoute(addressFound: Object, preparedForm: Form[TelephoneOption], mode: Mode)(implicit request: DataRequest[AnyContent]): Result = {
    val result: Result = addressFound match {
      case None =>
        Ok(telephoneNumber(appConfig, preparedForm, mode))
//        Redirect(routes.CheckYourAnswersController.onPageLoad().url)
      case _ =>
        request.userAnswers.anyTelephoneNumber match {
        case Some(_) =>
          if (mode == NormalMode) Ok(telephoneNumber(appConfig, preparedForm, mode)) else Redirect(routes.CheckYourAnswersController.onPageLoad().url)
        case None =>
          Ok(telephoneNumber(appConfig, preparedForm, mode))
      }
    }
    result
  }

  def onPageLoad(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData) {

    implicit request =>
      val preparedForm = request.userAnswers.anyTelephoneNumber match {
        case None => form
        case Some(value) => form.fill(value)
      }

      val addressFound: Future[Option[AddressLookup]] = addressLookupConnector.getAddress
      addressFound.map {
          address =>
              dataCacheConnector.save[AddressLookup](request.externalId, PaymentLookupAddressId.toString, address.get)
      }

      forwardRoute(addressFound, preparedForm, mode)
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      form.bindFromRequest().fold(
        (formWithErrors: Form[_]) =>
          Future.successful(BadRequest(telephoneNumber(appConfig, formWithErrors, mode))),
        value =>
          dataCacheConnector.save[TelephoneOption](request.externalId, AnyTelephoneId.toString, value).map(cacheMap =>
            Redirect(navigator.nextPage(TelephoneNumberId, mode)(new UserAnswers(cacheMap))))
      )
  }
}
