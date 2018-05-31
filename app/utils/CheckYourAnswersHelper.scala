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
import models.SelectTaxYear.{CYMinus1, CYMinus2, CYMinus3, CYMinus4, CYMinus5}
import models.{AgentRef, CheckMode, InternationalAddress, UkAddress}
import uk.gov.hmrc.time.TaxYearResolver
import viewmodels.AnswerRow

class CheckYourAnswersHelper(userAnswers: UserAnswers) {

  def anyCompanyBenefits: Option[AnswerRow] = userAnswers.anyCompanyBenefits map {
    x =>
      AnswerRow("anyCompanyBenefits.checkYourAnswersLabel",
        if(x) "site.yes" else "site.no",
        true, routes.AnyCompanyBenefitsController.onPageLoad(CheckMode).url, true)
  }

  val dateFormat = "dd MMMM YYYY"

  def userName: Option[AnswerRow] = userAnswers.userDetails map {
    x =>
      AnswerRow("userDetails.checkYourAnswersLabel.name",
        s"${x.name}", false, routes.SelectTaxYearController.onPageLoad(CheckMode).url, false)
  }

  def userNino: Option[AnswerRow] = userAnswers.userDetails map {
    x =>
      AnswerRow("userDetails.checkYourAnswersLabel.nino",
        s"${x.nino}", false, routes.SelectTaxYearController.onPageLoad(CheckMode).url, false)
  }

  def userAddress: Option[AnswerRow] = userAnswers.userDetails map {
    x =>
      AnswerRow("userDetails.checkYourAnswersLabel.address",
        UkAddress.asString(x.address),
        false, routes.SelectTaxYearController.onPageLoad(CheckMode).url, false)
  }

  def paymentUKAddress: Option[AnswerRow] = userAnswers.paymentUKAddress map {
    x =>
      AnswerRow("paymentUKAddress.checkYourAnswersLabel",
        UkAddress.asString(
          UkAddress(x.addressLine1,
            x.addressLine2,
            x.addressLine3,
            x.addressLine4,
            x.addressLine5,
            x.postcode)),
        false, routes.PaymentUKAddressController.onPageLoad(CheckMode).url, true)
  }

  def paymentInternationalAddress: Option[AnswerRow] = userAnswers.paymentInternationalAddress map {
    x =>
      AnswerRow("paymentInternationalAddress.checkYourAnswersLabel",
        InternationalAddress.asString(
          InternationalAddress(x.addressLine1,
            x.addressLine2,
            x.addressLine3,
            x.addressLine4,
            x.addressLine5,
            x.country)),
        false, routes.PaymentInternationalAddressController.onPageLoad(CheckMode).url, true)
  }

  def whereToSendPayment: Option[AnswerRow] = userAnswers.whereToSendPayment map {
    x =>
      AnswerRow("whereToSendPayment.checkYourAnswersLabel",
        s"whereToSendPayment.$x", true, routes.WhereToSendPaymentController.onPageLoad(CheckMode).url, true)
  }

  def isPaymentAddressInTheUK: Option[AnswerRow] = userAnswers.isPaymentAddressInTheUK map {
    x =>
      AnswerRow("isPaymentAddressInTheUK.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.IsPaymentAddressInTheUKController.onPageLoad(CheckMode).url, true)
  }

  def anyAgentRef: Option[AnswerRow] = userAnswers.anyAgentRef map {
    x => AnswerRow("anyAgentRef.checkYourAnswersLabel",
    x match {
      case AgentRef.Yes(agentRef) => "site.yes"
      case AgentRef.No => "site.no"
    }, true, routes.AnyAgentRefController.onPageLoad(CheckMode).url, true)
  }

  def agentReferenceNumber: Option[AnswerRow] = userAnswers.agentReferenceNumber map {
    x =>
      AnswerRow("anyAgentRef.agentRefField",
        s"$x", false, routes.AnyAgentRefController.onPageLoad(CheckMode).url, true)
  }

  def nomineeFullName: Option[AnswerRow] = userAnswers.nomineeFullName map {
    x =>
      AnswerRow("nomineeFullName.checkYourAnswersLabel",
        s"$x", false, routes.NomineeFullNameController.onPageLoad(CheckMode).url, true)
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

  def howMuchRentalIncome: Option[AnswerRow] = userAnswers.howMuchRentalIncome map {
    x =>
      AnswerRow("howMuchRentalIncome.checkYourAnswersLabel",
        s"$x", false, routes.HowMuchRentalIncomeController.onPageLoad(CheckMode).url, true)
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
          case CYMinus1 =>
            TaxYearResolver.startOfCurrentTaxYear.minusYears(1).toString(dateFormat) + " to " +
              TaxYearResolver.endOfCurrentTaxYear.minusYears(1).toString(dateFormat)
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

  def employmentDetails: Option[AnswerRow] = userAnswers.employmentDetails map {
    x => AnswerRow("employmentDetails.checkYourAnswersLabel",
      if(x) "site.yes" else "site.no",
      true, routes.EmploymentDetailsController.onPageLoad(CheckMode).url, true)
  }

  def telephoneNumber: Option[AnswerRow] = userAnswers.telephoneNumber map {
    x =>
      AnswerRow("telephoneNumber.checkYourAnswersLabel",
        s"$x", false, routes.TelephoneNumberController.onPageLoad(CheckMode).url, true)
  }
}
