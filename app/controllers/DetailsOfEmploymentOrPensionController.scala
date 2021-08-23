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
import controllers.actions._
import forms.DetailsOfEmploymentOrPensionForm
import identifiers.DetailsOfEmploymentOrPensionId
import javax.inject.Inject
import models.Mode
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.play.partials.FormPartialRetriever
import utils.{Navigator, UserAnswers}
import views.html.detailsOfEmploymentOrPension

import scala.concurrent.{ExecutionContext, Future}

class DetailsOfEmploymentOrPensionController @Inject()(
                                                        appConfig: FrontendAppConfig,
                                                        override val messagesApi: MessagesApi,
                                                        dataCacheConnector: DataCacheConnector,
                                                        navigator: Navigator,
                                                        authenticate: AuthAction,
                                                        getData: DataRetrievalAction,
                                                        requireData: DataRequiredAction,
                                                        detailsOfEmploymentOrPension: detailsOfEmploymentOrPension,
cc: MessagesControllerComponents,
                                                        formBuilder: DetailsOfEmploymentOrPensionForm
                                                      )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  private val form: Form[String] = formBuilder()
  private val characterLimit: Int = appConfig.detailsOfEmploymentOrPensionMaxLength

  def onPageLoad(mode: Mode) = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.detailsOfEmploymentOrPension match {
        case None => form
        case Some(value) => form.fill(value)
      }

      request.userAnswers.selectTaxYear.map {
        selectedTaxYear =>
          val taxYear = selectedTaxYear
          Ok(detailsOfEmploymentOrPension(appConfig, preparedForm, mode, taxYear, characterLimit))
      }.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }

  def onSubmit(mode: Mode) = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      request.userAnswers.selectTaxYear.map {
        selectedTaxYear =>
          val taxYear = selectedTaxYear
          form.bindFromRequest().fold(
            (formWithErrors: Form[_]) =>
              Future.successful(BadRequest(detailsOfEmploymentOrPension(appConfig, formWithErrors, mode, taxYear, characterLimit))),
            value =>
              dataCacheConnector.save[String](request.externalId, DetailsOfEmploymentOrPensionId.toString, value).map(cacheMap =>
                Redirect(navigator.nextPage(DetailsOfEmploymentOrPensionId, mode)(new UserAnswers(cacheMap))))
          )
      }.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }
}
