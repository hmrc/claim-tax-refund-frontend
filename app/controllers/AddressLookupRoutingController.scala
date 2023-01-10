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

import com.google.inject.Inject
import config.FrontendAppConfig
import connectors.AddressLookupConnector
import controllers.actions.{AuthAction, DataRequiredAction, DataRetrievalAction}
import identifiers.PaymentLookupAddressId
import models.{CheckMode, Mode, NormalMode}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import utils.Navigator

import scala.concurrent.{ExecutionContext, Future}

class AddressLookupRoutingController @Inject()(appConfig: FrontendAppConfig,
                                               navigator: Navigator,
                                               addressLookupConnector: AddressLookupConnector,
                                               authenticate: AuthAction,
                                               cc: MessagesControllerComponents,
                                               getData: DataRetrievalAction,
                                               requireData: DataRequiredAction
                                              )(implicit ec: ExecutionContext) extends FrontendController(cc) {


  def addressLookupCallback(addressId: Option[String], mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      addressId.map {
        id =>
          addressLookupConnector.getAddress(request.externalId, PaymentLookupAddressId.toString, id) map {
            userAnswers =>
              mode match {
                case NormalMode => Redirect(navigator.nextPage(PaymentLookupAddressId, NormalMode)(userAnswers))
                case CheckMode => Redirect(navigator.nextPage(PaymentLookupAddressId, CheckMode)(userAnswers))
              }
          }
      }.getOrElse {
        mode match {
          case NormalMode => Future.successful(Redirect(navigator.nextPage(PaymentLookupAddressId, NormalMode)(request.userAnswers)))
          case CheckMode => Future.successful(Redirect(navigator.nextPage(PaymentLookupAddressId, CheckMode)(request.userAnswers)))
        }
      }
  }
}
