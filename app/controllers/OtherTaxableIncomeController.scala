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
import forms.OtherTaxableIncomeForm
import identifiers.OtherTaxableIncomeId
import javax.inject.Inject
import models.{Index, Mode, OtherTaxableIncome}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import utils.{Navigator, SequenceUtil, UserAnswers}
import views.html.otherTaxableIncome

import scala.concurrent.{ExecutionContext, Future}

class OtherTaxableIncomeController @Inject()(
                                              override val messagesApi: MessagesApi,
                                              dataCacheConnector: DataCacheConnector,
                                              navigator: Navigator,
                                              authenticate: AuthAction,
                                              getData: DataRetrievalAction,
                                              requireData: DataRequiredAction,
                                              otherTaxableIncome: otherTaxableIncome,
                                              cc: MessagesControllerComponents,
                                              sequenceUtil: SequenceUtil[OtherTaxableIncome],
                                              formBuilder: OtherTaxableIncomeForm
                                            )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  def onPageLoad(mode: Mode, index: Index): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val form: Form[OtherTaxableIncome] = formBuilder(request.userAnswers.otherTaxableIncome.getOrElse(Seq.empty), index)

      val preparedForm = request.userAnswers.otherTaxableIncome match {
        case Some(value) =>
          if (index >= value.length) form else form.fill(value(index))
        case None => form
      }

      request.userAnswers.selectTaxYear.map {
        selectedTaxYear =>
          val taxYear = selectedTaxYear
          Ok(otherTaxableIncome(preparedForm, mode, index, taxYear))
      }.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad)
      }
  }

  def onSubmit(mode: Mode, index: Index): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val form: Form[OtherTaxableIncome] = formBuilder(request.userAnswers.otherTaxableIncome.getOrElse(Seq.empty), index)

      request.userAnswers.selectTaxYear.map {
        selectedTaxYear =>
          val taxYear = selectedTaxYear
          form.bindFromRequest().fold(
            (formWithErrors: Form[OtherTaxableIncome]) =>
              Future.successful(BadRequest(otherTaxableIncome(formWithErrors, mode, index, taxYear))),
            value => {
              val otherTaxableIncome: OtherTaxableIncome = request.userAnswers.otherTaxableIncome match {
                case Some(otherTaxableIncomes) =>
                  if (index >= otherTaxableIncomes.length) {
                    OtherTaxableIncome(value.name, value.amount, None)
                  } else {
                    val anyTaxPaid = request.userAnswers.otherTaxableIncome.flatMap(value => value(index).anyTaxPaid)
                    OtherTaxableIncome(value.name, value.amount, anyTaxPaid)
                  }
                case None =>
                  OtherTaxableIncome(value.name, value.amount, None)
              }

              val otherTaxableIncomeList: Seq[OtherTaxableIncome] = request.userAnswers.otherTaxableIncome.getOrElse(Seq(otherTaxableIncome))

              dataCacheConnector.save[Seq[OtherTaxableIncome]](request.externalId, OtherTaxableIncomeId.toString,
                sequenceUtil.updateSeq(otherTaxableIncomeList, index, otherTaxableIncome)
              ).map(cacheMap =>
                Redirect(navigator.nextPageWithIndex(OtherTaxableIncomeId(index), mode)(new UserAnswers(cacheMap))))
            }
          )
      }.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad))
      }
  }
}
