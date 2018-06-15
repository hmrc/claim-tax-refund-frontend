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

package models

import play.api.libs.json.{Format, Reads, Writes}
import utils.EnumUtils

object TaxableIncome extends Enumeration {

  val RENTAL_INCOME = Value("rental-income")
  val BANK_OR_BUILDING_SOCIETY_INTEREST = Value("bank-or-building-society-interest")
  val INVESTMENT_OR_DIVIDENDS = Value("investment-or-dividends")
  val FOREIGN_INCOME = Value("foreign-income")
  val OTHER_TAXABLE_INCOME = Value("other-taxable-income")

  val reads: Reads[Value] = EnumUtils.enumReads(TaxableIncome)
  val writes: Writes[Value] = EnumUtils.enumWrites

  implicit def enumFormats: Format[Value] = EnumUtils.enumFormat(TaxableIncome)
}
