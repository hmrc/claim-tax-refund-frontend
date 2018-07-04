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
import models.SelectTaxYear._
import models.WhereToSendPayment._
import models._
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar
import play.api.i18n.Messages

class CheckYourAnswersHelperSpec extends SpecBase with MockitoSugar with BeforeAndAfterEach {

  private val answers = MockUserAnswers.nothingAnswered
  private val helper = new CheckYourAnswersHelper(answers)(messages: Messages)


  "Change link" must {
    "be shown when set to true" in {
      when(answers.anyTelephoneNumber) thenReturn Some(TelephoneOption.Yes("0191123123"))
      helper.telephoneNumber.get.changeLink mustBe true
    }

    "be hidden when set to false" in {
      when(answers.userDetails) thenReturn Some(UserDetails("test", "test", UkAddress("test","test",None,None,None,"test")))
      helper.userName.get.changeLink mustBe false
    }
  }

  //Claim details
  //------------------------------------------------------------------------------

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

  "Enter PAYE Ref" must {
    s"have correct label" in {
      when(answers.enterPayeReference) thenReturn Some("AB12345")
      helper.enterPayeReference.get.label.key mustBe s"enterPayeReference.checkYourAnswersLabel"
    }
  }

  "Details of employment or pension" must {
    s"have correct label" in {
      when(answers.detailsOfEmploymentOrPension) thenReturn Some("Details of employment or pension")
      helper.detailsOfEmploymentOrPension.get.label.key mustBe s"detailsOfEmploymentOrPension.checkYourAnswersLabel"
    }
  }

  //Benefits details
  //------------------------------------------------------------------------------

  "Any benefits (true)" must {
    s"have the correct label" in {
      when(answers.anyBenefits) thenReturn Some(true)
      helper.anyBenefits.get.label.key mustBe s"anyBenefits.checkYourAnswersLabel"
    }
  }

  "Any benefits (false)" must {
    s"have the correct label" in {
      when(answers.anyBenefits) thenReturn Some(false)
      helper.anyBenefits.get.label.key mustBe s"anyBenefits.checkYourAnswersLabel"
    }
  }

  "selectBenefits" must {
    s"have correct label" in {
      when(answers.selectBenefits) thenReturn Some(
        Seq(
          Benefits.BEREAVEMENT_ALLOWANCE,
          Benefits.CARERS_ALLOWANCE,
          Benefits.JOBSEEKERS_ALLOWANCE,
          Benefits.INCAPACITY_BENEFIT,
          Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
          Benefits.STATE_PENSION,
          Benefits.OTHER_TAXABLE_BENEFIT
        ))
      helper.selectBenefits.get.label.key mustBe s"selectBenefits.checkYourAnswersLabel"
    }
  }

  "howMuchBereavementAllowance" must {
    s"have the correct label" in {
      when(answers.howMuchBereavementAllowance) thenReturn Some("1234")
      helper.howMuchBereavementAllowance.get.label.key mustBe s"howMuchBereavementAllowance.checkYourAnswersLabel"
    }
  }

  "howMuchCarersAllowance" must {
    s"have the correct label" in {
      when(answers.howMuchCarersAllowance) thenReturn Some("1234")
      helper.howMuchCarersAllowance.get.label.key mustBe s"howMuchCarersAllowance.checkYourAnswersLabel"
    }
  }

  "howMuchJobseekersAllowance" must {
    s"have the correct label" in {
      when(answers.howMuchJobseekersAllowance) thenReturn Some("1234")
      helper.howMuchJobseekersAllowance.get.label.key mustBe s"howMuchJobseekersAllowance.checkYourAnswersLabel"
    }
  }

  "howMuchIncapacityBenefit" must {
    s"have the correct label" in {
      when(answers.howMuchIncapacityBenefit) thenReturn Some("1234")
      helper.howMuchIncapacityBenefit.get.label.key mustBe s"howMuchIncapacityBenefit.checkYourAnswersLabel"
    }
  }

