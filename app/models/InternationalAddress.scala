/*
 * Copyright 2020 HM Revenue & Customs
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

import play.api.libs.json._

import scala.xml.Node
import scala.xml.Utility._

case class InternationalAddress(addressLine1: String,
                                addressLine2: String,
                                addressLine3: Option[String],
                                addressLine4: Option[String],
                                addressLine5: Option[String],
                                country: String)

object InternationalAddress {
  implicit val format: Format[InternationalAddress] = Json.format[InternationalAddress]

  def answeredLines(a: InternationalAddress): Seq[String] = Seq(
      Some(a.addressLine1),
      Some(a.addressLine2),
      a.addressLine3,
      a.addressLine4,
      a.addressLine5,
      Some(a.country)
    ).flatten

  def toXml(a: InternationalAddress): Node =
    trim(<paymentAddress>
      <internationalAddress>
        <addressLine1>{a.addressLine1}</addressLine1>
        <addressLine2>{a.addressLine2}</addressLine2>
        <addressLine3>{a.addressLine3.getOrElse("")}</addressLine3>
        <addressLine4>{a.addressLine4.getOrElse("")}</addressLine4>
        <addressLine5>{a.addressLine5.getOrElse("")}</addressLine5>
        <country>{a.country}</country>
      </internationalAddress>
    </paymentAddress>)

  def asString(a: InternationalAddress): String = answeredLines(a).mkString("<br>")
}
