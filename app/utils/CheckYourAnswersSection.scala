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

import viewmodels.AnswerSection

class CheckYourAnswersSections(cyaHelper: CheckYourAnswersHelper, userAnswers: UserAnswers) {

  def sectionsToShow: Seq[AnswerSection] = {
    if (userAnswers.anyBenefits == Some(true) && userAnswers.otherIncome == Some(false)) {
      Seq(incomeDetails, benefitDetails, paymentDetails, contactDetails)
    } else if (userAnswers.otherIncome == Some(true) && userAnswers.anyBenefits == Some(false)) {
      Seq(incomeDetails, otherIncomeDetails, paymentDetails, contactDetails)
    } else if (userAnswers.otherIncome == Some(true) && userAnswers.anyBenefits == Some(true)) {
      Seq(incomeDetails, benefitDetails, otherIncomeDetails, paymentDetails, contactDetails)
    } else {
      Seq(incomeDetails, paymentDetails, contactDetails)
    }
  }

  def incomeDetails = AnswerSection(Some("checkYourAnswers.incomeDetailsSection"), Seq(
    cyaHelper.selectTaxYear,
    cyaHelper.employmentDetails,
    cyaHelper.anyBenefits,
    cyaHelper.otherIncome
  ).flatten)

  def paymentDetails = AnswerSection(Some("checkYourAnswers.paymentDetailsSection"), Seq(
    cyaHelper.whereToSendPayment,
    cyaHelper.nomineeFullName,
    cyaHelper.anyAgentRef,
    cyaHelper.agentReferenceNumber,
    cyaHelper.isPaymentAddressInTheUK,
    cyaHelper.paymentUKAddress,
    cyaHelper.paymentInternationalAddress
  ).flatten)

  def benefitDetails = AnswerSection(Some("checkYourAnswers.benefitDetailsSection"), Seq(
    cyaHelper.selectCompanyBenefits,
    cyaHelper.howMuchJobseekersAllowance,
    cyaHelper.howMuchIncapacityBenefit,
    cyaHelper.howMuchEmploymentAndSupportAllowance,
    cyaHelper.howMuchStatePension,
    cyaHelper.anyOtherTaxableBenefits,
    cyaHelper.otherBenefitsDetailsAndAmount
  ).flatten)

  def companyBenefitDetails = AnswerSection(Some("checkYourAnswers.companyBenefitsDetailsSection"), Seq(
    cyaHelper.anyOtherCompanyBenefits,
    cyaHelper.selectCompanyBenefits,
    cyaHelper.howMuchCarBenefits,
    cyaHelper.howMuchFuelBenefit,
    cyaHelper.howMuchMedicalBenefits,
    cyaHelper.anyOtherCompanyBenefits,
    cyaHelper.otherCompanyBenefitsDetails,
    cyaHelper.howMuchOtherCompanyBenefit
  ).flatten)


  def otherIncomeDetails = AnswerSection(Some("checkYourAnswers.otherIncomeDetailsSection"), Seq(
    cyaHelper.anyOtherTaxableIncome,
    cyaHelper.howMuchCarBenefits,
    cyaHelper.howMuchRentalIncome,
    cyaHelper.howMuchBankInterest,
    cyaHelper.howMuchMedicalBenefits,
    cyaHelper.anyOtherTaxableIncome,
    cyaHelper.otherIncomeDetailsAndAmount
  ).flatten)

  def contactDetails = AnswerSection(Some("checkYourAnswers.contactDetailsSection"), Seq(
    cyaHelper.telephoneNumber
  ).flatten)
}