  "howMuchEmploymentAndSupportAllowance" must {
    s"have the correct label" in {
      when(answers.howMuchEmploymentAndSupportAllowance) thenReturn Some("1234")
      helper.howMuchEmploymentAndSupportAllowance.get.label.key mustBe s"howMuchEmploymentAndSupportAllowance.checkYourAnswersLabel"
    }
  }

  "howMuchStatePension" must {
    s"have the correct label" in {
      when(answers.howMuchStatePension) thenReturn Some("1234")
      helper.howMuchStatePension.get.label.key mustBe s"howMuchStatePension.checkYourAnswersLabel"
    }
  }

  "otherBenefitsName" must {
    s"have the correct label" in {
      when(answers.otherBenefitsName) thenReturn Some(Seq("Other benefit"))
      helper.otherBenefitsName(1).head.label.key mustBe s"otherBenefitsName.checkYourAnswersLabel"
    }
  }

  "howMuchOtherBenefit" must {
    s"have the correct label" in {
      when(answers.howMuchOtherBenefit) thenReturn Some("1234")
      helper.howMuchOtherBenefit.get.label.key mustBe s"howMuchOtherBenefit.checkYourAnswersLabel"
    }
  }

  "anyOtherBenefits (yes)" must {
    s"have the correct label" in {
      when(answers.anyOtherBenefits) thenReturn Some(true)
      helper.anyOtherBenefits.get.label.key mustBe s"anyOtherBenefits.checkYourAnswersLabel"
    }
  }

  "anyOtherBenefits (no)" must {
    s"have the correct label" in {
      when(answers.anyOtherBenefits) thenReturn Some(false)
      helper.anyOtherBenefits.get.label.key mustBe s"anyOtherBenefits.checkYourAnswersLabel"
    }
  }

  //Company benefits details
  //------------------------------------------------------------------------------

  "Any company benefit (true)" must {
    s"have the correct label" in {
      when(answers.anyCompanyBenefits) thenReturn Some(true)
      helper.anyCompanyBenefits.get.label.key mustBe s"anyCompanyBenefits.checkYourAnswersLabel"
    }
  }

  "Any company benefit (false)" must {
    s"have the correct label" in {
      when(answers.anyCompanyBenefits) thenReturn Some(false)
      helper.anyCompanyBenefits.get.label.key mustBe s"anyCompanyBenefits.checkYourAnswersLabel"
    }
  }

  "selectCompanyBenefits" must {
    s"have correct label" in {
      when(answers.selectCompanyBenefits) thenReturn Some(
        Seq(
          CompanyBenefits.COMPANY_CAR_BENEFIT,
          CompanyBenefits.FUEL_BENEFIT,
          CompanyBenefits.MEDICAL_BENEFIT,
          CompanyBenefits.OTHER_COMPANY_BENEFIT
        ))
      helper.selectCompanyBenefits.get.label.key mustBe s"selectCompanyBenefits.checkYourAnswersLabel"
    }
  }

  "howMuchCarBenefits" must {
    s"have the correct label" in {
      when(answers.howMuchCarBenefits) thenReturn Some("1234")
      helper.howMuchCarBenefits.get.label.key mustBe s"howMuchCarBenefits.checkYourAnswersLabel"
    }
  }

  "howMuchFuelBenefit" must {
    s"have the correct label" in {
      when(answers.howMuchFuelBenefit) thenReturn Some("1234")
      helper.howMuchFuelBenefit.get.label.key mustBe s"howMuchFuelBenefit.checkYourAnswersLabel"
    }
  }

  "howMuchMedicalBenefits" must {
    s"have the correct label" in {
      when(answers.howMuchMedicalBenefits) thenReturn Some("1234")
      helper.howMuchMedicalBenefits.get.label.key mustBe s"howMuchMedicalBenefits.checkYourAnswersLabel"
    }
  }

