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

import base.SpecBase
import models.{Address, AddressLookup, Country}
import play.api.libs.json.Json

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

    "return correct string format when passed asString from Json" in {
      val address: AddressLookup = testAddress.as[AddressLookup]
      AddressLookup.asString(address) mustBe "Line1<br>Line2<br>Line3<br>Line4<br>NE1 1LX<br>United Kingdom<br>GB"
    }
  }

  val testAddress = {
    Json.parse("{\n    \"auditRef\": \"e9e2fb3f-268f-4c4c-b928-3dc0b17259f2\",\n    \"address\": {\n        \"lines\": [\n            \"Line1\",\n            \"Line2\",\n            \"Line3\",\n            \"Line4\"\n        ],\n        \"postcode\": \"NE1 1LX\",\n        \"country\": {\n            \"code\": \"GB\",\n            \"name\": \"United Kingdom\"\n        }\n    }\n}")
  }
}

