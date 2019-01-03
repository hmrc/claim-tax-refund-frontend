/*
 * Copyright 2019 HM Revenue & Customs
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

case class Country (name: Option[String], code: Option[String])

object Country {
  implicit val format: Format[Country] = Json.format[Country]
}

case class Address (lines: Option[Seq[String]],
                    postcode: Option[String],
                    country: Option[Country])

object Address {
  implicit val format: Format[Address] = Json.format[Address]
}

final case class AddressLookup (address: Option[Address], auditRef: Option[String])

object AddressLookup {
  implicit val format: Format[AddressLookup] = Json.format[AddressLookup]

  def toXml(a: AddressLookup): Node =
    trim(<paymentAddress>
      <lookupAddress>
        <addressLine1>{a.address.get.lines.get.headOption.getOrElse("")}</addressLine1>
        <addressLine2>{a.address.get.lines.get.lift(1).getOrElse("")}</addressLine2>
        <addressLine3>{a.address.get.lines.get.lift(2).getOrElse("")}</addressLine3>
        <addressLine4>{a.address.get.lines.get.lift(3).getOrElse("")}</addressLine4>
        <addressLine5>{a.address.get.lines.get.lift(4).getOrElse("")}</addressLine5>
        <postcode>{a.address.get.postcode.getOrElse("")}</postcode>
        <country>{a.address.get.country.map(country => country.name.getOrElse("")).getOrElse("")}</country>
        <countryCode>{a.address.get.country.map(country => country.code.getOrElse("")).getOrElse("")}</countryCode>
      </lookupAddress>
    </paymentAddress>)

  def completedAddress(a: AddressLookup): Seq[String] = Seq(
		if (a.address.get.postcode.isDefined) {
			a.address.get.lines.get :+
				a.address.get.postcode.get :+
				a.address.get.country.get.name.get :+
				a.address.get.country.get.code.get
		} else {
			a.address.get.lines.get :+
				a.address.get.country.get.name.get :+
				a.address.get.country.get.code.get
		}
    ).flatten

  def asString(a: AddressLookup): String = completedAddress(a).mkString("<br>")
}


