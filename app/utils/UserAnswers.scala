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

import uk.gov.hmrc.http.cache.client.CacheMap
import identifiers._
import models._

class UserAnswers(val cacheMap: CacheMap) {
  def selfAssessmentClaim: Option[Boolean] = cacheMap.getEntry[Boolean](SelfAssessmentClaimId.toString)

  def payAsYouEarn: Option[String] = cacheMap.getEntry[String](PayAsYouEarnId.toString)

  def uniqueTaxpayerReference: Option[String] = cacheMap.getEntry[String](UniqueTaxpayerReferenceId.toString)

  def areYouSelfAssessed: Option[Boolean] = cacheMap.getEntry[Boolean](AreYouSelfAssessedId.toString)

  def telephoneNumber: Option[String] = cacheMap.getEntry[String](TelephoneNumberId.toString)

  def ukAddress: Option[UkAddress] = cacheMap.getEntry[UkAddress](UkAddressId.toString)

  def internationalAddress: Option[InternationalAddress] = cacheMap.getEntry[InternationalAddress](InternationalAddressId.toString)

  def isTheAddressInTheUK: Option[Boolean] = cacheMap.getEntry[Boolean](IsTheAddressInTheUKId.toString)

  def nationalInsuranceNumber: Option[String] = cacheMap.getEntry[String](NationalInsuranceNumberId.toString)

  def fullName: Option[String] = cacheMap.getEntry[String](FullNameId.toString)

}
