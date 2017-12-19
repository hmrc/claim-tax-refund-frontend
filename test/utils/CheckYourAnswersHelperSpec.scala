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
import models.FullOrPartialClaim.{OptionAll, OptionSome}
import models.SelectTaxYear._
import models.TypeOfClaim.{OptionPAYE, OptionSA}
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import models.{InternationalAddress, UkAddress}

class CheckYourAnswersHelperSpec extends SpecBase with MockitoSugar {

  private var answers = mock[UserAnswers]

  "Full Name" must {
    s"have the correct label" in {
      when(answers.fullName) thenReturn Some("value")
      val helper = new CheckYourAnswersHelper(answers)
      helper.fullName.get.label mustBe s"fullName.checkYourAnswersLabel"
    }
  }

  "National Insurance Number" must {
    s"have the correct label" in {
      when(answers.nationalInsuranceNumber) thenReturn Some("value")
      val helper = new CheckYourAnswersHelper(answers)
      helper.nationalInsuranceNumber.get.label mustBe s"nationalInsuranceNumber.checkYourAnswersLabel"
    }
  }


  "Is the address in the UK (true)" must {
    s"have the correct label" in {
      when(answers.isTheAddressInTheUK) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.isTheAddressInTheUK.get.label mustBe s"isTheAddressInTheUK.checkYourAnswersLabel"
    }
  }

  "Is the address in the UK (false)" must {
    s"have the correct label" in {
      when(answers.isTheAddressInTheUK) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.isTheAddressInTheUK.get.label mustBe s"isTheAddressInTheUK.checkYourAnswersLabel"
    }
  }


  "UK Address" must {
    s"have correct label" in {
      when(answers.ukAddress) thenReturn Some (UkAddress("line 1", "line 2", None, None, None, "AA11 1AA"))
      val helper = new CheckYourAnswersHelper(answers)
      helper.ukAddress.get.label mustBe s"ukAddress.checkYourAnswersLabel"
    }
  }

  "International Address" must {
    s"have correct label" in {
      when(answers.internationalAddress) thenReturn Some(InternationalAddress("line 1", "line 2", None, None, None, "country"))
      val helper = new CheckYourAnswersHelper(answers)
      helper.internationalAddress.get.label mustBe s"internationalAddress.checkYourAnswersLabel"
    }
  }

  "Telephone number" must {
    s"have the correct label" in {
      when(answers.telephoneNumber) thenReturn Some("01912134587")
      val helper = new CheckYourAnswersHelper(answers)
      helper.telephoneNumber.get.label mustBe s"telephoneNumber.checkYourAnswersLabel"
    }
  }

  "UTR Number" must {
    s"have the correct label" in {
      when(answers.uniqueTaxpayerReference) thenReturn Some("1234567890")
      val helper = new CheckYourAnswersHelper (answers)
      helper.uniqueTaxpayerReference.get.label mustBe s"uniqueTaxpayerReference.checkYourAnswersLabel"
    }
  }

  "PAYE Reference" must {
    s"have the correct label" in {
      when(answers.payAsYouEarn) thenReturn Some("ABC/123456")
      val helper = new CheckYourAnswersHelper (answers)
      helper.payAsYouEarn.get.label mustBe s"payAsYouEarn.checkYourAnswersLabel"
    }
  }

  "Select tax year (Option 1)" must {
    s"have correct label" in {
      when(answers.selectTaxYear) thenReturn Some(Option1)
      val helper = new CheckYourAnswersHelper (answers)
      helper.selectTaxYear.get.label mustBe s"selectTaxYear.checkYourAnswersLabel"
     }
  }

  "Select tax year (Option 2)" must {
    s"have correct label" in {
      when(answers.selectTaxYear) thenReturn Some(Option2)
      val helper = new CheckYourAnswersHelper (answers)
      helper.selectTaxYear.get.label mustBe s"selectTaxYear.checkYourAnswersLabel"
    }
  }

  "Select tax year (Option 3)" must {
    s"have correct label" in {
      when(answers.selectTaxYear) thenReturn Some(Option3)
      val helper = new CheckYourAnswersHelper (answers)
      helper.selectTaxYear.get.label mustBe s"selectTaxYear.checkYourAnswersLabel"
    }
  }

