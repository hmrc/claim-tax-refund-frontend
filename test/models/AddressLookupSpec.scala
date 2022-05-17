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

import base.SpecBase
import models.{Address, AddressLookup, Country}
import play.api.libs.json.Json

import scala.xml.Node
import scala.xml.Utility._

class AddressLookupSpec extends SpecBase {
  "AddressLookup" must {
    "return correct model when passed Json" in {
      val address = testAddress.as[AddressLookup]
      address mustBe new AddressLookup(
        address = Some(
          Address(
            lines = Some(Seq("Line1", "Line2", "Line3", "Line4")),
            postcode = Some("NE1 1LX"),
            country = Some(Country(Some("United Kingdom"),Some("GB")))
          )),
        auditRef = Some("e9e2fb3f-268f-4c4c-b928-3dc0b17259f2")
      )
    }

    "return correct xml when passed address" in {
      val address: AddressLookup = testAddress.as[AddressLookup]
      val responseElem: Node =
        trim(<paymentAddress>
          <lookupAddress>
            <addressLine1>Line1</addressLine1>
            <addressLine2>Line2</addressLine2>
            <addressLine3>Line3</addressLine3>
            <addressLine4>Line4</addressLine4>
            <addressLine5></addressLine5>
            <postcode>NE1 1LX</postcode>
            <country>United Kingdom</country>
            <countryCode>GB</countryCode>
          </lookupAddress>
        </paymentAddress>)

      AddressLookup.toXml(address) mustBe responseElem
    }

    "return correct string format when passed asString from Json" in {
      val address: AddressLookup = testAddress.as[AddressLookup]
      AddressLookup.asString(address) mustBe "Line1<br>Line2<br>Line3<br>Line4<br>NE1 1LX<br>United Kingdom<br>GB"
    }

    "return correct string format when passed asString missing postcode" in {
      val address = new AddressLookup(
        address = Some(
          Address(
            lines = Some(Seq("Line1", "Line2", "Line3", "Line4")),
            postcode = Some("NE1 1LX"),
            country = Some(Country(Some("United Kingdom"),Some("GB")))
          )),
        auditRef = Some("e9e2fb3f-268f-4c4c-b928-3dc0b17259f2")
      )
      AddressLookup.asString(address) mustBe "Line1<br>Line2<br>Line3<br>Line4<br>NE1 1LX<br>United Kingdom<br>GB"
    }
  }

  val testAddress = {
    Json.parse(input =
      """{
        |"auditRef": "e9e2fb3f-268f-4c4c-b928-3dc0b17259f2",
        |"address": {
        |   "lines": ["Line1","Line2","Line3","Line4"],
        |   "postcode":"NE1 1LX",
        |   "country": {
        |       "code": "GB",
        |       "name": "United Kingdom"
        |   }
        |}
        |}""".stripMargin)
  }
}

