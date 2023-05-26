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
import models.SelectTaxYear.{CYMinus1, CYMinus2, CYMinus3, CYMinus4, CustomTaxYear}
import models._
import play.api.i18n.Messages
import uk.gov.hmrc.auth.core.retrieve.ItmpAddress
import viewmodels.AnswerRow

class CheckYourAnswersHelper(userAnswers: UserAnswers)(implicit messages: Messages) {

  //Claim details
  //------------------------------------------------------------------

  def selectTaxYear: Option[AnswerRow] = userAnswers.selectTaxYear map {
    x =>
      AnswerRow(
				label = "selectTaxYear.heading",
        answer = x match {
          case CYMinus1 =>
            CYMinus1.asString
          case CYMinus2 =>
            CYMinus2.asString
          case CYMinus3 =>
            CYMinus3.asString
          case CYMinus4 =>
            CYMinus4.asString
          case year @ CustomTaxYear(y) =>
            year.asString
          case _ => ""
        },
				url = Some(routes.SelectTaxYearController.onPageLoad(CheckMode).url),
				changeLabel = "selectTaxYear.changeLabel")
  }

  def employmentDetails: Option[AnswerRow] = userAnswers.employmentDetails map {
    x => AnswerRow(label = "employmentDetails.correctDetails",
			answer = if(x) "site.yes" else "site.no",
			url = Some(routes.EmploymentDetailsController.onPageLoad(CheckMode).url),
      changeLabel = "employmentDetails.changeLabel"
    )
  }

  def enterPayeReference: Option[AnswerRow] = userAnswers.enterPayeReference map {
    x => AnswerRow("enterPayeReference.heading",
      s"$x", Some(routes.EnterPayeReferenceController.onPageLoad(CheckMode).url), "enterPayeReference.changeLabel")
  }

  def detailsOfEmploymentOrPension: Option[AnswerRow] = userAnswers.detailsOfEmploymentOrPension map {
    x => AnswerRow(
			label = "detailsOfEmploymentOrPension.heading",
			answer = s"$x",
			url = Some(routes.DetailsOfEmploymentOrPensionController.onPageLoad(CheckMode).url),
			changeLabel = "detailsOfEmploymentOrPension.changeLabel")
  }

  //Benefits
  //------------------------------------------------------------------

  def anyBenefits: Option[AnswerRow] = userAnswers.anyBenefits map {
    x =>
      AnswerRow("anyBenefits.heading",
        if (x) "site.yes" else "site.no",
        Some(routes.AnyBenefitsController.onPageLoad(CheckMode).url), "anyBenefits.changeLabel")
  }

  def selectBenefits: Option[AnswerRow] = userAnswers.selectBenefits map {
    val keyPrefix = "selectBenefits."
    x =>
			AnswerRow(
				label = keyPrefix + "heading",
				answer = x.map {
					case Benefits.BEREAVEMENT_ALLOWANCE => messages(keyPrefix + "bereavement-allowance").capitalize + "<br>"
					case Benefits.CARERS_ALLOWANCE => messages(keyPrefix + "carers-allowance").capitalize + "<br>"
					case Benefits.JOBSEEKERS_ALLOWANCE => messages(keyPrefix + "jobseekers-allowance").capitalize + "<br>"
					case Benefits.INCAPACITY_BENEFIT => messages(keyPrefix + "incapacity-benefit").capitalize + "<br>"
					case Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE => messages(keyPrefix + "employment-and-support-allowance").capitalize + "<br>"
					case Benefits.STATE_PENSION => messages(keyPrefix + "state-pension").capitalize + "<br>"
					case Benefits.OTHER_TAXABLE_BENEFIT => messages(keyPrefix + "other-taxable-benefit").capitalize + "<br>"
          case _ => ""
				}.mkString,
				url = Some(routes.SelectBenefitsController.onPageLoad(CheckMode).url),
				changeLabel = keyPrefix + "changeLabel"
			)
	}

  def howMuchBereavementAllowance: Option[AnswerRow] = userAnswers.howMuchBereavementAllowance map {
    x =>
      AnswerRow(label = "howMuchBereavementAllowance.heading",
                answer = s"£$x",
                url = Some(routes.HowMuchBereavementAllowanceController.onPageLoad(CheckMode).url),
                changeLabel = "howMuchBereavementAllowance.changeLabel")
  }

