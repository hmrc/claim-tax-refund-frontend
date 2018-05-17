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
import models.SelectTaxYear.{CYMinus2, CYMinus3, CYMinus4, CYMinus5}
import models.{CheckMode, InternationalAddress, UkAddress}
import uk.gov.hmrc.time.TaxYearResolver
import viewmodels.AnswerRow

class CheckYourAnswersHelper(userAnswers: UserAnswers) {

  def employmentDetails: Option[AnswerRow] = userAnswers.employmentDetails map {
    x => AnswerRow("employmentDetails.checkYourAnswersLabel",
      if(x) "site.yes" else "site.no", true, routes.EmploymentDetailsController.onPageLoad(CheckMode).url, false)
  }

  val dateFormat = "dd MMMM YYYY"

  def userName: Option[AnswerRow] = userAnswers.userDetails map {
    x =>
      AnswerRow("userDetails.checkYourAnswersLabel.name",
        s"${x.name}", false, routes.UserDetailsController.onPageLoad(CheckMode).url, false)
  }

  def userNino: Option[AnswerRow] = userAnswers.userDetails map {
    x =>
      AnswerRow("userDetails.checkYourAnswersLabel.nino",
        s"${x.nino}", false, routes.UserDetailsController.onPageLoad(CheckMode).url, false)
  }

  def userAddress: Option[AnswerRow] = userAnswers.userDetails map {
    x =>
      AnswerRow("userDetails.checkYourAnswersLabel.address",
        UkAddress.asString(x.address),
        false, routes.UserDetailsController.onPageLoad(CheckMode).url, false)
  }

  def payeeUKAddress: Option[AnswerRow] = userAnswers.payeeUKAddress map {
    x =>
      AnswerRow("payeeUKAddress.checkYourAnswersLabel",
        UkAddress.asString(
          UkAddress(x.addressLine1,
            x.addressLine2,
            x.addressLine3,
            x.addressLine4,
            x.addressLine5,
            x.postcode)),
        false, routes.PayeeUKAddressController.onPageLoad(CheckMode).url, true)
  }

  def payeeInternationalAddress: Option[AnswerRow] = userAnswers.payeeInternationalAddress map {
    x =>
      AnswerRow("payeeInternationalAddress.checkYourAnswersLabel",
        InternationalAddress.asString(
          InternationalAddress(x.addressLine1,
            x.addressLine2,
            x.addressLine3,
            x.addressLine4,
            x.addressLine5,
            x.country)),
        false, routes.PayeeInternationalAddressController.onPageLoad(CheckMode).url, true)
  }

  def whereToSendPayment: Option[AnswerRow] = userAnswers.whereToSendPayment map {
    x =>
      AnswerRow("whereToSendPayment.checkYourAnswersLabel",
        s"whereToSendPayment.$x", true, routes.WhereToSendPaymentController.onPageLoad(CheckMode).url, true)
  }

