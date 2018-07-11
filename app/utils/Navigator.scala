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
import identifiers._
import javax.inject.{Inject, Singleton}
import models.WhereToSendPayment.{Myself, Nominee}
import models.{Benefits, _}
import play.api.mvc.Call

@Singleton
class Navigator @Inject()() {

  private val routeMap: Map[Identifier, UserAnswers => Call] = Map(
    SelectTaxYearId -> (_ => routes.EmploymentDetailsController.onPageLoad(NormalMode)),
    EmploymentDetailsId -> employmentDetails,
    EnterPayeReferenceId -> (_ => routes.DetailsOfEmploymentOrPensionController.onPageLoad(NormalMode)),
    DetailsOfEmploymentOrPensionId -> (_ => routes.AnyBenefitsController.onPageLoad(NormalMode)),
    AnyBenefitsId -> anyBenefits,
    SelectBenefitsId -> selectBenefits,
    HowMuchBereavementAllowanceId -> benefitRouter(HowMuchBereavementAllowanceId.cyaId),
    HowMuchCarersAllowanceId -> benefitRouter(HowMuchCarersAllowanceId.cyaId),
    HowMuchJobseekersAllowanceId -> benefitRouter(HowMuchJobseekersAllowanceId.cyaId),
    HowMuchIncapacityBenefitId -> benefitRouter(HowMuchIncapacityBenefitId.cyaId),
    HowMuchEmploymentAndSupportAllowanceId -> benefitRouter(HowMuchEmploymentAndSupportAllowanceId.cyaId),
    HowMuchStatePensionId -> benefitRouter(HowMuchStatePensionId.cyaId),
    AnyOtherBenefitsId -> anyOtherBenefits,
    AnyCompanyBenefitsId -> anyCompanyBenefits,
    HowMuchCarBenefitsId -> companyBenefitRouter(HowMuchCarBenefitsId.cyaId),
    HowMuchFuelBenefitId -> companyBenefitRouter(HowMuchFuelBenefitId.cyaId),
    HowMuchMedicalBenefitsId -> companyBenefitRouter(HowMuchMedicalBenefitsId.cyaId),
    SelectCompanyBenefitsId -> selectCompanyBenefits,
    AnyTaxableIncomeId -> otherTaxableIncome,
    SelectTaxableIncomeId -> selectTaxableIncome,
    HowMuchRentalIncomeId -> (_ => routes.AnyTaxableRentalIncomeController.onPageLoad(NormalMode)),
    AnyTaxableRentalIncomeId -> taxableIncomeRouter(HowMuchRentalIncomeId.cyaId),
    HowMuchBankInterestId -> (_ => routes.AnyTaxableBankInterestController.onPageLoad(NormalMode)),
    AnyTaxableBankInterestId -> taxableIncomeRouter(HowMuchBankInterestId.cyaId),
    HowMuchInvestmentOrDividendId ->  (_ => routes.AnyTaxableInvestmentsController.onPageLoad(NormalMode)),
    AnyTaxableInvestmentsId -> taxableIncomeRouter(HowMuchInvestmentOrDividendId.cyaId),
    HowMuchForeignIncomeId ->  (_ => routes.AnyTaxableForeignIncomeController.onPageLoad(NormalMode)),
    AnyTaxableForeignIncomeId -> taxableIncomeRouter(HowMuchForeignIncomeId.cyaId),
    OtherTaxableIncomeNameId -> (_ => routes.HowMuchOtherTaxableIncomeController.onPageLoad(NormalMode)),
    HowMuchOtherTaxableIncomeId -> (_ => routes.AnyTaxableOtherIncomeController.onPageLoad(NormalMode)),
    AnyTaxableOtherIncomeId -> (_ => routes.AnyOtherTaxableIncomeController.onPageLoad(NormalMode)),
    AnyOtherTaxableIncomeId -> anyOtherTaxableIncome,
    OtherBenefitsNameId -> (_ => routes.HowMuchOtherBenefitController.onPageLoad(NormalMode)),
    HowMuchOtherBenefitId -> (_ => routes.AnyOtherBenefitsController.onPageLoad(NormalMode)),
    OtherCompanyBenefitsNameId -> (_ => routes.HowMuchOtherCompanyBenefitController.onPageLoad(NormalMode)),
    HowMuchOtherCompanyBenefitId -> (_ => routes.AnyOtherCompanyBenefitsController.onPageLoad(NormalMode)),
    AnyOtherCompanyBenefitsId -> anyOtherCompanyBenefits,
    WhereToSendPaymentId -> whereToSendPayment,
    NomineeFullNameId -> (_ => routes.AnyAgentRefController.onPageLoad(NormalMode)),
    AnyAgentRefId -> (_ => routes.IsPaymentAddressInTheUKController.onPageLoad(NormalMode)),
    PaymentAddressCorrectId -> paymentAddressCorrect,
    IsPaymentAddressInTheUKId -> isPaymentAddressInUkRoute,
    PaymentUKAddressId -> (_ => routes.TelephoneNumberController.onPageLoad(NormalMode)),
    PaymentInternationalAddressId -> (_ => routes.TelephoneNumberController.onPageLoad(NormalMode)),
    TelephoneNumberId -> (_ => routes.CheckYourAnswersController.onPageLoad())
  )

