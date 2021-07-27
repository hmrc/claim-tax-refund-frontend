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

import config.{FrontendAppConfig, LocalTemplateRenderer}
import connectors.DataCacheConnector
import controllers.actions._
import forms.BooleanForm
import identifiers.AnyOtherTaxableIncomeId
import javax.inject.Inject
import models._
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, Result}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.play.partials.FormPartialRetriever
import utils.{Navigator, UserAnswers}
import views.html.anyOtherTaxableIncome

import scala.concurrent.{ExecutionContext, Future}

class AnyOtherTaxableIncomeController @Inject()(appConfig: FrontendAppConfig,
                                                override val messagesApi: MessagesApi,
                                                dataCacheConnector: DataCacheConnector,
                                                navigator: Navigator,
                                                authenticate: AuthAction,
                                                getData: DataRetrievalAction,
                                                requireData: DataRequiredAction,
                                                anyOtherTaxableIncome: anyOtherTaxableIncome,
                                                cc: MessagesControllerComponents,
                                                formProvider: BooleanForm

                                               )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  private val errorKey = "anyOtherTaxableIncome.blank"

  def onPageLoad(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val form: Form[Boolean] = formProvider(cc.messagesApi.preferred(request).messages.apply(errorKey))

      val result: Option[Result] = for {
        taxYear <- request.userAnswers.selectTaxYear
        otherTaxableIncome <- request.userAnswers.otherTaxableIncome
      } yield {
        val otherTaxableIncomeWithIndex = otherTaxableIncome.zipWithIndex
        val complete: Seq[(OtherTaxableIncome, Int)] = otherTaxableIncomeWithIndex.filter(_._1.anyTaxPaid.isDefined)
        val incomplete: Seq[(OtherTaxableIncome, Int)] = otherTaxableIncomeWithIndex.filter(_._1.anyTaxPaid.isEmpty)
        Ok(anyOtherTaxableIncome(appConfig, form, mode, taxYear, complete, incomplete))
      }

      result.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val form: Form[Boolean] = formProvider(cc.messagesApi.preferred(request).messages.apply(errorKey))

      val result: Option[Future[Result]] = for {
        taxYear: SelectTaxYear <- request.userAnswers.selectTaxYear
        otherTaxableIncome: Seq[OtherTaxableIncome] <- request.userAnswers.otherTaxableIncome
      } yield {
        val otherTaxableIncomeWithIndex = otherTaxableIncome.zipWithIndex
        val complete: Seq[(OtherTaxableIncome, Int)] = otherTaxableIncomeWithIndex.filter(_._1.anyTaxPaid.isDefined)
        val incomplete: Seq[(OtherTaxableIncome, Int)] = otherTaxableIncomeWithIndex.filter(_._1.anyTaxPaid.isEmpty)
        form.bindFromRequest().fold(
          (formWithErrors: Form[_]) =>
            Future.successful(BadRequest(anyOtherTaxableIncome(appConfig, formWithErrors, mode, taxYear, complete, incomplete))),
          value =>
            dataCacheConnector.save[Boolean](request.externalId, AnyOtherTaxableIncomeId.toString, value).map(cacheMap =>
              Redirect(navigator.nextPage(AnyOtherTaxableIncomeId, mode)(new UserAnswers(cacheMap))))
        )
      }

      result.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }
}
