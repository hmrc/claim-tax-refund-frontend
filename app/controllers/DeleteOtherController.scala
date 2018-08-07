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
import play.api.mvc.{Action, AnyContent, Result}
import utils.{Navigator, UserAnswers}
import views.html.deleteOther

import scala.concurrent.Future

class DeleteOtherController @Inject()(appConfig: FrontendAppConfig,
                                      override val messagesApi: MessagesApi,
                                      dataCacheConnector: DataCacheConnector,
                                      navigator: Navigator,
                                      authenticate: AuthAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      formProvider: BooleanForm) extends FrontendController with I18nSupport {

  private val errorKey = "deleteOther.blank"
  val form: Form[Boolean] = formProvider(errorKey)

  def onPageLoad(mode: Mode, index: Index, itemName: String, collectionId: String): Action[AnyContent] = (authenticate andThen getData andThen requireData) {
    implicit request =>
      Ok(deleteOther(appConfig, form, mode, index, itemName, collectionId))
  }

  def onSubmit(mode: Mode, index: Index, itemName: String, collectionId: String): Action[AnyContent] = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      form.bindFromRequest().fold(
        (formWithErrors: Form[_]) =>
          Future.successful(BadRequest(deleteOther(appConfig, formWithErrors, mode, index, itemName, collectionId))),
        value =>
          if (value) {

            collectionId match {

              case "otherBenefits" =>
                val result: Option[Future[Result]] = for {
                  collection: Seq[OtherBenefit] <- request.userAnswers.otherBenefit
                } yield {
                  val newColl: Seq[OtherBenefit] = collection.filterNot(_ == collection(index))
                  dataCacheConnector.save[Seq[OtherBenefit]](request.externalId, OtherBenefitId.toString, newColl).map(_ =>
                    Redirect(routes.CheckYourAnswersController.onPageLoad()))
                }

                result.getOrElse {
                  Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
                }

              case "otherCompanyBenefits" =>
                val result: Option[Future[Result]] = for {
                  collection: Seq[OtherCompanyBenefit] <- request.userAnswers.otherCompanyBenefit
                } yield {
                  val newColl: Seq[OtherCompanyBenefit] = collection.filterNot(_ == collection(index))
                  dataCacheConnector.save[Seq[OtherCompanyBenefit]](request.externalId, OtherCompanyBenefitId.toString, newColl).map(_ =>
                    Redirect(routes.CheckYourAnswersController.onPageLoad()))
                }

                result.getOrElse {
                  Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
                }

              case "otherTaxableIncome" =>
                val result: Option[Future[Result]] = for {
                  otherTaxableIncome: Seq[OtherTaxableIncome] <- request.userAnswers.otherTaxableIncome
                  anyTaxableOtherIncome: Seq[AnyTaxPaid] <- request.userAnswers.anyTaxableOtherIncome
                } yield {
                  val newOtherTaxableIncome: Seq[OtherTaxableIncome] = otherTaxableIncome.filterNot(_ == otherTaxableIncome(index))
                  val newAnyTaxableOtherIncome: Seq[AnyTaxPaid] = anyTaxableOtherIncome.filterNot(_ == anyTaxableOtherIncome(index))
                  for {
                    _ <- dataCacheConnector.save[Seq[OtherTaxableIncome]](request.externalId, OtherTaxableIncomeId.toString, newOtherTaxableIncome)
                    _ <- dataCacheConnector.save[Seq[AnyTaxPaid]](request.externalId, AnyTaxableOtherIncomeId.toString, newAnyTaxableOtherIncome)
                  } yield Redirect(routes.CheckYourAnswersController.onPageLoad())
                }

                result.getOrElse {
                  Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
                }

              case _ =>
                Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))

            }
          } else {
            Future.successful(Redirect(routes.CheckYourAnswersController.onPageLoad()))
          }
      )
  }
}
