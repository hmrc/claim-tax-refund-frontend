/*
 * Copyright 2022 HM Revenue & Customs
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

  val COMPANY_CAR_BENEFIT: CompanyBenefits.Value = Value("company-car-benefit")
  val FUEL_BENEFIT: CompanyBenefits.Value = Value("fuel-benefit")
  val MEDICAL_BENEFIT: CompanyBenefits.Value = Value("medical-benefit")
  val OTHER_COMPANY_BENEFIT: CompanyBenefits.Value = Value("other-company-benefit")

  val reads: Reads[Value] = EnumUtils.enumReads(CompanyBenefits)
  val writes: Writes[Value] = EnumUtils.enumWrites

  implicit def enumFormats: Format[Value] = EnumUtils.enumFormat(CompanyBenefits)

  def getId(benefitValue: String): Identifier = {
    benefitValue match {
      case "company-car-benefit" => HowMuchCarBenefitsId
      case "fuel-benefit" => HowMuchFuelBenefitId
      case "medical-benefit" => HowMuchMedicalBenefitsId
      case "other-company-benefit" => OtherCompanyBenefitId
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
