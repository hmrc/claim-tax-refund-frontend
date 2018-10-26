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

object Benefits extends Enumeration {

  val BEREAVEMENT_ALLOWANCE = Value("bereavement-allowance")
  val CARERS_ALLOWANCE = Value("carers-allowance")
  val JOBSEEKERS_ALLOWANCE = Value("jobseekers-allowance")
  val INCAPACITY_BENEFIT = Value("incapacity-benefit")
  val EMPLOYMENT_AND_SUPPORT_ALLOWANCE = Value("employment-and-support-allowance")
  val STATE_PENSION = Value("state-pension")
  val OTHER_TAXABLE_BENEFIT = Value("other-taxable-benefit")


  val reads: Reads[Value] = EnumUtils.enumReads(Benefits)
  val writes: Writes[Value] = EnumUtils.enumWrites

  implicit def enumFormats: Format[Value] = EnumUtils.enumFormat(Benefits)

  def getId(benefitValue: String): Identifier = {
    benefitValue match {
      case "bereavement-allowance" => HowMuchBereavementAllowanceId
      case "carers-allowance" => HowMuchCarersAllowanceId
			case "jobseekers-allowance" => HowMuchJobseekersAllowanceId
			case "employment-and-support-allowance" => HowMuchEmploymentAndSupportAllowanceId
			case "incapacity-benefit" => HowMuchIncapacityBenefitId
      case "state-pension" => HowMuchStatePensionId
      case "other-taxable-benefit" => OtherBenefitId
    }
  }

  val sortedBenefits =
    Seq(
      BEREAVEMENT_ALLOWANCE,
      CARERS_ALLOWANCE,
			JOBSEEKERS_ALLOWANCE,
			EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
			INCAPACITY_BENEFIT,
      STATE_PENSION,
      OTHER_TAXABLE_BENEFIT
  )
}

