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
import models._
import play.api.i18n.Messages
import viewmodels.AnswerRow

class CheckYourAnswersHelper(userAnswers: UserAnswers) (implicit messages: Messages){

  //Claim details
  //------------------------------------------------------------------

  def selectTaxYear: Option[AnswerRow] = userAnswers.selectTaxYear map {
    x =>
      AnswerRow("selectTaxYear.checkYourAnswersLabel",
        x match {
          case CYMinus1 =>
            CYMinus1.asString
          case CYMinus2 =>
            CYMinus2.asString
          case CYMinus3 =>
            CYMinus3.asString
          case CYMinus4 =>
            CYMinus4.asString
          case CYMinus5 =>
            CYMinus5.asString
        },
        false, routes.SelectTaxYearController.onPageLoad(CheckMode).url)
  }

  def employmentDetails: Option[AnswerRow] = userAnswers.employmentDetails map {
    x => AnswerRow("employmentDetails.checkYourAnswersLabel",
      if(x) "site.yes" else "site.no",
      true, routes.EmploymentDetailsController.onPageLoad(CheckMode).url)
  }

  def enterPayeReference: Option[AnswerRow] = userAnswers.enterPayeReference map {
    x => AnswerRow("enterPayeReference.checkYourAnswersLabel",
      s"$x", false, routes.EnterPayeReferenceController.onPageLoad(CheckMode).url)
  }

  def detailsOfEmploymentOrPension: Option[AnswerRow] = userAnswers.detailsOfEmploymentOrPension map {
    x => AnswerRow("detailsOfEmploymentOrPension.checkYourAnswersLabel", s"$x", false, routes.DetailsOfEmploymentOrPensionController.onPageLoad(CheckMode).url)
  }

  //Benefits
  //------------------------------------------------------------------

