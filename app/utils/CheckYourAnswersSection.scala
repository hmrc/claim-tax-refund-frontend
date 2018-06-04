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
      Seq(yourDetails, incomeDetails, benefitDetails, paymentDetails, contactDetails)
    } else if (userAnswers.otherIncome == Some(true) && userAnswers.anyBenefits == Some(false)) {
      Seq(yourDetails, incomeDetails, otherIncomeDetails, paymentDetails, contactDetails)
    } else if (userAnswers.otherIncome == Some(true) && userAnswers.anyBenefits == Some(true)) {
      Seq(yourDetails, incomeDetails, benefitDetails, otherIncomeDetails, paymentDetails, contactDetails)
    } else {
      Seq(yourDetails, incomeDetails, paymentDetails, contactDetails)
    }
  }

  def yourDetails = AnswerSection(Some("checkYourAnswers.yourDetailsSection"), Seq(
    cyaHelper.userName,
    cyaHelper.userNino,
    cyaHelper.userAddress
  ).flatten)

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
    cyaHelper.selectCompanyBenefits
  ).flatten)

  def otherIncomeDetails = AnswerSection(Some("checkYourAnswers.otherIncomeDetailsSection"), Seq(
    cyaHelper.howMuchCarBenefits,
    cyaHelper.howMuchRentalIncome,
    cyaHelper.howMuchBankBuildingSocietyInterest,
    cyaHelper.howMuchMedicalBenefits,
    cyaHelper.anyOtherTaxableIncome,
    cyaHelper.otherIncomeDetailsAndAmount
  ).flatten)

  def contactDetails = AnswerSection(Some("checkYourAnswers.contactDetailsSection"), Seq(
    cyaHelper.telephoneNumber
  ).flatten)
}
