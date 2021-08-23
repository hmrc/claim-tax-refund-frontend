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

import config.FrontendAppConfig
import connectors.{AddressLookupConnector, DataCacheConnector}
import controllers.actions._
import forms.BooleanForm
import identifiers.IsPaymentAddressInTheUKId
import javax.inject.Inject
import models.{CheckMode, Mode, NormalMode}
import play.api.data.Form
import play.api.i18n.{I18nSupport, Lang, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.play.partials.FormPartialRetriever
import utils.{Navigator, UserAnswers}
import views.html.isPaymentAddressInTheUK

import scala.concurrent.{ExecutionContext, Future}

class IsPaymentAddressInTheUKController @Inject()(appConfig: FrontendAppConfig,
                                                  override val messagesApi: MessagesApi,
                                                  dataCacheConnector: DataCacheConnector,
                                                  navigator: Navigator,
                                                  authenticate: AuthAction,
                                                  getData: DataRetrievalAction,
                                                  requireData: DataRequiredAction,
                                                  isPaymentAddressInTheUK: isPaymentAddressInTheUK,
cc: MessagesControllerComponents,
                                                  formProvider: BooleanForm,
                                                  addressLookup: AddressLookupConnector
                                                 )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  private val errorKey = "isPaymentAddressInTheUK.blank"

  def onPageLoad(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val form: Form[Boolean] = formProvider(cc.messagesApi.preferred(request).messages.apply(errorKey))

      val preparedForm = request.userAnswers.isPaymentAddressInTheUK match {
        case None => form
        case Some(value) => form.fill(value)
      }

      val language: Lang = cc.messagesApi.preferred(request).lang
      request.userAnswers.selectTaxYear.map {
        taxYear =>
          val continueUrl = mode match {
            case NormalMode => appConfig.addressLookupContinueUrlNormalMode
            case CheckMode => appConfig.addressLookupContinueUrlCheckMode
          }

          val addressInit = for {
            result: Option[String] <- addressLookup.initialise(continueUrl = continueUrl,
              accessibilityFooterUrl = appConfig.accessibilityFooterUrl)(hc: HeaderCarrier, language)
          } yield {
            result map (
              url => Redirect(url)
              )
          }

          addressInit.map(_.getOrElse(
            Ok(isPaymentAddressInTheUK(appConfig, preparedForm, mode, taxYear))
          ))

      }.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val form: Form[Boolean] = formProvider(cc.messagesApi.preferred(request).messages.apply(errorKey))

      request.userAnswers.selectTaxYear.map {
        taxYear =>
          form.bindFromRequest().fold(
            (formWithErrors: Form[_]) =>
              Future.successful(BadRequest(isPaymentAddressInTheUK(appConfig, formWithErrors, mode, taxYear))),
            value =>
              dataCacheConnector.save[Boolean](request.externalId, IsPaymentAddressInTheUKId.toString, value).map(cacheMap =>
                Redirect(navigator.nextPage(IsPaymentAddressInTheUKId, mode)(new UserAnswers(cacheMap)))
              )
          )
      }.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }
}
