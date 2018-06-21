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

import identifiers.{HowMuchCarBenefitsId, HowMuchFuelBenefitId, HowMuchMedicalBenefitsId, HowMuchOtherCompanyBenefitId}
import play.api.libs.json.{Format, Reads, Writes}
import utils.EnumUtils

import scala.collection.mutable

object CompanyBenefits extends Enumeration {

  val COMPANY_CAR_BENEFIT = Value("company-car-benefit")
  val FUEL_BENEFIT = Value("fuel-benefit")
  val MEDICAL_BENEFIT = Value("medical-benefit")
  val OTHER_COMPANY_BENEFIT = Value("other-company-benefit")

  val reads: Reads[Value] = EnumUtils.enumReads(CompanyBenefits)
  val writes: Writes[Value] = EnumUtils.enumWrites

  implicit def enumFormats: Format[Value] = EnumUtils.enumFormat(CompanyBenefits)

  def getIdString(benefitValue: String) = {
    benefitValue match {
      case "company-car-benefit" => HowMuchCarBenefitsId
      case "fuel-benefit" => HowMuchFuelBenefitId
      case "medical-benefit" => HowMuchMedicalBenefitsId
      case "other-company-benefit" => HowMuchOtherCompanyBenefitId
    }
  }

  val sortedCompanyBenefits =
    mutable.LinkedHashMap(
      s"selectCompanyBenefits.$COMPANY_CAR_BENEFIT" -> COMPANY_CAR_BENEFIT.toString,
      s"selectCompanyBenefits.$FUEL_BENEFIT" -> FUEL_BENEFIT.toString,
      s"selectCompanyBenefits.$MEDICAL_BENEFIT" -> MEDICAL_BENEFIT.toString,
      s"selectCompanyBenefits.$OTHER_COMPANY_BENEFIT" -> OTHER_COMPANY_BENEFIT.toString
    )
}
