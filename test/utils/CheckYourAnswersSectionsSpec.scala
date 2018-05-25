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
import models.SelectTaxYear.CYMinus2
import models.WhereToSendPayment.Nominee
import models._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar

class CheckYourAnswersSectionsSpec extends SpecBase with MockitoSugar with BeforeAndAfterEach {

  private var answers = mock[UserAnswers]

  override def beforeEach = {
    super.beforeEach()
    answers = MockUserAnswers.nothingAnswered
  }

  "sections to show" must {

    "give the right sections when SA claim" in {
      val helper = new CheckYourAnswersHelper(answers)
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)

      sections.sectionsToShow mustBe Seq(
        sections.yourDetails,
        sections.incomeDetails,
        sections.paymentDetails,
        sections.contactDetails
      )
    }

    "give the right sections when PAYE claim and have benefits but no income" in {
      val helper = new CheckYourAnswersHelper(answers)
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.benefitsWithNoIncome)

      sections.sectionsToShow mustBe Seq(
        sections.yourDetails,
        sections.incomeDetails,
        sections.benefitDetails,
        sections.paymentDetails,
        sections.contactDetails
      )
    }

    "give the right sections when PAYE claim and have income but no benefits" in {
      val helper = new CheckYourAnswersHelper(answers)
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.incomeWithNoBenefits)

      sections.sectionsToShow mustBe Seq(
        sections.yourDetails,
        sections.incomeDetails,
        sections.otherIncomeDetails,
        sections.paymentDetails,
        sections.contactDetails
      )
    }

    "give the right sections when PAYE claim and have income and benefits" in {
      val helper = new CheckYourAnswersHelper(answers)
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.benefitsWithIncome)

      sections.sectionsToShow mustBe Seq(
        sections.yourDetails,
        sections.incomeDetails,
        sections.benefitDetails,
        sections.otherIncomeDetails,
        sections.paymentDetails,
        sections.contactDetails
      )
    }

    "have the right section title for Your Details" in {
      val helper = new CheckYourAnswersHelper(answers)
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
      sections.yourDetails.headingKey mustBe Some("checkYourAnswers.yourDetailsSection")
    }

    "have the right section title for Income Details" in {
      val helper = new CheckYourAnswersHelper(answers)
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
      sections.incomeDetails.headingKey mustBe Some("checkYourAnswers.incomeDetailsSection")
    }

    "have the right section title for Benefit Details" in {
      val helper = new CheckYourAnswersHelper(answers)
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.benefitsWithNoIncome)
      sections.benefitDetails.headingKey mustBe Some("checkYourAnswers.benefitDetailsSection")
    }

    "have the right section title for Other Income Details" in {
      val helper = new CheckYourAnswersHelper(answers)
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.benefitsWithIncome)
      sections.otherIncomeDetails.headingKey mustBe Some("checkYourAnswers.otherIncomeDetailsSection")
    }

    "have the right section title for Other Payment Details" in {
      val helper = new CheckYourAnswersHelper(answers)
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.benefitsWithIncome)
      sections.paymentDetails.headingKey mustBe Some("checkYourAnswers.paymentDetailsSection")
    }

    "have the right section title for Contact Details" in {
      val helper = new CheckYourAnswersHelper(answers)
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
      sections.contactDetails.headingKey mustBe Some("checkYourAnswers.contactDetailsSection")
    }
  }

  "all questions are answered" must {
    "have the correct rows in the right order in the Your Details section" in {
      when(answers.userDetails) thenReturn Some(UserDetails("Dave Smith", "AB123456A",
        UkAddress("Line 1", "Line 2", Some("Line 3"), Some("Line 4"), None, "AB123CD")))

      val helper = new CheckYourAnswersHelper(answers)
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
      val rows = sections.yourDetails.rows

      rows.size mustBe 3
      rows.head.label.key mustBe "userDetails.checkYourAnswersLabel.name"
      rows(1).label.key mustBe "userDetails.checkYourAnswersLabel.nino"
      rows(2).label.key mustBe "userDetails.checkYourAnswersLabel.address"
    }

    "have the correct rows in the right order in the Payment Details section" in {
      when(answers.whereToSendPayment) thenReturn Some(Nominee)
      when(answers.nomineeFullName) thenReturn Some("Agent Name")
      when(answers.anyAgentRef) thenReturn Some(false)
      when(answers.isPayeeAddressInTheUK) thenReturn Some(false)
      when(answers.payeeInternationalAddress) thenReturn Some(InternationalAddress("Line 1", "Line 2", None, None, None, "Thailand"))

      val helper = new CheckYourAnswersHelper(answers)
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
      val rows = sections.paymentDetails.rows

      rows.size mustBe 5
      rows.head.label.key mustBe "whereToSendPayment.checkYourAnswersLabel"
      rows(1).label.key mustBe "nomineeFullName.checkYourAnswersLabel"
      rows(2).label.key mustBe "anyAgentRef.checkYourAnswersLabel"
      rows(3).label.key mustBe "isPayeeAddressInTheUK.checkYourAnswersLabel"
      rows(4).label.key mustBe "payeeInternationalAddress.checkYourAnswersLabel"
    }

    "have the correct rows in the right order in the Income Details section" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus2)
      when(answers.employmentDetails) thenReturn Some(false)
      when(answers.anyBenefits) thenReturn Some(false)
      when(answers.otherIncome) thenReturn Some(false)

      val helper = new CheckYourAnswersHelper(answers)
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
      val rows = sections.incomeDetails.rows

      rows.size mustBe 4
      rows.head.label.key mustBe "selectTaxYear.checkYourAnswersLabel"
      rows(1).label.key mustBe "employmentDetails.checkYourAnswersLabel"
      rows(2).label.key mustBe "anyBenefits.checkYourAnswersLabel"
      rows(3).label.key mustBe "otherIncome.checkYourAnswersLabel"
    }

    "have the correct rows in the right order in the Contact Details section" in {
      when(answers.telephoneNumber) thenReturn Some("0191666666")

      val helper = new CheckYourAnswersHelper(answers)
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
      val rows = sections.contactDetails.rows

      rows.size mustBe 1
      rows.head.label.key mustBe "telephoneNumber.checkYourAnswersLabel"
    }
  }
}
