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
import models.requests.DataRequest
import play.api.mvc.{Action, AnyContent, Result}
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.play.partials.FormPartialRetriever
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.{Navigator, UserAnswers}
import views.html.deleteOther

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class DeleteOtherController @Inject()(appConfig: FrontendAppConfig,
                                      override val messagesApi: MessagesApi,
                                      dataCacheConnector: DataCacheConnector,
                                      navigator: Navigator,
                                      authenticate: AuthAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      formProvider: BooleanForm,
                                      implicit val formPartialRetriever: FormPartialRetriever,
                                      implicit val templateRenderer: TemplateRenderer) extends FrontendController with I18nSupport {

  private val errorKey = "deleteOther.blank"
  val form: Form[Boolean] = formProvider(errorKey)

  def onPageLoad(mode: Mode, index: Index, itemName: String, collectionId: String): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      request.userAnswers.selectTaxYear.map {
        taxYear =>
          Ok(deleteOther(appConfig, form, mode, index, itemName, collectionId, taxYear))
      }.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }

  def onSubmit(mode: Mode, index: Index, itemName: String, collectionId: String): Action[AnyContent] =
    (authenticate andThen getData andThen requireData).async {
      implicit request =>
        request.userAnswers.selectTaxYear.map {
          taxYear =>
            form.bindFromRequest().fold(
              (formWithErrors: Form[_]) =>
                Future.successful(BadRequest(deleteOther(appConfig, formWithErrors, mode, index, itemName, collectionId, taxYear))),
              success = (value: Boolean) =>
                if (value) {
                  collectionId match {
                    case OtherBenefit.collectionId =>
                      deleteOtherBenefit(request, mode, index)
                    case OtherCompanyBenefit.collectionId =>
                      deleteOtherCompanyBenefit(request, mode, index)
                    case OtherTaxableIncome.collectionId =>
                      deleteOtherTaxableIncome(request, mode, index)
                    case _ =>
                      Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
                  }
                } else {
                  collectionId match {
                    case OtherBenefit.collectionId =>
                      Future.successful(Redirect(routes.AnyOtherBenefitsController.onPageLoad(mode)))
                    case OtherCompanyBenefit.collectionId =>
                      Future.successful(Redirect(routes.AnyOtherCompanyBenefitsController.onPageLoad(mode)))
                    case OtherTaxableIncome.collectionId =>
                      Future.successful(Redirect(routes.AnyOtherTaxableIncomeController.onPageLoad(mode)))
                    case _ =>
                      Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
                  }
                }
            )
        }.getOrElse {
          Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
        }
    }

  def deleteOtherBenefit(request: DataRequest[AnyContent], mode: Mode, index: Index): Future[Result] = {
    val result: Option[Future[Result]] = for {
      otherBenefit: Seq[OtherBenefit] <- request.userAnswers.otherBenefit
    } yield {
      val updatedOtherBenefit: Seq[OtherBenefit] = otherBenefit.patch(index, Seq.empty, 1)
      dataCacheConnector.save[Seq[OtherBenefit]](request.externalId, OtherBenefitId.toString, updatedOtherBenefit).map(
        _ =>
          Redirect(routes.AnyOtherBenefitsController.onPageLoad(mode))
      )
    }

    result.getOrElse {
      Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
    }
  }

  def deleteOtherCompanyBenefit(request: DataRequest[AnyContent], mode: Mode, index: Index): Future[Result] = {
    val result: Option[Future[Result]] = for {
      otherCompanyBenefit: Seq[OtherCompanyBenefit] <- request.userAnswers.otherCompanyBenefit
    } yield {
      val updatedOtherCompanyBenefit: Seq[OtherCompanyBenefit] = otherCompanyBenefit.patch(index, Seq.empty, 1)
      dataCacheConnector.save[Seq[OtherCompanyBenefit]](request.externalId, OtherCompanyBenefitId.toString, updatedOtherCompanyBenefit).map(
        _ =>
          Redirect(routes.AnyOtherCompanyBenefitsController.onPageLoad(mode))
      )
    }

    result.getOrElse {
      Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
    }
  }

  def deleteOtherTaxableIncome(request: DataRequest[AnyContent], mode: Mode, index: Index): Future[Result] = {
    val result: Option[Future[Result]] = for {
      otherTaxableIncome: Seq[OtherTaxableIncome] <- request.userAnswers.otherTaxableIncome
    } yield {
      val updatedOtherTaxableIncome: Seq[OtherTaxableIncome] = otherTaxableIncome.patch(index, Seq.empty, 1)
      for {
        _ <- dataCacheConnector.save[Seq[OtherTaxableIncome]](request.externalId, OtherTaxableIncomeId.toString, updatedOtherTaxableIncome)
        updatedCacheMap: CacheMap <- dataCacheConnector.save[Seq[OtherTaxableIncome]](request.externalId, AnyTaxableOtherIncomeId.toString, updatedOtherTaxableIncome)
      } yield
        Redirect(navigator.nextPage(DeleteOtherTaxableIncomeId, mode)(new UserAnswers(updatedCacheMap)))
    }

    result.getOrElse {
      Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
    }
  }
}
