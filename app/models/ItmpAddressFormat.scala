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

import play.api.libs.json.{Format, Json}
import uk.gov.hmrc.auth.core.retrieve.ItmpAddress

import scala.xml._

object ItmpAddressFormat {
  implicit val format: Format[ItmpAddress] = Json.format[ItmpAddress]

  def answeredLines(a: ItmpAddress): Seq[String] = Seq(
      a.line1,
      a.line2,
      a.line3,
      a.line4,
      a.line5,
      a.postCode,
      a.countryName,
      a.countryCode
    ).flatten

  def toXml(a: ItmpAddress): Elem = <itmpAddress>{answeredLines(a).mkString(", ")}</itmpAddress>

  def asString(a: ItmpAddress): String = answeredLines(a).mkString(", <br>")
}