  "otherCompanyBenefitsName" must {
    s"have the correct label" in {
      when(answers.otherCompanyBenefitsName) thenReturn Some("Benefit name")
      helper.otherCompanyBenefitsName.get.label.key mustBe s"otherCompanyBenefitsName.checkYourAnswersLabel"
    }
  }

  "howMuchOtherCompanyBenefit" must {
    s"have the correct label" in {
      when(answers.howMuchOtherCompanyBenefit) thenReturn Some("Benefit name")
      helper.howMuchOtherCompanyBenefit.get.label.key mustBe s"howMuchOtherCompanyBenefit.checkYourAnswersLabel"
    }
  }

  "anyOtherCompanyBenefits (yes)" must {
    s"have the correct label" in {
      when(answers.anyOtherCompanyBenefits) thenReturn Some(true)
      helper.anyOtherCompanyBenefits.get.label.key mustBe s"anyOtherCompanyBenefits.checkYourAnswersLabel"
    }
  }

  "anyOtherCompanyBenefits (no)" must {
    s"have the correct label" in {
      when(answers.anyOtherCompanyBenefits) thenReturn Some(false)
      helper.anyOtherCompanyBenefits.get.label.key mustBe s"anyOtherCompanyBenefits.checkYourAnswersLabel"
    }
  }

  //Taxable income details
  //------------------------------------------------------------------------------

  "Any taxable income (true)" must {
    s"have the correct label" in {
      when(answers.anyTaxableIncome) thenReturn Some(true)
      helper.anyTaxableIncome.get.label.key mustBe s"anyTaxableIncome.checkYourAnswersLabel"
    }
  }

  "Any taxable income (false)" must {
    s"have the correct label" in {
      when(answers.anyTaxableIncome) thenReturn Some(false)
      helper.anyTaxableIncome.get.label.key mustBe s"anyTaxableIncome.checkYourAnswersLabel"
    }
  }

  "selectTaxableIncome" must {
    s"have correct label" in {
      when(answers.selectTaxableIncome) thenReturn Some(
        Seq(
          TaxableIncome.RENTAL_INCOME,
          TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
          TaxableIncome.INVESTMENT_OR_DIVIDENDS,
          TaxableIncome.FOREIGN_INCOME,
          TaxableIncome.OTHER_TAXABLE_INCOME
        ))
      helper.selectTaxableIncome.get.label.key mustBe s"selectTaxableIncome.checkYourAnswersLabel"
    }
  }



  "howMuchRentalIncome" must {
    s"have correct label" in {
      when(answers.howMuchRentalIncome) thenReturn Some("123")
      helper.howMuchRentalIncome.get.label.key mustBe s"howMuchRentalIncome.checkYourAnswersLabel"
    }
  }

  "anyTaxableRentalIncome" must {
    val labelOption = "anyTaxableRentalIncomeOption"
    val label = "anyTaxableRentalIncome"
    val route = routes.AnyTaxableRentalIncomeController.onPageLoad(CheckMode).url

    s"have correct label (yes)" in {
      when(answers.anyTaxableRentalIncome) thenReturn Some(AnyTaxPaid.Yes("123"))
      val answer = answers.anyTaxableRentalIncome
      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.taxPaid(label, answer, route).get.label.key mustBe s"$label.checkYourAnswersLabel"
    }
    s"have correct label (no)" in {
      when(answers.anyTaxableRentalIncome) thenReturn Some(AnyTaxPaid.No)
      val answer = answers.anyTaxableRentalIncome
      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
    }
  }

  "howMuchBankInterest" must {
    s"have correct label" in {
      when(answers.howMuchBankInterest) thenReturn Some("123")
      helper.howMuchBankInterest.get.label.key mustBe s"howMuchBankInterest.checkYourAnswersLabel"
    }
  }

