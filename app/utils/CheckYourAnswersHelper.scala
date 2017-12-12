/*
 * Copyright 2017 HM Revenue & Customs
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
import models.CheckMode
import viewmodels.{AnswerRow, RepeaterAnswerRow, RepeaterAnswerSection}

class CheckYourAnswersHelper(userAnswers: UserAnswers) {

  def anyMedicalBenefits: Option[AnswerRow] = userAnswers.anyMedicalBenefits map {
    x => AnswerRow("anyMedicalBenefits.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.AnyMedicalBenefitsController.onPageLoad(CheckMode).url)
  }

  def howMuchBankBuildingSocietyInterest: Option[AnswerRow] = userAnswers.howMuchBankBuildingSocietyInterest map {
    x => AnswerRow("howMuchBankBuildingSocietyInterest.checkYourAnswersLabel", s"$x", false, routes.HowMuchBankBuildingSocietyInterestController.onPageLoad(CheckMode).url)
  }

  def anyBankBuildingSocietyInterest: Option[AnswerRow] = userAnswers.anyBankBuildingSocietyInterest map {
    x => AnswerRow("anyBankBuildingSocietyInterest.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.AnyBankBuildingSocietyInterestController.onPageLoad(CheckMode).url)
  }

  def howMuchRentalIncome: Option[AnswerRow] = userAnswers.howMuchRentalIncome map {
    x => AnswerRow("howMuchRentalIncome.checkYourAnswersLabel", s"$x", false, routes.HowMuchRentalIncomeController.onPageLoad(CheckMode).url)
  }

  def anyRentalIncome: Option[AnswerRow] = userAnswers.anyRentalIncome map {
    x => AnswerRow("anyRentalIncome.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.AnyRentalIncomeController.onPageLoad(CheckMode).url)
  }

  def howMuchCarBenefits: Option[AnswerRow] = userAnswers.howMuchCarBenefits map {
    x => AnswerRow("howMuchCarBenefits.checkYourAnswersLabel", s"$x", false, routes.HowMuchCarBenefitsController.onPageLoad(CheckMode).url)
  }

  def anyCarBenefits: Option[AnswerRow] = userAnswers.anyCarBenefits map {
    x => AnswerRow("anyCarBenefits.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.AnyCarBenefitsController.onPageLoad(CheckMode).url)
  }

  def otherIncome: Option[AnswerRow] = userAnswers.otherIncome map {
    x => AnswerRow("otherIncome.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.OtherIncomeController.onPageLoad(CheckMode).url)
  }

  def anyBenefits: Option[AnswerRow] = userAnswers.anyBenefits map {
    x => AnswerRow("anyBenefits.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.AnyBenefitsController.onPageLoad(CheckMode).url)
  }

  def partialClaimAmount: Option[AnswerRow] = userAnswers.partialClaimAmount map {
    x => AnswerRow("partialClaimAmount.checkYourAnswersLabel", s"$x", false, routes.PartialClaimAmountController.onPageLoad(CheckMode).url)
  }

  def fullOrPartialClaim: Option[AnswerRow] = userAnswers.fullOrPartialClaim map {
    x => AnswerRow("fullOrPartialClaim.checkYourAnswersLabel", s"fullOrPartialClaim.$x", true, routes.FullOrPartialClaimController.onPageLoad(CheckMode).url)
  }

  def selectTaxYear: Option[AnswerRow] = userAnswers.selectTaxYear map {
    x => AnswerRow("selectTaxYear.checkYourAnswersLabel", s"selectTaxYear.$x", true, routes.SelectTaxYearController.onPageLoad(CheckMode).url)
  }

  def payAsYouEarn: Option[AnswerRow] = userAnswers.payAsYouEarn map {
    x => AnswerRow("payAsYouEarn.checkYourAnswersLabel", s"$x", false, routes.PayAsYouEarnController.onPageLoad(CheckMode).url)
  }

  def uniqueTaxpayerReference: Option[AnswerRow] = userAnswers.uniqueTaxpayerReference map {
    x => AnswerRow("uniqueTaxpayerReference.checkYourAnswersLabel", s"$x", false, routes.UniqueTaxpayerReferenceController.onPageLoad(CheckMode).url)
  }

  def typeOfClaim: Option[AnswerRow] = userAnswers.typeOfClaim map {
    x => AnswerRow("typeOfClaim.checkYourAnswersLabel", s"typeOfClaim.$x", true, routes.TypeOfClaimController.onPageLoad(CheckMode).url)
  }

  def telephoneNumber: Option[AnswerRow] = userAnswers.telephoneNumber map {
    x => AnswerRow("telephoneNumber.checkYourAnswersLabel", s"$x", false, routes.TelephoneNumberController.onPageLoad(CheckMode).url)
  }

  def ukAddress: Option[AnswerRow] = userAnswers.ukAddress map {
    x => AnswerRow("ukAddress.checkYourAnswersLabel", s"${x.addressLine1} ${x.addressLine2}", false, routes.UkAddressController.onPageLoad(CheckMode).url)
  }

  def internationalAddress: Option[AnswerRow] = userAnswers.internationalAddress map {
    x => AnswerRow("internationalAddress.checkYourAnswersLabel", s"${x.addressLine1} ${x.addressLine2} ${x.addressLine3} ${x.country}", false, routes.InternationalAddressController.onPageLoad(CheckMode).url)
  }

  def isTheAddressInTheUK: Option[AnswerRow] = userAnswers.isTheAddressInTheUK map {
    x => AnswerRow("isTheAddressInTheUK.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.IsTheAddressInTheUKController.onPageLoad(CheckMode).url)
  }

  def nationalInsuranceNumber: Option[AnswerRow] = userAnswers.nationalInsuranceNumber map {
    x => AnswerRow("nationalInsuranceNumber.checkYourAnswersLabel", s"$x", false, routes.NationalInsuranceNumberController.onPageLoad(CheckMode).url)
  }

  def fullName: Option[AnswerRow] = userAnswers.fullName map {
    x => AnswerRow("fullName.checkYourAnswersLabel", s"$x", false, routes.FullNameController.onPageLoad(CheckMode).url)
  }
}
