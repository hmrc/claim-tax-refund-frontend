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
import forms.HowMuchOtherBenefitForm
import identifiers.{HowMuchOtherBenefitId, OtherBenefitsId}
import javax.inject.Inject
import models.{Mode, OtherBenefit, SelectTaxYear}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import utils.{Navigator, UserAnswers}
import views.html.howMuchOtherBenefit

import scala.concurrent.Future

class HowMuchOtherBenefitController @Inject()(
                                        appConfig: FrontendAppConfig,
                                        override val messagesApi: MessagesApi,
                                        dataCacheConnector: DataCacheConnector,
                                        navigator: Navigator,
                                        authenticate: AuthAction,
                                        getData: DataRetrievalAction,
                                        requireData: DataRequiredAction,
                                        formBuilder: HowMuchOtherBenefitForm) extends FrontendController with I18nSupport {

  private val form: Form[String] = formBuilder()

  def onPageLoad(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.howMuchOtherBenefit match {
        case None => form
        case Some(value) => form.fill(value)
      }

      request.userAnswers.selectTaxYear.map{
        selectedTaxYear =>
          val taxYear = selectedTaxYear
          Ok(howMuchOtherBenefit(appConfig, preparedForm, mode, taxYear))
      }.getOrElse{
        Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val req = for {
        selectedTaxYear: SelectTaxYear <- request.userAnswers.selectTaxYear
        otherBenefitName: String <- request.userAnswers.otherBenefitsName
      } yield {
          form.bindFromRequest().fold(
            (formWithErrors: Form[_]) =>
              Future.successful(BadRequest(howMuchOtherBenefit(appConfig, formWithErrors, mode, selectedTaxYear))),
            value => {
              val otherBenefits: Seq[OtherBenefit] = request.userAnswers.otherBenefits.getOrElse(Seq(OtherBenefit(otherBenefitName, value)))
              if (otherBenefits.forall(p => !p.name.contains(otherBenefitName))) {
                otherBenefits :+ OtherBenefit(otherBenefitName, value)
              }
              dataCacheConnector.save[Seq[OtherBenefit]](request.externalId, OtherBenefitsId.toString, otherBenefits).map(
                cacheMap =>
                  Redirect(navigator.nextPage(HowMuchOtherBenefitId, mode)(new UserAnswers(cacheMap))))
            }
          )
      }
      req.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }
}