  "anyTaxableBankInterest" must {
    val labelOption = "anyTaxableBankInterestOption"
    val label = "anyTaxableBankInterest"
    val route = routes.AnyTaxableBankInterestController.onPageLoad(CheckMode).url

    s"have correct label (yes)" in {
      when(answers.anyTaxableBankInterest) thenReturn Some(AnyTaxPaid.Yes("123"))
      val answer = answers.anyTaxableBankInterest
      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.taxPaid(label, answer, route).get.label.key mustBe s"$label.checkYourAnswersLabel"
    }
    s"have correct label (no)" in {
      when(answers.anyTaxableBankInterest) thenReturn Some(AnyTaxPaid.No)
      val answer = answers.anyTaxableBankInterest
      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
    }
  }

  "howMuchInvestmentOrDividend" must {
    s"have correct label" in {
      when(answers.howMuchInvestmentOrDividend) thenReturn Some("123")
      helper.howMuchInvestmentOrDividend.get.label.key mustBe s"howMuchInvestmentOrDividend.checkYourAnswersLabel"
    }
  }

  "anyTaxableInvestments" must {
    val labelOption = "anyTaxableInvestmentsOption"
    val label = "anyTaxableInvestments"
    val route = routes.AnyTaxableInvestmentsController.onPageLoad(CheckMode).url

    s"have correct label (yes)" in {
      when(answers.anyTaxableInvestments) thenReturn Some(AnyTaxPaid.Yes("123"))
      val answer = answers.anyTaxableInvestments
      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.taxPaid(label, answer, route).get.label.key mustBe s"$label.checkYourAnswersLabel"
    }
    s"have correct label (no)" in {
      when(answers.anyTaxableBankInterest) thenReturn Some(AnyTaxPaid.No)
      val answer = answers.anyTaxableBankInterest
      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
    }
  }

  "howMuchForeignIncome" must {
    s"have correct label" in {
      when(answers.howMuchForeignIncome) thenReturn Some("123")
      helper.howMuchForeignIncome.get.label.key mustBe s"howMuchForeignIncome.checkYourAnswersLabel"
    }
  }

  "anyTaxableForeignIncome" must {
    val labelOption = "anyTaxableForeignIncomeOption"
    val label = "anyTaxableForeignIncome"
    val route = routes.AnyTaxableForeignIncomeController.onPageLoad(CheckMode).url

    s"have correct label (yes)" in {
      when(answers.anyTaxableForeignIncome) thenReturn Some(AnyTaxPaid.Yes("123"))
      val answer = answers.anyTaxableForeignIncome
      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.taxPaid(label, answer, route).get.label.key mustBe s"$label.checkYourAnswersLabel"
    }
    s"have correct label (no)" in {
      when(answers.anyTaxableForeignIncome) thenReturn Some(AnyTaxPaid.No)
      val answer = answers.anyTaxableForeignIncome
      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
    }
  }

  "otherTaxableIncomeName" must {
    s"have correct label" in {
      when(answers.otherTaxableIncomeName) thenReturn Some("Other taxable income")
      helper.otherTaxableIncomeName.get.label.key mustBe s"otherTaxableIncomeName.checkYourAnswersLabel"
    }
  }

  "howMuchOtherTaxableIncome" must {
    s"have correct label" in {
      when(answers.howMuchOtherTaxableIncome) thenReturn Some("123")
      helper.howMuchOtherTaxableIncome.get.label.key mustBe s"howMuchOtherTaxableIncome.checkYourAnswersLabel"
    }
  }

  "anyTaxableOtherIncome" must {
    val labelOption = "anyTaxableOtherIncomeOption"
    val label = "anyTaxableOtherIncome"
    val route = routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode).url

