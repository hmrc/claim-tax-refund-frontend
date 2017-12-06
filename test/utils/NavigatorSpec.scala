/*
 * Copyright 2017 HM Revenue & Customs
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
import models.TypeOfClaim.{OptionPAYE, OptionSA}
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
    }

    "in Check mode" must {
      "go to CheckYourAnswers from an identifier that doesn't exist in the edit route map" in {
        case object UnknownIdentifier extends Identifier
        navigator.nextPage(UnknownIdentifier, CheckMode)(mock[UserAnswers]) mustBe routes.CheckYourAnswersController.onPageLoad()
      }
    }
  }
}
