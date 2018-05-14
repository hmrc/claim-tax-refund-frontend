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
import identifiers.UserDetailsId
import javax.inject.Inject
import models.{Mode, UkAddress, UserDetails}
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import utils.{Navigator, UserAnswers}
import views.html.userDetails

class UserDetailsController @Inject()(
                                       appConfig: FrontendAppConfig,
                                       override val messagesApi: MessagesApi,
                                       dataCacheConnector: DataCacheConnector,
                                       navigator: Navigator,
                                       authenticate: AuthAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction) extends FrontendController with I18nSupport {

  def onPageLoad(mode: Mode) = (authenticate andThen getData) {
    implicit request =>
      val userName = request.name.givenName.get + " " + request.name.familyName.get
      val userNino = request.nino
      val userAddress = UkAddress(request.address.line1.get, request.address.line2.get, Some(request.address.line3.get),
        Some(request.address.line4.get), Some(request.address.line5.get), request.address.postCode.get)
      Ok(userDetails(appConfig, mode, userName, userNino, userAddress))
  }

  def onSubmit(mode: Mode) = (authenticate andThen getData).async {
    implicit request =>
      val userName = request.name.givenName.get + " " + request.name.familyName.get
      val userNino = request.nino
      val userAddress = UkAddress(request.address.line1.get, request.address.line2.get, Some(request.address.line3.get),
        Some(request.address.line4.get), Some(request.address.line5.get), request.address.postCode.get)
      dataCacheConnector.save[UserDetails](request.externalId, UserDetailsId.toString, UserDetails(userName, userNino, userAddress)).map(cacheMap =>
        Redirect(navigator.nextPage(UserDetailsId, mode)(new UserAnswers(cacheMap)))
      )
  }
}
