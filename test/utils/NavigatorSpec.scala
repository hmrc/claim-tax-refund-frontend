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
import controllers.routes
import identifiers._
import models.WhereToSendPayment.{SomeoneElse, You}
import models._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

class NavigatorSpec extends SpecBase with MockitoSugar {

  val navigator = new Navigator

  "Navigator" when {

    "in Normal mode" must {
      "go to Index from an identifier that doesn't exist in the route map" in {
        case object UnknownIdentifier extends Identifier
        navigator.nextPage(UnknownIdentifier, NormalMode)(mock[UserAnswers]) mustBe routes.IndexController.onPageLoad()
      }

      "go to SelectTaxYear from UserDetails" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(UserDetailsId, NormalMode)(answers) mustBe routes.SelectTaxYearController.onPageLoad(NormalMode)
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

      "go to PayeeFullName from WhereToSendPayment when SomeoneElse is selected" in {
        val answers = mock[UserAnswers]
        when(answers.whereToSendPayment) thenReturn Some(SomeoneElse)
        navigator.nextPage(WhereToSendPaymentId, NormalMode)(answers) mustBe routes.PayeeFullNameController.onPageLoad(NormalMode)
      }

      "go to PayeeFullName from WhereToSendPayment when Yourself is selected" in {
        val answers = mock[UserAnswers]
        when(answers.whereToSendPayment) thenReturn Some(You)
        navigator.nextPage(WhereToSendPaymentId, NormalMode)(answers) mustBe routes.TelephoneNumberController.onPageLoad(NormalMode)
      }

      "go to EmploymentDetails from SelectATaxYear" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(SelectTaxYearId, NormalMode)(answers) mustBe routes.EmploymentDetailsController.onPageLoad(NormalMode)
      }

      "go to AnyBenefits from EmploymentDetails" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(EmploymentDetailsId, NormalMode)(answers) mustBe routes.AnyBenefitsController.onPageLoad(NormalMode)
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

      "go to CheckYourAnswers from TelephoneNumber" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(TelephoneNumberId, NormalMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
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
