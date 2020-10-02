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

import config.{FrontendAppConfig, LocalTemplateRenderer}
import connectors.DataCacheConnector
import controllers.actions._
import forms.BooleanForm
import identifiers.{ItmpAddressId, PaymentAddressCorrectId}
import javax.inject.Inject
import models.{ItmpAddressFormat, Mode}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.auth.core.retrieve.ItmpAddress
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.play.partials.FormPartialRetriever
import utils.{Navigator, UserAnswers}
import views.html.paymentAddressCorrect

import scala.concurrent.{ExecutionContext, Future}

class PaymentAddressCorrectController @Inject()(appConfig: FrontendAppConfig,
                                                override val messagesApi: MessagesApi,
                                                dataCacheConnector: DataCacheConnector,
                                                navigator: Navigator,
                                                authenticate: AuthAction,
                                                getData: DataRetrievalAction,
                                                requireData: DataRequiredAction,
                                                paymentAddressCorrect: paymentAddressCorrect,
cc: MessagesControllerComponents,
                                                formProvider: BooleanForm,
                                                implicit val formPartialRetriever: FormPartialRetriever,
                                                implicit val templateRenderer: LocalTemplateRenderer
                                               )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  private val errorKey = "paymentAddressCorrect.blank"

  import ItmpAddressFormat.format

  def onPageLoad(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val form: Form[Boolean] = formProvider(cc.messagesApi.preferred(request).messages.apply(errorKey))

      val preparedForm = request.userAnswers.paymentAddressCorrect match {
        case None => form
        case Some(value) => form.fill(value)
      }

      request.userAnswers.selectTaxYear.map {
        taxYear =>
          request.address match {
            case Some(address)
              if address.line1.exists(_.trim.nonEmpty) && address.postCode.exists(_.trim.nonEmpty) =>
              Ok(paymentAddressCorrect(appConfig, preparedForm, mode, address, taxYear))
            case _ =>
              Redirect(routes.IsPaymentAddressInTheUKController.onPageLoad(mode))
          }
      }.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad())
      }

  }

  def onSubmit(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val form: Form[Boolean] = formProvider(cc.messagesApi.preferred(request).messages.apply(errorKey))

      request.userAnswers.selectTaxYear.map {
        taxYear =>
          form.bindFromRequest().fold(
            (formWithErrors: Form[_]) =>
              Future.successful(BadRequest(paymentAddressCorrect(appConfig, formWithErrors, mode, request.address.get, taxYear))),
            value => {
              if (value) {
                for {
                  _ <- dataCacheConnector.save[Boolean](request.externalId, PaymentAddressCorrectId.toString, value)
                  updatedCacheMap <- dataCacheConnector.save[ItmpAddress](request.externalId, ItmpAddressId.toString, request.address.get)
                } yield
                  Redirect(navigator.nextPage(PaymentAddressCorrectId, mode)(new UserAnswers(updatedCacheMap)))
              } else {
                dataCacheConnector.save[Boolean](request.externalId, PaymentAddressCorrectId.toString, value).map(cacheMap =>
                  Redirect(navigator.nextPage(PaymentAddressCorrectId, mode)(new UserAnswers(cacheMap)))
                )
              }
            }
          )
      }.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }
}
