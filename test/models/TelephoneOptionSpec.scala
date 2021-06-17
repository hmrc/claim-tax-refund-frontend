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

import base.SpecBase
import identifiers.{AnyTelephoneId, TelephoneNumberId}
import play.api.libs.json._

class TelephoneOptionSpec extends SpecBase {

  "write" must {

    "contain true and telephone number" in {
      Json.toJson(TelephoneOption.Yes("0191 1111 111"): TelephoneOption) mustBe
        Json.obj(AnyTelephoneId.toString -> JsBoolean(true), TelephoneNumberId.toString -> JsString("0191 1111 111"))
    }

    "contain false and no telephone number" in {
      Json.toJson(TelephoneOption.No: TelephoneOption) mustBe Json.obj(AnyTelephoneId.toString -> JsBoolean(false))
    }
  }

  "reads" must {

    "successfully read true" in {
      val json = Json.obj("anyTelephone" -> true, "telephoneNumber" -> "1234567")

      Json.fromJson[TelephoneOption](json).asOpt.value mustEqual TelephoneOption.Yes("1234567")
    }

    "successfully read false" in {
      val json = Json.obj("anyTelephone" -> false)

      Json.fromJson[TelephoneOption](json).asOpt.value mustEqual TelephoneOption.No
    }

    "return failure for true without telephone number" in {
      val json = Json.obj("anyTelephone" -> true)

      Json.fromJson[TelephoneOption](json) mustEqual JsError("TelephoneOption value expected")
    }

    "return failure for when no input given" in {
      val json = Json.obj("anyTelephone" -> "notABoolean")
      Json.fromJson[TelephoneOption](json) mustEqual JsError(Seq((JsPath \ "anyTelephone", Seq(JsonValidationError(Seq("error.expected.jsboolean"))))))
    }
  }
}
