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

import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import connectors.{DataCacheConnector, TaiConnector}
import controllers.actions._
import config.FrontendAppConfig
import forms.BooleanForm
import identifiers.TaiEmploymentDetailsId
import models.Mode
import models.SelectTaxYear.{CYMinus2, CYMinus3, CYMinus4, CYMinus5}
import uk.gov.hmrc.time.TaxYearResolver
import utils.{Navigator, UserAnswers}
import views.html.taiEmploymentDetails

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.control.NonFatal


class TaiEmploymentDetailsController @Inject()(appConfig: FrontendAppConfig,
                                               override val messagesApi: MessagesApi,
                                               dataCacheConnector: DataCacheConnector,
                                               navigator: Navigator,
                                               authenticate: AuthAction,
                                               getData: DataRetrievalAction,
                                               requireData: DataRequiredAction,
                                               formProvider: BooleanForm,
                                               taiConnector: TaiConnector) extends FrontendController with I18nSupport {

  private val errorKey = "taiEmploymentDetails.blank"
  val form: Form[Boolean] = formProvider(errorKey)

  def onPageLoad(mode: Mode) = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val preparedForm = request.userAnswers.taiEmploymentDetails match {
        case None => form
        case Some(value) => form.fill(value)
      }

      def selectedTaxYear: Option[Int] = request.userAnswers.selectTaxYear map {
            case CYMinus2 =>
              TaxYearResolver.startOfCurrentTaxYear.minusYears(2).getYear
            case CYMinus3 =>
              TaxYearResolver.startOfCurrentTaxYear.minusYears(3).getYear
            case CYMinus4 =>
              TaxYearResolver.startOfCurrentTaxYear.minusYears(4).getYear
            case CYMinus5 =>
              TaxYearResolver.startOfCurrentTaxYear.minusYears(5).getYear
      }

      val results = taiConnector.taiEmployments(request.nino, selectedTaxYear.get)

      results.map(
        employments =>
          Ok(taiEmploymentDetails(appConfig, preparedForm, mode, employments))
      ).recover {
        case NonFatal(e) =>
          ???
      }
  }


  def onSubmit(mode: Mode) = (authenticate andThen getData andThen requireData).async {
    implicit request =>

      def selectedTaxYear: Option[Int] = request.userAnswers.selectTaxYear map {
            case CYMinus2 =>
              TaxYearResolver.startOfCurrentTaxYear.minusYears(2).getYear
            case CYMinus3 =>
              TaxYearResolver.startOfCurrentTaxYear.minusYears(3).getYear
            case CYMinus4 =>
              TaxYearResolver.startOfCurrentTaxYear.minusYears(4).getYear
            case CYMinus5 =>
              TaxYearResolver.startOfCurrentTaxYear.minusYears(5).getYear

      }

      val results =  taiConnector.taiEmployments(request.nino, selectedTaxYear.get)

      results.flatMap {
        employments =>
          form.bindFromRequest().fold(
            (formWithErrors: Form[_]) =>
              Future.successful(BadRequest(taiEmploymentDetails(appConfig, formWithErrors, mode, employments))),
            (value) =>
              dataCacheConnector.save[Boolean](request.externalId, TaiEmploymentDetailsId.toString, value).map(cacheMap =>
                Redirect(navigator.nextPage(TaiEmploymentDetailsId, mode)(new UserAnswers(cacheMap))))
          )
      }
  }
}
