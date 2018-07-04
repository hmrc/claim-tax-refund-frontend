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

import controllers.routes
import models.CheckMode
import viewmodels.AnswerSection

class CheckYourAnswersSections(cyaHelper: CheckYourAnswersHelper, userAnswers: UserAnswers) {

  def sections: Seq[AnswerSection] = {
    Seq(claimSection, benefitSection, companyBenefitSection, taxableIncomeSection, paymentSection, contactSection)
  }

  def claimSection = AnswerSection(Some("checkYourAnswers.claimSection"), Seq(
    cyaHelper.selectTaxYear,
    cyaHelper.employmentDetails,
    cyaHelper.enterPayeReference,
    cyaHelper.detailsOfEmploymentOrPension
  ).flatten)

  def benefitSection = AnswerSection(Some("checkYourAnswers.benefitsSection"), Seq(
    cyaHelper.anyBenefits,
    cyaHelper.selectBenefits,
    cyaHelper.howMuchBereavementAllowance,
    cyaHelper.howMuchCarersAllowance,
    cyaHelper.howMuchJobseekersAllowance,
    cyaHelper.howMuchEmploymentAndSupportAllowance,
    cyaHelper.howMuchIncapacityBenefit,
    cyaHelper.howMuchStatePension,
//    cyaHelper.otherBenefitsName(),
    cyaHelper.howMuchOtherBenefit,
    cyaHelper.anyOtherBenefits
  ).flatten)

  def companyBenefitSection = AnswerSection(Some("checkYourAnswers.companyBenefitsSection"), Seq(
    cyaHelper.anyCompanyBenefits,
    cyaHelper.selectCompanyBenefits,
    cyaHelper.howMuchCarBenefits,
    cyaHelper.howMuchFuelBenefit,
    cyaHelper.howMuchMedicalBenefits,
    cyaHelper.otherCompanyBenefitsName,
    cyaHelper.howMuchOtherCompanyBenefit,
    cyaHelper.anyOtherCompanyBenefits
  ).flatten)


  def taxableIncomeSection = AnswerSection(Some("checkYourAnswers.taxableIncomeSection"), Seq(
    cyaHelper.anyTaxableIncome,
    cyaHelper.selectTaxableIncome,
    cyaHelper.howMuchRentalIncome,
    cyaHelper.anyTaxPaid(
      "anyTaxableRentalIncomeOption",
      userAnswers.anyTaxableRentalIncome,
      routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode).url
    ),
    cyaHelper.taxPaid(
      "anyTaxableRentalIncome",
      userAnswers.anyTaxableRentalIncome,
      routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode).url
    ),
    cyaHelper.howMuchBankInterest,
    cyaHelper.anyTaxPaid(
      "anyTaxableBankInterestOption",
      userAnswers.anyTaxableBankInterest,
      routes.AnyTaxableBankInterestController.onPageLoad(CheckMode).url
    ),
    cyaHelper.taxPaid(
      "anyTaxableBankInterest",
      userAnswers.anyTaxableBankInterest,
      routes.AnyTaxableBankInterestController.onPageLoad(CheckMode).url
    ),
    cyaHelper.howMuchInvestmentOrDividend,
    cyaHelper.anyTaxPaid(
      "anyTaxableInvestmentsOption",
      userAnswers.anyTaxableInvestments,
      routes.AnyTaxableInvestmentsController.onPageLoad(CheckMode).url
    ),
    cyaHelper.taxPaid(
      "anyTaxableInvestments",
      userAnswers.anyTaxableInvestments,
      routes.AnyTaxableInvestmentsController.onPageLoad(CheckMode).url
    ),
    cyaHelper.howMuchForeignIncome,
    cyaHelper.anyTaxPaid(
      "anyTaxableForeignIncomeOption",
      userAnswers.anyTaxableForeignIncome,
      routes.AnyTaxableForeignIncomeController.onPageLoad(CheckMode).url
    ),
    cyaHelper.taxPaid(
      "anyTaxableForeignIncome",
      userAnswers.anyTaxableForeignIncome,
      routes.AnyTaxableForeignIncomeController.onPageLoad(CheckMode).url
    ),
    cyaHelper.otherTaxableIncomeName,
    cyaHelper.howMuchOtherTaxableIncome,
    cyaHelper.anyTaxPaid(
      "anyTaxableOtherIncomeOption",
      userAnswers.anyTaxableOtherIncome,
      routes.AnyTaxableOtherIncomeController.onPageLoad(CheckMode).url
    ),
    cyaHelper.taxPaid(
      "anyTaxableOtherIncome",
      userAnswers.anyTaxableOtherIncome,
      routes.AnyTaxableOtherIncomeController.onPageLoad(CheckMode).url
    ),
    cyaHelper.anyOtherTaxableIncome
  ).flatten)

  def paymentSection = AnswerSection(Some("checkYourAnswers.paymentSection"), Seq(
    cyaHelper.whereToSendPayment,
    cyaHelper.paymentAddressCorrect,
    cyaHelper.nomineeFullName,
    cyaHelper.anyAgentRef,
    cyaHelper.agentReferenceNumber,
    cyaHelper.isPaymentAddressInTheUK,
    cyaHelper.paymentUKAddress,
    cyaHelper.paymentInternationalAddress
  ).flatten)

  def contactSection = AnswerSection(Some("checkYourAnswers.contactSection"), Seq(
    cyaHelper.anyTelephoneNumber,
    cyaHelper.telephoneNumber
  ).flatten)
}
