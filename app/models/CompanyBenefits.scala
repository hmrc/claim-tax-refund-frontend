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

import identifiers._
import play.api.libs.json.{Format, Reads, Writes}
import utils.EnumUtils

object CompanyBenefits extends Enumeration {

  val COMPANY_CAR_BENEFIT = Value("company-car-benefit")
  val FUEL_BENEFIT = Value("fuel-benefit")
  val MEDICAL_BENEFIT = Value("medical-benefit")
  val OTHER_COMPANY_BENEFIT = Value("other-company-benefit")

  val reads: Reads[Value] = EnumUtils.enumReads(CompanyBenefits)
  val writes: Writes[Value] = EnumUtils.enumWrites

  implicit def enumFormats: Format[Value] = EnumUtils.enumFormat(CompanyBenefits)

  def getIdString(benefitValue: String): String = {
    benefitValue match {
      case "company-car-benefit" => HowMuchCarBenefitsId.toString
      case "fuel-benefit" => HowMuchFuelBenefitId.toString
      case "medical-benefit" => HowMuchMedicalBenefitsId.toString
      case "other-company-benefit" => OtherCompanyBenefitsNameId.toString
    }
  }

  val sortedCompanyBenefits =
    Seq(
      COMPANY_CAR_BENEFIT,
      FUEL_BENEFIT,
      MEDICAL_BENEFIT,
      OTHER_COMPANY_BENEFIT
    )
}
