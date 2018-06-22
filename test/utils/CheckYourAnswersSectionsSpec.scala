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
      when(answers.otherCompanyBenefitsName) thenReturn Some("data")
      when(answers.howMuchOtherCompanyBenefit) thenReturn Some("1234")

      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.companyBenefits)
      val rows = sections.companyBenefitDetails.rows

      rows.size mustBe 8
      rows.head.label.key mustBe "anyOtherCompanyBenefits.checkYourAnswersLabel"
      rows(1).label.key mustBe "selectCompanyBenefits.checkYourAnswersLabel"
      rows(2).label.key mustBe "howMuchCarBenefits.checkYourAnswersLabel"
      rows(3).label.key mustBe "howMuchFuelBenefit.checkYourAnswersLabel"
      rows(4).label.key mustBe "howMuchMedicalBenefits.checkYourAnswersLabel"
      rows(5).label.key mustBe "anyOtherCompanyBenefits.checkYourAnswersLabel"
      rows(6).label.key mustBe "otherCompanyBenefitsName.checkYourAnswersLabel"
      rows(7).label.key mustBe "howMuchOtherCompanyBenefit.checkYourAnswersLabel"
    }

    "have the correct rows in the right order in the Payment Details section" in {
      when(answers.whereToSendPayment) thenReturn Some(Nominee)
      when(answers.nomineeFullName) thenReturn Some("Agent Name")
      when(answers.anyAgentRef) thenReturn Some(AnyAgentRef.Yes("AB1234"))
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
      when(answers.anyTaxableIncome) thenReturn Some(false)

      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
      val rows = sections.incomeDetails.rows

      rows.size mustBe 4
      rows.head.label.key mustBe "selectTaxYear.checkYourAnswersLabel"
      rows(1).label.key mustBe "employmentDetails.checkYourAnswersLabel"
      rows(2).label.key mustBe "anyBenefits.checkYourAnswersLabel"
      rows(3).label.key mustBe "anyTaxableIncome.checkYourAnswersLabel"
    }

    "have the correct rows in the right order in the Contact Details section" in {
      when(answers.telephoneNumber) thenReturn Some("0191666666")

      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
      val rows = sections.contactDetails.rows

      rows.size mustBe 1
      rows.head.label.key mustBe "telephoneNumber.checkYourAnswersLabel"
    }
  }

  "AnyBenefits section" must {
    "have the right section title for Benefit Details" in {
      val sections = new CheckYourAnswersSections(helper, MockUserAnswers.benefitsWithNoIncome)
      sections.benefitDetails.headingKey mustBe Some("checkYourAnswers.benefitDetailsSection")
    }

    "have the correct rows in the right order of Any Benefits Section" in {
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

      when(answers.howMuchBereavementAllowance) thenReturn Some("1234")
      when(answers.howMuchCarersAllowance) thenReturn Some("1234")
      when(answers.howMuchJobseekersAllowance) thenReturn Some("1234")
      when(answers.howMuchEmploymentAndSupportAllowance) thenReturn Some("1234")
      when(answers.howMuchIncapacityBenefit) thenReturn Some("1234")
      when(answers.howMuchStatePension) thenReturn Some("1234")
      when(answers.anyOtherTaxableIncome) thenReturn Some(true)
      when(answers.otherBenefitsName) thenReturn Some("Other")
      when(answers.howMuchOtherBenefit) thenReturn Some("1234")

      val sections = new CheckYourAnswersSections(helper, answers)
      val rows = sections.benefitDetails.rows

      rows.size mustBe 11
      rows.head.label.key mustBe "anyBenefits.checkYourAnswersLabel"
      rows(1).label.key mustBe "selectBenefits.checkYourAnswersLabel"
      rows(2).label.key mustBe "howMuchBereavementAllowance.checkYourAnswersLabel"
      rows(3).label.key mustBe "howMuchCarersAllowance.checkYourAnswersLabel"
      rows(4).label.key mustBe "howMuchJobseekersAllowance.checkYourAnswersLabel"
      rows(5).label.key mustBe "howMuchEmploymentAndSupportAllowance.checkYourAnswersLabel"
      rows(6).label.key mustBe "howMuchIncapacityBenefit.checkYourAnswersLabel"
      rows(7).label.key mustBe "howMuchStatePension.checkYourAnswersLabel"
      rows(8).label.key mustBe "anyOtherTaxableIncome.checkYourAnswersLabel"
      rows(9).label.key mustBe "otherBenefitsName.checkYourAnswersLabel"
      rows(10).label.key mustBe "howMuchOtherBenefit.checkYourAnswersLabel"

    }
  }
}
