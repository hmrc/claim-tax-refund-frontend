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
import identifiers.{AnyAgentRefId, _}
import javax.inject.{Inject, Singleton}

import models.WhereToSendPayment.{Myself, Nominee}
import models.{Benefits, _}
import play.api.mvc.Call

@Singleton
class Navigator @Inject()() {

  private val routeMapWithIndex: PartialFunction[Identifier, UserAnswers => Call] = {
    case OtherBenefitId(index) => otherBenefitsName(NormalMode)
  }

  private val editRouteMapWithIndex: PartialFunction[Identifier, UserAnswers => Call] = {
    case OtherBenefitId(index) => otherBenefitsName(CheckMode)
  }

  private val routeMap: Map[Identifier, UserAnswers => Call] = Map(
    //Claim details
    SelectTaxYearId -> (_ => routes.EmploymentDetailsController.onPageLoad(NormalMode)),
    EmploymentDetailsId -> employmentDetails,
    EnterPayeReferenceId -> (_ => routes.DetailsOfEmploymentOrPensionController.onPageLoad(NormalMode)),
    DetailsOfEmploymentOrPensionId -> (_ => routes.AnyBenefitsController.onPageLoad(NormalMode)),
    //Benefits
    AnyBenefitsId -> anyBenefits(NormalMode),
    SelectBenefitsId -> selectBenefits(NormalMode),
    HowMuchBereavementAllowanceId -> selectBenefits(NormalMode),
    HowMuchCarersAllowanceId -> selectBenefits(NormalMode),
    HowMuchJobseekersAllowanceId -> selectBenefits(NormalMode),
    HowMuchIncapacityBenefitId -> selectBenefits(NormalMode),
    HowMuchEmploymentAndSupportAllowanceId -> selectBenefits(NormalMode),
    HowMuchStatePensionId -> selectBenefits(NormalMode),
    //OtherBenefitId(index) -> otherBenefit(),
    AnyOtherBenefitsId -> anyOtherBenefits,
    //Company benefits
    AnyCompanyBenefitsId -> anyCompanyBenefits(NormalMode),
    SelectCompanyBenefitsId -> selectedCompanyBenefitsCheck(NormalMode),
    HowMuchCarBenefitsId -> selectedCompanyBenefitsCheck(NormalMode),
    HowMuchFuelBenefitId -> selectedCompanyBenefitsCheck(NormalMode),
    HowMuchMedicalBenefitsId -> selectedCompanyBenefitsCheck(NormalMode),
    OtherCompanyBenefitsNameId -> (_ => routes.HowMuchOtherCompanyBenefitController.onPageLoad(NormalMode)),
    HowMuchOtherCompanyBenefitId -> (_ => routes.AnyOtherCompanyBenefitsController.onPageLoad(NormalMode)),
    AnyOtherCompanyBenefitsId -> anyOtherCompanyBenefits,
    //Taxable income
    AnyTaxableIncomeId -> anyTaxableIncome(NormalMode),
    SelectTaxableIncomeId -> selectedTaxableIncomeCheck(NormalMode),
    HowMuchRentalIncomeId -> (_ => routes.AnyTaxableRentalIncomeController.onPageLoad(NormalMode)),
    AnyTaxableRentalIncomeId -> selectedTaxableIncomeCheck(NormalMode),
    HowMuchBankInterestId -> (_ => routes.AnyTaxableBankInterestController.onPageLoad(NormalMode)),
    AnyTaxableBankInterestId -> selectedTaxableIncomeCheck(NormalMode),
    HowMuchInvestmentsId -> (_ => routes.AnyTaxableInvestmentsController.onPageLoad(NormalMode)),
    AnyTaxableInvestmentsId -> selectedTaxableIncomeCheck(NormalMode),
    HowMuchForeignIncomeId -> (_ => routes.AnyTaxableForeignIncomeController.onPageLoad(NormalMode)),
    AnyTaxableForeignIncomeId -> selectedTaxableIncomeCheck(NormalMode),
    OtherTaxableIncomeNameId -> (_ => routes.HowMuchOtherTaxableIncomeController.onPageLoad(NormalMode)),
    HowMuchOtherTaxableIncomeId -> (_ => routes.AnyTaxableOtherIncomeController.onPageLoad(NormalMode)),
    AnyTaxableOtherIncomeId -> (_ => routes.AnyOtherTaxableIncomeController.onPageLoad(NormalMode)),
    AnyOtherTaxableIncomeId -> anyOtherTaxableIncome,
    //Payment
    WhereToSendPaymentId -> whereToSendPayment,
    NomineeFullNameId -> (_ => routes.AnyAgentRefController.onPageLoad(NormalMode)),
    AnyAgentRefId -> (_ => routes.IsPaymentAddressInTheUKController.onPageLoad(NormalMode)),
    PaymentAddressCorrectId -> paymentAddressCorrect,
    IsPaymentAddressInTheUKId -> isPaymentAddressInUk,
    PaymentUKAddressId -> (_ => routes.TelephoneNumberController.onPageLoad(NormalMode)),
    PaymentInternationalAddressId -> (_ => routes.TelephoneNumberController.onPageLoad(NormalMode)),
    TelephoneNumberId -> (_ => routes.CheckYourAnswersController.onPageLoad())
  )