  "Select tax year (Option 4)" must {
    s"have correct label" in {
      when(answers.selectTaxYear) thenReturn Some(Option4)
      val helper = new CheckYourAnswersHelper (answers)
      helper.selectTaxYear.get.label mustBe s"selectTaxYear.checkYourAnswersLabel"
    }
  }

  "Is any benefits (true)" must {
    s"have the correct label" in {
      when(answers.anyBenefits) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyBenefits.get.label mustBe s"anyBenefits.checkYourAnswersLabel"
    }
  }

  "Is any benefits (false)" must {
    s"have the correct label" in {
      when(answers.anyBenefits) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyBenefits.get.label mustBe s"anyBenefits.checkYourAnswersLabel"
    }
  }

  "Is type of claim (SA)" must {
    s"have the correct label" in {
      when(answers.typeOfClaim) thenReturn Some(OptionSA)
      val helper = new CheckYourAnswersHelper(answers)
      helper.typeOfClaim.get.label mustBe s"typeOfClaim.checkYourAnswersLabel"
    }
  }

  "Is type of claim (PAYE)" must {
    s"have the correct label" in {
      when(answers.typeOfClaim) thenReturn Some(OptionPAYE)
      val helper = new CheckYourAnswersHelper(answers)
      helper.typeOfClaim.get.label mustBe s"typeOfClaim.checkYourAnswersLabel"
    }
  }

    "Is fullOrPartial (Some)" must {
      s"have the correct label" in {
        when(answers.fullOrPartialClaim) thenReturn Some(OptionSome)
        val helper = new CheckYourAnswersHelper(answers)
        helper.fullOrPartialClaim.get.label mustBe s"fullOrPartialClaim.checkYourAnswersLabel"
      }
    }

  "Is fullOrPartial (All)" must {
    s"have the correct label" in {
      when(answers.fullOrPartialClaim) thenReturn Some(OptionAll)
      val helper = new CheckYourAnswersHelper(answers)
      helper.fullOrPartialClaim.get.label mustBe s"fullOrPartialClaim.checkYourAnswersLabel"
    }
  }

  "Is other income (true)" must {
    s"have the correct label" in {
      when(answers.otherIncome) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.otherIncome.get.label mustBe s"otherIncome.checkYourAnswersLabel"
    }
  }

  "Is other income (false)" must {
    s"have the correct label" in {
      when(answers.otherIncome) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.otherIncome.get.label mustBe s"otherIncome.checkYourAnswersLabel"
    }
  }

  "Is anyJobseekersAllownance (true)" must {
    s"have the correct label" in {
      when(answers.anyJobseekersAllowance) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyJobseekersAllowance.get.label mustBe s"anyJobseekersAllowance.checkYourAnswersLabel"
    }
  }

  "Is anyJobseekersAllownance (false)" must {
    s"have the correct label" in {
      when(answers.anyJobseekersAllowance) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyJobseekersAllowance.get.label mustBe s"anyJobseekersAllowance.checkYourAnswersLabel"
    }
  }

  "How much jobseekers allowance" must {
    s"have the correct label" in {
      when(answers.howMuchJobseekersAllowance) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper (answers)
      helper.howMuchJobseekersAllowance.get.label mustBe s"howMuchJobseekersAllowance.checkYourAnswersLabel"
    }
  }

  "Is anyIncapacityBenefit (true)" must {
    s"have the correct label" in {
      when(answers.anyIncapacityBenefit) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyIncapacityBenefit.get.label mustBe s"anyIncapacityBenefit.checkYourAnswersLabel"
    }
  }

  "Is anyIncapacityBenefit (false)" must {
    s"have the correct label" in {
      when(answers.anyIncapacityBenefit) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyIncapacityBenefit.get.label mustBe s"anyIncapacityBenefit.checkYourAnswersLabel"
    }
  }

  "How much incapacity benefit" must {
    s"have the correct label" in {
      when(answers.howMuchIncapacityBenefit) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper (answers)
      helper.howMuchIncapacityBenefit.get.label mustBe s"howMuchIncapacityBenefit.checkYourAnswersLabel"
    }
  }

  "Is anyEmploymentAndSupportAllowance (true)" must {
    s"have the correct label" in {
      when(answers.anyEmploymentAndSupportAllowance) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyEmploymentAndSupportAllowance.get.label mustBe s"anyEmploymentAndSupportAllowance.checkYourAnswersLabel"
    }
  }

