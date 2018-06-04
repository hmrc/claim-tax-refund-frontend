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

object Benefits extends Enumeration {

  val JOBSEEKERS_ALLOWANCE = Value("jobseekers-allowance")
  val INCAPACITY_BENEFIT = Value("incapacity-benefit")
  val EMPLOYMENT_AND_SUPPORT_ALLOWANCE = Value("employment-and-support-allowance")
  val STATE_PENSION = Value("state-pension")
  val OTHER_TAXABLE_BENEFIT = Value("other-taxable-benefit")


  val reads: Reads[Value] = EnumUtils.enumReads(Benefits)
  val writes: Writes[Value] = EnumUtils.enumWrites

  implicit def enumFormats: Format[Value] = EnumUtils.enumFormat(Benefits)
}

