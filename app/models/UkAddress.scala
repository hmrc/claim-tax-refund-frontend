/*
 * Copyright 2023 HM Revenue & Customs
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

case class UkAddress(addressLine1: String,
                     addressLine2: String,
                     addressLine3: Option[String],
                     addressLine4: Option[String],
                     addressLine5: Option[String],
                     postcode: String)

object UkAddress {
  implicit val format: Format[UkAddress] = Json.format[UkAddress]

  def answeredLines(a: UkAddress): Seq[String] = Seq(
      Some(a.addressLine1),
      Some(a.addressLine2),
      a.addressLine3,
      a.addressLine4,
      a.addressLine5,
      Some(a.postcode)
    ).flatten

  def toXml(a: UkAddress): Node =
    trim(<paymentAddress>
      <ukAddress>
        <addressLine1>{a.addressLine1}</addressLine1>
        <addressLine2>{a.addressLine2}</addressLine2>
        <addressLine3>{a.addressLine3.getOrElse("")}</addressLine3>
        <addressLine4>{a.addressLine4.getOrElse("")}</addressLine4>
        <addressLine5>{a.addressLine5.getOrElse("")}</addressLine5>
        <postcode>{a.postcode}</postcode>
      </ukAddress>
    </paymentAddress>)

  def asString(a: UkAddress): String = answeredLines(a).mkString("<br>")
}
