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

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.OtherCompanyBenefitForm
import identifiers.OtherCompanyBenefitId
import javax.inject.Inject
import models.{Index, Mode, OtherCompanyBenefit}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import utils.{Navigator, SequenceUtil, UserAnswers}
import views.html.otherCompanyBenefit

import scala.concurrent.{ExecutionContext, Future}

class OtherCompanyBenefitController @Inject()(appConfig: FrontendAppConfig,
                                              override val messagesApi: MessagesApi,
                                              dataCacheConnector: DataCacheConnector,
                                              navigator: Navigator,
                                              authenticate: AuthAction,
                                              getData: DataRetrievalAction,
                                              requireData: DataRequiredAction,
                                              otherCompanyBenefit: otherCompanyBenefit,
cc: MessagesControllerComponents,
                                              sequenceUtil: SequenceUtil[OtherCompanyBenefit],
                                              formBuilder: OtherCompanyBenefitForm
                                             )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {


  def onPageLoad(mode: Mode, index: Index): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val form = formBuilder(request.userAnswers.otherCompanyBenefit.getOrElse(Seq.empty), index)

      val preparedForm = request.userAnswers.otherCompanyBenefit match {
        case Some(value) =>
          if (index >= value.length) form else form.fill(value(index))
        case None => form
      }

      request.userAnswers.selectTaxYear.map {
        selectedTaxYear =>
          Ok(otherCompanyBenefit(appConfig, preparedForm, mode, index, selectedTaxYear))
      }.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad)
      }
  }

  def onSubmit(mode: Mode, index: Index): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val form = formBuilder(request.userAnswers.otherCompanyBenefit.getOrElse(Seq.empty), index)

      request.userAnswers.selectTaxYear.map {
        selectedTaxYear =>
          val taxYear = selectedTaxYear
          form.bindFromRequest().fold(
            (formWithErrors: Form[OtherCompanyBenefit]) =>
              Future.successful(BadRequest(otherCompanyBenefit(appConfig, formWithErrors, mode, index, taxYear))),
            value => {
              val companyBenefitNames: Seq[OtherCompanyBenefit] = request.userAnswers.otherCompanyBenefit.getOrElse(Seq(value))
              dataCacheConnector.save[Seq[OtherCompanyBenefit]](
                request.externalId,
                OtherCompanyBenefitId.toString,
                sequenceUtil.updateSeq(companyBenefitNames, index, value)
              ).map(cacheMap =>
                Redirect(navigator.nextPage(OtherCompanyBenefitId, mode)(new UserAnswers(cacheMap))))
            }
          )
      }.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad))
      }
  }
}
