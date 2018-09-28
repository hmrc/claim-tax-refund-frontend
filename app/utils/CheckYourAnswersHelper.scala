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
import uk.gov.hmrc.auth.core.retrieve.ItmpAddress
import viewmodels.AnswerRow

class CheckYourAnswersHelper(userAnswers: UserAnswers)(implicit messages: Messages) {
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
        false, Some(routes.SelectTaxYearController.onPageLoad(CheckMode).url))
  }

  def employmentDetails: Option[AnswerRow] = userAnswers.employmentDetails map {
    x => AnswerRow("employmentDetails.checkYourAnswersLabel",
      if(x) "site.yes" else "site.no",
      true, Some(routes.EmploymentDetailsController.onPageLoad(CheckMode).url))
  }

  def enterPayeReference: Option[AnswerRow] = userAnswers.enterPayeReference map {
    x => AnswerRow("enterPayeReference.checkYourAnswersLabel",
      s"$x", false, Some(routes.EnterPayeReferenceController.onPageLoad(CheckMode).url))
  }

  def detailsOfEmploymentOrPension: Option[AnswerRow] = userAnswers.detailsOfEmploymentOrPension map {
    x => AnswerRow("detailsOfEmploymentOrPension.checkYourAnswersLabel", s"$x", false, Some(routes.DetailsOfEmploymentOrPensionController.onPageLoad(CheckMode).url))
  }

  //Benefits
  //------------------------------------------------------------------

  def anyBenefits: Option[AnswerRow] = userAnswers.anyBenefits map {
    x =>
      AnswerRow("anyBenefits.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, Some(routes.AnyBenefitsController.onPageLoad(CheckMode).url))
  }

  def selectBenefits: Option[AnswerRow] = userAnswers.selectBenefits map {
    val keyPrefix = "selectBenefits."
    x => AnswerRow(keyPrefix + "checkYourAnswersLabel", x.map {
      case Benefits.BEREAVEMENT_ALLOWANCE => messages(keyPrefix + "bereavement-allowance").capitalize
      case Benefits.CARERS_ALLOWANCE => messages(keyPrefix + "carers-allowance").capitalize
      case Benefits.JOBSEEKERS_ALLOWANCE => messages(keyPrefix + "jobseekers-allowance").capitalize
      case Benefits.INCAPACITY_BENEFIT => messages(keyPrefix + "incapacity-benefit").capitalize
      case Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE => messages(keyPrefix + "employment-and-support-allowance").capitalize
      case Benefits.STATE_PENSION => messages(keyPrefix + "state-pension").capitalize
      case Benefits.OTHER_TAXABLE_BENEFIT => messages(keyPrefix + "other-taxable-benefit").capitalize
    }.mkString("<br>"),
      false,
      Some(routes.SelectBenefitsController.onPageLoad(CheckMode).url)
    )
  }

  def howMuchBereavementAllowance: Option[AnswerRow] = userAnswers.howMuchBereavementAllowance map {
    x => AnswerRow("howMuchBereavementAllowance.checkYourAnswersLabel", s"£$x", false, Some(routes.HowMuchBereavementAllowanceController.onPageLoad(CheckMode).url))
  }

  def howMuchCarersAllowance: Option[AnswerRow] = userAnswers.howMuchCarersAllowance map {
    x => AnswerRow("howMuchCarersAllowance.checkYourAnswersLabel", s"£$x", false, Some(routes.HowMuchCarersAllowanceController.onPageLoad(CheckMode).url))
  }

  def howMuchJobseekersAllowance: Option[AnswerRow] = userAnswers.howMuchJobseekersAllowance map {
    x => AnswerRow("howMuchJobseekersAllowance.checkYourAnswersLabel", s"£$x", false, Some(routes.HowMuchJobseekersAllowanceController.onPageLoad(CheckMode).url))
  }

  def howMuchIncapacityBenefit: Option[AnswerRow] = userAnswers.howMuchIncapacityBenefit map {
    x => AnswerRow("howMuchIncapacityBenefit.checkYourAnswersLabel", s"£$x", false, Some(routes.HowMuchIncapacityBenefitController.onPageLoad(CheckMode).url))
  }

  def howMuchEmploymentAndSupportAllowance: Option[AnswerRow] = userAnswers.howMuchEmploymentAndSupportAllowance map {
    x => AnswerRow("howMuchEmploymentAndSupportAllowance.checkYourAnswersLabel", s"£$x", false, Some(routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(CheckMode).url))
  }

  def howMuchStatePension: Option[AnswerRow] = userAnswers.howMuchStatePension map {
    x => AnswerRow("howMuchStatePension.checkYourAnswersLabel", s"£$x", false, Some(routes.HowMuchStatePensionController.onPageLoad(CheckMode).url))
  }

  def otherBenefitsNormalMode: Seq[Option[AnswerRow]] = {
    otherBenefitsAddToList(NormalMode)
  }

  def otherBenefitsCheckMode: Seq[Option[AnswerRow]] = {
    otherBenefitsAddToList(CheckMode)
  }

  def otherBenefitsCheckYourAnswers: Seq[Option[AnswerRow]] = {
    otherBenefitsAddToList(NormalMode, cya = true)
  }

  def otherBenefitsAddToList(mode: Mode, cya: Boolean = false): Seq[Option[AnswerRow]] = {
    for {
      otherBenefits <- userAnswers.otherBenefit
    } yield {
      otherBenefits.zipWithIndex.flatMap {
        case (benefits, index) =>
          Seq(
            Some(AnswerRow(
              label = benefits.name,
              answer = s"£${benefits.amount}",
              answerIsMessageKey = false,
              url = if(cya) None else Some(routes.OtherBenefitController.onPageLoad(mode, Index(index)).url),
              deleteUrl = if(cya) None else Some(routes.DeleteOtherController.onPageLoad(mode, Index(index), benefits.name, OtherBenefit.collectionId).url)
            )
           )
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
        true, Some(routes.AnyCompanyBenefitsController.onPageLoad(CheckMode).url))
  }

  def selectCompanyBenefits: Option[AnswerRow] = userAnswers.selectCompanyBenefits map {
    val keyPrefix = "selectCompanyBenefits."
    x => AnswerRow(keyPrefix + "checkYourAnswersLabel", x.map {
      case CompanyBenefits.COMPANY_CAR_BENEFIT => messages(keyPrefix + "company-car-benefit").capitalize
      case CompanyBenefits.FUEL_BENEFIT => messages(keyPrefix + "fuel-benefit").capitalize
      case CompanyBenefits.MEDICAL_BENEFIT => messages(keyPrefix + "medical-benefit").capitalize
      case CompanyBenefits.OTHER_COMPANY_BENEFIT => messages(keyPrefix + "other-company-benefit").capitalize
    }.mkString("<br>"), false, Some(routes.SelectCompanyBenefitsController.onPageLoad(CheckMode).url))
  }

  def howMuchCarBenefits: Option[AnswerRow] = userAnswers.howMuchCarBenefits map {
    x =>
      AnswerRow("howMuchCarBenefits.checkYourAnswersLabel",
        s"£$x", false, Some(routes.HowMuchCarBenefitsController.onPageLoad(CheckMode).url))
  }

  def howMuchFuelBenefit: Option[AnswerRow] = userAnswers.howMuchFuelBenefit map {
    x => AnswerRow("howMuchFuelBenefit.checkYourAnswersLabel", s"£$x", false, Some(routes.HowMuchFuelBenefitController.onPageLoad(CheckMode).url))
  }

  def howMuchMedicalBenefits: Option[AnswerRow] = userAnswers.howMuchMedicalBenefits map {
    x =>
      AnswerRow("howMuchMedicalBenefits.checkYourAnswersLabel",
        s"£$x", false, Some(routes.HowMuchMedicalBenefitsController.onPageLoad(CheckMode).url))
  }

  def otherCompanyBenefitsNormalMode: Seq[Option[AnswerRow]] = {
    otherCompanyBenefitsAddToList(NormalMode)
  }

  def otherCompanyBenefitsCheckMode: Seq[Option[AnswerRow]] = {
    otherCompanyBenefitsAddToList(CheckMode)
  }

  def otherCompanyBenefitsCheckYourAnswers: Seq[Option[AnswerRow]] = {
    otherCompanyBenefitsAddToList(NormalMode, cya = true)
  }

  def otherCompanyBenefitsAddToList(mode: Mode, cya: Boolean = false): Seq[Option[AnswerRow]] = {
    for {
      otherCompanyBenefits <- userAnswers.otherCompanyBenefit
    } yield {
      otherCompanyBenefits.zipWithIndex.flatMap {
        case (companyBenefits, index) =>
          Seq(
            Some(AnswerRow(
              label = companyBenefits.name,
              answer = s"£${companyBenefits.amount}",
              answerIsMessageKey = false,
              url = if(cya) None else Some(routes.OtherCompanyBenefitController.onPageLoad(mode, Index(index)).url),
              deleteUrl = if(cya) None else Some(routes.DeleteOtherController.onPageLoad(mode, Index(index), companyBenefits.name, OtherCompanyBenefit.collectionId).url)
            )
            )
          )
      }
    }
  }.getOrElse(Seq.empty)


  //Taxable Income
  //------------------------------------------------------------------

  def anyTaxPaid(label: String, answer: Option[AnyTaxPaid], route: String): Option[AnswerRow] = answer map {
    x =>
      AnswerRow(
        label,
        x match {
          case AnyTaxPaid.Yes(amount) => "site.yes"
          case AnyTaxPaid.No => "site.no"
        },
        false,
				Some(route)
      )
  }

  def taxPaid(label: String, answer: Option[AnyTaxPaid], route: String): Option[AnswerRow] = answer match {
    case Some(AnyTaxPaid.Yes(amount)) =>
      Some(AnswerRow(
        label,
        s"£$amount",
        false,
				Some(route)
      ))
    case _ => None
  }

  def anyTaxableIncome: Option[AnswerRow] = userAnswers.anyTaxableIncome map {
    x =>
      AnswerRow("anyTaxableIncome.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, Some(routes.AnyTaxableIncomeController.onPageLoad(CheckMode).url))
  }

  def selectTaxableIncome: Option[AnswerRow] = userAnswers.selectTaxableIncome map {
    val keyPrefix = "selectTaxableIncome."
    x => AnswerRow(keyPrefix + "checkYourAnswersLabel", x.map {
      case TaxableIncome.RENTAL_INCOME => messages(keyPrefix + "rental-income").capitalize
      case TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST => messages(keyPrefix + "bank-or-building-society-interest").capitalize
      case TaxableIncome.INVESTMENT_OR_DIVIDENDS => messages(keyPrefix + "investment-or-dividends").capitalize
      case TaxableIncome.FOREIGN_INCOME => messages(keyPrefix + "foreign-income").capitalize
      case TaxableIncome.OTHER_TAXABLE_INCOME => messages(keyPrefix + "other-taxable-income").capitalize
    }.mkString("<br>"), false, Some(routes.SelectTaxableIncomeController.onPageLoad(CheckMode).url))
  }

  def howMuchRentalIncome: Option[AnswerRow] = userAnswers.howMuchRentalIncome map {
    x =>
      AnswerRow("howMuchRentalIncome.checkYourAnswersLabel",
        s"£$x", false, Some(routes.HowMuchRentalIncomeController.onPageLoad(CheckMode).url))
  }

  def howMuchBankInterest: Option[AnswerRow] = userAnswers.howMuchBankInterest map {
    x => AnswerRow("howMuchBankInterest.checkYourAnswersLabel", s"£$x", false, Some(routes.HowMuchBankInterestController.onPageLoad(CheckMode).url))
  }

  def howMuchInvestmentOrDividend: Option[AnswerRow] = userAnswers.howMuchInvestmentOrDividend map {
    x => AnswerRow("howMuchInvestmentOrDividend.checkYourAnswersLabel", s"£$x", false, Some(routes.HowMuchInvestmentOrDividendController.onPageLoad(CheckMode).url))
  }

  def howMuchForeignIncome: Option[AnswerRow] = userAnswers.howMuchForeignIncome map {
    x => AnswerRow("howMuchForeignIncome.checkYourAnswersLabel", s"£$x", false, Some(routes.HowMuchForeignIncomeController.onPageLoad(CheckMode).url))
  }

  def otherTaxableIncome: Seq[Option[AnswerRow]] = {
    for {
      otherTaxableIncome <- userAnswers.fullOtherTaxableIncome
    } yield {
      otherTaxableIncome.zipWithIndex.flatMap {
        case (taxableIncome, index) =>
          Seq(
            Some(AnswerRow(
              label = taxableIncome.name,
              answer = s"£${taxableIncome.amount}",
              answerIsMessageKey = false,
              url = Some(routes.OtherTaxableIncomeController.onPageLoad(CheckMode, Index(index)).url),
              isHeadingRow = true
            )),
            Some(AnswerRow(
              label = messages("checkYourAnswers.otherTaxableIncome.label", taxableIncome.name),
              answer = s"£${taxableIncome.amount}",
              answerIsMessageKey = false,
              url = Some(routes.OtherTaxableIncomeController.onPageLoad(CheckMode, Index(index)).url)
            )),
            anyTaxPaid(
              label = messages("anyTaxableOtherIncomeOption.checkYourAnswersLabel", taxableIncome.name),
              answer = Some(taxableIncome.anyTaxPaid.get),
              route = routes.AnyTaxableOtherIncomeController.onPageLoad(CheckMode, Index(index)).url
            ),
            taxPaid(
              label = messages("anyTaxableOtherIncome.checkYourAnswersLabel", taxableIncome.name),
              answer = Some(taxableIncome.anyTaxPaid.get),
              route = routes.AnyTaxableOtherIncomeController.onPageLoad(CheckMode, Index(index)).url
            )
          )
      }

    }
  }.getOrElse(Seq.empty)

  def anyOtherTaxableIncome: Option[AnswerRow] = userAnswers.anyOtherTaxableIncome map {
    x =>
      AnswerRow("anyOtherTaxableIncome.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, Some(routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode).url))
  }


  //Payment details
  //------------------------------------------------------------------

  def whereToSendPayment: Option[AnswerRow] = userAnswers.whereToSendPayment map {
    x =>
      AnswerRow("whereToSendPayment.checkYourAnswersLabel",
        s"whereToSendPayment.$x", true, Some(routes.WhereToSendPaymentController.onPageLoad(CheckMode).url))
  }

  def paymentAddressCorrect: Option[AnswerRow] = userAnswers.paymentAddressCorrect map {
    x => AnswerRow("paymentAddressCorrect.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, Some(routes.PaymentAddressCorrectController.onPageLoad(CheckMode).url))
  }

  def itmpAddress: Option[AnswerRow] = userAnswers.itmpAddress map {
    x =>
      AnswerRow(
        "itmpAddress.checkYourAnswersLabel",
        ItmpAddressFormat.asString(
          ItmpAddress(
            x.line1,
            x.line2,
            x.line3,
            x.line4,
            x.line5,
            x.postCode,
            x.countryName,
            x.countryCode
          )),
        true,
        Some(routes.PaymentAddressCorrectController.onPageLoad(CheckMode).url)
      )
  }

  def nomineeFullName: Option[AnswerRow] = userAnswers.nomineeFullName map {
    x =>
      AnswerRow("nomineeFullName.checkYourAnswersLabel",
        s"$x", false, Some(routes.NomineeFullNameController.onPageLoad(CheckMode).url))
  }

  def anyAgentRef: Option[AnswerRow] = userAnswers.anyAgentRef map {
    x => AnswerRow("anyAgentRefOption.checkYourAnswersLabel",
      x match {
        case AnyAgentRef.Yes(agentRef) => "site.yes"
        case AnyAgentRef.No => "site.no"
      }, true, Some(routes.AnyAgentRefController.onPageLoad(CheckMode).url))
  }

  def agentReferenceNumber: Option[AnswerRow] = userAnswers.anyAgentRef match {
    case Some(AnyAgentRef.Yes(number)) =>
      Some(AnswerRow("anyAgentRef.checkYourAnswersLabel",
        s"$number",
        false,
        Some(routes.AnyAgentRefController.onPageLoad(CheckMode).url)
      ))
    case _ => None
  }

  def isPaymentAddressInTheUK: Option[AnswerRow] = userAnswers.isPaymentAddressInTheUK map {
    x =>
      AnswerRow("isPaymentAddressInTheUK.checkYourAnswersLabel",
        if (x) "site.yes" else "site.no",
        true, Some(routes.IsPaymentAddressInTheUKController.onPageLoad(CheckMode).url))
  }

  def paymentUKAddress: Option[AnswerRow] = userAnswers.paymentUKAddress map {
    x =>
      AnswerRow("paymentUKAddress.checkYourAnswersLabel",
        UkAddress.asString(
          UkAddress(
            x.addressLine1,
            x.addressLine2,
            x.addressLine3,
            x.addressLine4,
            x.addressLine5,
            x.postcode)),
        false, Some(routes.PaymentUKAddressController.onPageLoad(CheckMode).url))
  }

  def paymentInternationalAddress: Option[AnswerRow] = userAnswers.paymentInternationalAddress map {
    x =>
      AnswerRow("paymentInternationalAddress.checkYourAnswersLabel",
        InternationalAddress.asString(
          InternationalAddress(
            x.addressLine1,
            x.addressLine2,
            x.addressLine3,
            x.addressLine4,
            x.addressLine5,
            x.country)),
        false, Some(routes.PaymentInternationalAddressController.onPageLoad(CheckMode).url))
  }

  def paymentLookupAddress: Option[AnswerRow] = userAnswers.paymentLookupAddress map {
    x =>
      AnswerRow(
        "addressLookup.checkYourAnswersLabel",
        AddressLookup.asString(
          AddressLookup(
            x.address,
            x.auditRef)
        ),
        false,
        Some(routes.IsPaymentAddressInTheUKController.onPageLoad(CheckMode).url)
      )
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
        Some(routes.TelephoneNumberController.onPageLoad(CheckMode).url)
      )
  }

  def telephoneNumber: Option[AnswerRow] = userAnswers.anyTelephoneNumber match {
    case Some(TelephoneOption.Yes(number)) =>
      Some(AnswerRow("telephoneNumber.checkYourAnswersLabel",
        s"$number",
        false,
        Some(routes.TelephoneNumberController.onPageLoad(CheckMode).url)
      ))
    case _ => None
  }
}
