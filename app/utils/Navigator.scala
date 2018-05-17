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
import models.WhereToSendPayment.{SomeoneElse, You}
import models.{CheckMode, Mode, NormalMode}
import play.api.mvc.Call

@Singleton
class Navigator @Inject()() {

  private val routeMap: Map[Identifier, UserAnswers => Call] = Map(
    UserDetailsId -> (_ => routes.SelectTaxYearController.onPageLoad(NormalMode)),
    SelectTaxYearId -> (_ => routes.EmploymentDetailsController.onPageLoad(NormalMode)),
    EmploymentDetailsId -> (_ => routes.AnyBenefitsController.onPageLoad(NormalMode)),
    AnyBenefitsId -> anyBenefits,
    AnyJobseekersAllowanceId -> anyJobseekers,
    HowMuchJobseekersAllowanceId -> (_ => routes.AnyIncapacityBenefitController.onPageLoad(NormalMode)),
    AnyIncapacityBenefitId -> anyIncapacity,
    HowMuchIncapacityBenefitId -> (_ => routes.AnyEmploymentAndSupportAllowanceController.onPageLoad(NormalMode)),
    AnyEmploymentAndSupportAllowanceId -> anyEmploymentAndSupport,
    HowMuchEmploymentAndSupportAllowanceId -> (_ => routes.AnyStatePensionController.onPageLoad(NormalMode)),
    AnyStatePensionId -> anyStatePension,
    HowMuchStatePensionId -> (_ => routes.AnyOtherTaxableBenefitsController.onPageLoad(NormalMode)),
    AnyOtherTaxableBenefitsId -> anyOtherBenefits,
    OtherBenefitsDetailsAndAmountId -> (_ => routes.OtherIncomeController.onPageLoad(NormalMode)),
    OtherIncomeId -> otherTaxableIncome,
    HowMuchCarBenefitsId -> (_ => routes.AnyRentalIncomeController.onPageLoad(NormalMode)),
    AnyRentalIncomeId -> anyRental,
    HowMuchRentalIncomeId -> (_ => routes.AnyBankBuildingSocietyInterestController.onPageLoad(NormalMode)),
    AnyBankBuildingSocietyInterestId -> anyBankBuildingInterest,
    HowMuchBankBuildingSocietyInterestId -> (_ => routes.AnyMedicalBenefitsController.onPageLoad(NormalMode)),
    AnyMedicalBenefitsId -> anyMedicalBenefit,
    HowMuchMedicalBenefitsId -> (_ => routes.AnyOtherTaxableIncomeController.onPageLoad(NormalMode)),
    AnyOtherTaxableIncomeId -> anyOtherTaxableIncome,
    OtherIncomeDetailsAndAmountId -> (_ => routes.WhereToSendPaymentController.onPageLoad(NormalMode)),
    WhereToSendPaymentId -> whereToSendPayment,
    PayeeFullNameId -> (_ => routes.AnyAgentRefController.onPageLoad(NormalMode)),
    AnyAgentRefId -> anyAgentRef,
    AgentReferenceNumberId -> (_ => routes.IsPayeeAddressInTheUKController.onPageLoad(NormalMode)),
    IsPayeeAddressInTheUKId -> isPayeeAddressInUkRoute,
    PayeeUKAddressId -> (_ => routes.TelephoneNumberController.onPageLoad(NormalMode)),
    PayeeInternationalAddressId -> (_ => routes.TelephoneNumberController.onPageLoad(NormalMode)),
    TelephoneNumberId -> (_ => routes.CheckYourAnswersController.onPageLoad())
  )

  private val editRouteMap: Map[Identifier, UserAnswers => Call] = Map(

  )

  private def anyBenefits(userAnswers: UserAnswers) = userAnswers.anyBenefits match {
    case Some(true) => routes.AnyJobseekersAllowanceController.onPageLoad(NormalMode)
    case Some(false) => routes.OtherIncomeController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyJobseekers(userAnswers: UserAnswers) = userAnswers.anyJobseekersAllowance match {
    case Some(true) => routes.HowMuchJobseekersAllowanceController.onPageLoad(NormalMode)
    case Some(false) => routes.AnyIncapacityBenefitController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyIncapacity(userAnswers: UserAnswers) = userAnswers.anyIncapacityBenefit match {
    case Some(true) => routes.HowMuchIncapacityBenefitController.onPageLoad(NormalMode)
    case Some(false) => routes.AnyEmploymentAndSupportAllowanceController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

private def anyEmploymentAndSupport(userAnswers: UserAnswers) = userAnswers.anyEmploymentAndSupportAllowance match {
    case Some(true) => routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(NormalMode)
    case Some(false) => routes.AnyStatePensionController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyStatePension(userAnswers: UserAnswers) = userAnswers.anyStatePension match {
    case Some(true) => routes.HowMuchStatePensionController.onPageLoad(NormalMode)
    case Some(false) => routes.AnyOtherTaxableBenefitsController.onPageLoad(NormalMode)
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

  private def anyRental(userAnswers: UserAnswers) = userAnswers.anyRentalIncome match {
    case Some(true) => routes.HowMuchRentalIncomeController.onPageLoad(NormalMode)
    case Some(false) => routes.AnyBankBuildingSocietyInterestController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyBankBuildingInterest(userAnswers: UserAnswers) = userAnswers.anyBankBuildingSocietyInterest match {
    case Some(true) => routes.HowMuchBankBuildingSocietyInterestController.onPageLoad(NormalMode)
    case Some(false) => routes.AnyMedicalBenefitsController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyMedicalBenefit(userAnswers: UserAnswers) = userAnswers.anyMedicalBenefits match {
    case Some(true) => routes.HowMuchMedicalBenefitsController.onPageLoad(NormalMode)
    case Some(false) => routes.AnyOtherTaxableIncomeController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyOtherTaxableIncome(userAnswers: UserAnswers) = userAnswers.anyOtherTaxableIncome match {
    case Some(true) => routes.OtherIncomeDetailsAndAmountController.onPageLoad(NormalMode)
    case Some(false) => routes.WhereToSendPaymentController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def whereToSendPayment(userAnswers: UserAnswers) = userAnswers.whereToSendPayment match {
    case Some(SomeoneElse) => routes.PayeeFullNameController.onPageLoad(NormalMode)
    case Some(You) => routes.TelephoneNumberController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyAgentRef(userAnswers: UserAnswers) = userAnswers.anyAgentRef match {
    case Some(true) => routes.AgentReferenceNumberController.onPageLoad(NormalMode)
    case Some(false) => routes.IsPayeeAddressInTheUKController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def isPayeeAddressInUkRoute(userAnswers: UserAnswers) = userAnswers.isPayeeAddressInTheUK match {
    case Some(true) => routes.PayeeUKAddressController.onPageLoad(NormalMode)
    case Some(false) => routes.PayeeInternationalAddressController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  def nextPage(id: Identifier, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode =>
      routeMap.getOrElse(id, _ => routes.IndexController.onPageLoad())
    case CheckMode =>
      editRouteMap.getOrElse(id, _ => routes.CheckYourAnswersController.onPageLoad())
  }
}
