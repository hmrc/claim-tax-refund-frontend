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

import akka.japi.Option
import controllers.routes
import models.{CheckMode, Index}
import viewmodels.AnswerSection

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
    if (userAnswers.otherBenefit.isDefined) {
      AnswerSection(
        headingKey = Some("otherBenefit.checkYourAnswersLabel"),
        rows = cyaHelper.otherBenefits.flatten,
        addLinkText = Some("otherBenefit.add"),
        addLinkUrl = Some(routes.OtherBenefitController.onPageLoad(CheckMode, Index(userAnswers.otherBenefit.get.size)).url),
        columnHeadings = Some("checkYourAnswers.columnHeading.name" -> "checkYourAnswers.columnHeading.amount")
      )
    } else {
      AnswerSection(None, Seq.empty)
    }
  }

  def otherCompanyBenefitSection: AnswerSection = {
    if (userAnswers.otherCompanyBenefit.isDefined) {
      AnswerSection(
        headingKey = Some("otherCompanyBenefit.checkYourAnswersLabel"),
        rows = cyaHelper.otherCompanyBenefit.flatten,
        addLinkText = Some("otherCompanyBenefit.add"),
        addLinkUrl = Some(routes.OtherCompanyBenefitController.onPageLoad(CheckMode, Index(userAnswers.otherCompanyBenefit.get.size)).url),
        columnHeadings = Some("checkYourAnswers.columnHeading.name" -> "checkYourAnswers.columnHeading.amount")
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
    cyaHelper.howMuchRentalIncome,
    cyaHelper.anyTaxPaid(
      "anyTaxableRentalIncomeOption.checkYourAnswersLabel",
      userAnswers.anyTaxableRentalIncome,
      routes.AnyTaxableRentalIncomeController.onPageLoad(CheckMode).url
    ),
    cyaHelper.taxPaid(
      "anyTaxableRentalIncome.checkYourAnswersLabel",
      userAnswers.anyTaxableRentalIncome,
      routes.AnyTaxableRentalIncomeController.onPageLoad(CheckMode).url
    ),
    cyaHelper.howMuchBankInterest,
    cyaHelper.anyTaxPaid(
      "anyTaxableBankInterestOption.checkYourAnswersLabel",
      userAnswers.anyTaxableBankInterest,
      routes.AnyTaxableBankInterestController.onPageLoad(CheckMode).url
    ),
    cyaHelper.taxPaid(
      "anyTaxableBankInterest.checkYourAnswersLabel",
      userAnswers.anyTaxableBankInterest,
      routes.AnyTaxableBankInterestController.onPageLoad(CheckMode).url
    ),
    cyaHelper.howMuchInvestmentOrDividend,
    cyaHelper.anyTaxPaid(
      "anyTaxableInvestmentsOption.checkYourAnswersLabel",
      userAnswers.anyTaxableInvestments,
      routes.AnyTaxableInvestmentsController.onPageLoad(CheckMode).url
    ),
    cyaHelper.taxPaid(
      "anyTaxableInvestments.checkYourAnswersLabel",
      userAnswers.anyTaxableInvestments,
      routes.AnyTaxableInvestmentsController.onPageLoad(CheckMode).url
    ),
    cyaHelper.howMuchForeignIncome,
    cyaHelper.anyTaxPaid(
      "anyTaxableForeignIncomeOption.checkYourAnswersLabel",
      userAnswers.anyTaxableForeignIncome,
      routes.AnyTaxableForeignIncomeController.onPageLoad(CheckMode).url
    ),
    cyaHelper.taxPaid(
      "anyTaxableForeignIncome.checkYourAnswersLabel",
      userAnswers.anyTaxableForeignIncome,
      routes.AnyTaxableForeignIncomeController.onPageLoad(CheckMode).url
    )
  ).flatten)

  def otherTaxableIncomeSection: AnswerSection = {
    if (userAnswers.otherTaxableIncome.isDefined) {
      AnswerSection(
        headingKey = Some("otherTaxableIncome.checkYourAnswersLabel"),
        rows = cyaHelper.otherTaxableIncome.flatten,
        addLinkText = Some("otherTaxableIncome.add"),
        addLinkUrl = Some(routes.OtherTaxableIncomeController.onPageLoad(CheckMode, Index(userAnswers.otherTaxableIncome.get.size)).url),
        columnHeadings = Some("checkYourAnswers.columnHeading.income" -> "checkYourAnswers.columnHeading.amountAndTax")
      )
    } else {
      AnswerSection(None, Seq.empty)
    }
  }

  def paymentSection = AnswerSection(Some("checkYourAnswers.paymentSection"), Seq(
    cyaHelper.whereToSendPayment,
    cyaHelper.paymentAddressCorrect,
    cyaHelper.nomineeFullName,
    cyaHelper.anyAgentRef,
    cyaHelper.agentReferenceNumber,
    cyaHelper.isPaymentAddressInTheUK,
    cyaHelper.paymentUKAddress,
    cyaHelper.paymentInternationalAddress,
    cyaHelper.paymentLookupAddress
  ).flatten)

  def contactSection = AnswerSection(Some("checkYourAnswers.contactSection"), Seq(
    cyaHelper.anyTelephoneNumber,
    cyaHelper.telephoneNumber
  ).flatten)
}
