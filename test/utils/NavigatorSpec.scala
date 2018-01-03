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

import base.SpecBase
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import controllers.routes
import identifiers._
import models.FullOrPartialClaim.{OptionAll, OptionSome}
import models.TypeOfClaim.{OptionPAYE, OptionSA}
import models.WhereToSendPayment.OptionSomeoneElse
import models._

class NavigatorSpec extends SpecBase with MockitoSugar {

  val navigator = new Navigator

  "Navigator" when {

    "in Normal mode" must {
      "go to Index from an identifier that doesn't exist in the route map" in {
        case object UnknownIdentifier extends Identifier
        navigator.nextPage(UnknownIdentifier, NormalMode)(mock[UserAnswers]) mustBe routes.IndexController.onPageLoad()
      }

      "go to Nino from FullName" in {
        navigator.nextPage(FullNameId, NormalMode)(mock[UserAnswers]) mustBe routes.NationalInsuranceNumberController.onPageLoad(NormalMode)
      }

      "go to isTheAddressInTheUK from Nino" in {
        navigator.nextPage(NationalInsuranceNumberId, NormalMode)(mock[UserAnswers]) mustBe routes.IsTheAddressInTheUKController.onPageLoad(NormalMode)
      }

      "go to UkAddress from isTheAddressInTheUK when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.isTheAddressInTheUK) thenReturn Some(true)
        navigator.nextPage(IsTheAddressInTheUKId, NormalMode)(answers) mustBe routes.UkAddressController.onPageLoad(NormalMode)
      }

      "go to InternationalAddress from isTheAddressInTheUK when No is selected" in {
        val answers = mock[UserAnswers]
        when(answers.isTheAddressInTheUK) thenReturn Some(false)
        navigator.nextPage(IsTheAddressInTheUKId, NormalMode)(answers) mustBe routes.InternationalAddressController.onPageLoad(NormalMode)
      }

      "go to TelephoneNumber from UkAddress" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(UkAddressId, NormalMode)(answers) mustBe routes.TelephoneNumberController.onPageLoad(NormalMode)
      }

