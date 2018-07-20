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

import models.SelectTaxYear.CYMinus2
import models.WhereToSendPayment.{Myself, Nominee}
import models._
import models.templates.Metadata
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar

object MockUserAnswers extends MockitoSugar {

  def nothingAnswered: UserAnswers = {
    val answers = mock[UserAnswers]

    //claim details
    when(answers.selectTaxYear) thenReturn None
    when(answers.employmentDetails) thenReturn None
    when(answers.enterPayeReference) thenReturn None
    when(answers.detailsOfEmploymentOrPension) thenReturn None

    //benefits details
    when(answers.anyBenefits) thenReturn None
    when(answers.selectBenefits) thenReturn None
    when(answers.howMuchBereavementAllowance) thenReturn None
    when(answers.howMuchCarersAllowance) thenReturn None
    when(answers.howMuchJobseekersAllowance) thenReturn None
    when(answers.howMuchIncapacityBenefit) thenReturn None
    when(answers.howMuchEmploymentAndSupportAllowance) thenReturn None
    when(answers.howMuchStatePension) thenReturn None
    when(answers.otherBenefitsName) thenReturn None
    when(answers.anyOtherBenefits) thenReturn None

    //company benefits details
    when(answers.anyCompanyBenefits) thenReturn None
    when(answers.selectCompanyBenefits) thenReturn None
    when(answers.howMuchCarBenefits) thenReturn None
    when(answers.howMuchFuelBenefit) thenReturn None
    when(answers.howMuchMedicalBenefits) thenReturn None
    when(answers.otherCompanyBenefitsName) thenReturn None
    when(answers.howMuchOtherCompanyBenefit) thenReturn None
    when(answers.anyOtherCompanyBenefits) thenReturn None

    //taxable income details
    when(answers.anyTaxableIncome) thenReturn None
    when(answers.selectTaxableIncome) thenReturn None
    when(answers.howMuchRentalIncome) thenReturn None
    when(answers.anyTaxableRentalIncome) thenReturn None
    when(answers.howMuchBankInterest) thenReturn None
    when(answers.anyTaxableBankInterest) thenReturn None
    when(answers.howMuchInvestmentOrDividend) thenReturn None
    when(answers.anyTaxableInvestments) thenReturn None
    when(answers.howMuchForeignIncome) thenReturn None
    when(answers.anyTaxableForeignIncome) thenReturn None
    when(answers.otherTaxableIncomeName) thenReturn None
    when(answers.howMuchOtherTaxableIncome) thenReturn None
    when(answers.anyTaxableOtherIncome) thenReturn None
    when(answers.anyOtherTaxableIncome) thenReturn None

    //payment details
    when(answers.whereToSendPayment) thenReturn None
    when(answers.paymentAddressCorrect) thenReturn None
    when(answers.nomineeFullName) thenReturn None
    when(answers.anyAgentRef) thenReturn None
    when(answers.isPaymentAddressInTheUK) thenReturn None
    when(answers.paymentUKAddress) thenReturn None
    when(answers.paymentInternationalAddress) thenReturn None

    //contact details
    when(answers.anyTelephoneNumber) thenReturn None

    answers
  }

  def minimalValidUserAnswers: UserAnswers = {
    val answers = nothingAnswered
    val metadata = new Metadata("test_case")

    when(answers.selectTaxYear) thenReturn Some(CYMinus2)
    when(answers.employmentDetails) thenReturn Some(true)
    when(answers.anyBenefits) thenReturn Some(false)
    when(answers.anyCompanyBenefits) thenReturn Some(false)
    when(answers.anyTaxableIncome) thenReturn Some(false)
    when(answers.whereToSendPayment) thenReturn Some(Myself)
    when(answers.paymentAddressCorrect) thenReturn Some(true)
    when(answers.anyTelephoneNumber) thenReturn Some(TelephoneOption.No)


    when(answers.pdfHtml) thenReturn Some("<html>Test result</html>")
    when(answers.metadata) thenReturn Some(metadata)

    answers
  }

  def claimDetailsUserAnswers: UserAnswers = {
    val answers = nothingAnswered

    when(answers.selectTaxYear) thenReturn Some(CYMinus2)
    when(answers.employmentDetails) thenReturn Some(false)
    when(answers.enterPayeReference) thenReturn Some("AB12345")
    when(answers.detailsOfEmploymentOrPension) thenReturn Some("Details of employment")

    answers
  }

