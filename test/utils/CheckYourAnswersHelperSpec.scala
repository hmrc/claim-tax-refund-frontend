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
      helper.howMuchBereavementAllowance.get.answer.key mustBe s"£$amount"
    }
  }

  "howMuchCarersAllowance" must {
    s"have the correct label and answer" in {
      when(answers.howMuchCarersAllowance) thenReturn Some(amount)
      helper.howMuchCarersAllowance.get.label.key mustBe s"howMuchCarersAllowance.checkYourAnswersLabel"
      helper.howMuchCarersAllowance.get.answer.key mustBe s"£$amount"
    }
  }

  "howMuchJobseekersAllowance" must {
    s"have the correct label and answer" in {
      when(answers.howMuchJobseekersAllowance) thenReturn Some(amount)
      helper.howMuchJobseekersAllowance.get.label.key mustBe s"howMuchJobseekersAllowance.checkYourAnswersLabel"
      helper.howMuchJobseekersAllowance.get.answer.key mustBe s"£$amount"
    }
  }

  "howMuchIncapacityBenefit" must {
    s"have the correct label and answer" in {
      when(answers.howMuchIncapacityBenefit) thenReturn Some(amount)
      helper.howMuchIncapacityBenefit.get.label.key mustBe s"howMuchIncapacityBenefit.checkYourAnswersLabel"
      helper.howMuchIncapacityBenefit.get.answer.key mustBe s"£$amount"
    }
  }

  "howMuchEmploymentAndSupportAllowance" must {
    s"have the correct label and answer" in {
      when(answers.howMuchEmploymentAndSupportAllowance) thenReturn Some(amount)
      helper.howMuchEmploymentAndSupportAllowance.get.label.key mustBe s"howMuchEmploymentAndSupportAllowance.checkYourAnswersLabel"
      helper.howMuchEmploymentAndSupportAllowance.get.answer.key mustBe s"£$amount"
    }
  }

  "howMuchStatePension" must {
    s"have the correct label and answer" in {
      when(answers.howMuchStatePension) thenReturn Some(amount)
      helper.howMuchStatePension.get.label.key mustBe s"howMuchStatePension.checkYourAnswersLabel"
      helper.howMuchStatePension.get.answer.key mustBe s"£$amount"
    }
  }

  "otherBenefit" must {
    s"have the correct answer" in {
      val otherBenefit = Seq(OtherBenefit("qwerty", "1234"))
      when(answers.otherBenefit) thenReturn Some(otherBenefit)

      helper.otherBenefits.head.get.label.key mustBe "qwerty"
      helper.otherBenefits.head.get.answer.key mustBe s"£$amount"
    }
   }

  "otherBenefit" must {
    s"return a empty Seq when empty" in {
      when(answers.otherBenefit) thenReturn None

      helper.otherBenefits mustBe Seq()
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

  "otherCompanyBenefit" must {
    s"have the correct answer" in {
      val otherCompanyBenefit = Seq(OtherCompanyBenefit("qwerty", "1234"))
      when(answers.otherCompanyBenefit) thenReturn Some(otherCompanyBenefit)

      helper.otherCompanyBenefit.head.get.label.key mustBe "qwerty"
      helper.otherCompanyBenefit.head.get.answer.key mustBe s"£$amount"
    }
  }

  "otherCompanyBenefit" must {
    s"return a empty Seq when empty" in {
      when(answers.otherCompanyBenefit) thenReturn None

      helper.otherCompanyBenefit mustBe Seq()
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
    val route = routes.AnyTaxableRentalIncomeController.onPageLoad(CheckMode).url

    s"have correct label and answer (yes)" in {
      when(answers.anyTaxableRentalIncome) thenReturn Some(AnyTaxPaid.Yes(amount))
      val answer = answers.anyTaxableRentalIncome

      helper.anyTaxPaid("", answer, route).get.answer.key mustBe yes
      helper.taxPaid("", answer, route).get.answer.key mustBe s"£$amount"
    }

    s"have correct label and answer (no)" in {
      when(answers.anyTaxableRentalIncome) thenReturn Some(AnyTaxPaid.No)
      val answer = answers.anyTaxableRentalIncome

      helper.anyTaxPaid("", answer, route).get.answer.key mustBe no
    }
  }

  "howMuchBankInterest" must {
    s"have correct label and answer" in {
      when(answers.howMuchBankInterest) thenReturn Some(amount)
      helper.howMuchBankInterest.get.label.key mustBe "howMuchBankInterest.checkYourAnswersLabel"
      helper.howMuchBankInterest.get.answer.key mustBe amount
    }
  }

  "anyTaxableBankInterest" must {
    val route = routes.AnyTaxableBankInterestController.onPageLoad(CheckMode).url

    s"have correct label and answer (yes)" in {
      when(answers.anyTaxableBankInterest) thenReturn Some(AnyTaxPaid.Yes(amount))
      val answer = answers.anyTaxableBankInterest

      helper.anyTaxPaid("", answer, route).get.answer.key mustBe yes
      helper.taxPaid("", answer, route).get.answer.key mustBe s"£$amount"
    }

    s"have correct label and answer (no)" in {
      when(answers.anyTaxableBankInterest) thenReturn Some(AnyTaxPaid.No)
      val answer = answers.anyTaxableBankInterest

      helper.anyTaxPaid("", answer, route).get.answer.key mustBe no
    }
  }

  "howMuchInvestmentOrDividend" must {
    s"have correct label and answer" in {
      when(answers.howMuchInvestmentOrDividend) thenReturn Some(amount)
      helper.howMuchInvestmentOrDividend.get.label.key mustBe "howMuchInvestmentOrDividend.checkYourAnswersLabel"
      helper.howMuchInvestmentOrDividend.get.answer.key mustBe amount
    }
  }

  "anyTaxableInvestments" must {
    val route = routes.AnyTaxableInvestmentsController.onPageLoad(CheckMode).url

    s"have correct label and answer (yes)" in {
      when(answers.anyTaxableInvestments) thenReturn Some(AnyTaxPaid.Yes(amount))
      val answer = answers.anyTaxableInvestments

      helper.anyTaxPaid("", answer, route).get.answer.key mustBe yes
      helper.taxPaid("", answer, route).get.answer.key mustBe s"£$amount"
    }

    s"have correct label and answer (no)" in {
      when(answers.anyTaxableBankInterest) thenReturn Some(AnyTaxPaid.No)
      val answer = answers.anyTaxableBankInterest
      helper.anyTaxPaid("", answer, route).get.answer.key mustBe no
    }
  }

  "howMuchForeignIncome" must {
    s"have correct label and answer" in {
      when(answers.howMuchForeignIncome) thenReturn Some(amount)
      helper.howMuchForeignIncome.get.label.key mustBe "howMuchForeignIncome.checkYourAnswersLabel"
      helper.howMuchForeignIncome.get.answer.key mustBe amount
    }
  }

  "anyTaxableForeignIncome" must {
    val route = routes.AnyTaxableForeignIncomeController.onPageLoad(CheckMode).url

    s"have correct label and answer (yes)" in {
      when(answers.anyTaxableForeignIncome) thenReturn Some(AnyTaxPaid.Yes(amount))
      val answer = answers.anyTaxableForeignIncome

      helper.anyTaxPaid("", answer, route).get.answer.key mustBe yes
      helper.taxPaid("", answer, route).get.answer.key mustBe s"£$amount"
    }

    s"have correct label and answer (no)" in {
      when(answers.anyTaxableForeignIncome) thenReturn Some(AnyTaxPaid.No)
      val answer = answers.anyTaxableForeignIncome

      helper.anyTaxPaid("", answer, route).get.answer.key mustBe no
    }
  }

  "otherTaxableIncome" must {
    s"have correct label and answer" in {
      when(answers.otherTaxableIncome) thenReturn Some(Seq(OtherTaxableIncome("qwerty", "1234")))
      when(answers.anyTaxableOtherIncome) thenReturn Some(Seq(AnyTaxPaid.Yes("123")))

      helper.otherTaxableIncome.head.get.label.key mustBe "qwerty"
      helper.otherTaxableIncome.head.get.answer.key mustBe s"£$amount"
      helper.otherTaxableIncome.head.get.url mustBe routes.OtherTaxableIncomeController.onPageLoad(CheckMode, 0).url
      helper.otherTaxableIncome(1).get.label.key mustBe messages("anyTaxableOtherIncomeOption.checkYourAnswersLabel", "qwerty")
      helper.otherTaxableIncome(1).get.answer.key mustBe "site.yes"
      helper.otherTaxableIncome(1).get.url mustBe routes.AnyTaxableOtherIncomeController.onPageLoad(CheckMode, 0).url
      helper.otherTaxableIncome(2).get.label.key mustBe messages("anyTaxableOtherIncome.checkYourAnswersLabel", "qwerty")
      helper.otherTaxableIncome(2).get.answer.key mustBe "£123"
      helper.otherTaxableIncome(2).get.url mustBe routes.AnyTaxableOtherIncomeController.onPageLoad(CheckMode, 0).url
    }
  }

  "anyTaxableOtherIncome" must {
    val route = routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode).url

    s"have correct label and answer (yes)" in {
      when(answers.anyTaxableOtherIncome) thenReturn Some(Seq(AnyTaxPaid.Yes(amount)))
      val answer: Option[Seq[AnyTaxPaid]] = answers.anyTaxableOtherIncome

      helper.anyTaxPaid("", Some(answer.get.head), route).get.answer.key mustBe yes
      helper.taxPaid("", Some(answer.get.head), route).get.answer.key mustBe s"£$amount"
    }

    s"have correct label and answer (no)" in {
      when(answers.anyTaxableOtherIncome) thenReturn Some(Seq(AnyTaxPaid.No))
      val answer: Option[Seq[AnyTaxPaid]] = answers.anyTaxableOtherIncome

      helper.anyTaxPaid("", Some(answer.get.head), route).get.answer.key mustBe no
    }
  }

  "anyOtherTaxableIncome (yes)" must {
    s"have the correct label and answer" in {
      when(answers.anyOtherTaxableIncome) thenReturn Some(true)
      helper.anyOtherTaxableIncome.get.label.key mustBe "anyOtherTaxableIncome.checkYourAnswersLabel"
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
      helper.whereToSendPayment.get.label.key mustBe "whereToSendPayment.checkYourAnswersLabel"
      helper.whereToSendPayment.get.answer.key mustBe "whereToSendPayment.myself"
    }
  }

  "Is WhereToSendPayment nominee else" must {
    s"have the correct label and answer" in {
      when(answers.whereToSendPayment) thenReturn Some(Nominee)
      helper.whereToSendPayment.get.label.key mustBe "whereToSendPayment.checkYourAnswersLabel"
      helper.whereToSendPayment.get.answer.key mustBe "whereToSendPayment.nominee"
    }
  }

  "Payment address correct (yes)" must {
    s"have the correct label and answer" in {
      when(answers.paymentAddressCorrect) thenReturn Some(true)
      helper.paymentAddressCorrect.get.label.key mustBe "paymentAddressCorrect.checkYourAnswersLabel"
      helper.paymentAddressCorrect.get.answer.key mustBe yes
    }
  }

  "Payment address correct (no)" must {
    s"have the correct label and answer" in {
      when(answers.paymentAddressCorrect) thenReturn Some(false)
      helper.paymentAddressCorrect.get.label.key mustBe "paymentAddressCorrect.checkYourAnswersLabel"
      helper.paymentAddressCorrect.get.answer.key mustBe no
    }
  }

  "Nominee name" must {
    s"have the correct label and answer" in {
      val nomineeName = "Test name"
      when(answers.nomineeFullName) thenReturn Some(nomineeName)
      helper.nomineeFullName.get.label.key mustBe "nomineeFullName.checkYourAnswersLabel"
      helper.nomineeFullName.get.answer.key mustBe nomineeName
    }
  }

  "Is anyAgentRef (true)" must {
    s"have the correct label and answer" in {
      val agentRef = "AB12345"
      when(answers.anyAgentRef) thenReturn Some(AnyAgentRef.Yes(agentRef))

      helper.anyAgentRef.get.label.key mustBe "anyAgentRefOption.checkYourAnswersLabel"
      helper.agentReferenceNumber.get.label.key mustBe "anyAgentRef.checkYourAnswersLabel"

      helper.anyAgentRef.get.answer.key mustBe yes
      helper.agentReferenceNumber.get.answer.key mustBe agentRef
    }
  }

  "Is anyAgentRef (false)" must {
    s"have the correct label and answer" in {
      when(answers.anyAgentRef) thenReturn Some(AnyAgentRef.No)
      helper.anyAgentRef.get.label.key mustBe "anyAgentRefOption.checkYourAnswersLabel"
      helper.anyAgentRef.get.answer.key mustBe no
    }
  }

  "Is isPaymentAddressInTheUK (true)" must {
    s"have the correct label and answer" in {
      when(answers.isPaymentAddressInTheUK) thenReturn Some(true)
      helper.isPaymentAddressInTheUK.get.label.key mustBe "isPaymentAddressInTheUK.checkYourAnswersLabel"
      helper.isPaymentAddressInTheUK.get.answer.key mustBe yes
    }
  }

  "Is isPaymentAddressInTheUK (false)" must {
    s"have the correct label and answer" in {
      when(answers.isPaymentAddressInTheUK) thenReturn Some(false)
      helper.isPaymentAddressInTheUK.get.label.key mustBe "isPaymentAddressInTheUK.checkYourAnswersLabel"
      helper.isPaymentAddressInTheUK.get.answer.key mustBe no
    }
  }

  "Payment UK Address" must {
    s"have correct label and answer" in {
      val address = UkAddress("line 1", "line 2", None, None, None, "AA11 1AA")
      when(answers.paymentUKAddress) thenReturn Some(address)
      helper.paymentUKAddress.get.label.key mustBe "paymentUKAddress.checkYourAnswersLabel"
      helper.paymentUKAddress.get.answer.key mustBe UkAddress.asString(address)
    }
  }

  "Payment International Address" must {
    s"have correct label and answer" in {
      val intAddress = InternationalAddress("line 1", "line 2", None, None, None, "country")
      when(answers.paymentInternationalAddress) thenReturn Some(intAddress)
      helper.paymentInternationalAddress.get.label.key mustBe "paymentInternationalAddress.checkYourAnswersLabel"
      helper.paymentInternationalAddress.get.answer.key mustBe InternationalAddress.asString(intAddress)
    }
  }

  //Contact details
  //------------------------------------------------------------------------------

  "Telephone number (yes)" must {
    s"have the correct label and answer" in {
      val telNo = "0191123123"
      when(answers.anyTelephoneNumber) thenReturn Some(TelephoneOption.Yes(telNo))
      helper.anyTelephoneNumber.get.label.key mustBe "telephoneNumberOption.checkYourAnswersLabel"
      helper.telephoneNumber.get.label.key mustBe "telephoneNumber.checkYourAnswersLabel"

      helper.anyTelephoneNumber.get.answer.key mustBe yes
      helper.telephoneNumber.get.answer.key mustBe telNo
    }
  }

  "Telephone number (no)" must {
    s"have the correct label and answer" in {
      when(answers.anyTelephoneNumber) thenReturn Some(TelephoneOption.No)
      helper.anyTelephoneNumber.get.label.key mustBe "telephoneNumberOption.checkYourAnswersLabel"
      helper.anyTelephoneNumber.get.answer.key mustBe no
    }
  }
}