  private val editRouteMap: Map[Identifier, UserAnswers => Call] = Map(
    //Claim details
    EmploymentDetailsId -> employmentDetailsCheck,
    EnterPayeReferenceId -> detailsOfEmploymentCheck,
    //Benefits
    AnyBenefitsId -> anyBenefits(CheckMode),
    SelectBenefitsId -> selectBenefits(CheckMode),
    HowMuchBereavementAllowanceId -> selectBenefits(CheckMode),
    HowMuchCarersAllowanceId -> selectBenefits(CheckMode),
    HowMuchJobseekersAllowanceId -> selectBenefits(CheckMode),
    HowMuchIncapacityBenefitId -> selectBenefits(CheckMode),
    HowMuchEmploymentAndSupportAllowanceId -> selectBenefits(CheckMode),
    HowMuchStatePensionId -> selectBenefits(CheckMode),
    //Company Benefits
    AnyCompanyBenefitsId -> anyCompanyBenefits(CheckMode),
    SelectCompanyBenefitsId -> selectedCompanyBenefitsCheck(CheckMode),
    HowMuchCarBenefitsId -> selectedCompanyBenefitsCheck(CheckMode),
    HowMuchFuelBenefitId -> selectedCompanyBenefitsCheck(CheckMode),
    HowMuchMedicalBenefitsId -> selectedCompanyBenefitsCheck(CheckMode),
    OtherCompanyBenefitsNameId -> howMuchOtherCompanyBenefitsCheck,
    //Taxable Income
    AnyTaxableIncomeId -> anyTaxableIncome(CheckMode),
    SelectTaxableIncomeId -> selectedTaxableIncomeCheck(CheckMode),
    HowMuchRentalIncomeId -> howMuchRentalIncomeCheck,
    AnyTaxableRentalIncomeId -> selectedTaxableIncomeCheck(CheckMode),
    HowMuchBankInterestId -> howMuchBankInterestCheck,
    AnyTaxableBankInterestId -> selectedTaxableIncomeCheck(CheckMode),
    HowMuchInvestmentsId -> howMuchInvestmentOrDividendsCheck,
    AnyTaxableInvestmentsId -> selectedTaxableIncomeCheck(CheckMode),
    HowMuchForeignIncomeId -> howMuchForeignIncomeCheck,
    AnyTaxableForeignIncomeId -> selectedTaxableIncomeCheck(CheckMode),
    OtherTaxableIncomeNameId -> howMuchOtherTaxableIncomeCheck,
    HowMuchOtherTaxableIncomeId -> anyTaxableOtherIncomeCheck,
    //Payment
    WhereToSendPaymentId -> whereToSendPaymentCheck,
    PaymentAddressCorrectId -> paymentAddressCorrectCheck,
    NomineeFullNameId -> anyAgentRefCheck,
    AnyAgentRefId -> isPaymentAddressInUkCheck,
    IsPaymentAddressInTheUKId -> paymentAddressCheck
  )


  //Claim Details-----------------------------

