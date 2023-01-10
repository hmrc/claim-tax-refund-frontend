/*
 * Copyright 2023 HM Revenue & Customs
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
import models.CheckMode
import org.scalatest.BeforeAndAfterEach
import org.scalatestplus.mockito.MockitoSugar
import play.api.i18n.Messages
import viewmodels.AnswerRow

class CheckYourAnswersSectionsSpec extends SpecBase with MockitoSugar with BeforeAndAfterEach {



  "sections have correct label" must {
    val answers = MockUserAnswers.minimalValidUserAnswers()
    val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
    val sections = new CheckYourAnswersSections(helper, answers)

    "have the right section title for claim details" in {
      sections.claimSection.headingKey mustBe Some("checkYourAnswers.claimSection")
    }

    "have the right section title for benefits details" in {
      sections.benefitSection.headingKey mustBe Some("checkYourAnswers.benefitSection")
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
      val answers = MockUserAnswers.claimDetailsUserAnswers()
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val rows = sections.claimSection.rows

      rows.size mustBe 4
      rows.head.label.key mustBe "selectTaxYear.heading"
      rows(1).label.key mustBe "employmentDetails.correctDetails"
      rows(2).label.key mustBe "enterPayeReference.heading"
      rows(3).label.key mustBe "detailsOfEmploymentOrPension.heading"
    }

    "Benefits section" in {
      val answers = MockUserAnswers.benefitsUserAnswers()
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val rows: Seq[AnswerRow] = sections.benefitSection.rows
      val otherBenefitSection = sections.otherBenefitsSection

      rows.size mustBe 8
      rows.head.label.key mustBe "anyBenefits.heading"
      rows(1).label.key mustBe "selectBenefits.heading"
      rows(2).label.key mustBe "howMuchBereavementAllowance.heading"
      rows(3).label.key mustBe "howMuchCarersAllowance.heading"
      rows(4).label.key mustBe "howMuchJobseekersAllowance.heading"
      rows(5).label.key mustBe "howMuchEmploymentAndSupportAllowance.heading"
      rows(6).label.key mustBe "howMuchIncapacityBenefit.heading"
      rows(7).label.key mustBe "howMuchStatePension.heading"

      otherBenefitSection.rows.size mustBe 3
      otherBenefitSection.headingKey.get mustBe "otherBenefit.checkYourAnswersLabel"
      otherBenefitSection.addLinkText.get mustBe "otherBenefit.change"
      otherBenefitSection.addLinkUrl.get mustBe routes.AnyOtherBenefitsController.onPageLoad(CheckMode).url
    }

    "Company benefits section" in {
      val answers = MockUserAnswers.companyBenefitsUserAnswers
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val rows: Seq[AnswerRow] = sections.companyBenefitSection.rows
      val otherCompanyBenefitSection = sections.otherCompanyBenefitSection

      rows.size mustBe 5
      rows.head.label.key mustBe "anyCompanyBenefits.heading"
      rows(1).label.key mustBe "selectCompanyBenefits.heading"
      rows(2).label.key mustBe "howMuchCarBenefits.heading"
      rows(3).label.key mustBe "howMuchFuelBenefit.heading"
      rows(4).label.key mustBe "howMuchMedicalBenefits.heading"

      otherCompanyBenefitSection.rows.size mustBe 3
      otherCompanyBenefitSection.headingKey.get mustBe "otherCompanyBenefit.checkYourAnswersLabel"
      otherCompanyBenefitSection.addLinkText.get mustBe "otherCompanyBenefit.change"
      otherCompanyBenefitSection.addLinkUrl.get mustBe routes.AnyOtherCompanyBenefitsController.onPageLoad(CheckMode).url
    }

    "Taxable income section" in {
      val answers = MockUserAnswers.taxableIncomeUserAnswers
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val rows = sections.taxableIncomeSection.rows
      val otherTaxableIncomeSection = sections.otherTaxableIncomeSection

      rows.size mustBe 14
      rows.head.label.key mustBe "anyTaxableIncome.heading"
      rows(1).label.key mustBe "selectTaxableIncome.heading"

      rows(2).label.key mustBe "howMuchBankInterest.heading"
      rows(3).label.key mustBe "anyTaxableBankInterestOption.checkYourAnswersLabel"
      rows(3).url mustBe Some(routes.AnyTaxableBankInterestController.onPageLoad(CheckMode).url + "/#anyTaxPaid")
      rows(4).label.key mustBe "anyTaxableBankInterest.checkYourAnswersLabel"
      rows(4).url mustBe Some(routes.AnyTaxableBankInterestController.onPageLoad(CheckMode).url + "/#taxPaidAmount")

      rows(5).label.key mustBe "howMuchForeignIncome.heading"
      rows(6).label.key mustBe "anyTaxableForeignIncomeOption.checkYourAnswersLabel"
      rows(6).url mustBe Some(routes.AnyTaxableForeignIncomeController.onPageLoad(CheckMode).url + "/#anyTaxPaid")
      rows(7).label.key mustBe "anyTaxableForeignIncome.checkYourAnswersLabel"
      rows(7).url mustBe Some(routes.AnyTaxableForeignIncomeController.onPageLoad(CheckMode).url + "/#taxPaidAmount")

      rows(8).label.key mustBe "howMuchInvestmentOrDividend.heading"
      rows(9).label.key mustBe "anyTaxableInvestmentsOption.checkYourAnswersLabel"
      rows(9).url mustBe Some(routes.AnyTaxableInvestmentsController.onPageLoad(CheckMode).url + "/#anyTaxPaid")
      rows(10).label.key mustBe "anyTaxableInvestments.checkYourAnswersLabel"
      rows(10).url mustBe Some(routes.AnyTaxableInvestmentsController.onPageLoad(CheckMode).url + "/#taxPaidAmount")

      rows(11).label.key mustBe "howMuchRentalIncome.heading"
      rows(12).label.key mustBe "anyTaxableRentalIncomeOption.checkYourAnswersLabel"
      rows(12).url mustBe Some(routes.AnyTaxableRentalIncomeController.onPageLoad(CheckMode).url + "/#anyTaxPaid")
      rows(13).label.key mustBe "anyTaxableRentalIncome.checkYourAnswersLabel"
      rows(13).url mustBe Some(routes.AnyTaxableRentalIncomeController.onPageLoad(CheckMode).url + "/#taxPaidAmount")

      otherTaxableIncomeSection.rows.size mustBe 7
      otherTaxableIncomeSection.headingKey.get mustBe "otherTaxableIncome.checkYourAnswersLabel"

      //Header for first otherTaxableIncome
      otherTaxableIncomeSection.rows.head.label.key mustBe "qwerty"
      otherTaxableIncomeSection.rows(1).label.key mustBe messages("checkYourAnswers.otherTaxableIncome.label", "qwerty")
      otherTaxableIncomeSection.rows(1).answer.key mustBe "£12"
      otherTaxableIncomeSection.rows(2).label.key mustBe messages("anyTaxableOtherIncomeOption.checkYourAnswersLabel", "qwerty")
      otherTaxableIncomeSection.rows(2).answer.key mustBe "site.yes"
      otherTaxableIncomeSection.rows(3).label.key mustBe messages("anyTaxableOtherIncome.checkYourAnswersLabel", "qwerty")
      otherTaxableIncomeSection.rows(3).answer.key mustBe "£1234"

      //Header for second otherTaxableIncome
      otherTaxableIncomeSection.rows(4).label.key mustBe "qwerty1"
      otherTaxableIncomeSection.rows(5).label.key mustBe messages("checkYourAnswers.otherTaxableIncome.label", "qwerty1")
      otherTaxableIncomeSection.rows(5).answer.key mustBe "£34"
      otherTaxableIncomeSection.rows(6).label.key mustBe messages("anyTaxableOtherIncomeOption.checkYourAnswersLabel", "qwerty1")
      otherTaxableIncomeSection.rows(6).answer.key mustBe "site.no"

      otherTaxableIncomeSection.addLinkText.get mustBe "otherTaxableIncome.change"
      otherTaxableIncomeSection.addLinkUrl.get mustBe routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode).url
    }

    "Payment details section (Self)" in {
      val answers = MockUserAnswers.selfPaymentDetailsUserAnswers
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val rows = sections.paymentSection.rows

      rows.size mustBe 3
      rows.head.label.key mustBe "whereToSendPayment.heading"
      rows(1).label.key mustBe "isPaymentAddressInTheUK.heading"
      rows(2).label.key mustBe "paymentUKAddress.heading"
    }

    "Payment details section (Nominee)" in {
      val answers = MockUserAnswers.nomineePaymentDetailsUserAnswers
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val rows = sections.paymentSection.rows

      rows.size mustBe 6
      rows.head.label.key mustBe "whereToSendPayment.heading"
      rows(1).label.key mustBe "nomineeFullName.heading"
      rows(2).label.key mustBe messages("anyAgentRef.heading", "Nominee")
      rows(3).label.key mustBe "anyAgentRef.agentRefField"
      rows(4).label.key mustBe "isPaymentAddressInTheUK.heading"
      rows(5).label.key mustBe "paymentInternationalAddress.heading"
    }

    "Contact Details section" in {
      val answers = MockUserAnswers.contactDetailsUserAnswers
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val rows = sections.contactSection.rows

      rows.size mustBe 2
      rows.head.label.key mustBe "telephoneNumber.heading"
      rows(1).label.key mustBe "telephoneNumber.telephoneNumberField"
    }

    "Other Benefits section normal mode" in {
      val answers = MockUserAnswers.fullValidUserAnswers()
      val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
      val sections = new CheckYourAnswersSections(helper, answers)
      val normalModeRows = sections.otherBenefitsAddToListNormalMode.rows
      val checkModeRows = sections.otherBenefitsAddToListCheckMode.rows

      normalModeRows.size mustBe 3
      checkModeRows.size mustBe 3
    }
  }
}
