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

import models._
import viewmodels.AnswerSection

class CheckYourAnswersSections (cyaHelper: CheckYourAnswersHelper, userAnswers: UserAnswers) {

  def sectionsToShow: Seq[AnswerSection] = {
    if (userAnswers.anyBenefits == Some(true) && userAnswers.otherIncome == Some(false)) {
      Seq(yourDetails, incomeDetails, benefitDetails, paymentDetails)
    } else if (userAnswers.otherIncome == Some(true) && userAnswers.anyBenefits == Some(false)) {
      Seq(yourDetails, incomeDetails, otherIncomeDetails, paymentDetails)
    } else if (userAnswers.otherIncome == Some(true) && userAnswers.anyBenefits == Some(true)) {
      Seq(yourDetails, incomeDetails,benefitDetails, otherIncomeDetails, paymentDetails)
    } else {
      Seq(yourDetails, incomeDetails, paymentDetails)
    }
  }

  def yourDetails = AnswerSection(Some("checkYourAnswers.yourDetailsSection"), Seq(
    cyaHelper.fullName,
    cyaHelper.nationalInsuranceNumber,
    cyaHelper.isTheAddressInTheUK,
    cyaHelper.ukAddress,
    cyaHelper.internationalAddress,
    cyaHelper.telephoneNumber
  ).flatten)

  def incomeDetails = AnswerSection(Some("checkYourAnswers.incomeDetailsSection"), Seq(
    cyaHelper.anyBenefits,
    cyaHelper.otherIncome
  ).flatten)

  def paymentDetails = AnswerSection(Some("checkYourAnswers.paymentDetailsSection"), Seq(
    cyaHelper.whereToSendPayment,
    cyaHelper.payeeFullName,
    cyaHelper.anyAgentRef,
    cyaHelper.agentReferenceNumber,
    cyaHelper.isPayeeAddressInTheUK,
    cyaHelper.payeeUKAddress,
    cyaHelper.payeeInternationalAddress
  ).flatten)

  def benefitDetails = AnswerSection(Some("checkYourAnswers.benefitDetailsSection"), Seq(
    cyaHelper.anyJobseekersAllowance,
    cyaHelper.howMuchJobseekersAllowance,
    cyaHelper.anyIncapacityBenefit,
    cyaHelper.howMuchIncapacityBenefit,
    cyaHelper.anyEmploymentAndSupportAllowance,
    cyaHelper.howMuchEmploymentAndSupportAllowance,
    cyaHelper.anyStatePension,
    cyaHelper.howMuchStatePension,
    cyaHelper.anyOtherTaxableBenefits,
    cyaHelper.otherBenefitsDetailsAndAmount
  ).flatten)

  def otherIncomeDetails = AnswerSection(Some("checkYourAnswers.otherIncomeDetailsSection"), Seq(
    cyaHelper.anyCarBenefits,
    cyaHelper.howMuchCarBenefits,
    cyaHelper.anyRentalIncome,
    cyaHelper.howMuchRentalIncome,
    cyaHelper.anyBankBuildingSocietyInterest,
    cyaHelper.howMuchBankBuildingSocietyInterest,
    cyaHelper.anyMedicalBenefits,
    cyaHelper.howMuchMedicalBenefits,
    cyaHelper.anyOtherTaxableIncome,
    cyaHelper.otherIncomeDetailsAndAmount
  ).flatten)
}
