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
import models.WhereToSendPayment.{Myself, Nominee}
import models._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

class NavigatorSpec extends SpecBase with MockitoSugar {

  val navigator = new Navigator
  private val answers = MockUserAnswers.nothingAnswered

  "Navigator" when {

    "in Normal mode" must {
      "go to Index from an identifier that doesn't exist in the route map" in {
        case object UnknownIdentifier extends Identifier
        navigator.nextPage(UnknownIdentifier, NormalMode)(mock[UserAnswers]) mustBe routes.IndexController.onPageLoad()
      }

      //Claim details section

      "go to EmploymentDetails from SelectATaxYear" in {

        navigator.nextPage(SelectTaxYearId, NormalMode)(answers) mustBe routes.EmploymentDetailsController.onPageLoad(NormalMode)
      }

      "go to enter paye reference from employmentDetails when no is selected" in {
        when(answers.employmentDetails) thenReturn Some(false)
        navigator.nextPage(EmploymentDetailsId, NormalMode)(answers) mustBe routes.EnterPayeReferenceController.onPageLoad(NormalMode)
      }

      "go to DetailsOfEmploymentOrPension from EnterPayeReference" in {
        navigator.nextPage(EnterPayeReferenceId, NormalMode)(answers) mustBe routes.DetailsOfEmploymentOrPensionController.onPageLoad(NormalMode)
      }

      "go to AnyBenefits from Employment details when yes is selected" in {
        when(answers.employmentDetails) thenReturn Some(true)
        navigator.nextPage(EmploymentDetailsId, NormalMode)(answers) mustBe routes.AnyBenefitsController.onPageLoad(NormalMode)
      }

      //3 benefit selectors

      "go to AnyCompanyBenefits from AnyBenefits when No is selected" in {
        when(answers.anyBenefits) thenReturn Some(false)
        navigator.nextPage(AnyBenefitsId, NormalMode)(answers) mustBe routes.AnyCompanyBenefitsController.onPageLoad(NormalMode)
      }

      "go to AnyBenefits from AnyOtherTaxableBenefit when Yes is selected" in {
        when(answers.employmentDetails) thenReturn Some(true)
        navigator.nextPage(EmploymentDetailsId, NormalMode)(answers) mustBe routes.AnyBenefitsController.onPageLoad(NormalMode)
      }

      "go to AnyBenefits from DetailsOfEmploymentOrPension" in {
        navigator.nextPage(DetailsOfEmploymentOrPensionId, NormalMode)(answers) mustBe routes.AnyBenefitsController.onPageLoad(NormalMode)
      }

      //Payment details section

      "go to NomineeFullName from WhereToSendPayment when Nominee is selected" in {
        when(answers.whereToSendPayment) thenReturn Some(Nominee)
        navigator.nextPage(WhereToSendPaymentId, NormalMode)(answers) mustBe routes.NomineeFullNameController.onPageLoad(NormalMode)
      }

      "go to AnyAgentRef from NomineeFullName" in {
        navigator.nextPage(NomineeFullNameId, NormalMode)(answers) mustBe routes.AnyAgentRefController.onPageLoad(NormalMode)
      }

      "go to PaymentAddressCorrect from WhereToSendPayment when Myself is selected" in {
        when(answers.whereToSendPayment) thenReturn Some(Myself)
        navigator.nextPage(WhereToSendPaymentId, NormalMode)(answers) mustBe routes.PaymentAddressCorrectController.onPageLoad(NormalMode)
      }

      "go to IsPaymentAddressInTheUK from PaymentAddressCorrect when No is selected" in {
        when(answers.paymentAddressCorrect) thenReturn Some(false)
        navigator.nextPage(PaymentAddressCorrectId, NormalMode)(answers) mustBe routes.IsPaymentAddressInTheUKController.onPageLoad(NormalMode)
      }

      "go to IsPaymentAddressInTheUK from AnyAgentRef" in {
        navigator.nextPage(AnyAgentRefId, NormalMode)(answers) mustBe routes.IsPaymentAddressInTheUKController.onPageLoad(NormalMode)
      }

      "go to PaymentInternationalAddress from IsPaymentAddressInTheUK when no is selected" in {
        when(answers.isPaymentAddressInTheUK) thenReturn Some(false)
        navigator.nextPage(IsPaymentAddressInTheUKId, NormalMode)(answers) mustBe routes.PaymentInternationalAddressController.onPageLoad(NormalMode)
      }

      "go to PaymentUKAddress from IsPaymentAddressInTheUK when Yes is selected" in {
        when(answers.isPaymentAddressInTheUK) thenReturn Some(true)
        navigator.nextPage(IsPaymentAddressInTheUKId, NormalMode)(answers) mustBe routes.PaymentUKAddressController.onPageLoad(NormalMode)
      }

      //Telephone section 3 routes

      "go to TelephoneNumber from PaymentAddressCorrect when Yes is selected" in {
        when(answers.paymentAddressCorrect) thenReturn Some(true)
        navigator.nextPage(PaymentAddressCorrectId, NormalMode)(answers) mustBe routes.TelephoneNumberController.onPageLoad(NormalMode)
      }

      "go to TelephoneNumber from PaymentInternationalAddress" in {
        navigator.nextPage(PaymentInternationalAddressId, NormalMode)(answers) mustBe routes.TelephoneNumberController.onPageLoad(NormalMode)
      }

      "go to TelephoneNumber from PaymentUKAddress" in {
        navigator.nextPage(PaymentUKAddressId, NormalMode)(answers) mustBe routes.TelephoneNumberController.onPageLoad(NormalMode)
      }
    }

    //check mode

    "in Check mode" must {
      "go to CheckYourAnswers from an identifier that doesn't exist in the edit route map" in {
        case object UnknownIdentifier extends Identifier
        navigator.nextPage(UnknownIdentifier, CheckMode)(mock[UserAnswers]) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      //Claim details section

      "go to CYA when Employment details is (yes)" in {
        when(answers.employmentDetails) thenReturn Some(true)
        navigator.nextPage(EmploymentDetailsId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      "when the answer hasn't changed return the user to checkYourAnswer" in {
        when(answers.enterPayeReference) thenReturn Some("123/AB1234")
        when(answers.employmentDetails) thenReturn Some(false)
        navigator.nextPage(EmploymentDetailsId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      "go to Enter PAYE reference when Employment details is (no) and no previous answers" in {
        when(answers.employmentDetails) thenReturn Some(false)
        when(answers.enterPayeReference) thenReturn None
        navigator.nextPage(EmploymentDetailsId, CheckMode)(answers) mustBe routes.EnterPayeReferenceController.onPageLoad(CheckMode)
      }

      "go to DetailsOfEmploymentOrPension from Enter PAYE reference if details of employment or pension is empty" in {
        navigator.nextPage(EnterPayeReferenceId, CheckMode)(answers) mustBe routes.DetailsOfEmploymentOrPensionController.onPageLoad(CheckMode)
      }

      "go to CYA from Enter PAYE reference if details of employment or pension is present" in {
        when(answers.detailsOfEmploymentOrPension) thenReturn Some("employment details")
        navigator.nextPage(EnterPayeReferenceId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      "go to CYA from DetailsOfEmploymentOrPensionController" in {
        navigator.nextPage(DetailsOfEmploymentOrPensionId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      //Payment details section
      "go to NomineeFullName from WhereToSendPayment when Nominee is selected" in {
        when(answers.whereToSendPayment) thenReturn Some(Nominee)
        navigator.nextPage(WhereToSendPaymentId, CheckMode)(answers) mustBe routes.NomineeFullNameController.onPageLoad(CheckMode)
      }

      "go to AnyAgentRef from NomineeFullName" in {
        navigator.nextPage(NomineeFullNameId, CheckMode)(answers) mustBe routes.AnyAgentRefController.onPageLoad(CheckMode)
      }

      "go to PaymentAddressCorrect from WhereToSendPayment when Myself is selected" in {
        when(answers.whereToSendPayment) thenReturn Some(Myself)
        navigator.nextPage(WhereToSendPaymentId, CheckMode)(answers) mustBe routes.PaymentAddressCorrectController.onPageLoad(CheckMode)
      }

      "go to IsPaymentAddressInTheUK from PaymentAddressCorrect when No is selected" in {
        when(answers.paymentAddressCorrect) thenReturn Some(false)
        navigator.nextPage(PaymentAddressCorrectId, CheckMode)(answers) mustBe routes.IsPaymentAddressInTheUKController.onPageLoad(CheckMode)
      }

      "go to IsPaymentAddressInTheUK from AnyAgentRef" in {
        when(answers.isPaymentAddressInTheUK) thenReturn None
        navigator.nextPage(AnyAgentRefId, CheckMode)(answers) mustBe routes.IsPaymentAddressInTheUKController.onPageLoad(CheckMode)
      }

      "go to PaymentInternationalAddress from IsPaymentAddressInTheUK when no is selected" in {
        when(answers.isPaymentAddressInTheUK) thenReturn Some(false)
        navigator.nextPage(IsPaymentAddressInTheUKId, CheckMode)(answers) mustBe routes.PaymentInternationalAddressController.onPageLoad(CheckMode)
      }

      "go to PaymentUKAddress from IsPaymentAddressInTheUK when Yes is selected" in {
        when(answers.isPaymentAddressInTheUK) thenReturn Some(true)
        navigator.nextPage(IsPaymentAddressInTheUKId, CheckMode)(answers) mustBe routes.PaymentUKAddressController.onPageLoad(CheckMode)
      }


    }
  }
}