  def howMuchCarersAllowance: Option[AnswerRow] = userAnswers.howMuchCarersAllowance map {
    x =>
      AnswerRow(label = "howMuchCarersAllowance.heading",
                answer = s"£$x",
                url = Some(routes.HowMuchCarersAllowanceController.onPageLoad(CheckMode).url),
                changeLabel = "howMuchCarersAllowance.changeLabel")
  }

  def howMuchJobseekersAllowance: Option[AnswerRow] = userAnswers.howMuchJobseekersAllowance map {
    x =>
      AnswerRow(label = "howMuchJobseekersAllowance.heading",
                answer = s"£$x",
                url = Some(routes.HowMuchJobseekersAllowanceController.onPageLoad(CheckMode).url),
                changeLabel = "howMuchJobseekersAllowance.changeLabel")
  }

  def howMuchIncapacityBenefit: Option[AnswerRow] = userAnswers.howMuchIncapacityBenefit map {
    x =>
      AnswerRow(label = "howMuchIncapacityBenefit.heading",
                answer = s"£$x",
                url = Some(routes.HowMuchIncapacityBenefitController.onPageLoad(CheckMode).url),
                changeLabel = "howMuchIncapacityBenefit.changeLabel")
  }

  def howMuchEmploymentAndSupportAllowance: Option[AnswerRow] = userAnswers.howMuchEmploymentAndSupportAllowance map {
    x =>
      AnswerRow(label = "howMuchEmploymentAndSupportAllowance.heading",
                answer = s"£$x",
                url = Some(routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(CheckMode).url),
                changeLabel = "howMuchEmploymentAndSupportAllowance.changeLabel")
  }

  def howMuchStatePension: Option[AnswerRow] = userAnswers.howMuchStatePension map {
    x =>
      AnswerRow(label = "howMuchStatePension.heading",
                answer = s"£$x",
                url = Some(routes.HowMuchStatePensionController.onPageLoad(CheckMode).url),
                changeLabel = "howMuchStatePension.changeLabel")
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
              url = if(cya) None else Some(routes.OtherBenefitController.onPageLoad(mode, Index(index)).url),
              deleteUrl = if(cya) None else Some(routes.DeleteOtherController.onPageLoad(mode, Index(index), benefits.name, OtherBenefit.collectionId).url),
							changeLabel = s" ${benefits.name}"
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
      AnswerRow("anyCompanyBenefits.heading",
        if(x) "site.yes" else "site.no",
        Some(routes.AnyCompanyBenefitsController.onPageLoad(CheckMode).url), "anyCompanyBenefits.changeLabel")
  }

  def selectCompanyBenefits: Option[AnswerRow] = userAnswers.selectCompanyBenefits map {
    val keyPrefix = "selectCompanyBenefits."
    x => AnswerRow(
			label = keyPrefix + "heading",
			answer = x.map {
      	case CompanyBenefits.COMPANY_CAR_BENEFIT =>  messages(keyPrefix + "company-car-benefit").capitalize + "<br>"
      	case CompanyBenefits.FUEL_BENEFIT => messages(keyPrefix + "fuel-benefit").capitalize + "<br>"
      	case CompanyBenefits.MEDICAL_BENEFIT => messages(keyPrefix + "medical-benefit").capitalize + "<br>"
      	case CompanyBenefits.OTHER_COMPANY_BENEFIT => messages(keyPrefix + "other-company-benefit").capitalize + "<br>"
        case _ => ""
    	}.mkString,
			url = Some(routes.SelectCompanyBenefitsController.onPageLoad(CheckMode).url),
			changeLabel = keyPrefix + "changeLabel"
		)
  }

  def howMuchCarBenefits: Option[AnswerRow] = userAnswers.howMuchCarBenefits map {
    x =>
      AnswerRow(label = "howMuchCarBenefits.heading",
                answer = s"£$x",
                url = Some(routes.HowMuchCarBenefitsController.onPageLoad(CheckMode).url),
                changeLabel = "howMuchCarBenefits.changeLabel")
  }

  def howMuchFuelBenefit: Option[AnswerRow] = userAnswers.howMuchFuelBenefit map {
    x =>
      AnswerRow(label = "howMuchFuelBenefit.heading",
                answer = s"£$x",
                url = Some(routes.HowMuchFuelBenefitController.onPageLoad(CheckMode).url),
                changeLabel = "howMuchFuelBenefit.changeLabel")
  }

