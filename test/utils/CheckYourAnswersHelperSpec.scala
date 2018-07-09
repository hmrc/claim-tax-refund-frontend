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
  private val yes = "site.yes"
  private val no = "site.no"
  private val amount = "1234"

  "Change link" must {
    "be shown when set to true" in {
      when(answers.anyTelephoneNumber) thenReturn Some(TelephoneOption.Yes("0191123123"))
      helper.telephoneNumber.get.changeLink mustBe true
    }

    "be hidden when set to false" in {
      when(answers.userDetails) thenReturn Some(UserDetails("test", "test", UkAddress("test", "test", None, None, None, "test")))
      helper.userName.get.changeLink mustBe false
    }
  }

  //Claim details
  //------------------------------------------------------------------------------

  "Select current year minus 1" must {
    s"have correct label and answer" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus1)
      helper.selectTaxYear.get.label.key mustBe s"selectTaxYear.checkYourAnswersLabel"
      helper.selectTaxYear.get.answer.key mustBe CYMinus1.asString(messages)
    }
  }

  "Select current year minus 2" must {
    s"have correct label and answer" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus2)
      helper.selectTaxYear.get.label.key mustBe s"selectTaxYear.checkYourAnswersLabel"
      helper.selectTaxYear.get.answer.key mustBe CYMinus2.asString(messages)
    }
  }

  "Select current year minus 3" must {
    s"have correct label and answer" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus3)
      helper.selectTaxYear.get.label.key mustBe s"selectTaxYear.checkYourAnswersLabel"
      helper.selectTaxYear.get.answer.key mustBe CYMinus3.asString(messages)
    }
  }

  "Select current year minus 4" must {
    s"have correct label and answer" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus4)
      helper.selectTaxYear.get.label.key mustBe s"selectTaxYear.checkYourAnswersLabel"
      helper.selectTaxYear.get.answer.key mustBe CYMinus4.asString(messages)
    }
  }

  "Select current year minus 5" must {
    s"have correct label and answer" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus5)
      helper.selectTaxYear.get.label.key mustBe s"selectTaxYear.checkYourAnswersLabel"
      helper.selectTaxYear.get.answer.key mustBe CYMinus5.asString(messages)
    }
  }

  "Employment details (yes)" must {
    s"have correct label and answer" in {
      when(answers.employmentDetails) thenReturn Some(true)
      helper.employmentDetails.get.label.key mustBe s"employmentDetails.checkYourAnswersLabel"
      helper.employmentDetails.get.answer.key mustBe yes
    }
  }

  "Employment details (no)" must {
    s"have correct label and answer" in {
      when(answers.employmentDetails) thenReturn Some(false)
      helper.employmentDetails.get.label.key mustBe s"employmentDetails.checkYourAnswersLabel"
      helper.employmentDetails.get.answer.key mustBe no
    }
  }

  "Enter PAYE Ref" must {
    s"have correct label and answer" in {
      val testRef = "AB12345"
      when(answers.enterPayeReference) thenReturn Some(testRef)
      helper.enterPayeReference.get.label.key mustBe s"enterPayeReference.checkYourAnswersLabel"
      helper.enterPayeReference.get.answer.key mustBe testRef
    }
  }

  "Details of employment or pension" must {
    s"have correct label and answer" in {
      val details = "Details of employment or pension"
      when(answers.detailsOfEmploymentOrPension) thenReturn Some(details)
      helper.detailsOfEmploymentOrPension.get.label.key mustBe s"detailsOfEmploymentOrPension.checkYourAnswersLabel"
      helper.detailsOfEmploymentOrPension.get.answer.key mustBe details
    }
  }

  //Benefits details
  //------------------------------------------------------------------------------

  "Any benefits (true)" must {
    s"have the correct label and answer" in {
      when(answers.anyBenefits) thenReturn Some(true)
      helper.anyBenefits.get.label.key mustBe s"anyBenefits.checkYourAnswersLabel"
      helper.anyBenefits.get.answer.key mustBe yes
    }
  }

  "Any benefits (false)" must {
    s"have the correct label and answer" in {
      when(answers.anyBenefits) thenReturn Some(false)
      helper.anyBenefits.get.label.key mustBe s"anyBenefits.checkYourAnswersLabel"
      helper.anyBenefits.get.answer.key mustBe no
    }
  }

  "selectBenefits" must {
    s"have correct label and answer" in {
      val benefits = Seq(
        Benefits.BEREAVEMENT_ALLOWANCE,
        Benefits.CARERS_ALLOWANCE,
        Benefits.JOBSEEKERS_ALLOWANCE,
        Benefits.INCAPACITY_BENEFIT,
        Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
        Benefits.STATE_PENSION,
        Benefits.OTHER_TAXABLE_BENEFIT
      )
      when(answers.selectBenefits) thenReturn Some(benefits)
      helper.selectBenefits.get.label.key mustBe s"selectBenefits.checkYourAnswersLabel"
      helper.selectBenefits.get.answer.key mustBe benefits.map(
        benefit => messages("selectBenefits." + benefit)
      ).mkString("<br>")
    }
  }

  "howMuchBereavementAllowance" must {
    s"have the correct label and answer" in {
      when(answers.howMuchBereavementAllowance) thenReturn Some(amount)
      helper.howMuchBereavementAllowance.get.label.key mustBe s"howMuchBereavementAllowance.checkYourAnswersLabel"
      helper.howMuchBereavementAllowance.get.answer.key mustBe amount
    }
  }

  "howMuchCarersAllowance" must {
    s"have the correct label and answer" in {
      when(answers.howMuchCarersAllowance) thenReturn Some(amount)
      helper.howMuchCarersAllowance.get.label.key mustBe s"howMuchCarersAllowance.checkYourAnswersLabel"
      helper.howMuchCarersAllowance.get.answer.key mustBe amount
    }
  }

  "howMuchJobseekersAllowance" must {
    s"have the correct label and answer" in {
      when(answers.howMuchJobseekersAllowance) thenReturn Some(amount)
      helper.howMuchJobseekersAllowance.get.label.key mustBe s"howMuchJobseekersAllowance.checkYourAnswersLabel"
      helper.howMuchJobseekersAllowance.get.answer.key mustBe amount
    }
  }

  "howMuchIncapacityBenefit" must {
    s"have the correct label and answer" in {
      when(answers.howMuchIncapacityBenefit) thenReturn Some(amount)
      helper.howMuchIncapacityBenefit.get.label.key mustBe s"howMuchIncapacityBenefit.checkYourAnswersLabel"
      helper.howMuchIncapacityBenefit.get.answer.key mustBe amount
    }
  }

  "howMuchEmploymentAndSupportAllowance" must {
    s"have the correct label and answer" in {
      when(answers.howMuchEmploymentAndSupportAllowance) thenReturn Some(amount)
      helper.howMuchEmploymentAndSupportAllowance.get.label.key mustBe s"howMuchEmploymentAndSupportAllowance.checkYourAnswersLabel"
      helper.howMuchEmploymentAndSupportAllowance.get.answer.key mustBe amount
    }
  }

  "howMuchStatePension" must {
    s"have the correct label and answer" in {
      when(answers.howMuchStatePension) thenReturn Some(amount)
      helper.howMuchStatePension.get.label.key mustBe s"howMuchStatePension.checkYourAnswersLabel"
      helper.howMuchStatePension.get.answer.key mustBe amount
    }
  }

  "otherBenefitsName" must {
    s"have the correct label and answer" in {
      val benefitName = "other benefit"
      when(answers.otherBenefitsName) thenReturn Some(benefitName)
      helper.otherBenefitsName.get.label.key mustBe s"otherBenefitsName.checkYourAnswersLabel"
      helper.otherBenefitsName.get.answer.key mustBe benefitName
    }
  }

  "howMuchOtherBenefit" must {
    s"have the correct label and answer" in {
      when(answers.howMuchOtherBenefit) thenReturn Some(amount)
      helper.howMuchOtherBenefit.get.label.key mustBe s"howMuchOtherBenefit.checkYourAnswersLabel"
      helper.howMuchOtherBenefit.get.answer.key mustBe amount
    }
  }

  "anyOtherBenefits (yes)" must {
    s"have the correct label and answer" in {
      when(answers.anyOtherBenefits) thenReturn Some(true)
      helper.anyOtherBenefits.get.label.key mustBe s"anyOtherBenefits.checkYourAnswersLabel"
      helper.anyOtherBenefits.get.answer.key mustBe yes
    }
  }

  "anyOtherBenefits (no)" must {
    s"have the correct label and answer" in {
      when(answers.anyOtherBenefits) thenReturn Some(false)
      helper.anyOtherBenefits.get.label.key mustBe s"anyOtherBenefits.checkYourAnswersLabel"
      helper.anyOtherBenefits.get.answer.key mustBe no
    }
  }

  //Company benefits details
  //------------------------------------------------------------------------------

  "Any company benefit (true)" must {
    s"have the correct label and answer" in {
      when(answers.anyCompanyBenefits) thenReturn Some(true)
      helper.anyCompanyBenefits.get.label.key mustBe s"anyCompanyBenefits.checkYourAnswersLabel"
      helper.anyCompanyBenefits.get.answer.key mustBe yes
    }
  }

  "Any company benefit (false)" must {
    s"have the correct label and answer" in {
      when(answers.anyCompanyBenefits) thenReturn Some(false)
      helper.anyCompanyBenefits.get.label.key mustBe s"anyCompanyBenefits.checkYourAnswersLabel"
      helper.anyCompanyBenefits.get.answer.key mustBe no
    }
  }

  "selectCompanyBenefits" must {
    s"have correct label and answer" in {
      val companyBenefits = Seq(
        CompanyBenefits.COMPANY_CAR_BENEFIT,
        CompanyBenefits.FUEL_BENEFIT,
        CompanyBenefits.MEDICAL_BENEFIT,
        CompanyBenefits.OTHER_COMPANY_BENEFIT
      )
      when(answers.selectCompanyBenefits) thenReturn Some(companyBenefits)
      helper.selectCompanyBenefits.get.label.key mustBe s"selectCompanyBenefits.checkYourAnswersLabel"
      helper.selectCompanyBenefits.get.answer.key mustBe companyBenefits.map(
        companyBenefit => messages("selectCompanyBenefits." + companyBenefit)
      ).mkString("<br>")
    }
  }

  "howMuchCarBenefits" must {
    s"have the correct label and answer" in {
      when(answers.howMuchCarBenefits) thenReturn Some(amount)
      helper.howMuchCarBenefits.get.label.key mustBe s"howMuchCarBenefits.checkYourAnswersLabel"
      helper.howMuchCarBenefits.get.answer.key mustBe amount
    }
  }

  "howMuchFuelBenefit" must {
    s"have the correct label and answer" in {
      when(answers.howMuchFuelBenefit) thenReturn Some(amount)
      helper.howMuchFuelBenefit.get.label.key mustBe s"howMuchFuelBenefit.checkYourAnswersLabel"
      helper.howMuchFuelBenefit.get.answer.key mustBe amount
    }
  }

  "howMuchMedicalBenefits" must {
    s"have the correct label and answer" in {
      when(answers.howMuchMedicalBenefits) thenReturn Some(amount)
      helper.howMuchMedicalBenefits.get.label.key mustBe s"howMuchMedicalBenefits.checkYourAnswersLabel"
      helper.howMuchMedicalBenefits.get.answer.key mustBe amount
    }
  }

  "otherCompanyBenefitsName" must {
    s"have the correct label and answer" in {
      val companyBenefitName = "other benefit"
      when(answers.otherCompanyBenefitsName) thenReturn Some(companyBenefitName)
      helper.otherCompanyBenefitsName.get.label.key mustBe s"otherCompanyBenefitsName.checkYourAnswersLabel"
      helper.otherCompanyBenefitsName.get.answer.key mustBe companyBenefitName
    }
  }

  "howMuchOtherCompanyBenefit" must {
    s"have the correct label and answer" in {
      when(answers.howMuchOtherCompanyBenefit) thenReturn Some(amount)
      helper.howMuchOtherCompanyBenefit.get.label.key mustBe s"howMuchOtherCompanyBenefit.checkYourAnswersLabel"
      helper.howMuchOtherCompanyBenefit.get.answer.key mustBe amount
    }
  }

  "anyOtherCompanyBenefits (yes)" must {
    s"have the correct label and answer" in {
      when(answers.anyOtherCompanyBenefits) thenReturn Some(true)
      helper.anyOtherCompanyBenefits.get.label.key mustBe s"anyOtherCompanyBenefits.checkYourAnswersLabel"
      helper.anyOtherCompanyBenefits.get.answer.key mustBe yes
    }
  }

  "anyOtherCompanyBenefits (no)" must {
    s"have the correct label and answer" in {
      when(answers.anyOtherCompanyBenefits) thenReturn Some(false)
      helper.anyOtherCompanyBenefits.get.label.key mustBe s"anyOtherCompanyBenefits.checkYourAnswersLabel"
      helper.anyOtherCompanyBenefits.get.answer.key mustBe no
    }
  }

  //Taxable income details
  //------------------------------------------------------------------------------

  "Any taxable income (true)" must {
    s"have the correct label and answer" in {
      when(answers.anyTaxableIncome) thenReturn Some(true)
      helper.anyTaxableIncome.get.label.key mustBe s"anyTaxableIncome.checkYourAnswersLabel"
      helper.anyTaxableIncome.get.answer.key mustBe yes
    }
  }

  "Any taxable income (false)" must {
    s"have the correct label and answer" in {
      when(answers.anyTaxableIncome) thenReturn Some(false)
      helper.anyTaxableIncome.get.label.key mustBe s"anyTaxableIncome.checkYourAnswersLabel"
      helper.anyTaxableIncome.get.answer.key mustBe no
    }
  }

  "selectTaxableIncome" must {
    s"have correct label and answer" in {
      val taxableIncomes = Seq(
        TaxableIncome.RENTAL_INCOME,
        TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
        TaxableIncome.INVESTMENT_OR_DIVIDENDS,
        TaxableIncome.FOREIGN_INCOME,
        TaxableIncome.OTHER_TAXABLE_INCOME
      )
      when(answers.selectTaxableIncome) thenReturn Some(taxableIncomes)
      helper.selectTaxableIncome.get.label.key mustBe s"selectTaxableIncome.checkYourAnswersLabel"
      helper.selectTaxableIncome.get.answer.key mustBe taxableIncomes.map(
        income => messages("selectTaxableIncome." + income)
      ).mkString("<br>")
    }
  }

  "howMuchRentalIncome" must {
    s"have correct label and answer" in {
      when(answers.howMuchRentalIncome) thenReturn Some(amount)
      helper.howMuchRentalIncome.get.label.key mustBe s"howMuchRentalIncome.checkYourAnswersLabel"
      helper.howMuchRentalIncome.get.answer.key mustBe amount
    }
  }

  "anyTaxableRentalIncome" must {
    val labelOption = "anyTaxableRentalIncomeOption"
    val label = "anyTaxableRentalIncome"
    val route = routes.AnyTaxableRentalIncomeController.onPageLoad(CheckMode).url

    s"have correct label and answer (yes)" in {
      when(answers.anyTaxableRentalIncome) thenReturn Some(AnyTaxPaid.Yes(amount))
      val answer = answers.anyTaxableRentalIncome

      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.taxPaid(label, answer, route).get.label.key mustBe s"$label.checkYourAnswersLabel"

      helper.anyTaxPaid(labelOption, answer, route).get.answer.key mustBe yes
      helper.taxPaid(label, answer, route).get.answer.key mustBe amount
    }

    s"have correct label and answer (no)" in {
      when(answers.anyTaxableRentalIncome) thenReturn Some(AnyTaxPaid.No)
      val answer = answers.anyTaxableRentalIncome

      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.anyTaxPaid(labelOption, answer, route).get.answer.key mustBe no
    }
  }

  "howMuchBankInterest" must {
    s"have correct label and answer" in {
      when(answers.howMuchBankInterest) thenReturn Some(amount)
      helper.howMuchBankInterest.get.label.key mustBe s"howMuchBankInterest.checkYourAnswersLabel"
      helper.howMuchBankInterest.get.answer.key mustBe amount
    }
  }

  "anyTaxableBankInterest" must {
    val labelOption = "anyTaxableBankInterestOption"
    val label = "anyTaxableBankInterest"
    val route = routes.AnyTaxableBankInterestController.onPageLoad(CheckMode).url

    s"have correct label and answer (yes)" in {
      when(answers.anyTaxableBankInterest) thenReturn Some(AnyTaxPaid.Yes(amount))
      val answer = answers.anyTaxableBankInterest

      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.taxPaid(label, answer, route).get.label.key mustBe s"$label.checkYourAnswersLabel"

      helper.anyTaxPaid(labelOption, answer, route).get.answer.key mustBe yes
      helper.taxPaid(label, answer, route).get.answer.key mustBe amount
    }

    s"have correct label and answer (no)" in {
      when(answers.anyTaxableBankInterest) thenReturn Some(AnyTaxPaid.No)
      val answer = answers.anyTaxableBankInterest

      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.anyTaxPaid(labelOption, answer, route).get.answer.key mustBe no
    }
  }

  "howMuchInvestmentOrDividend" must {
    s"have correct label and answer" in {
      when(answers.howMuchInvestmentOrDividend) thenReturn Some(amount)
      helper.howMuchInvestmentOrDividend.get.label.key mustBe s"howMuchInvestmentOrDividend.checkYourAnswersLabel"
      helper.howMuchInvestmentOrDividend.get.answer.key mustBe amount
    }
  }

  "anyTaxableInvestments" must {
    val labelOption = "anyTaxableInvestmentsOption"
    val label = "anyTaxableInvestments"
    val route = routes.AnyTaxableInvestmentsController.onPageLoad(CheckMode).url

    s"have correct label and answer (yes)" in {
      when(answers.anyTaxableInvestments) thenReturn Some(AnyTaxPaid.Yes(amount))
      val answer = answers.anyTaxableInvestments

      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.taxPaid(label, answer, route).get.label.key mustBe s"$label.checkYourAnswersLabel"

      helper.anyTaxPaid(labelOption, answer, route).get.answer.key mustBe yes
      helper.taxPaid(label, answer, route).get.answer.key mustBe amount
    }

    s"have correct label and answer (no)" in {
      when(answers.anyTaxableBankInterest) thenReturn Some(AnyTaxPaid.No)
      val answer = answers.anyTaxableBankInterest

      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.anyTaxPaid(labelOption, answer, route).get.answer.key mustBe no
    }
  }

  "howMuchForeignIncome" must {
    s"have correct label and answer" in {
      when(answers.howMuchForeignIncome) thenReturn Some(amount)
      helper.howMuchForeignIncome.get.label.key mustBe s"howMuchForeignIncome.checkYourAnswersLabel"
      helper.howMuchForeignIncome.get.answer.key mustBe amount
    }
  }

  "anyTaxableForeignIncome" must {
    val labelOption = "anyTaxableForeignIncomeOption"
    val label = "anyTaxableForeignIncome"
    val route = routes.AnyTaxableForeignIncomeController.onPageLoad(CheckMode).url

    s"have correct label and answer (yes)" in {
      when(answers.anyTaxableForeignIncome) thenReturn Some(AnyTaxPaid.Yes(amount))
      val answer = answers.anyTaxableForeignIncome

      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.taxPaid(label, answer, route).get.label.key mustBe s"$label.checkYourAnswersLabel"

      helper.anyTaxPaid(labelOption, answer, route).get.answer.key mustBe yes
      helper.taxPaid(label, answer, route).get.answer.key mustBe amount
    }

    s"have correct label and answer (no)" in {
      when(answers.anyTaxableForeignIncome) thenReturn Some(AnyTaxPaid.No)
      val answer = answers.anyTaxableForeignIncome

      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.anyTaxPaid(labelOption, answer, route).get.answer.key mustBe no
    }
  }

  "otherTaxableIncomeName" must {
    s"have correct label and answer" in {
      val taxableIncomeName = "Other taxable income"
      when(answers.otherTaxableIncomeName) thenReturn Some(taxableIncomeName)
      helper.otherTaxableIncomeName.get.label.key mustBe s"otherTaxableIncomeName.checkYourAnswersLabel"
      helper.otherTaxableIncomeName.get.answer.key mustBe taxableIncomeName
    }
  }

  "howMuchOtherTaxableIncome" must {
    s"have correct label and answer" in {
      when(answers.howMuchOtherTaxableIncome) thenReturn Some(amount)
      helper.howMuchOtherTaxableIncome.get.label.key mustBe s"howMuchOtherTaxableIncome.checkYourAnswersLabel"
      helper.howMuchOtherTaxableIncome.get.answer.key mustBe amount
    }
  }

  "anyTaxableOtherIncome" must {
    val labelOption = "anyTaxableOtherIncomeOption"
    val label = "anyTaxableOtherIncome"
    val route = routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode).url

    s"have correct label and answer (yes)" in {
      when(answers.anyTaxableOtherIncome) thenReturn Some(AnyTaxPaid.Yes(amount))
      val answer = answers.anyTaxableOtherIncome

      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.taxPaid(label, answer, route).get.label.key mustBe s"$label.checkYourAnswersLabel"

      helper.anyTaxPaid(labelOption, answer, route).get.answer.key mustBe yes
      helper.taxPaid(label, answer, route).get.answer.key mustBe amount
    }

    s"have correct label and answer (no)" in {
      when(answers.anyTaxableOtherIncome) thenReturn Some(AnyTaxPaid.No)
      val answer = answers.anyTaxableOtherIncome

      helper.anyTaxPaid(labelOption, answer, route).get.label.key mustBe s"$labelOption.checkYourAnswersLabel"
      helper.anyTaxPaid(labelOption, answer, route).get.answer.key mustBe no
    }
  }

  "anyOtherTaxableIncome (yes)" must {
    s"have the correct label and answer" in {
      when(answers.anyOtherTaxableIncome) thenReturn Some(true)
      helper.anyOtherTaxableIncome.get.label.key mustBe s"anyOtherTaxableIncome.checkYourAnswersLabel"
      helper.anyOtherTaxableIncome.get.answer.key mustBe yes
    }
  }

  "anyOtherTaxableIncome (no)" must {
    s"have the correct label and answer" in {
      when(answers.anyOtherTaxableIncome) thenReturn Some(false)
      helper.anyOtherTaxableIncome.get.label.key mustBe s"anyOtherTaxableIncome.checkYourAnswersLabel"
      helper.anyOtherTaxableIncome.get.answer.key mustBe no
    }
  }


  //Payment details
  //------------------------------------------------------------------------------

  "Is WhereToSendPayment yourself" must {
    s"have the correct label and answer" in {
      when(answers.whereToSendPayment) thenReturn Some(Myself)
      helper.whereToSendPayment.get.label.key mustBe s"whereToSendPayment.checkYourAnswersLabel"
    }
  }

  "Is WhereToSendPayment nominee else" must {
    s"have the correct label and answer" in {
      when(answers.whereToSendPayment) thenReturn Some(Nominee)
      helper.whereToSendPayment.get.label.key mustBe s"whereToSendPayment.checkYourAnswersLabel"
    }
  }

  "Payment address correct (yes)" must {
    s"have the correct label and answer" in {
      when(answers.paymentAddressCorrect) thenReturn Some(true)
      helper.paymentAddressCorrect.get.label.key mustBe s"paymentAddressCorrect.checkYourAnswersLabel"
    }
  }

  "Payment address correct (no)" must {
    s"have the correct label and answer" in {
      when(answers.paymentAddressCorrect) thenReturn Some(false)
      helper.paymentAddressCorrect.get.label.key mustBe s"paymentAddressCorrect.checkYourAnswersLabel"
    }
  }

  "Nominee name" must {
    s"have the correct label and answer" in {
      when(answers.nomineeFullName) thenReturn Some("Test name")
      helper.nomineeFullName.get.label.key mustBe s"nomineeFullName.checkYourAnswersLabel"
    }
  }

  "Is anyAgentRef (true)" must {
    s"have the correct label and answer" in {
      when(answers.anyAgentRef) thenReturn Some(AnyAgentRef.Yes("AB12345"))
      helper.anyAgentRef.get.label.key mustBe s"anyAgentRefOption.checkYourAnswersLabel"
      helper.agentReferenceNumber.get.label.key mustBe s"anyAgentRef.checkYourAnswersLabel"
    }
  }

  "Is anyAgentRef (false)" must {
    s"have the correct label and answer" in {
      when(answers.anyAgentRef) thenReturn Some(AnyAgentRef.No)
      helper.anyAgentRef.get.label.key mustBe s"anyAgentRefOption.checkYourAnswersLabel"
    }
  }

  "Is isPaymentAddressInTheUK (true)" must {
    s"have the correct label and answer" in {
      when(answers.isPaymentAddressInTheUK) thenReturn Some(true)
      helper.isPaymentAddressInTheUK.get.label.key mustBe s"isPaymentAddressInTheUK.checkYourAnswersLabel"
    }
  }

  "Is isPaymentAddressInTheUK (false)" must {
    s"have the correct label and answer" in {
      when(answers.isPaymentAddressInTheUK) thenReturn Some(false)
      helper.isPaymentAddressInTheUK.get.label.key mustBe s"isPaymentAddressInTheUK.checkYourAnswersLabel"
    }
  }

  "Payment UK Address" must {
    s"have correct label and answer" in {
      when(answers.paymentUKAddress) thenReturn Some(UkAddress("line 1", "line 2", None, None, None, "AA11 1AA"))
      helper.paymentUKAddress.get.label.key mustBe s"paymentUKAddress.checkYourAnswersLabel"
    }
  }

  "Payment International Address" must {
    s"have correct label and answer" in {
      when(answers.paymentInternationalAddress) thenReturn Some(InternationalAddress("line 1", "line 2", None, None, None, "country"))
      helper.paymentInternationalAddress.get.label.key mustBe s"paymentInternationalAddress.checkYourAnswersLabel"
    }
  }

  //Contact details
  //------------------------------------------------------------------------------

  "Telephone number (yes)" must {
    s"have the correct label and answer" in {
      when(answers.anyTelephoneNumber) thenReturn Some(TelephoneOption.Yes("0191123123"))
      helper.anyTelephoneNumber.get.label.key mustBe s"telephoneNumberOption.checkYourAnswersLabel"
      helper.telephoneNumber.get.label.key mustBe s"telephoneNumber.checkYourAnswersLabel"
    }
  }

  "Telephone number (no)" must {
    s"have the correct label and answer" in {
      when(answers.anyTelephoneNumber) thenReturn Some(TelephoneOption.No)
      helper.anyTelephoneNumber.get.label.key mustBe s"telephoneNumberOption.checkYourAnswersLabel"
    }
  }

}
