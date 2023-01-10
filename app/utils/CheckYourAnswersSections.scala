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

import controllers.routes
import models.{CheckMode, Index, NormalMode, WhereToSendPayment}
import viewmodels.{AnswerRow, AnswerSection}

class CheckYourAnswersSections(cyaHelper: CheckYourAnswersHelper, userAnswers: UserAnswers) {

  def sections: Seq[AnswerSection] = {
    Seq(
      claimSection,
      benefitSection,
      otherBenefitsSection,
      companyBenefitSection,
      otherCompanyBenefitSection,
      taxableIncomeSection,
      otherTaxableIncomeSection,
      paymentSection,
      contactSection
    )
  }

  def claimSection = AnswerSection(Some("checkYourAnswers.claimSection"), Seq(
    cyaHelper.selectTaxYear,
    cyaHelper.employmentDetails,
    cyaHelper.enterPayeReference,
    cyaHelper.detailsOfEmploymentOrPension
  ).flatten)

  def benefitSection = AnswerSection(Some("checkYourAnswers.benefitSection"), Seq(
    cyaHelper.anyBenefits,
    cyaHelper.selectBenefits,
    cyaHelper.howMuchBereavementAllowance,
    cyaHelper.howMuchCarersAllowance,
    cyaHelper.howMuchJobseekersAllowance,
    cyaHelper.howMuchEmploymentAndSupportAllowance,
    cyaHelper.howMuchIncapacityBenefit,
    cyaHelper.howMuchStatePension
  ).flatten)

  def otherBenefitsSection: AnswerSection = {
    if (userAnswers.otherBenefit.isDefined && userAnswers.otherBenefit.get.nonEmpty) {
      AnswerSection(
        headingKey = Some("otherBenefit.checkYourAnswersLabel"),
        rows = cyaHelper.otherBenefitsCheckYourAnswers.flatten,
        addLinkText = Some("otherBenefit.change"),
        addLinkUrl = Some(routes.AnyOtherBenefitsController.onPageLoad(CheckMode).url)
      )
    } else {
      AnswerSection(None, Seq.empty)
    }
  }

  def otherBenefitsAddToListNormalMode: AnswerSection = {
    if (userAnswers.otherBenefit.isDefined) {
      AnswerSection(
        headingKey = Some("otherBenefit.checkYourAnswersLabel"),
        rows = cyaHelper.otherBenefitsNormalMode.flatten,
        addLinkText = Some("otherBenefit.change"),
        addLinkUrl = Some(routes.OtherBenefitController.onPageLoad(NormalMode, Index(userAnswers.otherBenefit.get.size)).url)
      )
    } else {
      AnswerSection(None, Seq.empty)
    }
  }

  def otherBenefitsAddToListCheckMode: AnswerSection = {
    if (userAnswers.otherBenefit.isDefined) {
      AnswerSection(
        headingKey = Some("otherBenefit.checkYourAnswersLabel"),
        rows = cyaHelper.otherBenefitsCheckMode.flatten,
        addLinkText = Some("otherBenefit.change"),
        addLinkUrl = Some(routes.OtherBenefitController.onPageLoad(CheckMode, Index(userAnswers.otherBenefit.get.size)).url)
      )
    } else {
      AnswerSection(None, Seq.empty)
    }
  }

  def otherCompanyBenefitSection: AnswerSection = {
    if (userAnswers.otherCompanyBenefit.isDefined && userAnswers.otherCompanyBenefit.get.nonEmpty) {
      AnswerSection(
        headingKey = Some("otherCompanyBenefit.checkYourAnswersLabel"),
        rows = cyaHelper.otherCompanyBenefitsCheckYourAnswers.flatten,
        addLinkText = Some("otherCompanyBenefit.change"),
        addLinkUrl = Some(routes.AnyOtherCompanyBenefitsController.onPageLoad(CheckMode).url)
      )
    } else {
      AnswerSection(None, Seq.empty)
    }
  }

  def otherCompanyBenefitsAddToListNormalMode: AnswerSection = {
    if (userAnswers.otherCompanyBenefit.isDefined) {
      AnswerSection(
        headingKey = Some("otherCompanyBenefit.checkYourAnswersLabel"),
        rows = cyaHelper.otherCompanyBenefitsNormalMode.flatten,
        addLinkText = Some("otherCompanyBenefit.add"),
        addLinkUrl = Some(routes.OtherCompanyBenefitController.onPageLoad(NormalMode, Index(userAnswers.otherCompanyBenefit.get.size)).url)
      )
    } else {
      AnswerSection(None, Seq.empty)
    }
  }

  def otherCompanyBenefitsAddToListCheckMode: AnswerSection = {
    if (userAnswers.otherCompanyBenefit.isDefined) {
      AnswerSection(
        headingKey = Some("otherCompanyBenefit.checkYourAnswersLabel"),
        rows = cyaHelper.otherCompanyBenefitsCheckMode.flatten,
        addLinkText = Some("otherCompanyBenefit.add"),
        addLinkUrl = Some(routes.OtherCompanyBenefitController.onPageLoad(CheckMode, Index(userAnswers.otherCompanyBenefit.get.size)).url)
      )
    } else {
      AnswerSection(None, Seq.empty)
    }
  }


  def companyBenefitSection = AnswerSection(Some("checkYourAnswers.companyBenefitSection"), Seq(
    cyaHelper.anyCompanyBenefits,
    cyaHelper.selectCompanyBenefits,
    cyaHelper.howMuchCarBenefits,
    cyaHelper.howMuchFuelBenefit,
    cyaHelper.howMuchMedicalBenefits
  ).flatten)


