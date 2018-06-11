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
import play.api.i18n.Messages

class CheckYourAnswersSectionsSpec extends SpecBase with MockitoSugar with BeforeAndAfterEach {
  private var answers = mock[UserAnswers]
  private var helper = new CheckYourAnswersHelper(answers)(messages: Messages)

  override def beforeEach = {
    super.beforeEach()
    answers = MockUserAnswers.nothingAnswered
    helper = new CheckYourAnswersHelper(answers)(messages: Messages)
  }
  "sections to show" must {

    "give the right sections in the minimal valid journey" in {
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)

      sections.sectionsToShow mustBe Seq(
        sections.incomeDetails,
        sections.paymentDetails,
        sections.contactDetails
      )
    }

    "give the right sections when PAYE claim and have benefits but no income" in {
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.benefitsWithNoIncome)

      sections.sectionsToShow mustBe Seq(
        sections.incomeDetails,
        sections.benefitDetails,
        sections.paymentDetails,
        sections.contactDetails
      )
    }

    "give the right sections when PAYE claim and have income but no benefits" in {
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.incomeWithNoBenefits)

      sections.sectionsToShow mustBe Seq(
        sections.incomeDetails,
        sections.otherIncomeDetails,
        sections.paymentDetails,
        sections.contactDetails
      )
    }

    "give the right sections when PAYE claim and have income and benefits" in {
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.benefitsWithIncome)

      sections.sectionsToShow mustBe Seq(
        sections.incomeDetails,
        sections.benefitDetails,
        sections.otherIncomeDetails,
        sections.paymentDetails,
        sections.contactDetails
      )
    }

    "have the right section title for Income Details" in {
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
      sections.incomeDetails.headingKey mustBe Some("checkYourAnswers.incomeDetailsSection")
    }

    "have the right section title for Benefit Details" in {
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.benefitsWithNoIncome)
      sections.benefitDetails.headingKey mustBe Some("checkYourAnswers.benefitDetailsSection")
    }

    "have the right section title for Company Benefits Details" in {
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.companyBenefits)
      sections.companyBenefitDetails.headingKey mustBe Some("checkYourAnswers.companyBenefitsDetailsSection")
    }

    "have the right section title for Other Income Details" in {
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.benefitsWithIncome)
      sections.otherIncomeDetails.headingKey mustBe Some("checkYourAnswers.otherIncomeDetailsSection")
    }

    "have the right section title for Other Payment Details" in {
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.benefitsWithIncome)
      sections.paymentDetails.headingKey mustBe Some("checkYourAnswers.paymentDetailsSection")
    }

    "have the right section title for Contact Details" in {
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
      sections.contactDetails.headingKey mustBe Some("checkYourAnswers.contactDetailsSection")
    }
  }

  "all questions are answered" must {
    "have the correct rows in the right order in Company Benefits Details section" in {
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
      val rows = sections.companyBenefitDetails.rows

      rows.size mustBe 8
      rows.head.label.key mustBe "anyCompanyBenefits.checkYourAnswersLabel"
      rows(1).label.key mustBe "selectCompanyBenefits.checkYourAnswersLabel"
      rows(2).label.key mustBe "howMuchCarBenefits.checkYourAnswersLabel"
      rows(3).label.key mustBe "howMuchMedicalBenefits.checkYourAnswersLabel"
      rows(4).label.key mustBe "howMuchFuelBenefit.checkYourAnswersLabel"
      rows(5).label.key mustBe "anyOtherCompanyBenefits.checkYourAnswersLabel"
      rows(6).label.key mustBe "otherCompanyBenefitsDetails.checkYourAnswersLabel"
      rows(7).label.key mustBe "howMuchOtherCompanyBenefit.checkYourAnswersLabel"
    }

    "have the correct rows in the right order in the Payment Details section" in {
      when(answers.whereToSendPayment) thenReturn Some(Nominee)
      when(answers.nomineeFullName) thenReturn Some("Agent Name")
      when(answers.anyAgentRef) thenReturn Some(AgentRef.Yes("AB1234"))
      when(answers.isPaymentAddressInTheUK) thenReturn Some(false)
      when(answers.paymentInternationalAddress) thenReturn Some(InternationalAddress("Line 1", "Line 2", None, None, None, "Thailand"))

      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
      val rows = sections.paymentDetails.rows

      rows.size mustBe 5
      rows.head.label.key mustBe "whereToSendPayment.checkYourAnswersLabel"
      rows(1).label.key mustBe "nomineeFullName.checkYourAnswersLabel"
      rows(2).label.key mustBe "anyAgentRef.checkYourAnswersLabel"
      rows(3).label.key mustBe "isPaymentAddressInTheUK.checkYourAnswersLabel"
      rows(4).label.key mustBe "paymentInternationalAddress.checkYourAnswersLabel"
    }

    "have the correct rows in the right order in the Income Details section" in {
      when(answers.selectTaxYear) thenReturn Some(CYMinus2)
      when(answers.employmentDetails) thenReturn Some(false)
      when(answers.anyBenefits) thenReturn Some(false)
      when(answers.otherIncome) thenReturn Some(false)

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

      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
      val rows = sections.contactDetails.rows

      rows.size mustBe 1
      rows.head.label.key mustBe "telephoneNumber.checkYourAnswersLabel"
    }
  }
}
