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
import models.{CheckMode, Index}
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar
import play.api.i18n.Messages
import viewmodels.AnswerRow

class CheckYourAnswersSectionsSpec extends SpecBase with MockitoSugar with BeforeAndAfterEach {



  "sections have correct label" must {
    val answers = MockUserAnswers.minimalValidUserAnswers
    val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
    val sections = new CheckYourAnswersSections(helper, answers)

    "have the right section title for claim details" in {
      sections.claimSection.headingKey mustBe Some("checkYourAnswers.claimSection")
    }

    "have the right section title for benefits details" in {
      sections.benefitSection.headingKey mustBe Some("checkYourAnswers.benefitsSection")
    }

    "have the right section title for company benefits details" in {
      sections.companyBenefitSection.headingKey mustBe Some("checkYourAnswers.companyBenefitSection")
    }

    "have the right section title for taxable income details" in {
      sections.taxableIncomeSection.headingKey mustBe Some("checkYourAnswers.taxableIncomeSection")
    }

    "have the right section title for payment details" in {
      sections.paymentSection.headingKey mustBe Some("checkYourAnswers.paymentSection")
    }

    "have the right section title for contact details" in {
      sections.contactSection.headingKey mustBe Some("checkYourAnswers.contactSection")
    }
  }

  "all questions are answered and have the correct rows in the right order" must {
    "Claim details section" in {
      val answers = MockUserAnswers.claimDetailsUserAnswers
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val rows = sections.claimSection.rows

      rows.size mustBe 4
      rows.head.label.key mustBe "selectTaxYear.checkYourAnswersLabel"
      rows(1).label.key mustBe "employmentDetails.checkYourAnswersLabel"
      rows(2).label.key mustBe "enterPayeReference.checkYourAnswersLabel"
      rows(3).label.key mustBe "detailsOfEmploymentOrPension.checkYourAnswersLabel"
    }

    "Benefits section" in {
      val answers = MockUserAnswers.benefitsUserAnswers
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val rows: Seq[AnswerRow] = sections.benefitSection.rows
      val otherBenefitSection = sections.otherBenefitsSection

      rows.size mustBe 8
      rows.head.label.key mustBe "anyBenefits.checkYourAnswersLabel"
      rows(1).label.key mustBe "selectBenefits.checkYourAnswersLabel"
      rows(2).label.key mustBe "howMuchBereavementAllowance.checkYourAnswersLabel"
      rows(3).label.key mustBe "howMuchCarersAllowance.checkYourAnswersLabel"
      rows(4).label.key mustBe "howMuchJobseekersAllowance.checkYourAnswersLabel"
      rows(5).label.key mustBe "howMuchEmploymentAndSupportAllowance.checkYourAnswersLabel"
      rows(6).label.key mustBe "howMuchIncapacityBenefit.checkYourAnswersLabel"
      rows(7).label.key mustBe "howMuchStatePension.checkYourAnswersLabel"

      otherBenefitSection.rows.size mustBe 3
      otherBenefitSection.headingKey.get mustBe "otherBenefit.checkYourAnswersLabel"
      otherBenefitSection.addLinkText.get mustBe "otherBenefit.add"
      otherBenefitSection.addLinkUrl.get mustBe routes.OtherBenefitController.onPageLoad(CheckMode, Index(otherBenefitSection.rows.size)).url
    }

    "Company benefits section" in {
      val answers = MockUserAnswers.companyBenefitsUserAnswers
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val rows = sections.companyBenefitSection.rows
      val otherCompanyBenefitSection = sections.otherCompanyBenefitSection

      rows.size mustBe 5
      rows.head.label.key mustBe "anyCompanyBenefits.checkYourAnswersLabel"
      rows(1).label.key mustBe "selectCompanyBenefits.checkYourAnswersLabel"
      rows(2).label.key mustBe "howMuchCarBenefits.checkYourAnswersLabel"
      rows(3).label.key mustBe "howMuchFuelBenefit.checkYourAnswersLabel"
      rows(4).label.key mustBe "howMuchMedicalBenefits.checkYourAnswersLabel"

      otherCompanyBenefitSection.rows.size mustBe 3
      otherCompanyBenefitSection.headingKey.get mustBe "otherCompanyBenefit.checkYourAnswersLabel"
      otherCompanyBenefitSection.addLinkText.get mustBe "otherCompanyBenefit.add"
      otherCompanyBenefitSection.addLinkUrl.get mustBe routes.OtherCompanyBenefitController.onPageLoad(CheckMode, Index(otherCompanyBenefitSection.rows.size)).url
    }

    "Taxable income section" in {
      val answers = MockUserAnswers.taxableIncomeUserAnswers
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val rows = sections.taxableIncomeSection.rows
      val otherTaxableIncomeSection = sections.otherTaxableIncomeSection

      rows.size mustBe 14
      rows.head.label.key mustBe "anyTaxableIncome.checkYourAnswersLabel"
      rows(1).label.key mustBe "selectTaxableIncome.checkYourAnswersLabel"
      rows(2).label.key mustBe "howMuchRentalIncome.checkYourAnswersLabel"
      rows(3).label.key mustBe "anyTaxableRentalIncomeOption.checkYourAnswersLabel"
      rows(4).label.key mustBe "anyTaxableRentalIncome.checkYourAnswersLabel"
      rows(5).label.key mustBe "howMuchBankInterest.checkYourAnswersLabel"
      rows(6).label.key mustBe "anyTaxableBankInterestOption.checkYourAnswersLabel"
      rows(7).label.key mustBe "anyTaxableBankInterest.checkYourAnswersLabel"
      rows(8).label.key mustBe "howMuchInvestmentOrDividend.checkYourAnswersLabel"
      rows(9).label.key mustBe "anyTaxableInvestmentsOption.checkYourAnswersLabel"
      rows(10).label.key mustBe "anyTaxableInvestments.checkYourAnswersLabel"
      rows(11).label.key mustBe "howMuchForeignIncome.checkYourAnswersLabel"
      rows(12).label.key mustBe "anyTaxableForeignIncomeOption.checkYourAnswersLabel"
      rows(13).label.key mustBe "anyTaxableForeignIncome.checkYourAnswersLabel"

      otherTaxableIncomeSection.rows.size mustBe 3
      otherTaxableIncomeSection.headingKey.get mustBe "otherTaxableIncome.checkYourAnswersLabel"
      otherTaxableIncomeSection.addLinkText.get mustBe "otherTaxableIncome.add"
      otherTaxableIncomeSection.addLinkUrl.get mustBe routes.OtherTaxableIncomeController.onPageLoad(CheckMode, Index(otherTaxableIncomeSection.rows.size)).url
    }

    "Payment details section (Self)" in {
      val answers = MockUserAnswers.selfPaymentDetailsUserAnswers
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val rows = sections.paymentSection.rows

      rows.size mustBe 4
      rows.head.label.key mustBe "whereToSendPayment.checkYourAnswersLabel"
      rows(1).label.key mustBe "paymentAddressCorrect.checkYourAnswersLabel"
      rows(2).label.key mustBe "isPaymentAddressInTheUK.checkYourAnswersLabel"
      rows(3).label.key mustBe "paymentUKAddress.checkYourAnswersLabel"
    }

    "Payment details section (Nominee)" in {
      val answers = MockUserAnswers.nomineePaymentDetailsUserAnswers
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val rows = sections.paymentSection.rows

      rows.size mustBe 6
      rows.head.label.key mustBe "whereToSendPayment.checkYourAnswersLabel"
      rows(1).label.key mustBe "nomineeFullName.checkYourAnswersLabel"
      rows(2).label.key mustBe "anyAgentRefOption.checkYourAnswersLabel"
      rows(3).label.key mustBe "anyAgentRef.checkYourAnswersLabel"
      rows(4).label.key mustBe "isPaymentAddressInTheUK.checkYourAnswersLabel"
      rows(5).label.key mustBe "paymentInternationalAddress.checkYourAnswersLabel"
    }

    "Contact Details section" in {
      val answers = MockUserAnswers.contactDetailsUserAnswers
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val rows = sections.contactSection.rows

      rows.size mustBe 2
      rows.head.label.key mustBe "telephoneNumberOption.checkYourAnswersLabel"
      rows(1).label.key mustBe "telephoneNumber.checkYourAnswersLabel"
    }
  }
}
