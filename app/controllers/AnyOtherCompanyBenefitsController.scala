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
import identifiers.AnyOtherCompanyBenefitsId
import models.{Mode, OtherCompanyBenefit, SelectTaxYear}
import play.api.mvc.{Action, AnyContent, Result}
import uk.gov.hmrc.play.partials.FormPartialRetriever
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.{Navigator, UserAnswers}
import views.html.anyOtherCompanyBenefits

import scala.concurrent.Future

class AnyOtherCompanyBenefitsController @Inject()(appConfig: FrontendAppConfig,
                                                  override val messagesApi: MessagesApi,
                                                  dataCacheConnector: DataCacheConnector,
                                                  navigator: Navigator,
                                                  authenticate: AuthAction,
                                                  getData: DataRetrievalAction,
                                                  requireData: DataRequiredAction,
                                                  formProvider: BooleanForm,
                                                  implicit val formPartialRetriever: FormPartialRetriever,
                                                  implicit val templateRenderer: TemplateRenderer) extends FrontendController with I18nSupport {

  private val errorKey = "anyOtherCompanyBenefits.blank"
  val form: Form[Boolean] = formProvider(errorKey)

  def onPageLoad(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val result: Option[Result] = for {
        taxYear: SelectTaxYear <- request.userAnswers.selectTaxYear
        otherCompanyBenefitNames: Seq[String] <- request.userAnswers.otherCompanyBenefit.map(_.map(_.name))
      } yield {
        Ok(anyOtherCompanyBenefits(appConfig, form, mode, taxYear, otherCompanyBenefitNames))
      }

      result.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val result: Option[Future[Result]] = for {
        taxYear: SelectTaxYear <- request.userAnswers.selectTaxYear
        otherCompanyBenefitNames: Seq[String] <- request.userAnswers.otherCompanyBenefit.map(_.map(_.name))
      } yield {
        form.bindFromRequest().fold(
          (formWithErrors: Form[_]) =>
            Future.successful(BadRequest(anyOtherCompanyBenefits(appConfig, formWithErrors, mode, taxYear, otherCompanyBenefitNames))),
          value =>
            dataCacheConnector.save[Boolean](request.externalId, AnyOtherCompanyBenefitsId.toString, value).map(cacheMap =>
              Redirect(navigator.nextPage(AnyOtherCompanyBenefitsId, mode)(new UserAnswers(cacheMap))))
        )
      }

      result.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }
}
