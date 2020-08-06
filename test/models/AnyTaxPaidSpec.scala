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

import base.SpecBase
import identifiers.{AnyTaxPaidId, TaxPaidAmountId}
import play.api.libs.json._

class AnyTaxPaidSpec extends SpecBase {

  private val testAmount = "9,999.99"

  "write" must {


    "contain true and tax paid amount" in {
      Json.toJson(AnyTaxPaid.Yes(testAmount)) mustBe
        Json.obj(AnyTaxPaidId.toString -> JsBoolean(true), TaxPaidAmountId.toString -> JsString(testAmount))
    }

    "contain false and tax paid amount" in {
      Json.toJson(AnyTaxPaid.No) mustBe Json.obj(AnyTaxPaidId.toString -> JsBoolean(false))
    }
  }

  "reads" must {

    "successfully read true" in {
      val json = Json.obj("anyTaxPaid" -> true, "taxPaidAmount" -> testAmount)

      Json.fromJson[AnyTaxPaid](json).asOpt.value mustEqual AnyTaxPaid.Yes(testAmount)
    }

    "successfully read false" in {
      val json = Json.obj("anyTaxPaid" -> false)

      Json.fromJson[AnyTaxPaid](json).asOpt.value mustEqual AnyTaxPaid.No
    }

    "return failure for true without any tax paid" in {
      val json = Json.obj("anyTaxPaid" -> true)

      Json.fromJson[AnyTaxPaid](json) mustEqual JsError("AnyTaxPaid value expected")
    }

    "return failure for when no input given" in {
      val json = Json.obj("anyTaxPaid" -> "notABoolean")
      Json.fromJson[AnyTaxPaid](json) mustEqual JsError(Seq((JsPath \ "anyTaxPaid", Seq(JsonValidationError(Seq("error.expected.jsboolean"))))))
    }
  }

}
