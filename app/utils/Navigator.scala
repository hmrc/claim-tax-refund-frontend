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
    SelectBenefitsId -> selectBenefits(NormalMode),
    HowMuchBereavementAllowanceId -> benefitRouter(HowMuchBereavementAllowanceId.cyaId, NormalMode),
    HowMuchCarersAllowanceId -> benefitRouter(HowMuchCarersAllowanceId.cyaId, NormalMode),
    HowMuchJobseekersAllowanceId -> benefitRouter(HowMuchJobseekersAllowanceId.cyaId, NormalMode),
    HowMuchIncapacityBenefitId -> benefitRouter(HowMuchIncapacityBenefitId.cyaId, NormalMode),
    HowMuchEmploymentAndSupportAllowanceId -> benefitRouter(HowMuchEmploymentAndSupportAllowanceId.cyaId, NormalMode),
    HowMuchStatePensionId -> benefitRouter(HowMuchStatePensionId.cyaId, NormalMode),
    AnyOtherBenefitsId -> anyOtherBenefits,
    AnyCompanyBenefitsId -> anyCompanyBenefits,
    SelectCompanyBenefitsId -> selectedCompanyBenefitsCheck(NormalMode),
    HowMuchCarBenefitsId -> selectedCompanyBenefitsCheck(NormalMode),
    HowMuchFuelBenefitId -> selectedCompanyBenefitsCheck(NormalMode),
    HowMuchMedicalBenefitsId -> selectedCompanyBenefitsCheck(NormalMode),
    AnyTaxableIncomeId -> otherTaxableIncome,
    SelectTaxableIncomeId -> selectTaxableIncome,
    HowMuchRentalIncomeId -> (_ => routes.AnyTaxableRentalIncomeController.onPageLoad(NormalMode)),
    AnyTaxableRentalIncomeId -> taxableIncomeRouter(HowMuchRentalIncomeId.cyaId),
    HowMuchBankInterestId -> (_ => routes.AnyTaxableBankInterestController.onPageLoad(NormalMode)),
    AnyTaxableBankInterestId -> taxableIncomeRouter(HowMuchBankInterestId.cyaId),
    HowMuchInvestmentOrDividendId -> (_ => routes.AnyTaxableInvestmentsController.onPageLoad(NormalMode)),
    AnyTaxableInvestmentsId -> taxableIncomeRouter(HowMuchInvestmentOrDividendId.cyaId),
    HowMuchForeignIncomeId -> (_ => routes.AnyTaxableForeignIncomeController.onPageLoad(NormalMode)),
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
    EmploymentDetailsId -> employmentDetailsCheck,
    EnterPayeReferenceId -> (_ => routes.DetailsOfEmploymentOrPensionController.onPageLoad(CheckMode)),
    DetailsOfEmploymentOrPensionId -> (_ => routes.CheckYourAnswersController.onPageLoad()),
    AnyAgentRefId -> (_ => routes.IsPaymentAddressInTheUKController.onPageLoad(CheckMode)),
    NomineeFullNameId -> (_ => routes.AnyAgentRefController.onPageLoad(CheckMode)),
    WhereToSendPaymentId -> whereToSendPaymentCheck,
    IsPaymentAddressInTheUKId -> isPaymentAddressInUkRouteCheck,
    PaymentAddressCorrectId -> paymentAddressCorrectCheck,
    AnyBenefitsId -> anyBenefitsCheck,
    SelectBenefitsId -> selectBenefits(CheckMode),
    HowMuchBereavementAllowanceId -> benefitRouter(HowMuchBereavementAllowanceId.cyaId, CheckMode),
    HowMuchCarersAllowanceId -> benefitRouter(HowMuchCarersAllowanceId.cyaId, CheckMode),
    HowMuchJobseekersAllowanceId -> benefitRouter(HowMuchJobseekersAllowanceId.cyaId, CheckMode),
    HowMuchIncapacityBenefitId -> benefitRouter(HowMuchIncapacityBenefitId.cyaId, CheckMode),
    HowMuchEmploymentAndSupportAllowanceId -> benefitRouter(HowMuchEmploymentAndSupportAllowanceId.cyaId, CheckMode),
    HowMuchStatePensionId -> benefitRouter(HowMuchStatePensionId.cyaId, CheckMode),
    SelectCompanyBenefitsId -> selectedCompanyBenefitsCheck(CheckMode),
    HowMuchCarBenefitsId -> selectedCompanyBenefitsCheck(CheckMode),
    HowMuchFuelBenefitId -> selectedCompanyBenefitsCheck(CheckMode),
    HowMuchMedicalBenefitsId -> selectedCompanyBenefitsCheck(CheckMode),
    OtherCompanyBenefitsNameId -> howMuchOtherCompanyBenefitsCheck,
    HowMuchOtherCompanyBenefitId -> (_ => routes.CheckYourAnswersController.onPageLoad()),
    AnyOtherCompanyBenefitsId -> anyOtherCompanyBenefitsCheck
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

  private def anyBenefitsCheck(userAnswers: UserAnswers): Call = userAnswers.anyBenefits match {
    case Some(true) if userAnswers.selectBenefits.isEmpty => routes.SelectBenefitsController.onPageLoad(CheckMode)
    case Some(true) => routes.CheckYourAnswersController.onPageLoad()
    case Some(false) => routes.CheckYourAnswersController.onPageLoad()
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def selectBenefits(mode: Mode)(userAnswers: UserAnswers): Call = userAnswers.selectBenefits match {
    case Some(benefits) =>
      if (benefits.contains(Benefits.BEREAVEMENT_ALLOWANCE) && (userAnswers.howMuchBereavementAllowance.isEmpty || mode == NormalMode)) {
        routes.HowMuchBereavementAllowanceController.onPageLoad(mode)
      } else if (benefits.contains(Benefits.CARERS_ALLOWANCE) && (userAnswers.howMuchCarersAllowance.isEmpty || mode == NormalMode)) {
        routes.HowMuchCarersAllowanceController.onPageLoad(mode)
      } else if (benefits.contains(Benefits.JOBSEEKERS_ALLOWANCE) && (userAnswers.howMuchJobseekersAllowance.isEmpty || mode == NormalMode)) {
        routes.HowMuchJobseekersAllowanceController.onPageLoad(mode)
      } else if (benefits.contains(Benefits.INCAPACITY_BENEFIT) && (userAnswers.howMuchIncapacityBenefit.isEmpty || mode == NormalMode)) {
        routes.HowMuchIncapacityBenefitController.onPageLoad(mode)
      } else if (benefits.contains(Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE) && (userAnswers.howMuchEmploymentAndSupportAllowance.isEmpty || mode == NormalMode)) {
        routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(mode)
      } else if (benefits.contains(Benefits.STATE_PENSION) && (userAnswers.howMuchStatePension.isEmpty || mode == NormalMode)) {
        routes.HowMuchStatePensionController.onPageLoad(mode)
      } else if (benefits.contains(Benefits.OTHER_TAXABLE_BENEFIT) && ((mode == CheckMode && userAnswers.howMuchOtherTaxableIncome.isEmpty) || mode == NormalMode)) {
        routes.OtherBenefitsNameController.onPageLoad(mode)
      } else {
        routes.CheckYourAnswersController.onPageLoad()
      }
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def benefitRouter(currentPageId: String, mode: Mode)(userAnswers: UserAnswers): Call = userAnswers.selectBenefits match {

    case Some(benefits) =>
      val nextPageIndex: Int = (benefits.map(_.toString) indexOf currentPageId) + 1

      if (nextPageIndex < benefits.length) {

        benefits(nextPageIndex) match {
          case Benefits.CARERS_ALLOWANCE if userAnswers.howMuchCarersAllowance.isEmpty || mode == NormalMode => routes.HowMuchCarersAllowanceController.onPageLoad(mode)
          case Benefits.JOBSEEKERS_ALLOWANCE if userAnswers.howMuchJobseekersAllowance.isEmpty || mode == NormalMode => routes.HowMuchJobseekersAllowanceController.onPageLoad(mode)
          case Benefits.INCAPACITY_BENEFIT if userAnswers.howMuchIncapacityBenefit.isEmpty || mode == NormalMode => routes.HowMuchIncapacityBenefitController.onPageLoad(mode)
          case Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE if userAnswers.howMuchEmploymentAndSupportAllowance.isEmpty || mode == NormalMode => routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(mode)
          case Benefits.STATE_PENSION if userAnswers.howMuchStatePension.isEmpty || mode == NormalMode => routes.HowMuchStatePensionController.onPageLoad(mode)
          case Benefits.OTHER_TAXABLE_BENEFIT if userAnswers.howMuchOtherTaxableIncome.isEmpty || mode == NormalMode => routes.OtherBenefitsNameController.onPageLoad(mode)
          case _ if mode == NormalMode => routes.SessionExpiredController.onPageLoad()
          case _ => routes.CheckYourAnswersController.onPageLoad()
        }
      } else  if (mode == NormalMode) {
        routes.AnyCompanyBenefitsController.onPageLoad(mode)
      } else {
        routes.CheckYourAnswersController.onPageLoad()
      }
    case None if mode == NormalMode => routes.SessionExpiredController.onPageLoad()
    case None => routes.CheckYourAnswersController.onPageLoad()
  }

  private def anyCompanyBenefits(userAnswers: UserAnswers): Call = userAnswers.anyCompanyBenefits match {
    case Some(true) => routes.SelectCompanyBenefitsController.onPageLoad(NormalMode)
    case Some(false) => routes.AnyTaxableIncomeController.onPageLoad(NormalMode)
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

  private def selectedCompanyBenefitsCheck(mode: Mode)(userAnswers: UserAnswers): Call = userAnswers.selectCompanyBenefits match {
    case Some(benefits) =>
      if (benefits.contains(CompanyBenefits.COMPANY_CAR_BENEFIT) && userAnswers.howMuchCarBenefits.isEmpty) {
        routes.HowMuchCarBenefitsController.onPageLoad(mode)
      } else if (benefits.contains(CompanyBenefits.FUEL_BENEFIT) && userAnswers.howMuchFuelBenefit.isEmpty) {
        routes.HowMuchFuelBenefitController.onPageLoad(mode)
      } else if (benefits.contains(CompanyBenefits.MEDICAL_BENEFIT) && userAnswers.howMuchMedicalBenefits.isEmpty) {
        routes.HowMuchMedicalBenefitsController.onPageLoad(mode)
      } else if (benefits.contains(CompanyBenefits.OTHER_COMPANY_BENEFIT) && userAnswers.otherCompanyBenefitsName.isEmpty) {
        routes.OtherCompanyBenefitsNameController.onPageLoad(mode)
      } else {
        if (mode == NormalMode) routes.AnyTaxableIncomeController.onPageLoad(mode) else routes.CheckYourAnswersController.onPageLoad()
      }
    case None =>
      routes.SessionExpiredController.onPageLoad()
  }

  private def howMuchOtherCompanyBenefitsCheck(userAnswers: UserAnswers): Call = userAnswers.howMuchOtherCompanyBenefit match {
    case Some(amount) => routes.CheckYourAnswersController.onPageLoad()
    case None => routes.HowMuchOtherCompanyBenefitController.onPageLoad(CheckMode)
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

  private def anyOtherCompanyBenefitsCheck(userAnswers: UserAnswers): Call = userAnswers.anyOtherCompanyBenefits match {
    case Some(true) => routes.OtherCompanyBenefitsNameController.onPageLoad(CheckMode)
    case Some(false) => routes.CheckYourAnswersController.onPageLoad()
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

