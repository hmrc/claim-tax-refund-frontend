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
import models.templates.Metadata
import models._
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar

object MockUserAnswers extends MockitoSugar {

  def nothingAnswered: UserAnswers = {

    val answers = mock[UserAnswers]
    when(answers.userDetails) thenReturn None
    when(answers.isTheAddressInTheUK) thenReturn None
    when(answers.ukAddress) thenReturn None
    when(answers.internationalAddress) thenReturn None
    when(answers.anyTelephoneNumber) thenReturn None
    when(answers.telephoneNumber) thenReturn None
    when(answers.selectTaxYear) thenReturn None
    when(answers.employmentDetails) thenReturn None
    when(answers.anyBenefits) thenReturn None
    when(answers.howMuchJobseekersAllowance) thenReturn None
    when(answers.howMuchIncapacityBenefit) thenReturn None
    when(answers.howMuchEmploymentAndSupportAllowance) thenReturn None
    when(answers.howMuchStatePension) thenReturn None
    when(answers.anyOtherBenefits) thenReturn None
    when(answers.anyTaxableIncome) thenReturn None
    when(answers.howMuchCarBenefits) thenReturn None
    when(answers.howMuchRentalIncome) thenReturn None
    when(answers.howMuchBankInterest) thenReturn None
    when(answers.howMuchMedicalBenefits) thenReturn None
    when(answers.anyOtherTaxableIncome) thenReturn None
    when(answers.whereToSendPayment) thenReturn None
    when(answers.nomineeFullName) thenReturn None
    when(answers.anyAgentRef) thenReturn None
    when(answers.agentReferenceNumber) thenReturn None
    when(answers.isPaymentAddressInTheUK) thenReturn None
    when(answers.paymentUKAddress) thenReturn None
    when(answers.paymentInternationalAddress) thenReturn None
    when(answers.howMuchFuelBenefit) thenReturn None
    when(answers.selectBenefits) thenReturn None
    when(answers.selectCompanyBenefits) thenReturn None
    when(answers.otherCompanyBenefitsDetails) thenReturn None
    when(answers.anyOtherCompanyBenefits) thenReturn None
    when(answers.howMuchOtherCompanyBenefit) thenReturn None
    when(answers.howMuchBereavementAllowance) thenReturn None
    when(answers.howMuchCarersAllowance) thenReturn None
    when(answers.otherBenefitsName) thenReturn None
    when(answers.howMuchOtherBenefit) thenReturn None
    when(answers.howMuchForeignIncome) thenReturn None
    when(answers.detailsOfEmploymentOrPension) thenReturn None
    when(answers.selectTaxableIncome) thenReturn None
    when(answers.howMuchInvestmentOrDividend) thenReturn None
    when(answers.howMuchOtherTaxableIncome) thenReturn None

    answers
  }

  def yourDetailsUserAnswers: UserAnswers = {

    val answers = nothingAnswered

    when(answers.selectTaxYear) thenReturn Some(CYMinus2)

    answers
  }

  def minimalValidUserAnswers: UserAnswers = {

    val answers = nothingAnswered
    val metadata = new Metadata("test_case")

    when(answers.selectTaxYear) thenReturn Some(SelectTaxYear.CYMinus2)
    when(answers.employmentDetails) thenReturn Some(true)
    when(answers.anyBenefits) thenReturn Some(false)
    when(answers.anyTaxableIncome) thenReturn Some(false)
    when(answers.whereToSendPayment) thenReturn Some(WhereToSendPayment.Myself)
    when(answers.telephoneNumber) thenReturn Some("123456789")

    when(answers.pdfHtml) thenReturn Some ("<html>Test result</html>")
    when(answers.metadata) thenReturn Some (metadata)

    answers
  }

  def benefitsUserAnswers: UserAnswers = {

    val answers = nothingAnswered
    yourDetailsUserAnswers
    when(answers.anyBenefits) thenReturn Some(true)
    when(answers.selectBenefits) thenReturn Some(
      Set(Benefits.CARERS_ALLOWANCE,
        Benefits.BEREAVEMENT_ALLOWANCE,
        Benefits.INCAPACITY_BENEFIT,
        Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
        Benefits.JOBSEEKERS_ALLOWANCE,
        Benefits.OTHER_TAXABLE_BENEFIT,
        Benefits.STATE_PENSION
      )
    )

    when (answers.howMuchBereavementAllowance) thenReturn Some("1234")
    when (answers.howMuchCarersAllowance) thenReturn Some("1234")
    when (answers.howMuchJobseekersAllowance) thenReturn Some("1234")
    when (answers.howMuchEmploymentAndSupportAllowance) thenReturn Some("1234")
    when (answers.howMuchIncapacityBenefit) thenReturn Some("1234")
    when (answers.howMuchStatePension) thenReturn Some("1234")
    when (answers.otherBenefitsName) thenReturn  Some("Other")
    when (answers.howMuchOtherBenefit) thenReturn Some("1234")

    answers
  }

  def incomeUserAnswers: UserAnswers = {

    val answers = nothingAnswered

    when(answers.howMuchCarBenefits) thenReturn Some("123123")
    when(answers.howMuchRentalIncome) thenReturn Some("123123")
    when(answers.howMuchBankInterest) thenReturn Some("123123")
    when(answers.howMuchMedicalBenefits) thenReturn Some("123123")
    when(answers.anyOtherTaxableIncome) thenReturn Some(true)
    when(answers.howMuchEmploymentAndSupportAllowance) thenReturn Some("123123")

    answers
  }

  def benefitsWithNoIncome: UserAnswers = {

    val answers = nothingAnswered

    yourDetailsUserAnswers
    when(answers.anyBenefits) thenReturn Some(true)
    benefitsUserAnswers
    when(answers.anyTaxableIncome) thenReturn Some(false)

    answers
  }

  def incomeWithNoBenefits: UserAnswers = {

    val answers = nothingAnswered

    yourDetailsUserAnswers
    when(answers.anyBenefits) thenReturn Some(false)
    when(answers.anyTaxableIncome) thenReturn Some(true)
    incomeUserAnswers

    answers
  }

  def benefitsWithIncome: UserAnswers = {

    val answers = nothingAnswered

    yourDetailsUserAnswers
    when(answers.anyBenefits) thenReturn Some(true)
    benefitsUserAnswers
    when(answers.anyTaxableIncome) thenReturn Some(true)
    incomeUserAnswers

    answers
  }

  def contactDetails: UserAnswers = {

    val answers = nothingAnswered

    when(answers.telephoneNumber) thenReturn Some("983475894357934")

    answers
  }

  def companyBenefits: UserAnswers = {

    val answers = nothingAnswered

    yourDetailsUserAnswers
    when(answers.anyCompanyBenefits) thenReturn Some(true)
    when(answers.selectCompanyBenefits) thenReturn Some(
      Set(CompanyBenefits.COMPANY_CAR_BENEFIT,
        CompanyBenefits.MEDICAL_BENEFIT,
        CompanyBenefits.FUEL_BENEFIT,
        CompanyBenefits.OTHER_COMPANY_BENEFIT)
    )
    when(answers.howMuchCarBenefits) thenReturn Some("1234")
    when(answers.howMuchMedicalBenefits) thenReturn Some("1234")
    when(answers.howMuchFuelBenefit) thenReturn Some("1234")
    when(answers.anyOtherCompanyBenefits) thenReturn Some(true)
    when(answers.otherCompanyBenefitsDetails) thenReturn Some("other company benefit")
    when(answers.howMuchOtherCompanyBenefit) thenReturn Some("1234")

    answers
  }

}