  def isPayeeAddressInTheUK: Option[AnswerRow] = userAnswers.isPayeeAddressInTheUK map {
    x =>
      AnswerRow("isPayeeAddressInTheUK.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.IsPayeeAddressInTheUKController.onPageLoad(CheckMode).url, true)
  }

  def anyAgentRef: Option[AnswerRow] = userAnswers.anyAgentRef map {
    x =>
      AnswerRow("anyAgentRef.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.AnyAgentRefController.onPageLoad(CheckMode).url, true)
  }

  def agentReferenceNumber: Option[AnswerRow] = userAnswers.agentReferenceNumber map {
    x =>
      AnswerRow("agentReferenceNumber.checkYourAnswersLabel",
        s"$x", false, routes.AgentReferenceNumberController.onPageLoad(CheckMode).url, true)
  }

  def payeeFullName: Option[AnswerRow] = userAnswers.payeeFullName map {
    x =>
      AnswerRow("payeeFullName.checkYourAnswersLabel",
        s"$x", false, routes.PayeeFullNameController.onPageLoad(CheckMode).url, true)
  }

  def otherIncomeDetailsAndAmount: Option[AnswerRow] = userAnswers.otherIncomeDetailsAndAmount map {
    x =>
      AnswerRow("otherIncomeDetailsAndAmount.checkYourAnswersLabel",
        s"$x", false, routes.OtherIncomeDetailsAndAmountController.onPageLoad(CheckMode).url, true)
  }

  def otherBenefitsDetailsAndAmount: Option[AnswerRow] = userAnswers.otherBenefitsDetailsAndAmount map {
    x =>
      AnswerRow("otherBenefitsDetailsAndAmount.checkYourAnswersLabel",
        s"$x", false, routes.OtherBenefitsDetailsAndAmountController.onPageLoad(CheckMode).url, true)
  }

  def anyOtherTaxableIncome: Option[AnswerRow] = userAnswers.anyOtherTaxableIncome map {
    x =>
      AnswerRow("anyOtherTaxableIncome.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode).url, true)
  }

  def howMuchStatePension: Option[AnswerRow] = userAnswers.howMuchStatePension map {
    x =>
      AnswerRow("howMuchStatePension.checkYourAnswersLabel",
        s"$x", false, routes.HowMuchStatePensionController.onPageLoad(CheckMode).url, true)
  }

  def howMuchEmploymentAndSupportAllowance: Option[AnswerRow] = userAnswers.howMuchEmploymentAndSupportAllowance map {
    x =>
      AnswerRow("howMuchEmploymentAndSupportAllowance.checkYourAnswersLabel",
        s"$x", false, routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(CheckMode).url, true)
  }

  def howMuchIncapacityBenefit: Option[AnswerRow] = userAnswers.howMuchIncapacityBenefit map {
    x =>
      AnswerRow("howMuchIncapacityBenefit.checkYourAnswersLabel",
        s"$x", false, routes.HowMuchIncapacityBenefitController.onPageLoad(CheckMode).url, true)
  }

  def howMuchJobseekersAllowance: Option[AnswerRow] = userAnswers.howMuchJobseekersAllowance map {
    x =>
      AnswerRow("howMuchJobseekersAllowance.checkYourAnswersLabel",
        s"$x", false, routes.HowMuchJobseekersAllowanceController.onPageLoad(CheckMode).url, true)
  }

  def anyOtherTaxableBenefits: Option[AnswerRow] = userAnswers.anyOtherTaxableBenefits map {
    x =>
      AnswerRow("anyOtherTaxableBenefits.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.AnyOtherTaxableBenefitsController.onPageLoad(CheckMode).url, true)
  }

  def anyStatePension: Option[AnswerRow] = userAnswers.anyStatePension map {
    x =>
      AnswerRow("anyStatePension.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.AnyStatePensionController.onPageLoad(CheckMode).url, true)
  }

  def anyEmploymentAndSupportAllowance: Option[AnswerRow] = userAnswers.anyEmploymentAndSupportAllowance map {
    x =>
      AnswerRow("anyEmploymentAndSupportAllowance.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.AnyEmploymentAndSupportAllowanceController.onPageLoad(CheckMode).url, true)
  }

  def anyIncapacityBenefit: Option[AnswerRow] = userAnswers.anyIncapacityBenefit map {
    x =>
      AnswerRow("anyIncapacityBenefit.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.AnyIncapacityBenefitController.onPageLoad(CheckMode).url, true)
  }

  def anyJobseekersAllowance: Option[AnswerRow] = userAnswers.anyJobseekersAllowance map {
    x =>
      AnswerRow("anyJobseekersAllowance.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.AnyJobseekersAllowanceController.onPageLoad(CheckMode).url, true)
  }

  def howMuchMedicalBenefits: Option[AnswerRow] = userAnswers.howMuchMedicalBenefits map {
    x =>
      AnswerRow("howMuchMedicalBenefits.checkYourAnswersLabel",
        s"$x", false, routes.HowMuchMedicalBenefitsController.onPageLoad(CheckMode).url, true)
  }

  def howMuchBankBuildingSocietyInterest: Option[AnswerRow] = userAnswers.howMuchBankBuildingSocietyInterest map {
    x =>
      AnswerRow("howMuchBankBuildingSocietyInterest.checkYourAnswersLabel",
        s"$x", false, routes.HowMuchBankBuildingSocietyInterestController.onPageLoad(CheckMode).url, true)
  }

  def anyBankBuildingSocietyInterest: Option[AnswerRow] = userAnswers.anyBankBuildingSocietyInterest map {
    x =>
      AnswerRow("anyBankBuildingSocietyInterest.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.AnyBankBuildingSocietyInterestController.onPageLoad(CheckMode).url, true)
  }

  def howMuchRentalIncome: Option[AnswerRow] = userAnswers.howMuchRentalIncome map {
    x =>
      AnswerRow("howMuchRentalIncome.checkYourAnswersLabel",
        s"$x", false, routes.HowMuchRentalIncomeController.onPageLoad(CheckMode).url, true)
  }

  def anyRentalIncome: Option[AnswerRow] = userAnswers.anyRentalIncome map {
    x =>
      AnswerRow("anyRentalIncome.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.AnyRentalIncomeController.onPageLoad(CheckMode).url, true)
  }

  def howMuchCarBenefits: Option[AnswerRow] = userAnswers.howMuchCarBenefits map {
    x =>
      AnswerRow("howMuchCarBenefits.checkYourAnswersLabel",
        s"$x", false, routes.HowMuchCarBenefitsController.onPageLoad(CheckMode).url, true)
  }

  def otherIncome: Option[AnswerRow] = userAnswers.otherIncome map {
    x =>
      AnswerRow("otherIncome.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.OtherIncomeController.onPageLoad(CheckMode).url, true)
  }

  def anyBenefits: Option[AnswerRow] = userAnswers.anyBenefits map {
    x =>
      AnswerRow("anyBenefits.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.AnyBenefitsController.onPageLoad(CheckMode).url, true)
  }

  def selectTaxYear: Option[AnswerRow] = userAnswers.selectTaxYear map {
    x =>
      AnswerRow("selectTaxYear.checkYourAnswersLabel",
        x match {
          case CYMinus2 =>
            TaxYearResolver.startOfCurrentTaxYear.minusYears(2).toString(dateFormat) + " to " +
              TaxYearResolver.endOfCurrentTaxYear.minusYears(2).toString(dateFormat)
          case CYMinus3 =>
            TaxYearResolver.startOfCurrentTaxYear.minusYears(3).toString(dateFormat) + " to " +
              TaxYearResolver.endOfCurrentTaxYear.minusYears(3).toString(dateFormat)
          case CYMinus4 =>
            TaxYearResolver.startOfCurrentTaxYear.minusYears(4).toString(dateFormat) + " to " +
              TaxYearResolver.endOfCurrentTaxYear.minusYears(4).toString(dateFormat)
          case CYMinus5 =>
            TaxYearResolver.startOfCurrentTaxYear.minusYears(5).toString(dateFormat) + " to " +
              TaxYearResolver.endOfCurrentTaxYear.minusYears(5).toString(dateFormat)
        },
        false, routes.SelectTaxYearController.onPageLoad(CheckMode).url, true)
  }

  def telephoneNumber: Option[AnswerRow] = userAnswers.telephoneNumber map {
    x =>
      AnswerRow("telephoneNumber.checkYourAnswersLabel",
        s"$x", false, routes.TelephoneNumberController.onPageLoad(CheckMode).url, true)
  }
}
