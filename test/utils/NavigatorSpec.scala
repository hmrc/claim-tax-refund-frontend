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
import models.WhereToSendPayment.{Nominee, Myself}
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

      "go to WhereToSendPayment from AnyOtherTaxableIncome when no is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyOtherTaxableIncome) thenReturn Some(false)
        navigator.nextPage(AnyOtherTaxableIncomeId, NormalMode)(answers) mustBe routes.WhereToSendPaymentController.onPageLoad(NormalMode)
      }

      "go to WhereToSendPayments from OtherIncomeDetailsAndAmount" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(OtherIncomeDetailsAndAmountId, NormalMode)(answers) mustBe routes.WhereToSendPaymentController.onPageLoad(NormalMode)
      }

      "go to NomineeFullName from WhereToSendPayment when SomeoneElse is selected" in {
        val answers = mock[UserAnswers]
        when(answers.whereToSendPayment) thenReturn Some(Nominee)
        navigator.nextPage(WhereToSendPaymentId, NormalMode)(answers) mustBe routes.NomineeFullNameController.onPageLoad(NormalMode)
      }

      "go to NomineeFullName from WhereToSendPayment when Yourself is selected" in {
        val answers = mock[UserAnswers]
        when(answers.whereToSendPayment) thenReturn Some(Myself)
        navigator.nextPage(WhereToSendPaymentId, NormalMode)(answers) mustBe routes.TelephoneNumberController.onPageLoad(NormalMode)
      }

      "go to EmploymentDetails from SelectATaxYear" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(SelectTaxYearId, NormalMode)(answers) mustBe routes.EmploymentDetailsController.onPageLoad(NormalMode)
      }

      "go to SelectBenefits page from AnyBenefits when answer is yes" in {
        val answers = mock[UserAnswers]
        when(answers.anyBenefits) thenReturn Some(true)
        navigator.nextPage(AnyBenefitsId, NormalMode)(answers) mustBe routes.SelectBenefitsController.onPageLoad(NormalMode)
      }

      "go to AnyCompanyBenefits from AnyBenefits when No is selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyBenefits) thenReturn Some(false)
        navigator.nextPage(AnyBenefitsId, NormalMode)(answers) mustBe routes.AnyCompanyBenefitsController.onPageLoad(NormalMode)
      }

      "go to SelectCompanyBenefits from AnyCompanyBenefits when Yes selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyCompanyBenefits) thenReturn Some(true)
        navigator.nextPage(AnyCompanyBenefitsId, NormalMode)(answers) mustBe routes.SelectCompanyBenefitsController.onPageLoad(NormalMode)
      }

      "go to SelectCompanyBenefits from AnyCompanyBenefits when No selected" in {
        val answers = mock[UserAnswers]
        when(answers.anyCompanyBenefits) thenReturn Some(false)
        navigator.nextPage(AnyCompanyBenefitsId, NormalMode)(answers) mustBe routes.OtherIncomeController.onPageLoad(NormalMode)
      }

      "go to TelephoneNumber from OtherIncome when answer is no" in {
        val answers = mock[UserAnswers]
        when(answers.otherIncome) thenReturn Some(false)
        navigator.nextPage(OtherIncomeId, NormalMode)(answers) mustBe routes.TelephoneNumberController.onPageLoad(NormalMode)
      }

      "go to SelectTaxableIncome from OtherIncome when answer is yes" in {
        val answers = mock[UserAnswers]
        when(answers.otherIncome) thenReturn Some(true)
        navigator.nextPage(OtherIncomeId, NormalMode)(answers) mustBe routes.SelectTaxableIncomeController.onPageLoad(NormalMode)
      }

      "go to WhereToSendPayment from TelephoneNumber" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(TelephoneNumberId, NormalMode)(answers) mustBe routes.WhereToSendPaymentController.onPageLoad(NormalMode)
      }

      "go to AnyBenefits from AnyOtherTaxableBenefit when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.employmentDetails) thenReturn Some(true)
        navigator.nextPage(EmploymentDetailsId, NormalMode)(answers) mustBe routes.AnyBenefitsController.onPageLoad(NormalMode)
      }

      "go to enter paye reference from employmentDetails when no is selected" in {
        val answers = mock[UserAnswers]
        when(answers.employmentDetails) thenReturn Some(false)
        navigator.nextPage(EmploymentDetailsId, NormalMode)(answers) mustBe routes.EnterPayeReferenceController.onPageLoad(NormalMode)
      }

      "go to DetailsOfEmploymentOrPension from EnterPayeReference" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(EnterPayeReferenceId, NormalMode)(answers) mustBe routes.DetailsOfEmploymentOrPensionController.onPageLoad(NormalMode)
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

      "go to AnyAgentRef from NomineeFullName" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(NomineeFullNameId, NormalMode)(answers) mustBe routes.AnyAgentRefController.onPageLoad(NormalMode)
      }

      "go to PaymentInternationalAddress from IsPaymentAddressInTheUK when no is selected" in {
        val answers = mock[UserAnswers]
        when(answers.isPaymentAddressInTheUK) thenReturn Some(false)
        navigator.nextPage(IsPaymentAddressInTheUKId, NormalMode)(answers) mustBe routes.PaymentInternationalAddressController.onPageLoad(NormalMode)
      }

      "go to PaymentUKAddress from IsPaymentAddressInTheUK when Yes is selected" in {
        val answers = mock[UserAnswers]
        when(answers.isPaymentAddressInTheUK) thenReturn Some(true)
        navigator.nextPage(IsPaymentAddressInTheUKId, NormalMode)(answers) mustBe routes.PaymentUKAddressController.onPageLoad(NormalMode)
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
