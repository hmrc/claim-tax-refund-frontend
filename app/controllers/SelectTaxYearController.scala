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

import javax.inject.Inject

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.SelectTaxYearForm
import identifiers.{SelectTaxYearId, UserDetailsId}
import models.{Mode, SelectTaxYear, UkAddress, UserDetails}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import utils.{Navigator, UserAnswers}
import views.html.selectTaxYear

import scala.concurrent.Future

class SelectTaxYearController @Inject()(
                                         appConfig: FrontendAppConfig,
                                         override val messagesApi: MessagesApi,
                                         dataCacheConnector: DataCacheConnector,
                                         navigator: Navigator,
                                         authenticate: AuthAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction) extends FrontendController with I18nSupport {

  def onPageLoad(mode: Mode) = (authenticate andThen getData) {
    implicit request =>
      val preparedForm = request.userAnswers.flatMap(x => x.selectTaxYear) match {
        case None => SelectTaxYearForm()
        case Some(value) => SelectTaxYearForm().fill(value)
      }
      Ok(selectTaxYear(appConfig, preparedForm, mode))
  }

  def onSubmit(mode: Mode) = (authenticate andThen getData).async {
    implicit request =>
      val userName = request.name.givenName.get + " " + request.name.familyName.get
      val userNino = request.nino
      val userAddress = UkAddress(request.address.line1.get, request.address.line2.get, Some(request.address.line3.get),
        Some(request.address.line4.get), Some(request.address.line5.get), request.address.postCode.get)

      SelectTaxYearForm().bindFromRequest().fold(
        (formWithErrors: Form[_]) =>
          Future.successful(BadRequest(selectTaxYear(appConfig, formWithErrors, mode))),
        (value) =>
          dataCacheConnector.save[UserDetails](request.externalId, UserDetailsId.toString, UserDetails(userName, userNino, userAddress)).flatMap(
            x => dataCacheConnector.save[SelectTaxYear](request.externalId, SelectTaxYearId.toString, value).map(cacheMap =>
              Redirect(navigator.nextPage(SelectTaxYearId, mode)(new UserAnswers(cacheMap))))
          )
      )
  }
}
