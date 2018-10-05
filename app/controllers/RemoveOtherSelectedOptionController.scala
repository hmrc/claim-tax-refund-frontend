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
import connectors.DataCacheConnector
import controllers.actions._
import config.FrontendAppConfig
import forms.BooleanForm
import identifiers._
import models._
import play.api.libs.json.JsArray
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.partials.FormPartialRetriever
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.{Navigator, UserAnswers}
import views.html.removeOtherSelectedOption

import scala.concurrent.Future

class RemoveOtherSelectedOptionController @Inject()(appConfig: FrontendAppConfig,
																										override val messagesApi: MessagesApi,
																										dataCacheConnector: DataCacheConnector,
																										navigator: Navigator,
																										authenticate: AuthAction,
																										getData: DataRetrievalAction,
																										requireData: DataRequiredAction,
																										formProvider: BooleanForm,
																										implicit val formPartialRetriever: FormPartialRetriever,
																										implicit val templateRenderer: TemplateRenderer) extends FrontendController with I18nSupport {

	private val errorKey = "RemoveOtherSelectedOption.blank"
	val form: Form[Boolean] = formProvider(errorKey)

	def onPageLoad(mode: Mode, collectionId: String): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
		implicit request =>
			val preparedForm = request.userAnswers.removeOtherSelectedOption match {
				case None => form
				case Some(value) => form.fill(value)
			}

			request.userAnswers.selectTaxYear.map {
				selectedTaxYear =>
					Ok(removeOtherSelectedOption(appConfig, preparedForm, mode, selectedTaxYear, collectionId))
			}.getOrElse {
				Redirect(routes.SessionExpiredController.onPageLoad())
			}
	}

	def onSubmit(mode: Mode, collectionId: String): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
		implicit request =>
			request.userAnswers.selectTaxYear.map {
				selectedTaxYear =>
					val taxYear = selectedTaxYear
					form.bindFromRequest().fold(
						(formWithErrors: Form[_]) =>
							Future.successful(BadRequest(removeOtherSelectedOption(appConfig, formWithErrors, mode, taxYear, collectionId))),
						(value: Boolean) => {

							if(!value) {
								collectionId match {
									case OtherBenefit.collectionId  =>
										dataCacheConnector.save(request.externalId, RemoveOtherBenefitId.toString, value)
									case OtherCompanyBenefit.collectionId =>
										dataCacheConnector.removeFromCollection(request.externalId, SelectCompanyBenefitsId.toString, CompanyBenefits.OTHER_COMPANY_BENEFIT)
									case OtherTaxableIncome.collectionId =>
										dataCacheConnector.removeFromCollection(request.externalId, SelectTaxableIncomeId.toString, TaxableIncome.OTHER_TAXABLE_INCOME)
								}
							}

							dataCacheConnector.save[Boolean](request.externalId, RemoveOtherSelectedOptionId.toString, value).map(cacheMap =>
								Redirect(navigator.nextPageWithCollectionId(collectionId, mode)(new UserAnswers(cacheMap))))
						}
					)
			}.getOrElse {
				Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
			}
	}
}