  private val editRouteMap: Map[Identifier, UserAnswers => Call] = Map(
    EmploymentDetailsId ->  employmentDetailsCheck,
    EnterPayeReferenceId -> (_ => routes.DetailsOfEmploymentOrPensionController.onPageLoad(CheckMode)),
    DetailsOfEmploymentOrPensionId -> (_ => routes.CheckYourAnswersController.onPageLoad()),
    AnyAgentRefId -> (_ => routes.IsPaymentAddressInTheUKController.onPageLoad(CheckMode)),
    NomineeFullNameId -> (_ => routes.AnyAgentRefController.onPageLoad(CheckMode)),
    WhereToSendPaymentId -> whereToSendPaymentCheck,
    IsPaymentAddressInTheUKId -> isPaymentAddressInUkRouteCheck,
    PaymentAddressCorrectId -> paymentAddressCorrectCheck
  )

  private def employmentDetails(userAnswers: UserAnswers): Call = userAnswers.employmentDetails match {
    case Some(true) => routes.AnyBenefitsController.onPageLoad(NormalMode)
    case Some(false) => routes.EnterPayeReferenceController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def employmentDetailsCheck(userAnswers: UserAnswers): Call = userAnswers.employmentDetails match {
    case Some(true) => routes.CheckYourAnswersController.onPageLoad()
    case Some(false) if userAnswers.enterPayeReference.nonEmpty => routes.CheckYourAnswersController.onPageLoad()
    case Some(false) => routes.EnterPayeReferenceController.onPageLoad(CheckMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyBenefits(userAnswers: UserAnswers): Call = userAnswers.anyBenefits match {
    case Some(true) => routes.SelectBenefitsController.onPageLoad(NormalMode)
    case Some(false) => routes.AnyCompanyBenefitsController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def selectBenefits(userAnswers: UserAnswers): Call = userAnswers.selectBenefits match {
    case Some(benefits) =>
      benefits.head match {
        case Benefits.BEREAVEMENT_ALLOWANCE => routes.HowMuchBereavementAllowanceController.onPageLoad(NormalMode)
        case Benefits.CARERS_ALLOWANCE => routes.HowMuchCarersAllowanceController.onPageLoad(NormalMode)
        case Benefits.JOBSEEKERS_ALLOWANCE => routes.HowMuchJobseekersAllowanceController.onPageLoad(NormalMode)
        case Benefits.INCAPACITY_BENEFIT => routes.HowMuchIncapacityBenefitController.onPageLoad(NormalMode)
        case Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE => routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(NormalMode)
        case Benefits.STATE_PENSION => routes.HowMuchStatePensionController.onPageLoad(NormalMode)
        case Benefits.OTHER_TAXABLE_BENEFIT => routes.OtherBenefitsNameController.onPageLoad(NormalMode)
        case _ => routes.SessionExpiredController.onPageLoad()
      }
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def selectTaxableIncome(userAnswers: UserAnswers): Call = userAnswers.selectTaxableIncome match {
    case Some(taxableIncome) =>
      taxableIncome.head match {
        case TaxableIncome.RENTAL_INCOME => routes.HowMuchRentalIncomeController.onPageLoad(NormalMode)
        case TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST => routes.HowMuchBankInterestController.onPageLoad(NormalMode)
        case TaxableIncome.INVESTMENT_OR_DIVIDENDS => routes.HowMuchInvestmentOrDividendController.onPageLoad(NormalMode)
        case TaxableIncome.FOREIGN_INCOME => routes.HowMuchForeignIncomeController.onPageLoad(NormalMode)
        case TaxableIncome.OTHER_TAXABLE_INCOME => routes.OtherTaxableIncomeNameController.onPageLoad(NormalMode)
        case _ => routes.SessionExpiredController.onPageLoad()
      }
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def benefitRouter(currentPageId: String)(userAnswers: UserAnswers): Call = userAnswers.selectBenefits match {
    case Some(benefits) =>
      val nextPageIndex: Int = (benefits.map(_.toString) indexOf currentPageId) + 1

      if (nextPageIndex < benefits.length) {
        benefits(nextPageIndex) match {
          case Benefits.CARERS_ALLOWANCE => routes.HowMuchCarersAllowanceController.onPageLoad(NormalMode)
          case Benefits.JOBSEEKERS_ALLOWANCE => routes.HowMuchJobseekersAllowanceController.onPageLoad(NormalMode)
          case Benefits.INCAPACITY_BENEFIT => routes.HowMuchIncapacityBenefitController.onPageLoad(NormalMode)
          case Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE => routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(NormalMode)
          case Benefits.STATE_PENSION => routes.HowMuchStatePensionController.onPageLoad(NormalMode)
          case Benefits.OTHER_TAXABLE_BENEFIT => routes.OtherBenefitsNameController.onPageLoad(NormalMode)
          case _ => routes.SessionExpiredController.onPageLoad()
        }
      } else {
        routes.AnyCompanyBenefitsController.onPageLoad(NormalMode)
      }
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyCompanyBenefits(userAnswers: UserAnswers): Call = userAnswers.anyCompanyBenefits match {
    case Some(true) => routes.SelectCompanyBenefitsController.onPageLoad(NormalMode)
    case Some(false) => routes.AnyTaxableIncomeController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }
  private def taxableIncomeRouter(currentPageId: String)(userAnswers: UserAnswers): Call = userAnswers.selectTaxableIncome match {
    case Some(taxableIncome) =>
      val nextPageIndex: Int = (taxableIncome.map(_.toString) indexOf currentPageId) + 1

      if (nextPageIndex < taxableIncome.length) {
        taxableIncome(nextPageIndex) match {
          case TaxableIncome.RENTAL_INCOME => routes.HowMuchRentalIncomeController.onPageLoad(NormalMode)
          case TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST => routes.HowMuchBankInterestController.onPageLoad(NormalMode)
          case TaxableIncome.INVESTMENT_OR_DIVIDENDS => routes.HowMuchInvestmentOrDividendController.onPageLoad(NormalMode)
          case TaxableIncome.FOREIGN_INCOME => routes.HowMuchForeignIncomeController.onPageLoad(NormalMode)
          case TaxableIncome.OTHER_TAXABLE_INCOME => routes.OtherTaxableIncomeNameController.onPageLoad(NormalMode)
          case _ => routes.SessionExpiredController.onPageLoad()
        }
      } else {
        routes.WhereToSendPaymentController.onPageLoad(NormalMode)
      }
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def selectCompanyBenefits(userAnswers: UserAnswers): Call = userAnswers.selectCompanyBenefits match {
    case Some(benefits) =>
      benefits.head match {
        case CompanyBenefits.COMPANY_CAR_BENEFIT => routes.HowMuchCarBenefitsController.onPageLoad(NormalMode)
        case CompanyBenefits.FUEL_BENEFIT => routes.HowMuchFuelBenefitController.onPageLoad(NormalMode)
        case CompanyBenefits.MEDICAL_BENEFIT => routes.HowMuchMedicalBenefitsController.onPageLoad(NormalMode)
        case CompanyBenefits.OTHER_COMPANY_BENEFIT => routes.OtherCompanyBenefitsNameController.onPageLoad(NormalMode)
        case _ => routes.SessionExpiredController.onPageLoad()
      }
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def companyBenefitRouter(currentPageId: String)(userAnswers: UserAnswers): Call = userAnswers.selectCompanyBenefits match {
    case Some(benefits) =>
      val nextPageIndex: Int = (benefits.map(_.toString) indexOf currentPageId) + 1

      if (nextPageIndex < benefits.length) {
        benefits(nextPageIndex) match {
          case CompanyBenefits.FUEL_BENEFIT => routes.HowMuchFuelBenefitController.onPageLoad(NormalMode)
          case CompanyBenefits.MEDICAL_BENEFIT => routes.HowMuchMedicalBenefitsController.onPageLoad(NormalMode)
          case CompanyBenefits.OTHER_COMPANY_BENEFIT => routes.OtherCompanyBenefitsNameController.onPageLoad(NormalMode)
          case _ => routes.SessionExpiredController.onPageLoad()
        }
      } else {
        routes.AnyTaxableIncomeController.onPageLoad(NormalMode)
      }
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyOtherBenefits(userAnswers: UserAnswers): Call = userAnswers.anyOtherBenefits match {
    case Some(true) => routes.OtherBenefitsNameController.onPageLoad(NormalMode)
    case Some(false) => routes.AnyCompanyBenefitsController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyOtherCompanyBenefits(userAnswers: UserAnswers): Call = userAnswers.anyOtherCompanyBenefits match {
    case Some(true) => routes.OtherCompanyBenefitsNameController.onPageLoad(NormalMode)
    case Some(false) => routes.AnyTaxableIncomeController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def otherTaxableIncome(userAnswers: UserAnswers): Call = userAnswers.anyTaxableIncome match {
    case Some(true) => routes.SelectTaxableIncomeController.onPageLoad(NormalMode)
    case Some(false) => routes.WhereToSendPaymentController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyOtherTaxableIncome(userAnswers: UserAnswers): Call = userAnswers.anyOtherTaxableIncome match {
    case Some(true) => routes.OtherTaxableIncomeNameController.onPageLoad(NormalMode)
    case Some(false) => routes.WhereToSendPaymentController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def whereToSendPayment(userAnswers: UserAnswers): Call = userAnswers.whereToSendPayment match {
    case Some(Nominee) => routes.NomineeFullNameController.onPageLoad(NormalMode)
    case Some(Myself) => routes.PaymentAddressCorrectController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def whereToSendPaymentCheck(userAnswers: UserAnswers): Call = userAnswers.whereToSendPayment match {
    case Some(Nominee) => routes.NomineeFullNameController.onPageLoad(CheckMode)
    case Some(Myself) => routes.PaymentAddressCorrectController.onPageLoad(CheckMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyAgentRef(userAnswers: UserAnswers): Option[Call] = userAnswers.anyAgentRef map {
    case AnyAgentRef.Yes(agentRef) => routes.IsPaymentAddressInTheUKController.onPageLoad(NormalMode)
    case AnyAgentRef.No => routes.IsPaymentAddressInTheUKController.onPageLoad(NormalMode)
    case _ => routes.SessionExpiredController.onPageLoad()
  }

  private def isPaymentAddressInUkRoute(userAnswers: UserAnswers): Call = userAnswers.isPaymentAddressInTheUK match {
    case Some(true) => routes.PaymentUKAddressController.onPageLoad(NormalMode)
    case Some(false) => routes.PaymentInternationalAddressController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def isPaymentAddressInUkRouteCheck(userAnswers: UserAnswers): Call = userAnswers.isPaymentAddressInTheUK match {
    case Some(true) => routes.PaymentUKAddressController.onPageLoad(CheckMode)
    case Some(false) => routes.PaymentInternationalAddressController.onPageLoad(CheckMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }


  private def paymentAddressCorrect(userAnswers: UserAnswers): Call = userAnswers.paymentAddressCorrect match {
    case Some(true) => routes.TelephoneNumberController.onPageLoad(NormalMode)
    case Some(false) => routes.IsPaymentAddressInTheUKController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }


  private def paymentAddressCorrectCheck(userAnswers: UserAnswers): Call = userAnswers.paymentAddressCorrect match {
    case Some(true) => routes.TelephoneNumberController.onPageLoad(CheckMode)
    case Some(false) => routes.IsPaymentAddressInTheUKController.onPageLoad(CheckMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  def nextPage(id: Identifier, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode =>
      routeMap.getOrElse(id, _ => routes.IndexController.onPageLoad())
    case CheckMode =>
      editRouteMap.getOrElse(id, _ => routes.CheckYourAnswersController.onPageLoad())
  }
}
