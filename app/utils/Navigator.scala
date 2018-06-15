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

package utils

import controllers.routes
import identifiers._
import javax.inject.{Inject, Singleton}
import models.WhereToSendPayment.{Myself, Nominee}
import models.{AgentRef, CheckMode, Mode, NormalMode}
import play.api.mvc.Call

@Singleton
class Navigator @Inject()() {

  private val routeMap: Map[Identifier, UserAnswers => Call] = Map(
    SelectTaxYearId -> (_ => routes.EmploymentDetailsController.onPageLoad(NormalMode)),
    EmploymentDetailsId -> employmentDetails,
    AnyBenefitsId -> anyBenefits,
    HowMuchStatePensionId -> (_ => routes.AnyOtherTaxableBenefitsController.onPageLoad(NormalMode)),
    AnyOtherTaxableBenefitsId -> anyOtherBenefits,
    OtherBenefitsDetailsAndAmountId -> (_ => routes.OtherIncomeController.onPageLoad(NormalMode)),
    OtherIncomeId -> otherTaxableIncome,
    HowMuchMedicalBenefitsId -> (_ => routes.AnyOtherTaxableIncomeController.onPageLoad(NormalMode)),
    AnyOtherTaxableIncomeId -> anyOtherTaxableIncome,
    OtherIncomeDetailsAndAmountId -> (_ => routes.WhereToSendPaymentController.onPageLoad(NormalMode)),
    WhereToSendPaymentId -> whereToSendPayment,
    NomineeFullNameId -> (_ => routes.AnyAgentRefController.onPageLoad(NormalMode)),
    IsPaymentAddressInTheUKId -> isPaymentAddressInUkRoute,
    PaymentUKAddressId -> (_ => routes.TelephoneNumberController.onPageLoad(NormalMode)),
    PaymentInternationalAddressId -> (_ => routes.TelephoneNumberController.onPageLoad(NormalMode)),
    TelephoneNumberId -> (_ => routes.CheckYourAnswersController.onPageLoad())
  )

  private val editRouteMap: Map[Identifier, UserAnswers => Call] = Map(

  )

  private def employmentDetails(userAnswers: UserAnswers) = userAnswers.employmentDetails match {
    case Some(true) => routes.AnyBenefitsController.onPageLoad(NormalMode)
    case Some(false) => routes.EnterPayeReferenceController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyBenefits(userAnswers: UserAnswers) = userAnswers.anyBenefits match {
    case Some(true) => ???
    case Some(false) => routes.OtherIncomeController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyOtherBenefits(userAnswers: UserAnswers) = userAnswers.anyOtherTaxableBenefits match {
    case Some(true) => routes.OtherBenefitsDetailsAndAmountController.onPageLoad(NormalMode)
    case Some(false) => routes.OtherIncomeController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def otherTaxableIncome(userAnswers: UserAnswers) = userAnswers.otherIncome match {
    case Some(true) => ???
    case Some(false) => routes.WhereToSendPaymentController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyOtherTaxableIncome(userAnswers: UserAnswers) = userAnswers.anyOtherTaxableIncome match {
    case Some(true) => routes.OtherIncomeDetailsAndAmountController.onPageLoad(NormalMode)
    case Some(false) => routes.WhereToSendPaymentController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def whereToSendPayment(userAnswers: UserAnswers) = userAnswers.whereToSendPayment match {
    case Some(Nominee) => routes.NomineeFullNameController.onPageLoad(NormalMode)
    case Some(Myself) => routes.TelephoneNumberController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyAgentRef(userAnswers: UserAnswers) = userAnswers.anyAgentRef map {
    case AgentRef.Yes(agentRef) => ???
    case AgentRef.No => routes.IsPaymentAddressInTheUKController.onPageLoad(NormalMode)
    case _ => routes.SessionExpiredController.onPageLoad()
  }

  private def isPaymentAddressInUkRoute(userAnswers: UserAnswers) = userAnswers.isPaymentAddressInTheUK match {
    case Some(true) => routes.PaymentUKAddressController.onPageLoad(NormalMode)
    case Some(false) => routes.PaymentInternationalAddressController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  def nextPage(id: Identifier, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode =>
      routeMap.getOrElse(id, _ => routes.IndexController.onPageLoad())
    case CheckMode =>
      editRouteMap.getOrElse(id, _ => routes.CheckYourAnswersController.onPageLoad())
  }
}