  "Is anyEmploymentAndSupportAllowance (false)" must {
    s"have the correct label" in {
      when(answers.anyEmploymentAndSupportAllowance) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyEmploymentAndSupportAllowance.get.label mustBe s"anyEmploymentAndSupportAllowance.checkYourAnswersLabel"
    }
  }

  "How much employment and support allowance" must {
    s"have the correct label" in {
      when(answers.howMuchEmploymentAndSupportAllowance) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper (answers)
      helper.howMuchEmploymentAndSupportAllowance.get.label mustBe s"howMuchEmploymentAndSupportAllowance.checkYourAnswersLabel"
    }
  }

  "Is anyStatePension (true)" must {
    s"have the correct label" in {
      when(answers.anyStatePension) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyStatePension.get.label mustBe s"anyStatePension.checkYourAnswersLabel"
    }
  }

  "Is anyStatePension (false)" must {
    s"have the correct label" in {
      when(answers.anyStatePension) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyStatePension.get.label mustBe s"anyStatePension.checkYourAnswersLabel"
    }
  }

  "How much state pension" must {
    s"have the correct label" in {
      when(answers.howMuchStatePension) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper (answers)
      helper.howMuchStatePension.get.label mustBe s"howMuchStatePension.checkYourAnswersLabel"
    }
  }

  "Is anyOtherTaxableBenefits (true)" must {
    s"have the correct label" in {
      when(answers.anyOtherTaxableBenefits) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyOtherTaxableBenefits.get.label mustBe s"anyOtherTaxableBenefits.checkYourAnswersLabel"
    }
  }

  "Is anyOtherTaxableBenefits (false)" must {
    s"have the correct label" in {
      when(answers.anyOtherTaxableBenefits) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyOtherTaxableBenefits.get.label mustBe s"anyOtherTaxableBenefits.checkYourAnswersLabel"
    }
  }

  "Other benefits details and amount" must {
    s"have the correct label" in {
      when(answers.otherBenefitsDetailsAndAmount) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper (answers)
      helper.otherBenefitsDetailsAndAmount.get.label mustBe s"otherBenefitsDetailsAndAmount.checkYourAnswersLabel"
    }
  }

  "Is anyCarBenefits (true)" must {
    s"have the correct label" in {
      when(answers.anyCarBenefits) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyCarBenefits.get.label mustBe s"anyCarBenefits.checkYourAnswersLabel"
    }
  }

  "Is anyCarBenefits (false)" must {
    s"have the correct label" in {
      when(answers.anyCarBenefits) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyCarBenefits.get.label mustBe s"anyCarBenefits.checkYourAnswersLabel"
    }
  }

  "How much car benefits" must {
    s"have the correct label" in {
      when(answers.howMuchCarBenefits) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper (answers)
      helper.howMuchCarBenefits.get.label mustBe s"howMuchCarBenefits.checkYourAnswersLabel"
    }
  }

  "Is anyRentalIncome (true)" must {
    s"have the correct label" in {
      when(answers.anyRentalIncome) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyRentalIncome.get.label mustBe s"anyRentalIncome.checkYourAnswersLabel"
    }
  }

  "Is anyRentalIncome (false)" must {
    s"have the correct label" in {
      when(answers.anyRentalIncome) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyRentalIncome.get.label mustBe s"anyRentalIncome.checkYourAnswersLabel"
    }
  }

  "How much rental income" must {
    s"have the correct label" in {
      when(answers.howMuchRentalIncome) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper (answers)
      helper.howMuchRentalIncome.get.label mustBe s"howMuchRentalIncome.checkYourAnswersLabel"
    }
  }

  "Is anyBankBuildingSocietyInterest (true)" must {
    s"have the correct label" in {
      when(answers.anyBankBuildingSocietyInterest) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyBankBuildingSocietyInterest.get.label mustBe s"anyBankBuildingSocietyInterest.checkYourAnswersLabel"
    }
  }

  "Is anyBankBuildingSocietyInterest (false)" must {
    s"have the correct label" in {
      when(answers.anyBankBuildingSocietyInterest) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyBankBuildingSocietyInterest.get.label mustBe s"anyBankBuildingSocietyInterest.checkYourAnswersLabel"
    }
  }

