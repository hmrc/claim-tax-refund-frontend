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
import models.SelectTaxYear._
import models.WhereToSendPayment._
import models.{AnyAgentRef, InternationalAddress, UkAddress, UserDetails}
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar
import play.api.i18n.Messages
import viewmodels.AnswerRow

class CheckYourAnswersHelperSpec extends SpecBase with MockitoSugar with BeforeAndAfterEach {

  private var answers = mock[UserAnswers]
  private var answerRow = mock[AnswerRow]
  private var helper = new CheckYourAnswersHelper(answers)(messages: Messages)


  override def beforeEach = {
    super.beforeEach()
    answers = MockUserAnswers.nothingAnswered
    helper = new CheckYourAnswersHelper(answers)(messages: Messages)
  }

  "Change link" must {
    "be shown when set to true" in {
      when(answers.telephoneNumber) thenReturn Some("01912134587")
      helper.telephoneNumber.get.changeLink mustBe true
    }

    "be hidden when set to false" in {
      when(answers.userDetails) thenReturn Some(UserDetails("test", "test", UkAddress("test","test",None,None,None,"test")))
      helper.userName.get.changeLink mustBe false
    }
  }

  "Telephone number" must {
    s"have the correct label" in {
      when(answers.telephoneNumber) thenReturn Some("01912134587")
      helper.telephoneNumber.get.label.key mustBe s"telephoneNumber.checkYourAnswersLabel"
    }
  }