      "go to TelephoneNumber from InternationalAddress" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(InternationalAddressId, NormalMode)(answers) mustBe routes.TelephoneNumberController.onPageLoad(NormalMode)
      }

      "go to TypeOfClaim from TelephoneNumber" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(TelephoneNumberId, NormalMode)(answers) mustBe routes.TypeOfClaimController.onPageLoad(NormalMode)
      }

      "go to UniqueTaxpayerReference from TypeOfClaim when SA is selected" in {
        val answers = mock[UserAnswers]
        when(answers.typeOfClaim) thenReturn Some(OptionSA)
        navigator.nextPage(TypeOfClaimId, NormalMode)(answers) mustBe routes.UniqueTaxpayerReferenceController.onPageLoad(NormalMode)
      }

      "go to PayAsYouEarn from TypeOfClaim when PAYE is selected" in {
        val answers = mock[UserAnswers]
        when(answers.typeOfClaim) thenReturn Some(OptionPAYE)
        navigator.nextPage(TypeOfClaimId, NormalMode)(answers) mustBe routes.PayAsYouEarnController.onPageLoad(NormalMode)
      }

      "go to FullOrPartialClaim from UniqueTaxpayerReference" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(UniqueTaxpayerReferenceId, NormalMode)(answers) mustBe routes.FullOrPartialClaimController.onPageLoad(NormalMode)
      }

      "go to SelectTaxYear from PayAsYouEarn" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(PayAsYouEarnId, NormalMode)(answers) mustBe routes.SelectTaxYearController.onPageLoad(NormalMode)
      }

      "go to PartialClaimAmount from TypeOfClaim when FullOrPartialClaim is selected" in {
        val answers = mock[UserAnswers]
        when(answers.fullOrPartialClaim) thenReturn Some(OptionSome)
        navigator.nextPage(FullOrPartialClaimId, NormalMode)(answers) mustBe routes.PartialClaimAmountController.onPageLoad(NormalMode)
      }

      "go to WhereToSendPayment from TypeOfClaim when FullClaim is selected" in {
        val answers = mock[UserAnswers]
        when(answers.fullOrPartialClaim) thenReturn Some(OptionAll)
        navigator.nextPage(FullOrPartialClaimId, NormalMode)(answers) mustBe routes.WhereToSendPaymentController.onPageLoad(NormalMode)
      }

      "go to WhereToSendPayments from PartialClaimAmount" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(PartialClaimAmountId, NormalMode)(answers) mustBe routes.WhereToSendPaymentController.onPageLoad(NormalMode)
      }

      "go to WhereToSendPayment from AnyOtherTaxableIncome when no is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyOtherTaxableIncome) thenReturn Some(false)
        navigator.nextPage(AnyOtherTaxableIncomeId, NormalMode)(answers) mustBe routes.WhereToSendPaymentController.onPageLoad(NormalMode)
      }

      "go to WhereToSendPayments from OtherIncomeDetailsAndAmount" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(OtherIncomeDetailsAndAmountId, NormalMode)(answers) mustBe routes.WhereToSendPaymentController.onPageLoad(NormalMode)
      }

      "go to PayeeFullName from WhereToSendPayment when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.whereToSendPayment) thenReturn Some(OptionSomeoneElse)
        navigator.nextPage(WhereToSendPaymentId, NormalMode)(answers) mustBe routes.PayeeFullNameController.onPageLoad(NormalMode)
      }

      //TODO - Add "you" path to summary page

      "go to AnyBenefits from SelectATaxYear" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(SelectTaxYearId, NormalMode)(answers) mustBe routes.AnyBenefitsController.onPageLoad(NormalMode)
      }

      "go to AnyJobseekers from AnyBenefits when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyBenefits) thenReturn Some(true)
        navigator.nextPage(AnyBenefitsId, NormalMode)(answers) mustBe routes.AnyJobseekersAllowanceController.onPageLoad(NormalMode)
      }

      "go to OtherIncome from AnyBenefits when No is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyBenefits) thenReturn Some(false)
        navigator.nextPage(AnyBenefitsId, NormalMode)(answers) mustBe routes.OtherIncomeController.onPageLoad(NormalMode)
      }

      "go to HowMuchJobseekers from AnyJobseekers when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyJobseekersAllowance) thenReturn Some(true)
        navigator.nextPage(AnyJobseekersAllowanceId, NormalMode)(answers) mustBe routes.HowMuchJobseekersAllowanceController.onPageLoad(NormalMode)
      }

      "go to AnyIncapacityBenefit from AnyJobseekers when No is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyJobseekersAllowance) thenReturn Some(false)
        navigator.nextPage(AnyJobseekersAllowanceId, NormalMode)(answers) mustBe routes.AnyIncapacityBenefitController.onPageLoad(NormalMode)
      }

      "go to AnyIncapacityBenefit from HowMuchJobseekers" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(HowMuchJobseekersAllowanceId, NormalMode)(answers) mustBe routes.AnyIncapacityBenefitController.onPageLoad(NormalMode)
      }

      "go to HowMuchIncapacityBenefit from AnyIncapacityBenefit when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyIncapacityBenefit) thenReturn Some(true)
        navigator.nextPage(AnyIncapacityBenefitId, NormalMode)(answers) mustBe routes.HowMuchIncapacityBenefitController.onPageLoad(NormalMode)
      }

      "go to AnyEmploymentSupport from AnyIncapacityBenefit when No is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyIncapacityBenefit) thenReturn Some(false)
        navigator.nextPage(AnyIncapacityBenefitId, NormalMode)(answers) mustBe routes.AnyEmploymentAndSupportAllowanceController.onPageLoad(NormalMode)
      }

      "go to AnyEmploymentSupport from HowMuchIncapacityBenefit" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(HowMuchIncapacityBenefitId, NormalMode)(answers) mustBe routes.AnyEmploymentAndSupportAllowanceController.onPageLoad(NormalMode)
      }

      "go to HowMuchEmploymentSupport from AnyEmploymentSupport when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyEmploymentAndSupportAllowance) thenReturn Some(true)
        navigator.nextPage(AnyEmploymentAndSupportAllowanceId, NormalMode)(answers) mustBe routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(NormalMode)
      }

      "go to AnyStatePension from AnyEmploymentSupport when No is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyEmploymentAndSupportAllowance) thenReturn Some(false)
        navigator.nextPage(AnyEmploymentAndSupportAllowanceId, NormalMode)(answers) mustBe routes.AnyStatePensionController.onPageLoad(NormalMode)
      }

      "go to AnyStatePension from HowMuchEmploymentSupport" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(HowMuchEmploymentAndSupportAllowanceId, NormalMode)(answers) mustBe routes.AnyStatePensionController.onPageLoad(NormalMode)
      }

      "go to HowMuchStatePension from AnyStatePension when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyStatePension) thenReturn Some(true)
        navigator.nextPage(AnyStatePensionId, NormalMode)(answers) mustBe routes.HowMuchStatePensionController.onPageLoad(NormalMode)
      }

      "go to AnyOtherTaxableBenefit from AnyStatePension when No is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyStatePension) thenReturn Some(false)
        navigator.nextPage(AnyStatePensionId, NormalMode)(answers) mustBe routes.AnyOtherTaxableBenefitsController.onPageLoad(NormalMode)
      }

      "go to AnyOtherTaxableBenefit from HowMuchStatePension" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(HowMuchStatePensionId, NormalMode)(answers) mustBe routes.AnyOtherTaxableBenefitsController.onPageLoad(NormalMode)
      }

      "go to OtherBenefitsDetailsAndAmount from AnyOtherTaxableBenefit when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyOtherTaxableBenefits) thenReturn Some(true)
        navigator.nextPage(AnyOtherTaxableBenefitsId, NormalMode)(answers) mustBe routes.OtherBenefitsDetailsAndAmountController.onPageLoad(NormalMode)
      }

      "go to OtherIncome from AnyOtherTaxableBenefit when No is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyOtherTaxableBenefits) thenReturn Some(false)
        navigator.nextPage(AnyOtherTaxableBenefitsId, NormalMode)(answers) mustBe routes.OtherIncomeController.onPageLoad(NormalMode)
      }

      "go to OtherIncome from OtherBenefitsDetailsAndAmount" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(OtherBenefitsDetailsAndAmountId, NormalMode)(answers) mustBe routes.OtherIncomeController.onPageLoad(NormalMode)
      }

      "go to AnyCarBenefits from OtherIncome when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.otherIncome) thenReturn Some(true)
        navigator.nextPage(OtherIncomeId, NormalMode)(answers) mustBe routes.AnyCarBenefitsController.onPageLoad(NormalMode)
      }

      "go to HowMuchCarBenefits from AnyCarBenefits when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyCarBenefits) thenReturn Some(true)
        navigator.nextPage(AnyCarBenefitsId, NormalMode)(answers) mustBe routes.HowMuchCarBenefitsController.onPageLoad(NormalMode)
      }

      "go to AnyRentalIncome from AnyCarBenefits when No is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyCarBenefits) thenReturn Some(false)
        navigator.nextPage(AnyCarBenefitsId, NormalMode)(answers) mustBe routes.AnyRentalIncomeController.onPageLoad(NormalMode)
      }

      "go to AnyRentalIncome from HowMuchCarBenefits" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(HowMuchCarBenefitsId, NormalMode)(answers) mustBe routes.AnyRentalIncomeController.onPageLoad(NormalMode)
      }

      "go to HowMuchRentalIncome from AnyRentalIncome when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyRentalIncome) thenReturn Some(true)
        navigator.nextPage(AnyRentalIncomeId, NormalMode)(answers) mustBe routes.HowMuchRentalIncomeController.onPageLoad(NormalMode)
      }

      "go to AnyBankSocietyInterest from AnyRentalIncome when No is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyRentalIncome) thenReturn Some(false)
        navigator.nextPage(AnyRentalIncomeId, NormalMode)(answers) mustBe routes.AnyBankBuildingSocietyInterestController.onPageLoad(NormalMode)
      }

      "go to AnyBankSocietyInterest from HowMuchRentalIncome" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(HowMuchRentalIncomeId, NormalMode)(answers) mustBe routes.AnyBankBuildingSocietyInterestController.onPageLoad(NormalMode)
      }

      "go to HowMuchBankSocietyInterest from AnyBankSocietyInterest when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyBankBuildingSocietyInterest) thenReturn Some(true)
        navigator.nextPage(AnyBankBuildingSocietyInterestId, NormalMode)(answers) mustBe routes.HowMuchBankBuildingSocietyInterestController.onPageLoad(NormalMode)
      }

      "go to AnyMedicalBenefits from AnyBankSocietyInterest when No is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyBankBuildingSocietyInterest) thenReturn Some(false)
        navigator.nextPage(AnyBankBuildingSocietyInterestId, NormalMode)(answers) mustBe routes.AnyMedicalBenefitsController.onPageLoad(NormalMode)
      }

      "go to AnyMedicalBenefits from HowMuchBankSocietyInterest" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(HowMuchBankBuildingSocietyInterestId, NormalMode)(answers) mustBe routes.AnyMedicalBenefitsController.onPageLoad(NormalMode)
      }

      "go to HowMuchMedicalBenefits from AnyMedicalBenefits when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyMedicalBenefits) thenReturn Some(true)
        navigator.nextPage(AnyMedicalBenefitsId, NormalMode)(answers) mustBe routes.HowMuchMedicalBenefitsController.onPageLoad(NormalMode)
      }

      "go to AnyOtherTaxableIncome from AnyMedicalBenefits when No is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyMedicalBenefits) thenReturn Some(false)
        navigator.nextPage(AnyMedicalBenefitsId, NormalMode)(answers) mustBe routes.AnyOtherTaxableIncomeController.onPageLoad(NormalMode)
      }

      "go to AnyOtherTaxableIncome from HowMuchMedicalBenefits" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(HowMuchMedicalBenefitsId, NormalMode)(answers) mustBe routes.AnyOtherTaxableIncomeController.onPageLoad(NormalMode)
      }

      "go to OtherIncomeDetailsAndAmount from AnyOtherTaxableIncome when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyOtherTaxableIncome) thenReturn Some(true)
        navigator.nextPage(AnyOtherTaxableIncomeId, NormalMode)(answers) mustBe routes.OtherIncomeDetailsAndAmountController.onPageLoad(NormalMode)
      }

      "go to AnyAgentRef from PayeeFullName" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(PayeeFullNameId, NormalMode)(answers) mustBe routes.AnyAgentRefController.onPageLoad(NormalMode)
      }

      "go to AgentReferenceNumber from AnyAgentRef when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyAgentRef) thenReturn Some(true)
        navigator.nextPage(AnyAgentRefId, NormalMode)(answers) mustBe routes.AgentReferenceNumberController.onPageLoad(NormalMode)
      }

      "go to IsPayeeAddressInTheUK from AgentReferenceNumber" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(AgentReferenceNumberId, NormalMode)(answers) mustBe routes.IsPayeeAddressInTheUKController.onPageLoad(NormalMode)
      }

      "go to IsPayeeAddressInTheUK from AnyAgentRef when no is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyAgentRef) thenReturn Some(false)
        navigator.nextPage(AnyAgentRefId, NormalMode)(answers) mustBe routes.IsPayeeAddressInTheUKController.onPageLoad(NormalMode)
      }

      "go to PayeeInternationalAddress from IsPayeeAddressInTheUK when no is selected" in {
        val answers = mock[UserAnswers]
        when(answers.isPayeeAddressInTheUK) thenReturn Some(false)
        navigator.nextPage(IsPayeeAddressInTheUKId, NormalMode)(answers) mustBe routes.PayeeInternationalAddressController.onPageLoad(NormalMode)
      }

      "go to payeeUKAddress from IsPayeeAddressInTheUK when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.isPayeeAddressInTheUK) thenReturn Some(true)
        navigator.nextPage(IsPayeeAddressInTheUKId, NormalMode)(answers) mustBe routes.PayeeUKAddressController.onPageLoad(NormalMode)
      }
    }

    "in Check mode" must {
      "go to CheckYourAnswers from an identifier that doesn't exist in the edit route map" in {
        case object UnknownIdentifier extends Identifier
        navigator.nextPage(UnknownIdentifier, CheckMode)(mock[UserAnswers]) mustBe routes.CheckYourAnswersController.onPageLoad()
      }
    }
  }
}
