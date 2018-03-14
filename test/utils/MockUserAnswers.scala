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

import models.WhereToSendPayment.OptionYou
import models.{UkAddress, UserDetails}
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
    when(answers.anyBenefits) thenReturn None
    when(answers.anyJobseekersAllowance) thenReturn None
    when(answers.howMuchJobseekersAllowance) thenReturn None
    when(answers.anyIncapacityBenefit) thenReturn None
    when(answers.howMuchIncapacityBenefit) thenReturn None
    when(answers.anyEmploymentAndSupportAllowance) thenReturn None
    when(answers.howMuchEmploymentAndSupportAllowance) thenReturn None
    when(answers.anyStatePension) thenReturn None
    when(answers.howMuchStatePension) thenReturn None
    when(answers.anyOtherTaxableBenefits) thenReturn None
    when(answers.otherBenefitsDetailsAndAmount) thenReturn None
    when(answers.otherIncome) thenReturn None
    when(answers.anyCarBenefits) thenReturn None
    when(answers.howMuchCarBenefits) thenReturn None
    when(answers.anyRentalIncome) thenReturn None
    when(answers.howMuchRentalIncome) thenReturn None
    when(answers.anyBankBuildingSocietyInterest) thenReturn None
    when(answers.howMuchBankBuildingSocietyInterest) thenReturn None
    when(answers.anyMedicalBenefits) thenReturn None
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
    when(answers.isTheAddressInTheUK) thenReturn Some(true)
    when(answers.ukAddress) thenReturn Some(UkAddress("Line 1", "Line 2", None, None, None, "DE2 7RD"))
    when(answers.telephoneNumber) thenReturn Some("983475894357934")

    answers
  }

  def minimalValidUserAnswers: UserAnswers = {

    val answers = nothingAnswered

    yourDetailsUserAnswers
    when(answers.whereToSendPayment) thenReturn Some(You)

    answers
  }

  def benefitsUserAnswers: UserAnswers = {

    val answers = nothingAnswered

    when(answers.anyJobseekersAllowance) thenReturn Some(true)
    when(answers.howMuchJobseekersAllowance) thenReturn Some("123123")
    when(answers.anyIncapacityBenefit) thenReturn Some(true)
    when(answers.howMuchIncapacityBenefit) thenReturn Some("123123")
    when(answers.anyEmploymentAndSupportAllowance) thenReturn Some(true)
    when(answers.howMuchEmploymentAndSupportAllowance) thenReturn Some("123123")
    when(answers.anyStatePension) thenReturn Some(true)
    when(answers.howMuchStatePension) thenReturn Some("123123")
    when(answers.anyOtherTaxableBenefits) thenReturn Some(true)
    when(answers.otherBenefitsDetailsAndAmount) thenReturn Some("123123")

    answers
  }

  def incomeUserAnswers: UserAnswers = {

    val answers = nothingAnswered

    when(answers.anyCarBenefits) thenReturn Some(true)
    when(answers.howMuchCarBenefits) thenReturn Some("123123")
    when(answers.anyRentalIncome) thenReturn Some(true)
    when(answers.howMuchRentalIncome) thenReturn Some("123123")
    when(answers.anyBankBuildingSocietyInterest) thenReturn Some(true)
    when(answers.howMuchBankBuildingSocietyInterest) thenReturn Some("123123")
    when(answers.anyMedicalBenefits) thenReturn Some(true)
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
}