    s"have correct label (yes)" in {
      when(answers.anyTaxableOtherIncome) thenReturn Some(AnyTaxPaid.Yes("123"))
      val answer = answers.anyTaxableOtherIncome
      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.taxPaid(label, answer, route).get.label.key mustBe s"$label.checkYourAnswersLabel"
    }
    s"have correct label (no)" in {
      when(answers.anyTaxableOtherIncome) thenReturn Some(AnyTaxPaid.No)
      val answer = answers.anyTaxableOtherIncome
      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
    }
  }


  //Payment details
  //------------------------------------------------------------------------------

  "Is WhereToSendPayment yourself" must {
    s"have the correct label" in {
      when(answers.whereToSendPayment) thenReturn Some(Myself)
      helper.whereToSendPayment.get.label.key mustBe s"whereToSendPayment.checkYourAnswersLabel"
    }
  }

  "Is WhereToSendPayment nominee else" must {
    s"have the correct label" in {
      when(answers.whereToSendPayment) thenReturn Some(Nominee)
      helper.whereToSendPayment.get.label.key mustBe s"whereToSendPayment.checkYourAnswersLabel"
    }
  }

  "Payment address correct (yes)" must {
    s"have the correct label" in {
      when(answers.paymentAddressCorrect) thenReturn Some(true)
      helper.paymentAddressCorrect.get.label.key mustBe s"paymentAddressCorrect.checkYourAnswersLabel"
    }
  }

  "Payment address correct (no)" must {
    s"have the correct label" in {
      when(answers.paymentAddressCorrect) thenReturn Some(false)
      helper.paymentAddressCorrect.get.label.key mustBe s"paymentAddressCorrect.checkYourAnswersLabel"
    }
  }

  "Nominee name" must {
    s"have the correct label" in {
      when(answers.nomineeFullName) thenReturn Some("Test name")
      helper.nomineeFullName.get.label.key mustBe s"nomineeFullName.checkYourAnswersLabel"
    }
  }

  "Is anyAgentRef (true)" must {
    s"have the correct label" in {
      when(answers.anyAgentRef) thenReturn Some(AnyAgentRef.Yes("AB12345"))
      helper.anyAgentRef.get.label.key mustBe s"anyAgentRefOption.checkYourAnswersLabel"
      helper.agentReferenceNumber.get.label.key mustBe s"anyAgentRef.checkYourAnswersLabel"
    }
  }

  "Is anyAgentRef (false)" must {
    s"have the correct label" in {
      when(answers.anyAgentRef) thenReturn Some(AnyAgentRef.No)
      helper.anyAgentRef.get.label.key mustBe s"anyAgentRefOption.checkYourAnswersLabel"
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

  "Payment UK Address" must {
    s"have correct label" in {
      when(answers.paymentUKAddress) thenReturn Some(UkAddress("line 1", "line 2", None, None, None, "AA11 1AA"))
      helper.paymentUKAddress.get.label.key mustBe s"paymentUKAddress.checkYourAnswersLabel"
    }
  }

  "Payment International Address" must {
    s"have correct label" in {
      when(answers.paymentInternationalAddress) thenReturn Some(InternationalAddress("line 1", "line 2", None, None, None, "country"))
      helper.paymentInternationalAddress.get.label.key mustBe s"paymentInternationalAddress.checkYourAnswersLabel"
    }
  }

  //Contact details
  //------------------------------------------------------------------------------

  "Telephone number (yes)" must {
    s"have the correct label" in {
      when(answers.anyTelephoneNumber) thenReturn Some(TelephoneOption.Yes("0191123123"))
      helper.anyTelephoneNumber.get.label.key mustBe s"telephoneNumberOption.checkYourAnswersLabel"
      helper.telephoneNumber.get.label.key mustBe s"telephoneNumber.checkYourAnswersLabel"
    }
  }

  "Telephone number (no)" must {
    s"have the correct label" in {
      when(answers.anyTelephoneNumber) thenReturn Some(TelephoneOption.No)
      helper.anyTelephoneNumber.get.label.key mustBe s"telephoneNumberOption.checkYourAnswersLabel"
    }
  }

}
