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
import models.{Benefits, CompanyBenefits, TaxableIncome, _}
import play.api.mvc.Call

@Singleton
class Navigator @Inject()() {

  private val routeMapWithIndex: PartialFunction[Identifier, UserAnswers => Call] = {
    case OtherTaxableIncomeId(index) => otherTaxableIncome(NormalMode, index)
  }

  private val editRouteMapWithIndex: PartialFunction[Identifier, UserAnswers => Call] = {
    case OtherTaxableIncomeId(index) => otherTaxableIncome(CheckMode, index)
  }

  private val routeMapWithCollectionId: PartialFunction[String, UserAnswers => Call] = {
    case OtherBenefit.collectionId => removeOtherSelectedOptionNavigation(NormalMode, OtherBenefit.collectionId)
    case OtherCompanyBenefit.collectionId => removeOtherSelectedOptionNavigation(NormalMode, OtherCompanyBenefit.collectionId)
    case OtherTaxableIncome.collectionId => removeOtherSelectedOptionNavigation(NormalMode, OtherTaxableIncome.collectionId)
  }

  private val editRouteMapWithCollectionId: PartialFunction[String, UserAnswers => Call] = {
    case OtherBenefit.collectionId => removeOtherSelectedOptionNavigation(CheckMode, OtherBenefit.collectionId)
    case OtherCompanyBenefit.collectionId => removeOtherSelectedOptionNavigation(CheckMode, OtherCompanyBenefit.collectionId)
    case OtherTaxableIncome.collectionId => removeOtherSelectedOptionNavigation(CheckMode, OtherTaxableIncome.collectionId)
  }

  private def removeOtherSelectedOptionNavigation(mode: Mode, collectionId: String)(userAnswers: UserAnswers): Call = {
    userAnswers.removeOtherSelectedOption match {
      case Some(true) => otherSectionYes(mode, collectionId)
      case Some(false) => otherSectionNo(mode, collectionId)(userAnswers)
      case _ => routes.SessionExpiredController.onPageLoad()
    }
  }

  private def otherSectionYes(mode: Mode, collectionId: String): Call = {
    collectionId match {
      case OtherBenefit.collectionId => routes.OtherBenefitController.onPageLoad(mode, 0)
      case OtherCompanyBenefit.collectionId => routes.OtherCompanyBenefitController.onPageLoad(mode, 0)
      case OtherTaxableIncome.collectionId => routes.OtherTaxableIncomeController.onPageLoad(mode, 0)
      case _ => routes.SessionExpiredController.onPageLoad()
    }
  }

  private def otherSectionNo(mode: Mode, collectionId: String)(userAnswers: UserAnswers): Call = {
    if (mode == NormalMode) {
      collectionId match {
        case OtherBenefit.collectionId => routes.AnyCompanyBenefitsController.onPageLoad(mode)
        case OtherCompanyBenefit.collectionId => routes.AnyTaxableIncomeController.onPageLoad(mode)
        case OtherTaxableIncome.collectionId => routes.WhereToSendPaymentController.onPageLoad(mode)
        case _ => routes.SessionExpiredController.onPageLoad()
      }
    } else {
      collectionId match {
        case OtherBenefit.collectionId =>
          if(userAnswers.selectBenefits.isEmpty){routes.AnyBenefitsController.onPageLoad(mode)} else routes.CheckYourAnswersController.onPageLoad()
        case OtherCompanyBenefit.collectionId => routes.AnyTaxableIncomeController.onPageLoad(mode)
        case OtherTaxableIncome.collectionId => routes.WhereToSendPaymentController.onPageLoad(mode)
        case _ => routes.SessionExpiredController.onPageLoad()
      }
    }
  }