  private def employmentDetails(userAnswers: UserAnswers): Call = userAnswers.employmentDetails match {
    case Some(true) => routes.AnyBenefitsController.onPageLoad(NormalMode)
    case Some(false) => routes.EnterPayeReferenceController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def employmentDetailsCheck(userAnswers: UserAnswers): Call = userAnswers.employmentDetails match {
    case Some(true) => routes.CheckYourAnswersController.onPageLoad()
    case Some(false) => userAnswers.enterPayeReference match {
      case None => routes.EnterPayeReferenceController.onPageLoad(CheckMode)
      case _ => routes.CheckYourAnswersController.onPageLoad()
    }
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def detailsOfEmploymentCheck(userAnswers: UserAnswers): Call = userAnswers.detailsOfEmploymentOrPension match {
    case None => routes.DetailsOfEmploymentOrPensionController.onPageLoad(CheckMode)
    case _ => routes.CheckYourAnswersController.onPageLoad()
  }


  //Benefits----------------------------------

  private def anyBenefits(mode: Mode)(userAnswers: UserAnswers): Call = userAnswers.anyBenefits match {
    case Some(true) =>
      routes.SelectBenefitsController.onPageLoad(mode)
    case Some(false) =>
      if(mode == NormalMode) routes.AnyCompanyBenefitsController.onPageLoad(mode) else routes.CheckYourAnswersController.onPageLoad()
    case None =>
      routes.SessionExpiredController.onPageLoad()
  }

  private def selectBenefits(mode: Mode)(userAnswers: UserAnswers): Call = userAnswers.selectBenefits match {
    case Some(benefits) =>
      if (benefits.contains(Benefits.BEREAVEMENT_ALLOWANCE) && userAnswers.howMuchBereavementAllowance.isEmpty) {
        routes.HowMuchBereavementAllowanceController.onPageLoad(mode)
      } else if (benefits.contains(Benefits.CARERS_ALLOWANCE) && userAnswers.howMuchCarersAllowance.isEmpty) {
        routes.HowMuchCarersAllowanceController.onPageLoad(mode)
      } else if (benefits.contains(Benefits.JOBSEEKERS_ALLOWANCE) && userAnswers.howMuchJobseekersAllowance.isEmpty) {
        routes.HowMuchJobseekersAllowanceController.onPageLoad(mode)
      } else if (benefits.contains(Benefits.INCAPACITY_BENEFIT) && userAnswers.howMuchIncapacityBenefit.isEmpty) {
        routes.HowMuchIncapacityBenefitController.onPageLoad(mode)
      } else if (benefits.contains(Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE) && userAnswers.howMuchEmploymentAndSupportAllowance.isEmpty) {
        routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(mode)
      } else if (benefits.contains(Benefits.STATE_PENSION) && userAnswers.howMuchStatePension.isEmpty) {
        routes.HowMuchStatePensionController.onPageLoad(mode)
      } else if (benefits.contains(Benefits.OTHER_TAXABLE_BENEFIT)) {
        routes.OtherBenefitController.onPageLoad(mode, Index(0))
      } else {
        if (mode == NormalMode) routes.AnyCompanyBenefitsController.onPageLoad(mode) else routes.CheckYourAnswersController.onPageLoad()
      }
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyOtherBenefits(userAnswers: UserAnswers): Call = userAnswers.anyOtherBenefits match {
    case Some(true) => routes.OtherBenefitController.onPageLoad(NormalMode, Index(userAnswers.otherBenefit.get.length))
    case Some(false) => routes.AnyCompanyBenefitsController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  def otherBenefitsName(mode: Mode)(userAnswers: UserAnswers): Call =
    if (mode == NormalMode) routes.AnyOtherBenefitsController.onPageLoad(mode) else routes.CheckYourAnswersController.onPageLoad()


  //Company benefits--------------------------

  private def anyCompanyBenefits(mode: Mode)(userAnswers: UserAnswers): Call = userAnswers.anyCompanyBenefits match {
    case Some(true) =>
      userAnswers.selectCompanyBenefits match {
        case None => routes.SelectCompanyBenefitsController.onPageLoad(mode)
        case _ => selectedCompanyBenefitsCheck(mode)(userAnswers)
      }
    case Some(false) =>
      if(mode == NormalMode) routes.AnyTaxableIncomeController.onPageLoad(mode) else routes.CheckYourAnswersController.onPageLoad()
    case None =>
      routes.SessionExpiredController.onPageLoad()
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

  private def anyOtherCompanyBenefits(userAnswers: UserAnswers): Call = userAnswers.anyOtherCompanyBenefits match {
    case Some(true) => routes.OtherCompanyBenefitsNameController.onPageLoad(NormalMode)
    case Some(false) => routes.AnyTaxableIncomeController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def howMuchOtherCompanyBenefitsCheck(userAnswers: UserAnswers): Call = userAnswers.howMuchOtherCompanyBenefit match {
    case None => routes.HowMuchOtherCompanyBenefitController.onPageLoad(CheckMode)
    case _ => routes.CheckYourAnswersController.onPageLoad()
  }


  //Taxable income--------------------------

  private def anyTaxableIncome(mode: Mode)(userAnswers: UserAnswers): Call = userAnswers.anyTaxableIncome match {
    case Some(true) =>
      userAnswers.selectTaxableIncome match {
        case None => routes.SelectTaxableIncomeController.onPageLoad(mode)
        case _ => selectedTaxableIncomeCheck(mode)(userAnswers)
      }
    case Some(false) =>
      if(mode == NormalMode) routes.WhereToSendPaymentController.onPageLoad(NormalMode) else routes.CheckYourAnswersController.onPageLoad()
    case None =>
      routes.SessionExpiredController.onPageLoad()
  }

  private def selectedTaxableIncomeCheck(mode: Mode)(userAnswers: UserAnswers): Call = userAnswers.selectTaxableIncome match {
    case Some(taxableIncome) =>
      if (taxableIncome.contains(TaxableIncome.RENTAL_INCOME) && userAnswers.howMuchRentalIncome.isEmpty) {
        routes.HowMuchRentalIncomeController.onPageLoad(mode)
      } else if (taxableIncome.contains(TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST) && userAnswers.howMuchBankInterest.isEmpty) {
        routes.HowMuchBankInterestController.onPageLoad(mode)
      } else if (taxableIncome.contains(TaxableIncome.INVESTMENT_OR_DIVIDENDS) && userAnswers.howMuchInvestmentOrDividend.isEmpty) {
        routes.HowMuchInvestmentOrDividendController.onPageLoad(mode)
      } else if (taxableIncome.contains(TaxableIncome.FOREIGN_INCOME) && userAnswers.howMuchForeignIncome.isEmpty) {
        routes.HowMuchForeignIncomeController.onPageLoad(mode)
      } else if (taxableIncome.contains(TaxableIncome.OTHER_TAXABLE_INCOME) && userAnswers.otherTaxableIncomeName.isEmpty) {
        routes.OtherTaxableIncomeNameController.onPageLoad(mode)
      } else {
        if (mode == NormalMode) routes.WhereToSendPaymentController.onPageLoad(mode) else routes.CheckYourAnswersController.onPageLoad()
      }
    case None =>
      routes.SessionExpiredController.onPageLoad()
  }

  private def howMuchRentalIncomeCheck(userAnswers: UserAnswers): Call = userAnswers.howMuchRentalIncome match {
    case None => routes.HowMuchRentalIncomeController.onPageLoad(CheckMode)
    case _ => userAnswers.anyTaxableRentalIncome match {
      case None => routes.AnyTaxableRentalIncomeController.onPageLoad(CheckMode)
      case _ => routes.CheckYourAnswersController.onPageLoad()
    }
  }

  private def howMuchBankInterestCheck(userAnswers: UserAnswers): Call = userAnswers.howMuchBankInterest match {
    case None => routes.HowMuchBankInterestController.onPageLoad(CheckMode)
    case _ => userAnswers.anyTaxableBankInterest match {
      case None => routes.AnyTaxableBankInterestController.onPageLoad(CheckMode)
      case _ => routes.CheckYourAnswersController.onPageLoad()
    }
  }

  private def howMuchInvestmentOrDividendsCheck(userAnswers: UserAnswers): Call = userAnswers.howMuchInvestmentOrDividend match {
    case None => routes.HowMuchInvestmentOrDividendController.onPageLoad(CheckMode)
    case _ => userAnswers.anyTaxableInvestments match {
      case None => routes.AnyTaxableInvestmentsController.onPageLoad(CheckMode)
      case _ => routes.CheckYourAnswersController.onPageLoad()
    }
  }

  private def howMuchForeignIncomeCheck(userAnswers: UserAnswers): Call = userAnswers.howMuchForeignIncome match {
    case None => routes.HowMuchForeignIncomeController.onPageLoad(CheckMode)
    case _ => userAnswers.anyTaxableForeignIncome match {
      case None => routes.AnyTaxableForeignIncomeController.onPageLoad(CheckMode)
      case _ => routes.CheckYourAnswersController.onPageLoad()
    }
  }

  private def anyOtherTaxableIncome(userAnswers: UserAnswers): Call = userAnswers.anyOtherTaxableIncome match {
    case Some(true) => routes.OtherTaxableIncomeNameController.onPageLoad(NormalMode)
    case Some(false) => routes.WhereToSendPaymentController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def howMuchOtherTaxableIncomeCheck(userAnswers: UserAnswers): Call = userAnswers.howMuchOtherTaxableIncome match {
    case None => routes.HowMuchOtherTaxableIncomeController.onPageLoad(CheckMode)
    case _ => userAnswers.anyTaxableOtherIncome match {
      case None => routes.AnyTaxableOtherIncomeController.onPageLoad(CheckMode)
      case _ => routes.CheckYourAnswersController.onPageLoad()
    }
  }

  private def anyTaxableOtherIncomeCheck(userAnswers: UserAnswers): Call = userAnswers.anyTaxableOtherIncome match {
    case None => routes.AnyTaxableOtherIncomeController.onPageLoad(CheckMode)
    case _ => routes.CheckYourAnswersController.onPageLoad()
  }


  //Payment----------------------------

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

  private def anyAgentRefCheck(userAnswers: UserAnswers): Call = userAnswers.anyAgentRef match {
    case None => routes.AnyAgentRefController.onPageLoad(CheckMode)
    case _ => routes.CheckYourAnswersController.onPageLoad()
  }

  private def isPaymentAddressInUk(userAnswers: UserAnswers): Call = userAnswers.isPaymentAddressInTheUK match {
    case Some(true) => routes.PaymentUKAddressController.onPageLoad(NormalMode)
    case Some(false) => routes.PaymentInternationalAddressController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def isPaymentAddressInUkCheck(userAnswers: UserAnswers): Call = userAnswers.isPaymentAddressInTheUK match {
    case None => routes.IsPaymentAddressInTheUKController.onPageLoad(CheckMode)
    case _ => routes.CheckYourAnswersController.onPageLoad()
  }

  private def paymentAddressCheck(userAnswers: UserAnswers): Call = userAnswers.isPaymentAddressInTheUK match {
    case Some(true) => userAnswers.paymentUKAddress match {
      case None => routes.PaymentUKAddressController.onPageLoad(CheckMode)
      case _ => routes.CheckYourAnswersController.onPageLoad()
    }
    case Some(false) => userAnswers.paymentInternationalAddress match {
      case None => routes.PaymentInternationalAddressController.onPageLoad(CheckMode)
      case _ => routes.CheckYourAnswersController.onPageLoad()
    }
    case None => routes.SessionExpiredController.onPageLoad()
  }

  def nextPage(id: Identifier, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode =>
      routeMap.getOrElse(id, _ => routes.IndexController.onPageLoad())
    case CheckMode =>
      editRouteMap.getOrElse(id, _ => routes.CheckYourAnswersController.onPageLoad())
  }

  def nextPageWithIndex(id: Identifier, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode =>
      routeMapWithIndex.lift(id).getOrElse(_ => routes.IndexController.onPageLoad())
    case CheckMode =>
      editRouteMapWithIndex.lift(id).getOrElse(_ => routes.CheckYourAnswersController.onPageLoad())
  }
}