  "Select current year minus 1" must {
    s"have correct label" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus1)
      helper.selectTaxYear.get.label.key mustBe s"selectTaxYear.checkYourAnswersLabel"
    }
  }

  "Select current year minus 2" must {
    s"have correct label" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus2)
      helper.selectTaxYear.get.label.key mustBe s"selectTaxYear.checkYourAnswersLabel"
    }
  }

  "Select current year minus 3" must {
    s"have correct label" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus3)
      helper.selectTaxYear.get.label.key mustBe s"selectTaxYear.checkYourAnswersLabel"
    }
  }

  "Select current year minus 4" must {
    s"have correct label" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus4)
      helper.selectTaxYear.get.label.key mustBe s"selectTaxYear.checkYourAnswersLabel"
    }
  }

  "Select current year minus 5" must {
    s"have correct label" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus5)
      helper.selectTaxYear.get.label.key mustBe s"selectTaxYear.checkYourAnswersLabel"
    }
  }

  "Employment details (yes)" must {
    s"have correct label" in {
      when(answers.employmentDetails) thenReturn Some(true)
      helper.employmentDetails.get.label.key mustBe s"employmentDetails.checkYourAnswersLabel"
    }
  }

  "Employment details (no)" must {
    s"have correct label" in {
      when(answers.employmentDetails) thenReturn Some(false)
      helper.employmentDetails.get.label.key mustBe s"employmentDetails.checkYourAnswersLabel"
    }
  }

  "Is any benefits (true)" must {
    s"have the correct label" in {
      when(answers.anyBenefits) thenReturn Some(true)
      helper.anyBenefits.get.label.key mustBe s"anyBenefits.checkYourAnswersLabel"
    }
  }

  "Is any benefits (false)" must {
    s"have the correct label" in {
      when(answers.anyBenefits) thenReturn Some(false)
      helper.anyBenefits.get.label.key mustBe s"anyBenefits.checkYourAnswersLabel"
    }
  }

  "Is other income (true)" must {
    s"have the correct label" in {
      when(answers.anyTaxableIncome) thenReturn Some(true)
      helper.anyTaxableIncome.get.label.key mustBe s"anyTaxableIncome.checkYourAnswersLabel"
    }
  }

  "Is other income (false)" must {
    s"have the correct label" in {
      when(answers.anyTaxableIncome) thenReturn Some(false)
      helper.anyTaxableIncome.get.label.key mustBe s"anyTaxableIncome.checkYourAnswersLabel"
    }
  }

  "How much jobseekers allowance" must {
    s"have the correct label" in {
      when(answers.howMuchJobseekersAllowance) thenReturn Some("9,999.99")
      helper.howMuchJobseekersAllowance.get.label.key mustBe s"howMuchJobseekersAllowance.checkYourAnswersLabel"
    }
  }

  "How much incapacity benefit" must {
    s"have the correct label" in {
      when(answers.howMuchIncapacityBenefit) thenReturn Some("9,999.99")
      helper.howMuchIncapacityBenefit.get.label.key mustBe s"howMuchIncapacityBenefit.checkYourAnswersLabel"
    }
  }

  "How much employment and support allowance" must {
    s"have the correct label" in {
      when(answers.howMuchEmploymentAndSupportAllowance) thenReturn Some("9,999.99")
      helper.howMuchEmploymentAndSupportAllowance.get.label.key mustBe s"howMuchEmploymentAndSupportAllowance.checkYourAnswersLabel"
    }
  }

  "How much state pension" must {
    s"have the correct label" in {
      when(answers.howMuchStatePension) thenReturn Some("9,999.99")
      helper.howMuchStatePension.get.label.key mustBe s"howMuchStatePension.checkYourAnswersLabel"
    }
  }

  "Is anyOtherTaxableBenefits (true)" must {
    s"have the correct label" in {
      when(answers.anyOtherTaxableBenefits) thenReturn Some(true)
      helper.anyOtherTaxableBenefits.get.label.key mustBe s"anyOtherTaxableBenefits.checkYourAnswersLabel"
    }
  }

  "Is anyOtherTaxableBenefits (false)" must {
    s"have the correct label" in {
      when(answers.anyOtherTaxableBenefits) thenReturn Some(false)
      helper.anyOtherTaxableBenefits.get.label.key mustBe s"anyOtherTaxableBenefits.checkYourAnswersLabel"
    }
  }

  "How much car benefits" must {
    s"have the correct label" in {
      when(answers.howMuchCarBenefits) thenReturn Some("9,999.99")
      helper.howMuchCarBenefits.get.label.key mustBe s"howMuchCarBenefits.checkYourAnswersLabel"
    }
  }

  "How much rental income" must {
    s"have the correct label" in {
      when(answers.howMuchRentalIncome) thenReturn Some("9,999.99")
      helper.howMuchRentalIncome.get.label.key mustBe s"howMuchRentalIncome.checkYourAnswersLabel"
    }
  }

  "How much bank building society interest" must {
    s"have the correct label" in {
      when(answers.howMuchBankInterest) thenReturn Some("9,999.99")
      helper.howMuchBankInterest.get.label.key mustBe s"howMuchBankInterest.checkYourAnswersLabel"
    }
  }

  "How much medical benefits" must {
    s"have the correct label" in {
      when(answers.howMuchMedicalBenefits) thenReturn Some("9,999.99")
      helper.howMuchMedicalBenefits.get.label.key mustBe s"howMuchMedicalBenefits.checkYourAnswersLabel"
    }
  }

  "Is anyOtherTaxableIncome (true)" must {
    s"have the correct label" in {
      when(answers.anyOtherTaxableIncome) thenReturn Some(true)
      helper.anyOtherTaxableIncome.get.label.key mustBe s"anyOtherTaxableIncome.checkYourAnswersLabel"
    }
  }

  "Is anyOtherTaxableIncome (false)" must {
    s"have the correct label" in {
      when(answers.anyOtherTaxableIncome) thenReturn Some(false)
      helper.anyOtherTaxableIncome.get.label.key mustBe s"anyOtherTaxableIncome.checkYourAnswersLabel"
    }
  }

  "Is WhereToSendPayment yourself" must {
    s"have the correct label" in {
      when(answers.whereToSendPayment) thenReturn Some(Myself)
      helper.whereToSendPayment.get.label.key mustBe s"whereToSendPayment.checkYourAnswersLabel"
    }
  }

  "Is WhereToSendPayment someone else" must {
    s"have the correct label" in {
      when(answers.whereToSendPayment) thenReturn Some(Nominee)
      helper.whereToSendPayment.get.label.key mustBe s"whereToSendPayment.checkYourAnswersLabel"
    }
  }

  "Nominee Full Name" must {
    s"have the correct label" in {
      when(answers.nomineeFullName) thenReturn Some("Test name")
      helper.nomineeFullName.get.label.key mustBe s"nomineeFullName.checkYourAnswersLabel"
    }
  }

  "Is anyAgentRef (true)" must {
    s"have the correct label" in {
      when(answers.anyAgentRef) thenReturn Some(AnyAgentRef.Yes("AB12345"))
      helper.anyAgentRef.get.label.key mustBe s"anyAgentRef.checkYourAnswersLabel"
    }
  }

  "Is anyAgentRef (false)" must {
    s"have the correct label" in {
      when(answers.anyAgentRef) thenReturn Some(AnyAgentRef.No)
      helper.anyAgentRef.get.label.key mustBe s"anyAgentRef.checkYourAnswersLabel"
    }
  }

  "Is isPaymentAddressInTheUK (true)" must {
    s"have the correct label" in {
      when(answers.isPaymentAddressInTheUK) thenReturn Some(true)
      helper.isPaymentAddressInTheUK.get.label.key mustBe s"isPaymentAddressInTheUK.checkYourAnswersLabel"
    }
  }

  "Is isPaymentAddressInTheUK (false)" must {
    s"have the correct label" in {
      when(answers.isPaymentAddressInTheUK) thenReturn Some(false)
      helper.isPaymentAddressInTheUK.get.label.key mustBe s"isPaymentAddressInTheUK.checkYourAnswersLabel"
    }
  }

  "Payment International Address" must {
    s"have correct label" in {
      when(answers.paymentInternationalAddress) thenReturn Some(InternationalAddress("line 1", "line 2", None, None, None, "country"))
      helper.paymentInternationalAddress.get.label.key mustBe s"paymentInternationalAddress.checkYourAnswersLabel"
    }
  }

  "Payment UK Address" must {
    s"have correct label" in {
      when(answers.paymentUKAddress) thenReturn Some(UkAddress("line 1", "line 2", None, None, None, "AA11 1AA"))
      helper.paymentUKAddress.get.label.key mustBe s"paymentUKAddress.checkYourAnswersLabel"
    }
  }

}