  def benefitsUserAnswers: UserAnswers = {
    val answers = nothingAnswered

    when(answers.anyBenefits) thenReturn Some(true)
    when(answers.selectBenefits) thenReturn Some(
      Seq(Benefits.CARERS_ALLOWANCE,
        Benefits.BEREAVEMENT_ALLOWANCE,
        Benefits.INCAPACITY_BENEFIT,
        Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
        Benefits.JOBSEEKERS_ALLOWANCE,
        Benefits.OTHER_TAXABLE_BENEFIT,
        Benefits.STATE_PENSION
      )
    )
    when(answers.howMuchBereavementAllowance) thenReturn Some("1234")
    when(answers.howMuchCarersAllowance) thenReturn Some("1234")
    when(answers.howMuchJobseekersAllowance) thenReturn Some("1234")
    when(answers.howMuchEmploymentAndSupportAllowance) thenReturn Some("1234")
    when(answers.howMuchIncapacityBenefit) thenReturn Some("1234")
    when(answers.howMuchStatePension) thenReturn Some("1234")
    when(answers.otherBenefitsName) thenReturn Some(Seq("Other"))
    when(answers.anyOtherBenefits) thenReturn Some(false)

    answers
  }

  def companyBenefitsUserAnswers: UserAnswers = {
    val answers = nothingAnswered

    when(answers.anyCompanyBenefits) thenReturn Some(true)
    when(answers.selectCompanyBenefits) thenReturn Some(
      Seq(
        CompanyBenefits.COMPANY_CAR_BENEFIT,
        CompanyBenefits.MEDICAL_BENEFIT,
        CompanyBenefits.FUEL_BENEFIT,
        CompanyBenefits.OTHER_COMPANY_BENEFIT)
    )
    when(answers.howMuchCarBenefits) thenReturn Some("1234")
    when(answers.howMuchMedicalBenefits) thenReturn Some("1234")
    when(answers.howMuchFuelBenefit) thenReturn Some("1234")
    when(answers.otherCompanyBenefitsName) thenReturn Some("other company benefit")
    when(answers.howMuchOtherCompanyBenefit) thenReturn Some("1234")
    when(answers.anyOtherCompanyBenefits) thenReturn Some(false)

    answers
  }

  def taxableIncomeUserAnswers: UserAnswers = {
    val answers = nothingAnswered

    when(answers.anyTaxableIncome) thenReturn Some(true)
    when(answers.selectTaxableIncome) thenReturn Some(
      Seq(
        TaxableIncome.RENTAL_INCOME,
        TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
        TaxableIncome.INVESTMENT_OR_DIVIDENDS,
        TaxableIncome.FOREIGN_INCOME,
        TaxableIncome.OTHER_TAXABLE_INCOME
      )
    )
    when(answers.howMuchRentalIncome) thenReturn Some("1234")
    when(answers.anyTaxableRentalIncome) thenReturn Some(AnyTaxPaid.Yes("1234"))
    when(answers.howMuchBankInterest) thenReturn Some("1234")
    when(answers.anyTaxableBankInterest) thenReturn Some(AnyTaxPaid.Yes("1234"))
    when(answers.howMuchInvestmentOrDividend) thenReturn Some("other company benefit")
    when(answers.anyTaxableInvestments) thenReturn Some(AnyTaxPaid.Yes("1234"))
    when(answers.howMuchForeignIncome) thenReturn Some("1234")
    when(answers.anyTaxableForeignIncome) thenReturn Some(AnyTaxPaid.Yes("1234"))
    when(answers.otherTaxableIncomeName) thenReturn Some("1234")
    when(answers.howMuchOtherTaxableIncome) thenReturn Some("1234")
    when(answers.anyTaxableOtherIncome) thenReturn Some(AnyTaxPaid.Yes("1234"))
    when(answers.anyOtherTaxableIncome) thenReturn Some(false)

    answers
  }

  def selfPaymentDetailsUserAnswers: UserAnswers = {
    val answers = nothingAnswered

    when(answers.whereToSendPayment) thenReturn Some(Myself)
    when(answers.paymentAddressCorrect) thenReturn Some(false)
    when(answers.isPaymentAddressInTheUK) thenReturn Some(true)
    when(answers.paymentUKAddress) thenReturn Some(UkAddress("1","2",None,None,None,"AA11 11A"))

    answers
  }

  def nomineePaymentDetailsUserAnswers: UserAnswers = {
    val answers = nothingAnswered

    when(answers.whereToSendPayment) thenReturn Some(Nominee)
    when(answers.nomineeFullName) thenReturn Some("Nominee")
    when(answers.anyAgentRef) thenReturn Some(AnyAgentRef.Yes("12341234"))
    when(answers.isPaymentAddressInTheUK) thenReturn Some(false)
    when(answers.paymentInternationalAddress) thenReturn Some(InternationalAddress("1","2",None,None,None,"Country"))

    answers
  }

  def contactDetailsUserAnswers: UserAnswers = {
    val answers = nothingAnswered

    when(answers.anyTelephoneNumber) thenReturn Some(TelephoneOption.Yes("0191123123"))

    answers
  }

}
