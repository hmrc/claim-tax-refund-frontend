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

import identifiers._
import models._
import models.templates.Metadata
import uk.gov.hmrc.http.cache.client.CacheMap

class UserAnswers(val cacheMap: CacheMap) {

  //Submission data
  //------------------------------------------------------------------------------

  def userDetails: Option[UserDetails] = cacheMap.getEntry[UserDetails](UserDetailsId.toString)

  def pdfHtml: Option[String] = cacheMap.getEntry[String]("pdfHtml")

  def metadata: Option[Metadata] = cacheMap.getEntry[Metadata]("metadata")


  //Claim details
  //------------------------------------------------------------------------------

  def selectTaxYear: Option[SelectTaxYear] = cacheMap.getEntry[SelectTaxYear](SelectTaxYearId.toString)

  def employmentDetails: Option[Boolean] = cacheMap.getEntry[Boolean](EmploymentDetailsId.toString)

  def enterPayeReference: Option[String] = cacheMap.getEntry[String](EnterPayeReferenceId.toString)

  def detailsOfEmploymentOrPension: Option[String] = cacheMap.getEntry[String](DetailsOfEmploymentOrPensionId.toString)

  //Benefits details
  //------------------------------------------------------------------------------

  def anyBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyBenefitsId.toString)

  def selectBenefits: Option[Seq[Benefits.Value]] = cacheMap.getEntry[Seq[Benefits.Value]](SelectBenefitsId.toString)

  def howMuchBereavementAllowance: Option[String] = cacheMap.getEntry[String](HowMuchBereavementAllowanceId.toString)

  def howMuchCarersAllowance: Option[String] = cacheMap.getEntry[String](HowMuchCarersAllowanceId.toString)

  def howMuchJobseekersAllowance: Option[String] = cacheMap.getEntry[String](HowMuchJobseekersAllowanceId.toString)

  def howMuchIncapacityBenefit: Option[String] = cacheMap.getEntry[String](HowMuchIncapacityBenefitId.toString)

  def howMuchEmploymentAndSupportAllowance: Option[String] = cacheMap.getEntry[String](HowMuchEmploymentAndSupportAllowanceId.toString)

  def howMuchStatePension: Option[String] = cacheMap.getEntry[String](HowMuchStatePensionId.toString)

  def otherBenefit: Option[Seq[OtherBenefit]] = cacheMap.getEntry[Seq[OtherBenefit]](OtherBenefitId.toString)

  def anyOtherBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyOtherBenefitsId.toString)

  //Company benefits details
  //------------------------------------------------------------------------------

  def anyCompanyBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyCompanyBenefitsId.toString)

  def selectCompanyBenefits: Option[Seq[CompanyBenefits.Value]] = cacheMap.getEntry[Seq[CompanyBenefits.Value]](SelectCompanyBenefitsId.toString)

  def howMuchCarBenefits: Option[String] = cacheMap.getEntry[String](HowMuchCarBenefitsId.toString)

  def howMuchFuelBenefit: Option[String] = cacheMap.getEntry[String](HowMuchFuelBenefitId.toString)

  def howMuchMedicalBenefits: Option[String] = cacheMap.getEntry[String](HowMuchMedicalBenefitsId.toString)

  def otherCompanyBenefit: Option[Seq[OtherCompanyBenefit]] = cacheMap.getEntry[Seq[OtherCompanyBenefit]](OtherCompanyBenefitId.toString)

  def anyOtherCompanyBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyOtherCompanyBenefitsId.toString)

  //Taxable income details
  //------------------------------------------------------------------------------

  def anyTaxableIncome: Option[Boolean] = cacheMap.getEntry[Boolean](AnyTaxableIncomeId.toString)

  def selectTaxableIncome: Option[Seq[TaxableIncome.Value]] = cacheMap.getEntry[Seq[TaxableIncome.Value]](SelectTaxableIncomeId.toString)

  def howMuchRentalIncome: Option[String] = cacheMap.getEntry[String](HowMuchRentalIncomeId.toString)

  def anyTaxableRentalIncome: Option[AnyTaxPaid] = cacheMap.getEntry[AnyTaxPaid](AnyTaxableRentalIncomeId.toString)

  def howMuchBankInterest: Option[String] = cacheMap.getEntry[String](HowMuchBankInterestId.toString)

  def anyTaxableBankInterest: Option[AnyTaxPaid] = cacheMap.getEntry[AnyTaxPaid](AnyTaxableBankInterestId.toString)

  def howMuchInvestmentOrDividend: Option[String] = cacheMap.getEntry[String](HowMuchInvestmentsId.toString)

  def anyTaxableInvestments: Option[AnyTaxPaid] = cacheMap.getEntry[AnyTaxPaid](AnyTaxableInvestmentsId.toString)

  def howMuchForeignIncome: Option[String] = cacheMap.getEntry[String](HowMuchForeignIncomeId.toString)

  def anyTaxableForeignIncome: Option[AnyTaxPaid] = cacheMap.getEntry[AnyTaxPaid](AnyTaxableForeignIncomeId.toString)

  def otherTaxableIncome: Option[Seq[OtherTaxableIncome]] = cacheMap.getEntry[Seq[OtherTaxableIncome]](OtherTaxableIncomeId.toString)

  def anyTaxableOtherIncome: Option[Seq[AnyTaxPaid]] = cacheMap.getEntry[Seq[AnyTaxPaid]](AnyTaxableOtherIncomeId.toString)

  def anyOtherTaxableIncome: Option[Boolean] = cacheMap.getEntry[Boolean](AnyOtherTaxableIncomeId.toString)

  //Payment details
  //------------------------------------------------------------------------------

  def whereToSendPayment: Option[WhereToSendPayment] = cacheMap.getEntry[WhereToSendPayment](WhereToSendPaymentId.toString)

  def paymentAddressCorrect: Option[Boolean] = cacheMap.getEntry[Boolean](PaymentAddressCorrectId.toString)

  def nomineeFullName: Option[String] = cacheMap.getEntry[String](NomineeFullNameId.toString)

  def anyAgentRef: Option[AnyAgentRef] = cacheMap.getEntry[AnyAgentRef](AnyAgentRefId.toString)

  def isPaymentAddressInTheUK: Option[Boolean] = cacheMap.getEntry[Boolean](IsPaymentAddressInTheUKId.toString)

  def paymentUKAddress: Option[UkAddress] = cacheMap.getEntry[UkAddress](PaymentUKAddressId.toString)

  def paymentInternationalAddress: Option[InternationalAddress] = cacheMap.getEntry[InternationalAddress](PaymentInternationalAddressId.toString)

  def paymentLookupAddress: Option[AddressLookup] = cacheMap.getEntry[AddressLookup](PaymentLookupAddressId.toString)

  //Contact details
  //------------------------------------------------------------------------------

  def anyTelephoneNumber: Option[TelephoneOption] = cacheMap.getEntry[TelephoneOption](AnyTelephoneId.toString)
}