  "How much bank building society interest" must {
    s"have the correct label" in {
      when(answers.howMuchBankBuildingSocietyInterest) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper (answers)
      helper.howMuchBankBuildingSocietyInterest.get.label mustBe s"howMuchBankBuildingSocietyInterest.checkYourAnswersLabel"
    }
  }

  "Is anyMedicalBenefits (true)" must {
    s"have the correct label" in {
      when(answers.anyMedicalBenefits) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyMedicalBenefits.get.label mustBe s"anyMedicalBenefits.checkYourAnswersLabel"
    }
  }

  "Is anyMedicalBenefits (false)" must {
    s"have the correct label" in {
      when(answers.anyMedicalBenefits) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyMedicalBenefits.get.label mustBe s"anyMedicalBenefits.checkYourAnswersLabel"
    }
  }

  "How much medical benefits" must {
    s"have the correct label" in {
      when(answers.howMuchMedicalBenefits) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper (answers)
      helper.howMuchMedicalBenefits.get.label mustBe s"howMuchMedicalBenefits.checkYourAnswersLabel"
    }
  }

  "Is anyOtherTaxableIncome (true)" must {
    s"have the correct label" in {
      when(answers.anyOtherTaxableIncome) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyOtherTaxableIncome.get.label mustBe s"anyOtherTaxableIncome.checkYourAnswersLabel"
    }
  }

  "Is WhereToSendPayment (false)" must {
    s"have the correct label" in {
      when(answers.whereToSendPayment) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.whereToSendPayment.get.label mustBe s"whereToSendPayment.checkYourAnswersLabel"
    }
  }

  "Is WhereToSendPayment (true)" must {
    s"have the correct label" in {
      when(answers.whereToSendPayment) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.whereToSendPayment.get.label mustBe s"whereToSendPayment.checkYourAnswersLabel"
    }
  }

  "Is anyOtherTaxableIncome (false)" must {
    s"have the correct label" in {
      when(answers.anyOtherTaxableIncome) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyOtherTaxableIncome.get.label mustBe s"anyOtherTaxableIncome.checkYourAnswersLabel"
    }
  }

  "Other taxable income details and amount" must {
    s"have the correct label" in {
      when(answers.otherIncomeDetailsAndAmount) thenReturn Some("9,999.99")
      val helper = new CheckYourAnswersHelper (answers)
      helper.otherIncomeDetailsAndAmount.get.label mustBe s"otherIncomeDetailsAndAmount.checkYourAnswersLabel"
    }
  }

  "Payee Full Name" must {
    s"have the correct label" in {
      when(answers.payeeFullName) thenReturn Some("Test name")
      val helper = new CheckYourAnswersHelper (answers)
      helper.payeeFullName.get.label mustBe s"payeeFullName.checkYourAnswersLabel"
    }
  }

  "Is anyAgentRef (true)" must {
    s"have the correct label" in {
      when(answers.anyAgentRef) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyAgentRef.get.label mustBe s"anyAgentRef.checkYourAnswersLabel"
    }
  }

  "Is anyAgentRef (false)" must {
    s"have the correct label" in {
      when(answers.anyAgentRef) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.anyAgentRef.get.label mustBe s"anyAgentRef.checkYourAnswersLabel"
    }
  }

  "Agent Reference Number" must {
    s"have the correct label" in {
      when(answers.agentReferenceNumber) thenReturn Some("Test number")
      val helper = new CheckYourAnswersHelper (answers)
      helper.agentReferenceNumber.get.label mustBe s"agentReferenceNumber.checkYourAnswersLabel"
    }
  }
  "Is the agent address in the UK  (true)" must {
    s"have the correct label" in {
      when(answers.isAgentAddressInUK) thenReturn Some(true)
      val helper = new CheckYourAnswersHelper(answers)
      helper.isAgentAddressInUK.get.label mustBe s"isAgentAddressInUK.checkYourAnswersLabel"
    }
  }
  "Is the agent address in the UK (false)" must {
    s"have the correct label" in {
      when(answers.isAgentAddressInUK) thenReturn Some(false)
      val helper = new CheckYourAnswersHelper(answers)
      helper.isAgentAddressInUK.get.label mustBe s"isAgentAddressInUK.checkYourAnswersLabel"
    }
  }

}
