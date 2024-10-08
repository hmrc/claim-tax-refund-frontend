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

import connectors.DataCacheConnector
import controllers.actions._
import forms.{AnyTaxPaidForm, OtherTaxableIncomeForm}
import identifiers._
import javax.inject.Inject
import models._
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Result}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import utils.{Navigator, SequenceUtil, UserAnswers}
import views.html.anyTaxableOtherIncome

import scala.concurrent.{ExecutionContext, Future}

class AnyTaxableOtherIncomeController @Inject()(
                                                override val messagesApi: MessagesApi,
                                                dataCacheConnector: DataCacheConnector,
                                                navigator: Navigator,
                                                authenticate: AuthAction,
                                                getData: DataRetrievalAction,
                                                requireData: DataRequiredAction,
                                                anyTaxableOtherIncome: anyTaxableOtherIncome,
                                                cc: MessagesControllerComponents,
                                                sequenceUtil: SequenceUtil[OtherTaxableIncome],
                                                formProvider: OtherTaxableIncomeForm,
                                                taxPaidFormProvider: AnyTaxPaidForm
                                               )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  private val notSelectedKey = "anyTaxableOtherIncome.notSelected"
  private val blankKey = "anyTaxableOtherIncome.blank"
  private val invalidKey = "anyTaxableOtherIncome.invalid"

  def onPageLoad(mode: Mode, index: Index): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val details: Option[Result] = for {
        selectedTaxYear: SelectTaxYear <- request.userAnswers.selectTaxYear
        otherTaxableIncome: Seq[OtherTaxableIncome] <- request.userAnswers.otherTaxableIncome
      } yield {
        val form: Form[AnyTaxPaid] = taxPaidFormProvider(
          cc.messagesApi.preferred(request).messages(notSelectedKey, otherTaxableIncome(index).name),
          cc.messagesApi.preferred(request).messages(blankKey, otherTaxableIncome(index).name),
          cc.messagesApi.preferred(request).messages(invalidKey, otherTaxableIncome(index).name)
        )

        val preparedForm = request.userAnswers.otherTaxableIncome match {
          case Some(value) =>
            if (index >= value.length || value(index).anyTaxPaid.isEmpty) form else form.fill(value(index).anyTaxPaid.get)
          case None => form
        }

        Ok(anyTaxableOtherIncome(preparedForm, mode, index, selectedTaxYear, otherTaxableIncome(index).name))
      }
      details.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad)
      }
  }

  def onSubmit(mode: Mode, index: Index): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val details: Option[Future[Result]] = for {
        selectedTaxYear: SelectTaxYear <- request.userAnswers.selectTaxYear
        otherTaxableIncome: Seq[OtherTaxableIncome] <- request.userAnswers.otherTaxableIncome
      } yield {
        val form: Form[AnyTaxPaid] = taxPaidFormProvider(
          cc.messagesApi.preferred(request).messages(notSelectedKey, otherTaxableIncome(index).name),
          cc.messagesApi.preferred(request).messages(blankKey, otherTaxableIncome(index).name),
          cc.messagesApi.preferred(request).messages(invalidKey, otherTaxableIncome(index).name)
        )
        form.bindFromRequest().fold(
          (formWithErrors: Form[AnyTaxPaid]) =>
            Future.successful(BadRequest(anyTaxableOtherIncome(formWithErrors, mode, index, selectedTaxYear, otherTaxableIncome(index).name))),
          value => {

            val otherIncomeDetails = OtherTaxableIncome(otherTaxableIncome(index).name, otherTaxableIncome(index).amount, Some(value))

            dataCacheConnector.save[Seq[OtherTaxableIncome]](
              request.externalId, OtherTaxableIncomeId.toString, sequenceUtil.updateSeq(otherTaxableIncome, index, otherIncomeDetails)
            ).map(cacheMap =>
              Redirect(navigator.nextPage(AnyTaxableOtherIncomeId, mode)(new UserAnswers(cacheMap))))
          }
        )
      }
      details.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad))
      }
  }
}
