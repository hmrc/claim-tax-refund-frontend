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
import uk.gov.hmrc.http.cache.client.CacheMap

class UserAnswers(val cacheMap: CacheMap) {
  def employmentDetails: Option[Boolean] = cacheMap.getEntry[Boolean](EmploymentDetailsId.toString)

  def userDetails: Option[UserDetails] = cacheMap.getEntry[UserDetails](UserDetailsId.toString)

  def payeeInternationalAddress: Option[InternationalAddress] = cacheMap.getEntry[InternationalAddress](PayeeInternationalAddressId.toString)

  def payeeUKAddress: Option[UkAddress] = cacheMap.getEntry[UkAddress](PayeeUKAddressId.toString)

  def isPayeeAddressInTheUK: Option[Boolean] = cacheMap.getEntry[Boolean](IsPayeeAddressInTheUKId.toString)

  def whereToSendPayment: Option[WhereToSendPayment] = cacheMap.getEntry[WhereToSendPayment](WhereToSendPaymentId.toString)

  def anyAgentRef: Option[Boolean] = cacheMap.getEntry[Boolean](AnyAgentRefId.toString)

  def agentReferenceNumber: Option[String] = cacheMap.getEntry[String](AgentReferenceNumberId.toString)

  def payeeFullName: Option[String] = cacheMap.getEntry[String](PayeeFullNameId.toString)

  def otherIncomeDetailsAndAmount: Option[String] = cacheMap.getEntry[String](OtherIncomeDetailsAndAmountId.toString)

  def otherBenefitsDetailsAndAmount: Option[String] = cacheMap.getEntry[String](OtherBenefitsDetailsAndAmountId.toString)

  def anyOtherTaxableIncome: Option[Boolean] = cacheMap.getEntry[Boolean](AnyOtherTaxableIncomeId.toString)

  def howMuchStatePension: Option[String] = cacheMap.getEntry[String](HowMuchStatePensionId.toString)

  def howMuchEmploymentAndSupportAllowance: Option[String] = cacheMap.getEntry[String](HowMuchEmploymentAndSupportAllowanceId.toString)

  def howMuchIncapacityBenefit: Option[String] = cacheMap.getEntry[String](HowMuchIncapacityBenefitId.toString)

  def howMuchJobseekersAllowance: Option[String] = cacheMap.getEntry[String](HowMuchJobseekersAllowanceId.toString)

  def anyOtherTaxableBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyOtherTaxableBenefitsId.toString)

  def anyStatePension: Option[Boolean] = cacheMap.getEntry[Boolean](AnyStatePensionId.toString)

  def anyEmploymentAndSupportAllowance: Option[Boolean] = cacheMap.getEntry[Boolean](AnyEmploymentAndSupportAllowanceId.toString)

  def anyIncapacityBenefit: Option[Boolean] = cacheMap.getEntry[Boolean](AnyIncapacityBenefitId.toString)

  def anyJobseekersAllowance: Option[Boolean] = cacheMap.getEntry[Boolean](AnyJobseekersAllowanceId.toString)

  def howMuchMedicalBenefits: Option[String] = cacheMap.getEntry[String](HowMuchMedicalBenefitsId.toString)

  def howMuchBankBuildingSocietyInterest: Option[String] = cacheMap.getEntry[String](HowMuchBankBuildingSocietyInterestId.toString)

  def anyBankBuildingSocietyInterest: Option[Boolean] = cacheMap.getEntry[Boolean](AnyBankBuildingSocietyInterestId.toString)

  def howMuchRentalIncome: Option[String] = cacheMap.getEntry[String](HowMuchRentalIncomeId.toString)

  def anyRentalIncome: Option[Boolean] = cacheMap.getEntry[Boolean](AnyRentalIncomeId.toString)

  def howMuchCarBenefits: Option[String] = cacheMap.getEntry[String](HowMuchCarBenefitsId.toString)

  def otherIncome: Option[Boolean] = cacheMap.getEntry[Boolean](OtherIncomeId.toString)

  def anyBenefits: Option[Boolean] = cacheMap.getEntry[Boolean](AnyBenefitsId.toString)

  def selectTaxYear: Option[SelectTaxYear] = cacheMap.getEntry[SelectTaxYear](SelectTaxYearId.toString)

  def telephoneNumber: Option[String] = cacheMap.getEntry[String](TelephoneNumberId.toString)

  def ukAddress: Option[UkAddress] = cacheMap.getEntry[UkAddress](UkAddressId.toString)

  def internationalAddress: Option[InternationalAddress] = cacheMap.getEntry[InternationalAddress](InternationalAddressId.toString)

  def isTheAddressInTheUK: Option[Boolean] = cacheMap.getEntry[Boolean](IsTheAddressInTheUKId.toString)

}
