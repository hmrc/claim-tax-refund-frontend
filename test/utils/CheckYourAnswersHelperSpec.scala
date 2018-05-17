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
import models.{InternationalAddress, UkAddress, UserDetails}
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import viewmodels.AnswerRow

class CheckYourAnswersHelperSpec extends SpecBase with MockitoSugar {

  private var answers = mock[UserAnswers]
  private var answerRow = mock[AnswerRow]

  "User details" must {
    s"have the correct label" in {
      when(answers.userDetails) thenReturn Some(UserDetails("Joe Smith", "AB123456A",
        UkAddress("Line 1", "Line 2", Some("Line 3"), Some("Line 4"), None, "AB123CD")))
      val helper = new CheckYourAnswersHelper(answers)
      helper.userName.get.label.key mustBe s"userDetails.checkYourAnswersLabel.name"
      helper.userNino.get.label.key mustBe s"userDetails.checkYourAnswersLabel.nino"
      helper.userAddress.get.label.key mustBe s"userDetails.checkYourAnswersLabel.address"
    }
    s"not have a change link when set to false" in {
      when(answerRow.changeLink) thenReturn false
      val helper = new CheckYourAnswersHelper(answers)
      helper.userName.get.changeLink mustBe false
      helper.userNino.get.changeLink mustBe false
      helper.userAddress.get.changeLink mustBe false
    }
  }

  "Telephone number" must {
    s"have the correct label" in {
      when(answers.telephoneNumber) thenReturn Some("01912134587")
      val helper = new CheckYourAnswersHelper(answers)
      helper.telephoneNumber.get.label.key mustBe s"telephoneNumber.checkYourAnswersLabel"
    }
    s"have a change link when set to true" in {
      when(answerRow.changeLink) thenReturn true
      val helper = new CheckYourAnswersHelper(answers)
      helper.telephoneNumber.get.changeLink mustBe true
    }
  }