  def howMuchMedicalBenefits: Option[AnswerRow] = userAnswers.howMuchMedicalBenefits map {
    x =>
      AnswerRow(label = "howMuchMedicalBenefits.heading",
                answer = s"£$x",
                url = Some(routes.HowMuchMedicalBenefitsController.onPageLoad(CheckMode).url),
                changeLabel = "howMuchMedicalBenefits.changeLabel")
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
              url = if(cya) None else Some(routes.OtherCompanyBenefitController.onPageLoad(mode, Index(index)).url),
              deleteUrl = if(cya) {None}
								else {Some(routes.DeleteOtherController.onPageLoad(mode, Index(index), companyBenefits.name, OtherCompanyBenefit.collectionId).url)},
							changeLabel = s" ${companyBenefits.name}"
            ))
          )
      }
    }
  }.getOrElse(Seq.empty)


  //Taxable Income
  //------------------------------------------------------------------

  def anyTaxPaid(label: String, answer: Option[AnyTaxPaid], route: Option[String]): Option[AnswerRow] = answer map {
    x =>
      AnswerRow(
        label = label,
        answer = x match {
          case AnyTaxPaid.Yes(amount) => "site.yes"
          case AnyTaxPaid.No => "site.no"
          case _ => ""
        },
				url = route,
				changeLabel = label
      )
  }

  def taxPaid(label: String, answer: Option[AnyTaxPaid], route: Option[String]): Option[AnswerRow] = answer match {
    case Some(AnyTaxPaid.Yes(amount)) =>
      Some(AnswerRow(
        label = label,
        answer = s"£$amount",
				url = route,
				changeLabel = label
      ))
    case _ => None
  }

  def anyTaxableIncome: Option[AnswerRow] = userAnswers.anyTaxableIncome map {
    x =>
      AnswerRow("anyTaxableIncome.heading",
        if (x) "site.yes" else "site.no",
        Some(routes.AnyTaxableIncomeController.onPageLoad(CheckMode).url), "anyTaxableIncome.changeLabel")
  }

  def selectTaxableIncome: Option[AnswerRow] = userAnswers.selectTaxableIncome map {
    val keyPrefix = "selectTaxableIncome."
    x => AnswerRow(
			label = keyPrefix + "heading",
			answer = x.map {
      	case TaxableIncome.RENTAL_INCOME => messages(keyPrefix + "rental-income").capitalize + "<br>"
      	case TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST => messages(keyPrefix + "bank-or-building-society-interest").capitalize + "<br>"
      	case TaxableIncome.INVESTMENT_OR_DIVIDENDS => messages(keyPrefix + "investment-or-dividends").capitalize + "<br>"
      	case TaxableIncome.FOREIGN_INCOME => messages(keyPrefix + "foreign-income").capitalize + "<br>"
      	case TaxableIncome.OTHER_TAXABLE_INCOME => messages(keyPrefix + "other-taxable-income").capitalize + "<br>"
        case _ => ""
    	}.mkString,
			url = Some(routes.SelectTaxableIncomeController.onPageLoad(CheckMode).url),
			changeLabel = keyPrefix + "changeLabel"
		)
  }


	def howMuchBankInterest: Option[AnswerRow] = userAnswers.howMuchBankInterest map {
		x =>
			AnswerRow(label = "howMuchBankInterest.heading",
				answer = s"£$x",
				url = Some(routes.HowMuchBankInterestController.onPageLoad(CheckMode).url),
				changeLabel = "howMuchBankInterest.changeLabel")
	}

  def howMuchRentalIncome: Option[AnswerRow] = userAnswers.howMuchRentalIncome map {
    x =>
      AnswerRow(label = "howMuchRentalIncome.heading",
                answer = s"£$x",
                url = Some(routes.HowMuchRentalIncomeController.onPageLoad(CheckMode).url),
                changeLabel = "howMuchRentalIncome.changeLabel")
  }

  def howMuchInvestmentOrDividend: Option[AnswerRow] = userAnswers.howMuchInvestmentOrDividend map {
    x =>
      AnswerRow(label = "howMuchInvestmentOrDividend.heading",
                answer = s"£$x",
                url = Some(routes.HowMuchInvestmentOrDividendController.onPageLoad(CheckMode).url),
                changeLabel = "howMuchInvestmentOrDividend.changeLabel")
  }

  def howMuchForeignIncome: Option[AnswerRow] = userAnswers.howMuchForeignIncome map {
    x =>
      AnswerRow(label = "howMuchForeignIncome.heading",
                answer = s"£$x",
                url = Some(routes.HowMuchForeignIncomeController.onPageLoad(CheckMode).url),
                changeLabel = "howMuchForeignIncome.changeLabel")
  }

  def otherTaxableIncomeNormalMode: Seq[Option[AnswerRow]] = {
    otherTaxableIncomeAddToList(NormalMode)
  }

  def otherTaxableIncomeCheckMode: Seq[Option[AnswerRow]] = {
    otherTaxableIncomeAddToList(CheckMode)
  }

  def otherTaxableIncomeCheckYourAnswers: Seq[Option[AnswerRow]] = {
    otherTaxableIncomeAddToList(NormalMode, cya = true)
  }

  def otherTaxableIncomeAddToList(mode: Mode, cya: Boolean = false): Seq[Option[AnswerRow]] = {
    for {
      otherTaxableIncome <- userAnswers.otherTaxableIncome
    } yield {
      otherTaxableIncome.zipWithIndex.flatMap {
        case (taxableIncome, index) =>
          Seq(
            Some(AnswerRow(
              label = taxableIncome.name,
              answer = s"${taxableIncome.name}",
              isHeadingRow = true,
							changeLabel = s" ${taxableIncome.name}"
            )),
            Some(AnswerRow(
              label = messages("checkYourAnswers.otherTaxableIncome.label", taxableIncome.name),
              answer = s"£${taxableIncome.amount}",
              url = if(cya) None else Some(routes.OtherTaxableIncomeController.onPageLoad(mode, Index(index)).url),
							changeLabel = s" ${messages("checkYourAnswers.otherTaxableIncome.label", taxableIncome.name)}"
            )),
            anyTaxPaid(
              label = messages("anyTaxableOtherIncome.heading", taxableIncome.name),
              answer = Some(taxableIncome.anyTaxPaid.get),
              route = if(cya) None else Some(routes.AnyTaxableOtherIncomeController.onPageLoad(CheckMode, Index(index)).url)
            ),
            taxPaid(
              label = messages("anyTaxableOtherIncome.checkYourAnswersLabel", taxableIncome.name),
              answer = Some(taxableIncome.anyTaxPaid.get),
              route = if(cya) None else Some(routes.AnyTaxableOtherIncomeController.onPageLoad(CheckMode, Index(index)).url)
            ),
            if(cya) None else {
              Some(AnswerRow(
                label = s"${taxableIncome.name}",
                answer = s"${taxableIncome.name}",
                isDeleteLinkRow = true,
                deleteUrl = Some(routes.DeleteOtherController.onPageLoad(mode, Index(index), taxableIncome.name, OtherTaxableIncome.collectionId).url),
								changeLabel = s" ${taxableIncome.name}"
								))
            }
          )
      }

    }
  }.getOrElse(Seq.empty)

  def anyOtherTaxableIncome: Option[AnswerRow] = userAnswers.anyOtherTaxableIncome map {
    x =>
      AnswerRow("anyOtherTaxableIncome.heading",
        if (x) "site.yes" else "site.no",
        Some(routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode).url), "anyOtherTaxableIncome.changeLabel")
  }


  //Payment details
  //------------------------------------------------------------------

  def whereToSendPayment: Option[AnswerRow] = userAnswers.whereToSendPayment map {
    x =>
      AnswerRow("whereToSendPayment.heading",
        s"whereToSendPayment.$x", Some(routes.WhereToSendPaymentController.onPageLoad(CheckMode).url), "whereToSendPayment.changeLabel")
  }

  def paymentAddressCorrect: Option[AnswerRow] = userAnswers.itmpAddress map {
    x =>
      AnswerRow(
        label = "<p>" + messages("itmpAddress.heading") + "</p>" + ItmpAddressFormat.asString(
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
        answer = if(userAnswers.paymentAddressCorrect.getOrElse(true)) "site.yes" else "site.no",
        url = Some(routes.PaymentAddressCorrectController.onPageLoad(CheckMode).url),
				changeLabel = "itmpAddress.changeLabel"
      )
  }

  def nomineeFullName: Option[AnswerRow] = userAnswers.nomineeFullName map {
    x =>
      AnswerRow("nomineeFullName.heading",
        s"$x",Some(routes.NomineeFullNameController.onPageLoad(CheckMode).url), "nomineeFullName.changeLabel")
  }

  def anyAgentRef: Option[AnswerRow] = userAnswers.anyAgentRef map {
    x => AnswerRow(
			label = messages("anyAgentRef.heading", userAnswers.nomineeFullName.getOrElse("your nominee")),
      answer = x match {
        case AnyAgentRef.Yes(agentRef) => "site.yes"
        case AnyAgentRef.No => "site.no"
        case _ => ""
      },
			url = Some(routes.AnyAgentRefController.onPageLoad(CheckMode).url + "/#anyAgentRef"),
			changeLabel = messages("anyAgentRef.changeLabel", userAnswers.nomineeFullName.getOrElse("your nominee"))
		)
  }

  def agentReferenceNumber: Option[AnswerRow] = userAnswers.anyAgentRef match {
    case Some(AnyAgentRef.Yes(number)) =>
      Some(
				AnswerRow(label = "anyAgentRef.agentRefField",
        answer = s"$number",
        url = Some(routes.AnyAgentRefController.onPageLoad(CheckMode).url + "/#agentRef"),
				changeLabel = messages("anyAgentRef.changeLabel", userAnswers.nomineeFullName.getOrElse("your nominee"))
      ))
    case _ => None
  }

  def isPaymentAddressInTheUK: Option[AnswerRow] = userAnswers.isPaymentAddressInTheUK map {
    x =>
      AnswerRow("isPaymentAddressInTheUK.heading",
        if (x) "site.yes" else "site.no",
        Some(routes.IsPaymentAddressInTheUKController.onPageLoad(CheckMode).url), "isPaymentAddressInTheUK.changeLabel")
  }

  def paymentUKAddress: Option[AnswerRow] = userAnswers.paymentUKAddress map {
    x =>
      AnswerRow(
				label = "paymentUKAddress.heading",
        answer = UkAddress.asString(
          UkAddress(
            x.addressLine1,
            x.addressLine2,
            x.addressLine3,
            x.addressLine4,
            x.addressLine5,
            x.postcode)),
				url = Some(routes.PaymentUKAddressController.onPageLoad(CheckMode).url),
				changeLabel = "paymentUKAddress.changeLabel"
			)
  }

  def paymentInternationalAddress: Option[AnswerRow] = userAnswers.paymentInternationalAddress map {
    x =>
      AnswerRow(
				label = "paymentInternationalAddress.heading",
        answer = InternationalAddress.asString(
          InternationalAddress(
            x.addressLine1,
            x.addressLine2,
            x.addressLine3,
            x.addressLine4,
            x.addressLine5,
            x.country)),
				url = Some(routes.PaymentInternationalAddressController.onPageLoad(CheckMode).url),
				changeLabel ="paymentInternationalAddress.changeLabel"
			)
  }

  def paymentLookupAddress: Option[AnswerRow] = userAnswers.paymentLookupAddress map {
    x =>
      AnswerRow(
        label = "paymentAddressCorrect.checkYourAnswersLabel",
        answer = AddressLookup.asString(
          AddressLookup(
            x.address,
            x.auditRef)
        ),
        url = Some(routes.IsPaymentAddressInTheUKController.onPageLoad(CheckMode).url),
				changeLabel = "paymentAddressCorrect.changeLabel"
      )
  }

  //Contact details
  //------------------------------------------------------------------


  def anyTelephoneNumber: Option[AnswerRow] = userAnswers.anyTelephoneNumber map {
    x =>
      AnswerRow(
				label = "telephoneNumber.heading",
        answer = x match {
          case TelephoneOption.Yes(number) => "site.yes"
          case TelephoneOption.No => "site.no"
          case _ => ""
        },
        url = Some(routes.TelephoneNumberController.onPageLoad(CheckMode).url + "/#anyTelephoneNumber"),
				changeLabel = "telephoneNumber.changeLabel"
      )
  }

  def telephoneNumber: Option[AnswerRow] = userAnswers.anyTelephoneNumber match {
    case Some(TelephoneOption.Yes(number)) =>
      Some(AnswerRow(
				label = "telephoneNumber.telephoneNumberField",
        answer = s"$number",
        url = Some(routes.TelephoneNumberController.onPageLoad(CheckMode).url + "/#telephoneNumber"),
				changeLabel = "telephoneNumber.telephoneNumberField.changeLabel"
      ))
    case _ => None
  }
}
