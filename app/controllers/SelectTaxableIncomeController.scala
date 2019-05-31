/*
 * Copyright 2019 HM Revenue & Customs
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

import com.github.tototoshi.play2.scalate.Scalate
import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.SelectTaxableIncomeForm
import identifiers.SelectTaxableIncomeId
import javax.inject.Inject
import models.{Mode, TaxableIncome}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import uk.gov.hmrc.play.partials.FormPartialRetriever
import utils.{Navigator, UserAnswers}
import views.html.selectTaxableIncome

import scala.concurrent.{ExecutionContext, Future}

class SelectTaxableIncomeController @Inject()(appConfig: FrontendAppConfig,
                                              override val messagesApi: MessagesApi,
                                              dataCacheConnector: DataCacheConnector,
                                              navigator: Navigator,
                                              authenticate: AuthAction,
                                              getData: DataRetrievalAction,
                                              requireData: DataRequiredAction,
                                              implicit val formPartialRetriever: FormPartialRetriever,
                                              implicit val scalate: Scalate
                                             )(implicit ec: ExecutionContext) extends FrontendController with I18nSupport {

  def onPageLoad(mode: Mode) = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.selectTaxableIncome match {
        case None => SelectTaxableIncomeForm()
        case Some(value) => SelectTaxableIncomeForm().fill(value)
      }

      request.userAnswers.selectTaxYear.map {
        selectedTaxYear =>
          val taxYear = selectedTaxYear
          Ok(selectTaxableIncome(appConfig, preparedForm, mode, taxYear))
      }.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }

  def onSubmit(mode: Mode) = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      request.userAnswers.selectTaxYear.map {
        selectedTaxYear =>
          val taxYear = selectedTaxYear
          SelectTaxableIncomeForm().bindFromRequest().fold(
            (formWithErrors: Form[_]) =>
              Future.successful(BadRequest(selectTaxableIncome(appConfig, formWithErrors, mode, taxYear))),
            (value) =>
              dataCacheConnector.save[Seq[TaxableIncome.Value]](request.externalId, SelectTaxableIncomeId.toString, value).map(cacheMap =>
                Redirect(navigator.nextPage(SelectTaxableIncomeId, mode)(new UserAnswers(cacheMap))))
          )
      }.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }
}