  "Select current year minus 2" must {
    s"have correct label" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus2)
      val helper = new CheckYourAnswersHelper(answers)
      helper.selectTaxYear.get.label.key mustBe s"selectTaxYear.checkYourAnswersLabel"
    }
  }

  "Select current year minus 3" must {
    s"have correct label" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus3)
      val helper = new CheckYourAnswersHelper(answers)
      helper.selectTaxYear.get.label.key mustBe s"selectTaxYear.checkYourAnswersLabel"
    }
  }

  "Select current year minus 4" must {
    s"have correct label" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus4)
      val helper = new CheckYourAnswersHelper(answers)
      helper.selectTaxYear.get.label.key mustBe s"selectTaxYear.checkYourAnswersLabel"
    }
  }

  "Select current year minus 5" must {
    s"have correct label" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus5)
      val helper = new CheckYourAnswersHelper(answers)
      helper.selectTaxYear.get.label.key mustBe s"selectTaxYear.checkYourAnswersLabel"
    }
  }

  "Is any benefits (true)" must {
    s"have the correct label" in {
      when(answers.anyBenefits) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyBenefits.get.label.key mustBe s"anyBenefits.checkYourAnswersLabel"
    }
  }

  "Is any benefits (false)" must {
    s"have the correct label" in {
      when(answers.anyBenefits) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyBenefits.get.label.key mustBe s"anyBenefits.checkYourAnswersLabel"
    }
  }

  "Is other income (true)" must {
    s"have the correct label" in {
      when(answers.otherIncome) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.otherIncome.get.label.key mustBe s"otherIncome.checkYourAnswersLabel"
    }
  }

  "Is other income (false)" must {
    s"have the correct label" in {
      when(answers.otherIncome) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.otherIncome.get.label.key mustBe s"otherIncome.checkYourAnswersLabel"
    }
  }

  "How much jobseekers allowance" must {
    s"have the correct label" in {
      when(answers.howMuchJobseekersAllowance) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper(answers)
      helper.howMuchJobseekersAllowance.get.label.key mustBe s"howMuchJobseekersAllowance.checkYourAnswersLabel"
    }
  }

  "How much incapacity benefit" must {
    s"have the correct label" in {
      when(answers.howMuchIncapacityBenefit) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper(answers)
      helper.howMuchIncapacityBenefit.get.label.key mustBe s"howMuchIncapacityBenefit.checkYourAnswersLabel"
    }
  }

  "Is anyEmploymentAndSupportAllowance (true)" must {
    s"have the correct label" in {
      when(answers.anyEmploymentAndSupportAllowance) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyEmploymentAndSupportAllowance.get.label.key mustBe s"anyEmploymentAndSupportAllowance.checkYourAnswersLabel"
    }
  }

  "Is anyEmploymentAndSupportAllowance (false)" must {
    s"have the correct label" in {
      when(answers.anyEmploymentAndSupportAllowance) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyEmploymentAndSupportAllowance.get.label.key mustBe s"anyEmploymentAndSupportAllowance.checkYourAnswersLabel"
    }
  }

  "How much employment and support allowance" must {
    s"have the correct label" in {
      when(answers.howMuchEmploymentAndSupportAllowance) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper(answers)
      helper.howMuchEmploymentAndSupportAllowance.get.label.key mustBe s"howMuchEmploymentAndSupportAllowance.checkYourAnswersLabel"
    }
  }

  "Is anyStatePension (true)" must {
    s"have the correct label" in {
      when(answers.anyStatePension) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyStatePension.get.label.key mustBe s"anyStatePension.checkYourAnswersLabel"
    }
  }

  "Is anyStatePension (false)" must {
    s"have the correct label" in {
      when(answers.anyStatePension) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyStatePension.get.label.key mustBe s"anyStatePension.checkYourAnswersLabel"
    }
  }

  "How much state pension" must {
    s"have the correct label" in {
      when(answers.howMuchStatePension) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper(answers)
      helper.howMuchStatePension.get.label.key mustBe s"howMuchStatePension.checkYourAnswersLabel"
    }
  }

  "Is anyOtherTaxableBenefits (true)" must {
    s"have the correct label" in {
      when(answers.anyOtherTaxableBenefits) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyOtherTaxableBenefits.get.label.key mustBe s"anyOtherTaxableBenefits.checkYourAnswersLabel"
    }
  }

  "Is anyOtherTaxableBenefits (false)" must {
    s"have the correct label" in {
      when(answers.anyOtherTaxableBenefits) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyOtherTaxableBenefits.get.label.key mustBe s"anyOtherTaxableBenefits.checkYourAnswersLabel"
    }
  }

  "Other benefits details and amount" must {
    s"have the correct label" in {
      when(answers.otherBenefitsDetailsAndAmount) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper(answers)
      helper.otherBenefitsDetailsAndAmount.get.label.key mustBe s"otherBenefitsDetailsAndAmount.checkYourAnswersLabel"
    }
  }

  "Is anyCarBenefits (true)" must {
    s"have the correct label" in {
      when(answers.anyCarBenefits) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyCarBenefits.get.label.key mustBe s"anyCarBenefits.checkYourAnswersLabel"
    }
  }

  "Is anyCarBenefits (false)" must {
    s"have the correct label" in {
      when(answers.anyCarBenefits) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyCarBenefits.get.label.key mustBe s"anyCarBenefits.checkYourAnswersLabel"
    }
  }

  "How much car benefits" must {
    s"have the correct label" in {
      when(answers.howMuchCarBenefits) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper(answers)
      helper.howMuchCarBenefits.get.label.key mustBe s"howMuchCarBenefits.checkYourAnswersLabel"
    }
  }

  "Is anyRentalIncome (true)" must {
    s"have the correct label" in {
      when(answers.anyRentalIncome) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyRentalIncome.get.label.key mustBe s"anyRentalIncome.checkYourAnswersLabel"
    }
  }

  "Is anyRentalIncome (false)" must {
    s"have the correct label" in {
      when(answers.anyRentalIncome) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyRentalIncome.get.label.key mustBe s"anyRentalIncome.checkYourAnswersLabel"
    }
  }

  "How much rental income" must {
    s"have the correct label" in {
      when(answers.howMuchRentalIncome) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper(answers)
      helper.howMuchRentalIncome.get.label.key mustBe s"howMuchRentalIncome.checkYourAnswersLabel"
    }
  }

  "Is anyBankBuildingSocietyInterest (true)" must {
    s"have the correct label" in {
      when(answers.anyBankBuildingSocietyInterest) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyBankBuildingSocietyInterest.get.label.key mustBe s"anyBankBuildingSocietyInterest.checkYourAnswersLabel"
    }
  }

  "Is anyBankBuildingSocietyInterest (false)" must {
    s"have the correct label" in {
      when(answers.anyBankBuildingSocietyInterest) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyBankBuildingSocietyInterest.get.label.key mustBe s"anyBankBuildingSocietyInterest.checkYourAnswersLabel"
    }
  }

  "How much bank building society interest" must {
    s"have the correct label" in {
      when(answers.howMuchBankBuildingSocietyInterest) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper(answers)
      helper.howMuchBankBuildingSocietyInterest.get.label.key mustBe s"howMuchBankBuildingSocietyInterest.checkYourAnswersLabel"
    }
  }

  "Is anyMedicalBenefits (true)" must {
    s"have the correct label" in {
      when(answers.anyMedicalBenefits) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyMedicalBenefits.get.label.key mustBe s"anyMedicalBenefits.checkYourAnswersLabel"
    }
  }

  "Is anyMedicalBenefits (false)" must {
    s"have the correct label" in {
      when(answers.anyMedicalBenefits) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyMedicalBenefits.get.label.key mustBe s"anyMedicalBenefits.checkYourAnswersLabel"
    }
  }

  "How much medical benefits" must {
    s"have the correct label" in {
      when(answers.howMuchMedicalBenefits) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper(answers)
      helper.howMuchMedicalBenefits.get.label.key mustBe s"howMuchMedicalBenefits.checkYourAnswersLabel"
    }
  }

  "Is anyOtherTaxableIncome (true)" must {
    s"have the correct label" in {
      when(answers.anyOtherTaxableIncome) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyOtherTaxableIncome.get.label.key mustBe s"anyOtherTaxableIncome.checkYourAnswersLabel"
    }
  }

  "Is anyOtherTaxableIncome (false)" must {
    s"have the correct label" in {
      when(answers.anyOtherTaxableIncome) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyOtherTaxableIncome.get.label.key mustBe s"anyOtherTaxableIncome.checkYourAnswersLabel"
    }
  }

  "Other taxable income details and amount" must {
    s"have the correct label" in {
      when(answers.otherIncomeDetailsAndAmount) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper(answers)
      helper.otherIncomeDetailsAndAmount.get.label.key mustBe s"otherIncomeDetailsAndAmount.checkYourAnswersLabel"
    }
  }

  "Is WhereToSendPayment yourself" must {
    s"have the correct label" in {
      when(answers.whereToSendPayment) thenReturn Some(You)
      val helper = new CheckYourAnswersHelper(answers)
      helper.whereToSendPayment.get.label.key mustBe s"whereToSendPayment.checkYourAnswersLabel"
    }
  }

  "Is WhereToSendPayment someone else" must {
    s"have the correct label" in {
      when(answers.whereToSendPayment) thenReturn Some(SomeoneElse)
      val helper = new CheckYourAnswersHelper(answers)
      helper.whereToSendPayment.get.label.key mustBe s"whereToSendPayment.checkYourAnswersLabel"
    }
  }

  "Payee Full Name" must {
    s"have the correct label" in {
      when(answers.payeeFullName) thenReturn Some("Test name")
      val helper = new CheckYourAnswersHelper(answers)
      helper.payeeFullName.get.label.key mustBe s"payeeFullName.checkYourAnswersLabel"
    }
  }

  "Is anyAgentRef (true)" must {
    s"have the correct label" in {
      when(answers.anyAgentRef) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyAgentRef.get.label.key mustBe s"anyAgentRef.checkYourAnswersLabel"
    }
  }

  "Is anyAgentRef (false)" must {
    s"have the correct label" in {
      when(answers.anyAgentRef) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyAgentRef.get.label.key mustBe s"anyAgentRef.checkYourAnswersLabel"
    }
  }

  "Agent Reference Number" must {
    s"have the correct label" in {
      when(answers.agentReferenceNumber) thenReturn Some("Test number")
      val helper = new CheckYourAnswersHelper(answers)
      helper.agentReferenceNumber.get.label.key mustBe s"agentReferenceNumber.checkYourAnswersLabel"
    }
  }
  "Is isPayeeAddressInTheUK (true)" must {
    s"have the correct label" in {
      when(answers.isPayeeAddressInTheUK) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.isPayeeAddressInTheUK.get.label.key mustBe s"isPayeeAddressInTheUK.checkYourAnswersLabel"
    }
  }

  "Is isPayeeAddressInTheUK (false)" must {
    s"have the correct label" in {
      when(answers.isPayeeAddressInTheUK) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.isPayeeAddressInTheUK.get.label.key mustBe s"isPayeeAddressInTheUK.checkYourAnswersLabel"
    }
  }

  "Payee International Address" must {
    s"have correct label" in {
      when(answers.payeeInternationalAddress) thenReturn Some(InternationalAddress("line 1", "line 2", None, None, None, "country"))
      val helper = new CheckYourAnswersHelper(answers)
      helper.payeeInternationalAddress.get.label.key mustBe s"payeeInternationalAddress.checkYourAnswersLabel"
    }
  }

  "Payee UK Address" must {
    s"have correct label" in {
      when(answers.payeeUKAddress) thenReturn Some(UkAddress("line 1", "line 2", None, None, None, "AA11 1AA"))
      val helper = new CheckYourAnswersHelper(answers)
      helper.payeeUKAddress.get.label.key mustBe s"payeeUKAddress.checkYourAnswersLabel"
    }
  }

}
