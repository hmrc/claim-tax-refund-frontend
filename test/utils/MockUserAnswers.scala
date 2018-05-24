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
import models.{SelectTaxYear, UkAddress, UserDetails}
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar

object MockUserAnswers extends MockitoSugar {

  def nothingAnswered: UserAnswers = {

    val answers = mock[UserAnswers]
    when(answers.userDetails) thenReturn None
    when(answers.isTheAddressInTheUK) thenReturn None
    when(answers.ukAddress) thenReturn None
    when(answers.internationalAddress) thenReturn None
    when(answers.telephoneNumber) thenReturn None
    when(answers.selectTaxYear) thenReturn None
    when(answers.employmentDetails) thenReturn None
    when(answers.anyBenefits) thenReturn None
    when(answers.howMuchJobseekersAllowance) thenReturn None
    when(answers.howMuchIncapacityBenefit) thenReturn None
    when(answers.howMuchEmploymentAndSupportAllowance) thenReturn None
    when(answers.howMuchStatePension) thenReturn None
    when(answers.anyOtherTaxableBenefits) thenReturn None
    when(answers.otherBenefitsDetailsAndAmount) thenReturn None
    when(answers.otherIncome) thenReturn None
    when(answers.howMuchCarBenefits) thenReturn None
    when(answers.howMuchRentalIncome) thenReturn None
    when(answers.howMuchBankBuildingSocietyInterest) thenReturn None
    when(answers.howMuchMedicalBenefits) thenReturn None
    when(answers.anyOtherTaxableIncome) thenReturn None
    when(answers.otherIncomeDetailsAndAmount) thenReturn None
    when(answers.whereToSendPayment) thenReturn None
    when(answers.payeeFullName) thenReturn None
    when(answers.anyAgentRef) thenReturn None
    when(answers.agentReferenceNumber) thenReturn None
    when(answers.isPayeeAddressInTheUK) thenReturn None
    when(answers.payeeUKAddress) thenReturn None
    when(answers.payeeInternationalAddress) thenReturn None

    answers
  }

  def yourDetailsUserAnswers: UserAnswers = {

    val answers = nothingAnswered

    when(answers.userDetails) thenReturn Some(UserDetails("test name", "AB123123A", UkAddress("testLine1", "testLine2", None, None, None, "AB1 2CD")))
    when(answers.selectTaxYear) thenReturn Some(CYMinus2)

    answers
  }

  def minimalValidUserAnswers: UserAnswers = {

    val answers = nothingAnswered

    when(answers.selectTaxYear) thenReturn Some(SelectTaxYear.CYMinus2)
    when(answers.employmentDetails) thenReturn Some(true)

    answers
  }

  def benefitsUserAnswers: UserAnswers = {

    val answers = nothingAnswered

    when(answers.howMuchJobseekersAllowance) thenReturn Some("123123")
    when(answers.howMuchIncapacityBenefit) thenReturn Some("123123")
    when(answers.howMuchEmploymentAndSupportAllowance) thenReturn Some("123123")
    when(answers.howMuchStatePension) thenReturn Some("123123")
    when(answers.anyOtherTaxableBenefits) thenReturn Some(true)
    when(answers.otherBenefitsDetailsAndAmount) thenReturn Some("123123")

    answers
  }

  def incomeUserAnswers: UserAnswers = {

    val answers = nothingAnswered

    when(answers.howMuchCarBenefits) thenReturn Some("123123")
    when(answers.howMuchRentalIncome) thenReturn Some("123123")
    when(answers.howMuchBankBuildingSocietyInterest) thenReturn Some("123123")
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
    when(answers.otherIncome) thenReturn Some(false)

    answers
  }

  def incomeWithNoBenefits: UserAnswers = {

    val answers = nothingAnswered

    yourDetailsUserAnswers
    when(answers.anyBenefits) thenReturn Some(false)
    when(answers.otherIncome) thenReturn Some(true)
    incomeUserAnswers

    answers
  }

  def benefitsWithIncome: UserAnswers = {

    val answers = nothingAnswered

    yourDetailsUserAnswers
    when(answers.anyBenefits) thenReturn Some(true)
    benefitsUserAnswers
    when(answers.otherIncome) thenReturn Some(true)
    incomeUserAnswers

    answers
  }

  def contactDetails: UserAnswers = {

    val answers = nothingAnswered

    when(answers.telephoneNumber) thenReturn Some("983475894357934")

    answers
  }
}