  def taxableIncomeSection = AnswerSection(Some("checkYourAnswers.taxableIncomeSection"), Seq(
    cyaHelper.anyTaxableIncome,
    cyaHelper.selectTaxableIncome,
		//bank interest
		cyaHelper.howMuchBankInterest,
		cyaHelper.anyTaxPaid(
			"anyTaxableBankInterestOption.checkYourAnswersLabel",
			userAnswers.anyTaxableBankInterest,
			Some(routes.AnyTaxableBankInterestController.onPageLoad(CheckMode).url + "/#anyTaxPaid")
		),
		cyaHelper.taxPaid(
			"anyTaxableBankInterest.checkYourAnswersLabel",
			userAnswers.anyTaxableBankInterest,
			Some(routes.AnyTaxableBankInterestController.onPageLoad(CheckMode).url + "/#taxPaidAmount")
		),
		//Foreign income
		cyaHelper.howMuchForeignIncome,
		cyaHelper.anyTaxPaid(
			"anyTaxableForeignIncomeOption.checkYourAnswersLabel",
			userAnswers.anyTaxableForeignIncome,
			Some(routes.AnyTaxableForeignIncomeController.onPageLoad(CheckMode).url + "/#anyTaxPaid")
		),
		cyaHelper.taxPaid(
			"anyTaxableForeignIncome.checkYourAnswersLabel",
			userAnswers.anyTaxableForeignIncome,
			Some(routes.AnyTaxableForeignIncomeController.onPageLoad(CheckMode).url + "/#taxPaidAmount")
		),
		//Investments and dividends
		cyaHelper.howMuchInvestmentOrDividend,
		cyaHelper.anyTaxPaid(
			"anyTaxableInvestmentsOption.checkYourAnswersLabel",
			userAnswers.anyTaxableInvestments,
			Some(routes.AnyTaxableInvestmentsController.onPageLoad(CheckMode).url + "/#anyTaxPaid")
		),
		cyaHelper.taxPaid(
			"anyTaxableInvestments.checkYourAnswersLabel",
			userAnswers.anyTaxableInvestments,
			Some(routes.AnyTaxableInvestmentsController.onPageLoad(CheckMode).url + "/#taxPaidAmount")
		),
		//property Rental
    cyaHelper.howMuchRentalIncome,
    cyaHelper.anyTaxPaid(
      "anyTaxableRentalIncomeOption.checkYourAnswersLabel",
      userAnswers.anyTaxableRentalIncome,
      Some(routes.AnyTaxableRentalIncomeController.onPageLoad(CheckMode).url + "/#anyTaxPaid")
    ),
    cyaHelper.taxPaid(
      "anyTaxableRentalIncome.checkYourAnswersLabel",
      userAnswers.anyTaxableRentalIncome,
      Some(routes.AnyTaxableRentalIncomeController.onPageLoad(CheckMode).url + "/#taxPaidAmount")
    )
  ).flatten)

  def otherTaxableIncomeAddToListNormalMode: AnswerSection = {
    if (userAnswers.otherTaxableIncome.isDefined) {
      AnswerSection(
        headingKey = Some("otherTaxableIncome.checkYourAnswersLabel"),
        rows = cyaHelper.otherTaxableIncomeNormalMode.flatten,
        addLinkText = Some("otherTaxableIncome.add"),
        addLinkUrl = Some(routes.AnyOtherTaxableIncomeController.onPageLoad(NormalMode).url)
      )
    } else {
      AnswerSection(None, Seq.empty)
    }
  }

  def otherTaxableIncomeAddToListCheckMode: AnswerSection = {
    if (userAnswers.otherTaxableIncome.isDefined) {
      AnswerSection(
        headingKey = Some("otherTaxableIncome.checkYourAnswersLabel"),
        rows = cyaHelper.otherTaxableIncomeCheckMode.flatten,
        addLinkText = Some("otherTaxableIncome.add"),
        addLinkUrl = Some(routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode).url)
      )
    } else {
      AnswerSection(None, Seq.empty)
    }
  }

  def otherTaxableIncomeSection: AnswerSection = {
    if (userAnswers.otherTaxableIncome.isDefined && userAnswers.otherTaxableIncome.get.nonEmpty) {
      AnswerSection(
        headingKey = Some("otherTaxableIncome.checkYourAnswersLabel"),
        rows = cyaHelper.otherTaxableIncomeCheckYourAnswers.flatten,
        addLinkText = Some("otherTaxableIncome.change"),
        addLinkUrl = Some(routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode).url)
      )
    } else {
      AnswerSection(None, Seq.empty)
    }
  }

  def generatePaymentSection: Seq[AnswerRow] = {
		if (userAnswers.whereToSendPayment.nonEmpty && userAnswers.whereToSendPayment.get == WhereToSendPayment.Myself) {
      Seq(cyaHelper.whereToSendPayment,
        cyaHelper.paymentAddressCorrect,
        cyaHelper.isPaymentAddressInTheUK,
        cyaHelper.paymentUKAddress,
        cyaHelper.paymentInternationalAddress,
        cyaHelper.paymentLookupAddress
      )
    } else {
      Seq(cyaHelper.whereToSendPayment,
        cyaHelper.nomineeFullName,
				cyaHelper.anyAgentRef,
        cyaHelper.agentReferenceNumber,
        cyaHelper.isPaymentAddressInTheUK,
        cyaHelper.paymentUKAddress,
        cyaHelper.paymentInternationalAddress,
        cyaHelper.paymentLookupAddress
			)
    }
  }.flatten

  def paymentSection = AnswerSection(Some("checkYourAnswers.paymentSection"), generatePaymentSection)

  def contactSection = AnswerSection(Some("checkYourAnswers.contactSection"), Seq(
    cyaHelper.anyTelephoneNumber,
    cyaHelper.telephoneNumber
  ).flatten)
}