  private val routeMap: Map[Identifier, UserAnswers => Call] = Map(
    //Claim details
    SelectTaxYearId -> (_ => routes.EmploymentDetailsController.onPageLoad(NormalMode)),
    EmploymentDetailsId -> employmentDetails,
    EnterPayeReferenceId -> (_ => routes.DetailsOfEmploymentOrPensionController.onPageLoad(NormalMode)),
    DetailsOfEmploymentOrPensionId -> (_ => routes.AnyBenefitsController.onPageLoad(NormalMode)),
    //Benefits
    AnyBenefitsId -> anyBenefits(NormalMode),
    SelectBenefitsId -> selectedBenefitsCheck(NormalMode),
    HowMuchBereavementAllowanceId -> selectedBenefitsCheck(NormalMode),
    HowMuchCarersAllowanceId -> selectedBenefitsCheck(NormalMode),
    HowMuchJobseekersAllowanceId -> selectedBenefitsCheck(NormalMode),
    HowMuchIncapacityBenefitId -> selectedBenefitsCheck(NormalMode),
    HowMuchEmploymentAndSupportAllowanceId -> selectedBenefitsCheck(NormalMode),
    HowMuchStatePensionId -> selectedBenefitsCheck(NormalMode),
    OtherBenefitId -> (_ => routes.AnyOtherBenefitsController.onPageLoad(NormalMode)),
    AnyOtherBenefitsId -> anyOtherBenefits(NormalMode),
    //Company benefits
    AnyCompanyBenefitsId -> anyCompanyBenefits(NormalMode),
    SelectCompanyBenefitsId -> selectedCompanyBenefitsCheck(NormalMode),
    HowMuchCarBenefitsId -> selectedCompanyBenefitsCheck(NormalMode),
    HowMuchFuelBenefitId -> selectedCompanyBenefitsCheck(NormalMode),
    HowMuchMedicalBenefitsId -> selectedCompanyBenefitsCheck(NormalMode),
    OtherCompanyBenefitId -> (_ => routes.AnyOtherCompanyBenefitsController.onPageLoad(NormalMode)),
    AnyOtherCompanyBenefitsId -> anyOtherCompanyBenefits(NormalMode),
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
    AnyTaxableOtherIncomeId -> (_ => routes.AnyOtherTaxableIncomeController.onPageLoad(NormalMode)),
    AnyOtherTaxableIncomeId -> anyOtherTaxableIncome(NormalMode),
    //Payment
    WhereToSendPaymentId -> whereToSendPayment,
    NomineeFullNameId -> (_ => routes.AnyAgentRefController.onPageLoad(NormalMode)),
    AnyAgentRefId -> (_ => routes.IsPaymentAddressInTheUKController.onPageLoad(NormalMode)),
    PaymentAddressCorrectId -> paymentAddressCorrect,
    IsPaymentAddressInTheUKId -> isPaymentAddressInUk,
    PaymentUKAddressId -> (_ => routes.TelephoneNumberController.onPageLoad(NormalMode)),
    PaymentInternationalAddressId -> (_ => routes.TelephoneNumberController.onPageLoad(NormalMode)),
    PaymentLookupAddressId -> addressLookup(NormalMode),
    TelephoneNumberId -> (_ => routes.CheckYourAnswersController.onPageLoad()),
    DeleteOtherBenefitId -> deleteOtherBenefit,
    DeleteOtherCompanyBenefitId -> deleteOtherCompanyBenefit,
    DeleteOtherTaxableIncomeId -> (_ => routes.AnyOtherTaxableIncomeController.onPageLoad(NormalMode))

  )

