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
import connectors.DataCacheConnector
import controllers.actions.{AuthAction, DataRequiredAction, DataRetrievalAction}
import forms.AnyTaxPaidForm
import identifiers.AnyTaxableForeignIncomeId
import javax.inject.Inject
import models.{AnyTaxPaid, Mode}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.play.partials.FormPartialRetriever
import utils.{Navigator, UserAnswers}
import views.html.anyTaxableForeignIncome

import scala.concurrent.{ExecutionContext, Future}

class AnyTaxableForeignIncomeController @Inject()(appConfig: FrontendAppConfig,
                                                  override val messagesApi: MessagesApi,
                                                  dataCacheConnector: DataCacheConnector,
                                                  navigator: Navigator,
                                                  authenticate: AuthAction,
                                                  getData: DataRetrievalAction,
                                                  requireData: DataRequiredAction,
                                                  anyTaxableForeignIncome: anyTaxableForeignIncome,
cc: MessagesControllerComponents,
                                                  formProvider: AnyTaxPaidForm
                                                 )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  private val notSelectedKey = "anyTaxableForeignIncome.notSelected"
  private val blankKey = "anyTaxableForeignIncome.blank"
  private val invalidKey = "anyTaxableForeignIncome.invalid"

  private val form: Form[AnyTaxPaid] = formProvider(notSelectedKey, blankKey, invalidKey)

  def onPageLoad(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.anyTaxableForeignIncome match {
        case None => form
        case Some(value) => form.fill(value)
      }

      request.userAnswers.selectTaxYear.map {
        selectedTaxYear =>
          Ok(anyTaxableForeignIncome(preparedForm, mode, selectedTaxYear))
      }.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      request.userAnswers.selectTaxYear.map {
        selectedTaxYear =>
          form.bindFromRequest().fold(
            (formWithErrors: Form[_]) =>
              Future.successful(BadRequest(anyTaxableForeignIncome(formWithErrors, mode, selectedTaxYear))),
            value =>
              dataCacheConnector.save[AnyTaxPaid](request.externalId, AnyTaxableForeignIncomeId.toString, value).map(cacheMap =>
                Redirect(navigator.nextPage(AnyTaxableForeignIncomeId, mode)(new UserAnswers(cacheMap))))
          )
      }.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }
}
