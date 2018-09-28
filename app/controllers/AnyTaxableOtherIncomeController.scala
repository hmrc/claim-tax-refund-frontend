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
import forms.AnyTaxPaidForm
import identifiers._
import javax.inject.Inject
import models._
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Result}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import uk.gov.hmrc.play.partials.FormPartialRetriever
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.{Navigator, SequenceUtil, UserAnswers}
import views.html.anyTaxableOtherIncome

import scala.concurrent.Future

class AnyTaxableOtherIncomeController @Inject()(appConfig: FrontendAppConfig,
                                                override val messagesApi: MessagesApi,
                                                dataCacheConnector: DataCacheConnector,
                                                navigator: Navigator,
                                                authenticate: AuthAction,
                                                getData: DataRetrievalAction,
                                                requireData: DataRequiredAction,
                                                sequenceUtil: SequenceUtil[FullOtherTaxableIncome],
                                                formProvider: AnyTaxPaidForm,
                                                implicit val formPartialRetriever: FormPartialRetriever,
                                                implicit val templateRenderer: TemplateRenderer) extends FrontendController with I18nSupport {

  private val notSelectedKey = "anyTaxableOtherIncome.notSelected"
  private val blankKey = "anyTaxableOtherIncome.blank"
  private val invalidKey = "anyTaxableOtherIncome.invalid"

  private val form: Form[AnyTaxPaid] = formProvider(notSelectedKey, blankKey, invalidKey)

  def onPageLoad(mode: Mode, index: Index): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.anyTaxableOtherIncome match {
        case Some(value) =>
          if (index >= value.length) form else form.fill(value(index))
        case None => form
      }

      val details: Option[Result] = for {
        selectedTaxYear: SelectTaxYear <- request.userAnswers.selectTaxYear
        otherTaxableIncome: Seq[OtherTaxableIncome] <- request.userAnswers.otherTaxableIncome
      } yield {
        Ok(anyTaxableOtherIncome(appConfig, preparedForm, mode, index, selectedTaxYear, otherTaxableIncome(index).name))
      }
      details.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }

  def onSubmit(mode: Mode, index: Index): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val details: Option[Future[Result]] = for {
        selectedTaxYear: SelectTaxYear <- request.userAnswers.selectTaxYear
        otherTaxableIncome: Seq[FullOtherTaxableIncome] <- request.userAnswers.fullOtherTaxableIncome
      } yield {
        form.bindFromRequest().fold(
          (formWithErrors: Form[AnyTaxPaid]) =>
            Future.successful(BadRequest(anyTaxableOtherIncome(appConfig, formWithErrors, mode, index, selectedTaxYear, otherTaxableIncome(index).name))),
          value => {

            val otherIncomeDetails = FullOtherTaxableIncome(otherTaxableIncome(index).name, otherTaxableIncome(index).amount, Some(value))

            dataCacheConnector.save[Seq[FullOtherTaxableIncome]](
              request.externalId, OtherTaxableIncomeId.toString, sequenceUtil.updateSeq(otherTaxableIncome, index, otherIncomeDetails)
            ).map(cacheMap =>
              Redirect(navigator.nextPage(AnyTaxableOtherIncomeId, mode)(new UserAnswers(cacheMap))))
          }
        )
      }
      details.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }
}
