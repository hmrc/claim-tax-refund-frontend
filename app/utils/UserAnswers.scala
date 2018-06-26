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
import models.templates._
import play.api.i18n.Messages
import uk.gov.hmrc.http.cache.client.CacheMap

class UserAnswers(val cacheMap: CacheMap) {

  def anyTaxableBankInterest: Option[AnyTaxPaid] = cacheMap.getEntry[AnyTaxPaid](AnyTaxableBankInterestId.toString)

  def anyTaxableRentalIncome: Option[AnyTaxPaid] = cacheMap.getEntry[AnyTaxPaid](AnyTaxableRentalIncomeId.toString)

  def otherTaxableIncomeName: Option[String] = cacheMap.getEntry[String](OtherTaxableIncomeNameId.toString)

  def selectTaxableIncome: Option[Set[TaxableIncome.Value]] = cacheMap.getEntry[Set[TaxableIncome.Value]](SelectTaxableIncomeId.toString)

  def howMuchOtherTaxableIncome: Option[String] = cacheMap.getEntry[String](HowMuchOtherTaxableIncomeId.toString)

  def howMuchInvestmentOrDividend: Option[String] = cacheMap.getEntry[String](HowMuchInvestmentOrDividendId.toString)

  def detailsOfEmploymentOrPension: Option[String] = cacheMap.getEntry[String](DetailsOfEmploymentOrPensionId.toString)

  def howMuchBankInterest: Option[String] = cacheMap.getEntry[String](HowMuchBankInterestId.toString)

  def howMuchForeignIncome: Option[String] = cacheMap.getEntry[String](HowMuchForeignIncomeId.toString)

  def howMuchCarersAllowance: Option[String] = cacheMap.getEntry[String](HowMuchCarersAllowanceId.toString)

  def howMuchOtherBenefit: Option[String] = cacheMap.getEntry[String](HowMuchOtherBenefitId.toString)

  def otherBenefitsName: Option[String] = cacheMap.getEntry[String](OtherBenefitsNameId.toString)

  def howMuchBereavementAllowance: Option[String] = cacheMap.getEntry[String](HowMuchBereavementAllowanceId.toString)

  def anyOtherCompanyBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyOtherCompanyBenefitsId.toString)

  def enterPayeReference: Option[String] = cacheMap.getEntry[String](EnterPayeReferenceId.toString)

  def selectBenefits: Option[Set[Benefits.Value]] = cacheMap.getEntry[Set[Benefits.Value]](SelectBenefitsId.toString)

  def howMuchOtherCompanyBenefit: Option[String] = cacheMap.getEntry[String](HowMuchOtherCompanyBenefitId.toString)

  def otherCompanyBenefitsName: Option[String] = cacheMap.getEntry[String](OtherCompanyBenefitsNameId.toString)

  def howMuchFuelBenefit: Option[String] = cacheMap.getEntry[String](HowMuchFuelBenefitId.toString)

  def selectCompanyBenefits: Option[Set[CompanyBenefits.Value]] = cacheMap.getEntry[Set[CompanyBenefits.Value]](SelectCompanyBenefitsId.toString)

  def anyCompanyBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyCompanyBenefitsId.toString)

  def employmentDetails: Option[Boolean] = cacheMap.getEntry[Boolean](EmploymentDetailsId.toString)

  def userDetails: Option[UserDetails] = cacheMap.getEntry[UserDetails](UserDetailsId.toString)

  def paymentInternationalAddress: Option[InternationalAddress] = cacheMap.getEntry[InternationalAddress](PaymentInternationalAddressId.toString)

  def paymentUKAddress: Option[UkAddress] = cacheMap.getEntry[UkAddress](PaymentUKAddressId.toString)

  def isPaymentAddressInTheUK: Option[Boolean] = cacheMap.getEntry[Boolean](IsPaymentAddressInTheUKId.toString)

  def whereToSendPayment: Option[WhereToSendPayment] = cacheMap.getEntry[WhereToSendPayment](WhereToSendPaymentId.toString)

  def anyAgentRef: Option[AnyAgentRef] = cacheMap.getEntry[AnyAgentRef](AnyAgentRefId.toString)

  def agentReferenceNumber: Option[String] = cacheMap.getEntry[String](AgentRefId.toString)

  def nomineeFullName: Option[String] = cacheMap.getEntry[String](NomineeFullNameId.toString)

  def anyOtherTaxableIncome: Option[Boolean] = cacheMap.getEntry[Boolean](AnyOtherTaxableIncomeId.toString)

  def howMuchStatePension: Option[String] = cacheMap.getEntry[String](HowMuchStatePensionId.toString)

  def howMuchEmploymentAndSupportAllowance: Option[String] = cacheMap.getEntry[String](HowMuchEmploymentAndSupportAllowanceId.toString)

  def howMuchIncapacityBenefit: Option[String] = cacheMap.getEntry[String](HowMuchIncapacityBenefitId.toString)

  def howMuchJobseekersAllowance: Option[String] = cacheMap.getEntry[String](HowMuchJobseekersAllowanceId.toString)

  def anyOtherBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyOtherBenefitsId.toString)

  def howMuchMedicalBenefits: Option[String] = cacheMap.getEntry[String](HowMuchMedicalBenefitsId.toString)

  def howMuchRentalIncome: Option[String] = cacheMap.getEntry[String](HowMuchRentalIncomeId.toString)

  def howMuchCarBenefits: Option[String] = cacheMap.getEntry[String](HowMuchCarBenefitsId.toString)

  def anyTaxableIncome: Option[Boolean] = cacheMap.getEntry[Boolean](AnyTaxableIncomeId.toString)

  def anyBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyBenefitsId.toString)

  def selectTaxYear: Option[SelectTaxYear] = cacheMap.getEntry[SelectTaxYear](SelectTaxYearId.toString)

  def telephoneNumber: Option[String] = cacheMap.getEntry[String](TelephoneNumberId.toString)

  def anyTelephoneNumber: Option[TelephoneOption] = cacheMap.getEntry[TelephoneOption](AnyTelephoneId.toString)

  def ukAddress: Option[UkAddress] = cacheMap.getEntry[UkAddress](UkAddressId.toString)

  def internationalAddress: Option[InternationalAddress] = cacheMap.getEntry[InternationalAddress](InternationalAddressId.toString)

  def isTheAddressInTheUK: Option[Boolean] = cacheMap.getEntry[Boolean](IsTheAddressInTheUKId.toString)

  def pdfHtml: Option[String]= cacheMap.getEntry[String]("pdfHtml")

  def metadata: Option[Metadata] = cacheMap.getEntry[Metadata]("metadata")

  def getSelectedTaxYear(implicit messages: Messages): String = {
    this.selectTaxYear.map {
      selectedTaxYear =>
        selectedTaxYear.asString
    }.orNull
  }

}