  def anyBenefits: Option[AnswerRow] = userAnswers.anyBenefits map {
    x =>
      AnswerRow("anyBenefits.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.AnyBenefitsController.onPageLoad(CheckMode).url)
  }

  def selectBenefits: Option[AnswerRow] = userAnswers.selectBenefits map {
    val keyPrefix = "selectBenefits."
    x => AnswerRow(keyPrefix + "checkYourAnswersLabel", x.map {
      case Benefits.BEREAVEMENT_ALLOWANCE => Messages(keyPrefix + "bereavement-allowance")
      case Benefits.CARERS_ALLOWANCE => Messages(keyPrefix + "carers-allowance")
      case Benefits.JOBSEEKERS_ALLOWANCE => Messages(keyPrefix + "jobseekers-allowance")
      case Benefits.INCAPACITY_BENEFIT => Messages(keyPrefix + "incapacity-benefit")
      case Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE => Messages(keyPrefix + "employment-and-support-allowance")
      case Benefits.STATE_PENSION => Messages(keyPrefix + "state-pension")
      case Benefits.OTHER_TAXABLE_BENEFIT => Messages(keyPrefix + "other-taxable-benefit")
    }.mkString("<br>"), false, routes.SelectBenefitsController.onPageLoad(CheckMode).url)
  }

  def howMuchBereavementAllowance: Option[AnswerRow] = userAnswers.howMuchBereavementAllowance map {
    x => AnswerRow("howMuchBereavementAllowance.checkYourAnswersLabel", s"£$x", false, routes.HowMuchBereavementAllowanceController.onPageLoad(CheckMode).url)
  }

  def howMuchCarersAllowance: Option[AnswerRow] = userAnswers.howMuchCarersAllowance map {
    x => AnswerRow("howMuchCarersAllowance.checkYourAnswersLabel", s"£$x", false, routes.HowMuchCarersAllowanceController.onPageLoad(CheckMode).url)
  }

  def howMuchJobseekersAllowance: Option[AnswerRow] = userAnswers.howMuchJobseekersAllowance map {
    x => AnswerRow("howMuchJobseekersAllowance.checkYourAnswersLabel", s"£$x", false, routes.HowMuchJobseekersAllowanceController.onPageLoad(CheckMode).url)
  }

  def howMuchIncapacityBenefit: Option[AnswerRow] = userAnswers.howMuchIncapacityBenefit map {
    x => AnswerRow("howMuchIncapacityBenefit.checkYourAnswersLabel", s"£$x", false, routes.HowMuchIncapacityBenefitController.onPageLoad(CheckMode).url)
  }

  def howMuchEmploymentAndSupportAllowance: Option[AnswerRow] = userAnswers.howMuchEmploymentAndSupportAllowance map {
    x => AnswerRow("howMuchEmploymentAndSupportAllowance.checkYourAnswersLabel", s"£$x", false, routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(CheckMode).url)
  }

  def howMuchStatePension: Option[AnswerRow] = userAnswers.howMuchStatePension map {
    x => AnswerRow("howMuchStatePension.checkYourAnswersLabel", s"£$x", false, routes.HowMuchStatePensionController.onPageLoad(CheckMode).url)
  }

  def otherBenefits: Seq[Option[AnswerRow]] = {
    for {
      otherBenefits <- userAnswers.otherBenefit
    } yield {
      otherBenefits.zipWithIndex.flatMap {
        case (benefits, index) =>
          Seq(
            Some(AnswerRow(benefits.name, s"£${benefits.amount}", answerIsMessageKey = false, routes.OtherBenefitController.onPageLoad(CheckMode, Index(index)).url))
          )
      }
    }
  }.getOrElse(Seq.empty)

  //Company benefits
  //------------------------------------------------------------------

  def anyCompanyBenefits: Option[AnswerRow] = userAnswers.anyCompanyBenefits map {
    x =>
      AnswerRow("anyCompanyBenefits.checkYourAnswersLabel",
        if(x) "site.yes" else "site.no",
        true, routes.AnyCompanyBenefitsController.onPageLoad(CheckMode).url)
  }

  def selectCompanyBenefits: Option[AnswerRow] = userAnswers.selectCompanyBenefits map {
    val keyPrefix = "selectCompanyBenefits."
    x => AnswerRow(keyPrefix + "checkYourAnswersLabel", x.map {
      case CompanyBenefits.COMPANY_CAR_BENEFIT => Messages(keyPrefix + "company-car-benefit")
      case CompanyBenefits.FUEL_BENEFIT => Messages(keyPrefix + "fuel-benefit")
      case CompanyBenefits.MEDICAL_BENEFIT => Messages(keyPrefix + "medical-benefit")
      case CompanyBenefits.OTHER_COMPANY_BENEFIT => Messages(keyPrefix + "other-company-benefit")
    }.mkString("<br>"), false, routes.SelectCompanyBenefitsController.onPageLoad(CheckMode).url)
  }

  def howMuchCarBenefits: Option[AnswerRow] = userAnswers.howMuchCarBenefits map {
    x =>
      AnswerRow("howMuchCarBenefits.checkYourAnswersLabel",
        s"$x", false, routes.HowMuchCarBenefitsController.onPageLoad(CheckMode).url)
  }

  def howMuchFuelBenefit: Option[AnswerRow] = userAnswers.howMuchFuelBenefit map {
    x => AnswerRow("howMuchFuelBenefit.checkYourAnswersLabel", s"$x", false, routes.HowMuchFuelBenefitController.onPageLoad(CheckMode).url)
  }

  def howMuchMedicalBenefits: Option[AnswerRow] = userAnswers.howMuchMedicalBenefits map {
    x =>
      AnswerRow("howMuchMedicalBenefits.checkYourAnswersLabel",
        s"$x", false, routes.HowMuchMedicalBenefitsController.onPageLoad(CheckMode).url)
  }

  def otherCompanyBenefitsName: Option[AnswerRow] = userAnswers.otherCompanyBenefitsName map {
    x => AnswerRow("otherCompanyBenefitsName.checkYourAnswersLabel", s"$x", false, routes.OtherCompanyBenefitsNameController.onPageLoad(CheckMode).url)
  }

  def howMuchOtherCompanyBenefit: Option[AnswerRow] = userAnswers.howMuchOtherCompanyBenefit map {
    x => AnswerRow("howMuchOtherCompanyBenefit.checkYourAnswersLabel", s"$x", false, routes.HowMuchOtherCompanyBenefitController.onPageLoad(CheckMode).url)
  }

  def anyOtherCompanyBenefits: Option[AnswerRow] = userAnswers.anyOtherCompanyBenefits map {
    x => AnswerRow("anyOtherCompanyBenefits.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.AnyOtherCompanyBenefitsController.onPageLoad(CheckMode).url)
  }


  //Taxable Income
  //------------------------------------------------------------------

  def anyTaxPaid(label: String, answer: Option[AnyTaxPaid], route: String): Option[AnswerRow] = answer map {
    x =>
      AnswerRow(s"$label.checkYourAnswersLabel",
        x match {
          case AnyTaxPaid.Yes(amount) => "site.yes"
          case AnyTaxPaid.No => "site.no"
        },
        false,
        route
      )
  }

  def taxPaid(label: String, answer: Option[AnyTaxPaid], route: String): Option[AnswerRow] = answer match {
    case Some(AnyTaxPaid.Yes(amount)) =>
      Some(AnswerRow(s"$label.checkYourAnswersLabel",
        s"$amount",
        false,
        route
      ))
    case _ => None
  }

  def anyTaxableIncome: Option[AnswerRow] = userAnswers.anyTaxableIncome map {
    x =>
      AnswerRow("anyTaxableIncome.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.AnyTaxableIncomeController.onPageLoad(CheckMode).url)
  }

  def selectTaxableIncome: Option[AnswerRow] = userAnswers.selectTaxableIncome map {
    val keyPrefix = "selectTaxableIncome."
    x => AnswerRow(keyPrefix + "checkYourAnswersLabel", x.map {
      case TaxableIncome.RENTAL_INCOME => Messages(keyPrefix + "rental-income")
      case TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST => Messages(keyPrefix + "bank-or-building-society-interest")
      case TaxableIncome.INVESTMENT_OR_DIVIDENDS => Messages(keyPrefix + "investment-or-dividends")
      case TaxableIncome.FOREIGN_INCOME => Messages(keyPrefix + "foreign-income")
      case TaxableIncome.OTHER_TAXABLE_INCOME => Messages(keyPrefix + "other-taxable-income")
    }.mkString("<br>"), false, routes.SelectTaxableIncomeController.onPageLoad(CheckMode).url)
  }

  def howMuchRentalIncome: Option[AnswerRow] = userAnswers.howMuchRentalIncome map {
    x =>
      AnswerRow("howMuchRentalIncome.checkYourAnswersLabel",
        s"$x", false, routes.HowMuchRentalIncomeController.onPageLoad(CheckMode).url)
  }

  def howMuchBankInterest: Option[AnswerRow] = userAnswers.howMuchBankInterest map {
    x => AnswerRow("howMuchBankInterest.checkYourAnswersLabel", s"$x", false, routes.HowMuchBankInterestController.onPageLoad(CheckMode).url)
  }

  def howMuchInvestmentOrDividend: Option[AnswerRow] = userAnswers.howMuchInvestmentOrDividend map {
    x => AnswerRow("howMuchInvestmentOrDividend.checkYourAnswersLabel", s"$x", false, routes.HowMuchInvestmentOrDividendController.onPageLoad(CheckMode).url)
  }

  def howMuchForeignIncome: Option[AnswerRow] = userAnswers.howMuchForeignIncome map {
    x => AnswerRow("howMuchForeignIncome.checkYourAnswersLabel", s"$x", false, routes.HowMuchForeignIncomeController.onPageLoad(CheckMode).url)
  }

  def otherTaxableIncome: Seq[Option[AnswerRow]] = {
        for {
          otherTaxableIncome <- userAnswers.otherTaxableIncome
        } yield {
          otherTaxableIncome.zipWithIndex.flatMap {
            case (income, index) =>
              Seq(
                Some(AnswerRow(income.name, s"£${income.amount}", answerIsMessageKey = false, routes.OtherBenefitController.onPageLoad(CheckMode, Index(index)).url))
              )
          }
        }
//    x => AnswerRow("otherTaxableIncomeName.checkYourAnswersLabel", s"$x", false, routes.OtherTaxableIncomeNameController.onPageLoad(CheckMode).url)
  }.getOrElse(Seq.empty)

  def anyOtherTaxableIncome: Option[AnswerRow] = userAnswers.anyOtherTaxableIncome map {
    x =>
      AnswerRow("anyOtherTaxableIncome.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode).url)
  }


  //Payment details
  //------------------------------------------------------------------

  def whereToSendPayment: Option[AnswerRow] = userAnswers.whereToSendPayment map {
    x =>
      AnswerRow("whereToSendPayment.checkYourAnswersLabel",
        s"whereToSendPayment.$x", true, routes.WhereToSendPaymentController.onPageLoad(CheckMode).url)
  }

  def paymentAddressCorrect: Option[AnswerRow] = userAnswers.paymentAddressCorrect map {
    x => AnswerRow("paymentAddressCorrect.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.PaymentAddressCorrectController.onPageLoad(CheckMode).url)
  }

  def nomineeFullName: Option[AnswerRow] = userAnswers.nomineeFullName map {
    x =>
      AnswerRow("nomineeFullName.checkYourAnswersLabel",
        s"$x", false, routes.NomineeFullNameController.onPageLoad(CheckMode).url)
  }

  def anyAgentRef: Option[AnswerRow] = userAnswers.anyAgentRef map {
    x => AnswerRow("anyAgentRefOption.checkYourAnswersLabel",
      x match {
        case AnyAgentRef.Yes(agentRef) => "site.yes"
        case AnyAgentRef.No => "site.no"
      }, true, routes.AnyAgentRefController.onPageLoad(CheckMode).url)
  }

  def agentReferenceNumber: Option[AnswerRow] = userAnswers.anyAgentRef match {
    case Some(AnyAgentRef.Yes(number)) =>
      Some(AnswerRow("anyAgentRef.checkYourAnswersLabel",
        s"$number",
        false,
        routes.AnyAgentRefController.onPageLoad(CheckMode).url
      ))
    case _ => None
  }

  def isPaymentAddressInTheUK: Option[AnswerRow] = userAnswers.isPaymentAddressInTheUK map {
    x =>
      AnswerRow("isPaymentAddressInTheUK.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, routes.IsPaymentAddressInTheUKController.onPageLoad(CheckMode).url)
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
        false, routes.PaymentUKAddressController.onPageLoad(CheckMode).url)
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
        false, routes.PaymentInternationalAddressController.onPageLoad(CheckMode).url)
  }


  //Contact details
  //------------------------------------------------------------------


  def anyTelephoneNumber: Option[AnswerRow] = userAnswers.anyTelephoneNumber map {
    x =>
      AnswerRow("telephoneNumberOption.checkYourAnswersLabel",
        x match {
          case TelephoneOption.Yes(number) => "site.yes"
          case TelephoneOption.No => "site.no"
        },
        false,
        routes.TelephoneNumberController.onPageLoad(CheckMode).url
      )
  }

  def telephoneNumber: Option[AnswerRow] = userAnswers.anyTelephoneNumber match {
    case Some(TelephoneOption.Yes(number)) =>
      Some(AnswerRow("telephoneNumber.checkYourAnswersLabel",
        s"$number",
        false,
        routes.TelephoneNumberController.onPageLoad(CheckMode).url
      ))
    case _ => None
  }

}
