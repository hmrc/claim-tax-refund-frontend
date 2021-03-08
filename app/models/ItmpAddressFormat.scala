/*
 * Copyright 2021 HM Revenue & Customs
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

import scala.xml.Elem

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

  def toXml(a: ItmpAddress): Elem =
    <itmpAddress>
      <addressLine1>{a.line1.getOrElse("")}</addressLine1>
      <addressLine2>{a.line2.getOrElse("")}</addressLine2>
      <addressLine3>{a.line3.getOrElse("")}</addressLine3>
      <addressLine4>{a.line4.getOrElse("")}</addressLine4>
      <addressLine5>{a.line5.getOrElse("")}</addressLine5>
      <postcode>{a.postCode.getOrElse("")}</postcode>
      <country>{a.countryName.getOrElse("")}</country>
      <countryCode>{a.countryCode.getOrElse("")}</countryCode>
    </itmpAddress>

  def asString(a: ItmpAddress): String =  answeredLines(a).mkString("<br>")

}