  private val editRouteMap: Map[Identifier, UserAnswers => Call] = Map(
    //Claim details
    EmploymentDetailsId -> employmentDetailsCheck,
    EnterPayeReferenceId -> detailsOfEmploymentCheck,
    //Benefits
    AnyBenefitsId -> anyBenefits(CheckMode),
    SelectBenefitsId -> selectedBenefitsCheck(CheckMode),
    HowMuchBereavementAllowanceId -> selectedBenefitsCheck(CheckMode),
    HowMuchCarersAllowanceId -> selectedBenefitsCheck(CheckMode),
    HowMuchJobseekersAllowanceId -> selectedBenefitsCheck(CheckMode),
    HowMuchIncapacityBenefitId -> selectedBenefitsCheck(CheckMode),
    HowMuchEmploymentAndSupportAllowanceId -> selectedBenefitsCheck(CheckMode),
    HowMuchStatePensionId -> selectedBenefitsCheck(CheckMode),
    OtherBenefitId -> (_ => routes.AnyOtherBenefitsController.onPageLoad(CheckMode)),
		AnyOtherBenefitsId -> anyOtherBenefits(CheckMode),
    //Company Benefits
    AnyCompanyBenefitsId -> anyCompanyBenefits(CheckMode),
    SelectCompanyBenefitsId -> selectedCompanyBenefitsCheck(CheckMode),
    HowMuchCarBenefitsId -> selectedCompanyBenefitsCheck(CheckMode),
    HowMuchFuelBenefitId -> selectedCompanyBenefitsCheck(CheckMode),
    HowMuchMedicalBenefitsId -> selectedCompanyBenefitsCheck(CheckMode),
    OtherCompanyBenefitId -> (_ => routes.AnyOtherCompanyBenefitsController.onPageLoad(CheckMode)),
    AnyOtherCompanyBenefitsId -> anyOtherCompanyBenefits(CheckMode),
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
    DeleteOtherTaxableIncomeId -> (_ => routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode)),
    AnyOtherTaxableIncomeId -> anyOtherTaxableIncome(CheckMode),
    AnyTaxableOtherIncomeId -> (_ => routes.AnyOtherTaxableIncomeController.onPageLoad(CheckMode)),
    //Payment
    WhereToSendPaymentId -> whereToSendPaymentCheck,
    PaymentAddressCorrectId -> paymentAddressCorrectCheck,
    NomineeFullNameId -> anyAgentRefCheck,
    AnyAgentRefId -> isPaymentAddressInUkCheck,
    IsPaymentAddressInTheUKId -> paymentAddressCheck,
    PaymentLookupAddressId -> addressLookup(CheckMode)
  )


  private def deleteOtherBenefit(userAnswers: UserAnswers): Call = {
    val otherBenefits: Seq[OtherBenefit] = userAnswers.otherBenefit.get
    val selectBenefits: Seq[Benefits.Value] = userAnswers.selectBenefits.get

    if (otherBenefits.isEmpty && selectBenefits.length == 1) {
      routes.AnyBenefitsController.onPageLoad(CheckMode)
    } else if (otherBenefits.isEmpty && selectBenefits.length > 1){
      routes.SelectBenefitsController.onPageLoad(CheckMode)
    } else {
      routes.CheckYourAnswersController.onPageLoad()
    }
  }

  private def deleteOtherCompanyBenefit(userAnswers: UserAnswers): Call = {
    val otherCompanyBenefits: Seq[OtherCompanyBenefit] = userAnswers.otherCompanyBenefit.get
    val selectCompanyBenefits: Seq[CompanyBenefits.Value] = userAnswers.selectCompanyBenefits.get

    if (otherCompanyBenefits.isEmpty && selectCompanyBenefits.length == 1) {
      routes.AnyCompanyBenefitsController.onPageLoad(CheckMode)
    } else if (otherCompanyBenefits.isEmpty && selectCompanyBenefits.length > 1){
      routes.SelectCompanyBenefitsController.onPageLoad(CheckMode)
    } else {
      routes.CheckYourAnswersController.onPageLoad()
    }
  }

  private def deleteOtherTaxableIncome(userAnswers: UserAnswers): Call = {
    val otherTaxableIncome: Seq[OtherTaxableIncome] = userAnswers.otherTaxableIncome.get
    val selectTaxableIncome: Seq[TaxableIncome.Value] = userAnswers.selectTaxableIncome.get

    if (otherTaxableIncome.isEmpty && selectTaxableIncome.size == 1) {
      routes.AnyTaxableIncomeController.onPageLoad(CheckMode)
    } else if (otherTaxableIncome.isEmpty && selectTaxableIncome.size > 1){
      routes.SelectTaxableIncomeController.onPageLoad(CheckMode)
    } else {
      routes.CheckYourAnswersController.onPageLoad()
    }
  }



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
    case Some(true)  =>
      mode match {
        case NormalMode =>
          routes.SelectBenefitsController.onPageLoad(mode)
        case CheckMode =>
          userAnswers.selectBenefits match {
            case None => routes.SelectBenefitsController.onPageLoad(mode)
            case _ => selectedBenefitsCheck(mode)(userAnswers)
          }
      }
    case Some(false) =>
      if(mode == NormalMode) routes.AnyCompanyBenefitsController.onPageLoad(mode) else routes.CheckYourAnswersController.onPageLoad()
    case None =>
      routes.SessionExpiredController.onPageLoad()
  }

  private def selectedBenefitsCheck(mode: Mode)(userAnswers: UserAnswers): Call = userAnswers.selectBenefits match {
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
      } else if (benefits.contains(Benefits.OTHER_TAXABLE_BENEFIT) && (userAnswers.otherBenefit.isEmpty || userAnswers.otherBenefit.get.isEmpty)) {
        routes.OtherBenefitController.onPageLoad(mode, Index(0))
      } else if (benefits.contains(Benefits.OTHER_TAXABLE_BENEFIT) && userAnswers.otherBenefit.isDefined) {
        routes.AnyOtherBenefitsController.onPageLoad(mode)
      } else if (mode == NormalMode) {
        routes.AnyCompanyBenefitsController.onPageLoad(mode)
      } else routes.CheckYourAnswersController.onPageLoad()
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyOtherBenefits(mode: Mode)(userAnswers: UserAnswers): Call = userAnswers.anyOtherBenefits match {
    case Some(true) => routes.OtherBenefitController.onPageLoad(mode, Index(userAnswers.otherBenefit.get.length))
    case Some(false) => mode match {
			case NormalMode => routes.AnyCompanyBenefitsController.onPageLoad(NormalMode)
			case CheckMode => routes.CheckYourAnswersController.onPageLoad()
		}
    case None => routes.SessionExpiredController.onPageLoad()
  }


  //Company benefits--------------------------

  private def anyCompanyBenefits(mode: Mode)(userAnswers: UserAnswers): Call = userAnswers.anyCompanyBenefits match {
    case Some(true) =>
      mode match {
        case NormalMode =>
          routes.SelectCompanyBenefitsController.onPageLoad(NormalMode)
        case CheckMode =>
          userAnswers.selectCompanyBenefits match {
            case None => routes.SelectCompanyBenefitsController.onPageLoad(mode)
            case _ => selectedCompanyBenefitsCheck(mode)(userAnswers)
        }
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
      } else if (benefits.contains(CompanyBenefits.OTHER_COMPANY_BENEFIT) && userAnswers.otherCompanyBenefit.isEmpty) {
        routes.OtherCompanyBenefitController.onPageLoad(mode, Index(0))
      } else if (mode == NormalMode) {
        routes.AnyTaxableIncomeController.onPageLoad(mode)
      } else routes.CheckYourAnswersController.onPageLoad()
    case None =>
      routes.SessionExpiredController.onPageLoad()
  }

  private def anyOtherCompanyBenefits(mode: Mode)(userAnswers: UserAnswers): Call = userAnswers.anyOtherCompanyBenefits match {
    case Some(true) => routes.OtherCompanyBenefitController.onPageLoad(mode, Index(userAnswers.otherCompanyBenefit.get.length))
    case Some(false) => mode match {
      case NormalMode => routes.AnyTaxableIncomeController.onPageLoad(NormalMode)
      case CheckMode => routes.CheckYourAnswersController.onPageLoad()
    }
    case None => routes.SessionExpiredController.onPageLoad()
  }

  def otherCompanyBenefit(mode: Mode)(userAnswers: UserAnswers): Call =
    if (mode == NormalMode) routes.AnyOtherCompanyBenefitsController.onPageLoad(mode) else routes.CheckYourAnswersController.onPageLoad()

  //Taxable income--------------------------

  private def anyTaxableIncome(mode: Mode)(userAnswers: UserAnswers): Call = userAnswers.anyTaxableIncome match {
    case Some(true) =>
      mode match {
        case NormalMode =>
          routes.SelectTaxableIncomeController.onPageLoad(mode)
        case CheckMode =>
          userAnswers.selectTaxableIncome match {
            case None => routes.SelectTaxableIncomeController.onPageLoad(mode)
            case _ => selectedTaxableIncomeCheck(mode)(userAnswers)
          }
      }
    case Some(false) =>
      if (mode == NormalMode) routes.WhereToSendPaymentController.onPageLoad(NormalMode) else routes.CheckYourAnswersController.onPageLoad()
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
      } else if (taxableIncome.contains(TaxableIncome.OTHER_TAXABLE_INCOME) && (userAnswers.otherTaxableIncome.isEmpty || userAnswers.otherTaxableIncome.get.isEmpty)) {
        routes.OtherTaxableIncomeController.onPageLoad(mode, Index(0))
      } else if (mode == NormalMode) {
        routes.WhereToSendPaymentController.onPageLoad(mode)
      } else routes.CheckYourAnswersController.onPageLoad()
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

  private def anyOtherTaxableIncome(mode: Mode)(userAnswers: UserAnswers): Call = userAnswers.anyOtherTaxableIncome match {
    case Some(true) => routes.OtherTaxableIncomeController.onPageLoad(mode, Index(userAnswers.otherTaxableIncome.get.length))
    case Some(false) => mode match {
      case NormalMode => routes.WhereToSendPaymentController.onPageLoad(mode)
      case _ => routes.CheckYourAnswersController.onPageLoad()
    }
    case None => routes.SessionExpiredController.onPageLoad()
  }

  def otherTaxableIncome(mode: Mode, index: Index)(userAnswers: UserAnswers): Call = {
    routes.AnyTaxableOtherIncomeController.onPageLoad(mode, index)
  }

  //Payment----------------------------

  private def whereToSendPayment(userAnswers: UserAnswers): Call = userAnswers.whereToSendPayment match {
    case Some(Nominee) => routes.NomineeFullNameController.onPageLoad(NormalMode)
    case Some(Myself) => routes.PaymentAddressCorrectController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def whereToSendPaymentCheck(userAnswers: UserAnswers): Call = userAnswers.whereToSendPayment match {
    case Some(Nominee) =>
      if (userAnswers.nomineeFullName.isEmpty) routes.NomineeFullNameController.onPageLoad(CheckMode) else routes.CheckYourAnswersController.onPageLoad()
    case Some(Myself) =>
      if (userAnswers.isPaymentAddressInTheUK.isEmpty) routes.PaymentAddressCorrectController.onPageLoad(CheckMode) else routes.CheckYourAnswersController.onPageLoad()
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

  private def addressLookup(mode: Mode)(userAnswers: UserAnswers): Call = mode match {
    case CheckMode => routes.CheckYourAnswersController.onPageLoad()
    case NormalMode => routes.TelephoneNumberController.onPageLoad(mode)
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

  def nextPageWithCollectionId(collectionId: String, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode =>
      routeMapWithCollectionId.lift(collectionId).getOrElse(_ => routes.IndexController.onPageLoad())
    case CheckMode =>
      editRouteMapWithCollectionId.lift(collectionId).getOrElse(_ => routes.CheckYourAnswersController.onPageLoad())
  }
}
