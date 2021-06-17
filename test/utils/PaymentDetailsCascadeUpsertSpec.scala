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

package utils

import base.SpecBase
import identifiers._
import models.{AnyAgentRef, InternationalAddress, UkAddress, WhereToSendPayment}
import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class PaymentDetailsCascadeUpsertSpec extends SpecBase with ScalaCheckPropertyChecks {

  "Payment details section" must {
    "when answering where to send payment" must {
      "(UK) remove nominee name and agent reference answers when selecting 'Self'" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          WhereToSendPaymentId.toString -> Json.toJson(WhereToSendPayment.Nominee: WhereToSendPayment),
          NomineeFullNameId.toString -> Json.toJson("Test Name"),
          AgentRefId.toString -> Json.toJson(AnyAgentRef.Yes("12345"): AnyAgentRef),
          IsPaymentAddressInTheUKId.toString -> Json.toJson(true),
          PaymentUKAddressId.toString -> Json.toJson(UkAddress("Road", "Town", None, None, None, "NE1 1LX"))
        ))
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert(WhereToSendPaymentId.toString, Json.toJson(WhereToSendPayment.Myself: WhereToSendPayment), originalCacheMap)
        result.data mustBe Map(
          WhereToSendPaymentId.toString -> Json.toJson(WhereToSendPayment.Myself: WhereToSendPayment)
        )
      }

      "(International) remove nominee name and agent reference answers when selecting 'Self'" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          WhereToSendPaymentId.toString -> Json.toJson(WhereToSendPayment.Nominee: WhereToSendPayment),
          NomineeFullNameId.toString -> Json.toJson("Test Name"),
          AgentRefId.toString -> Json.toJson(AnyAgentRef.Yes("12345"): AnyAgentRef),
          IsPaymentAddressInTheUKId.toString -> Json.toJson(false),
          PaymentInternationalAddressId.toString ->
            Json.toJson(InternationalAddress("Line 1", "Line 2", Some("Line 3"), Some("Line 4"), Some("Line 5"), "Line 6"))
        ))
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert(WhereToSendPaymentId.toString, Json.toJson(WhereToSendPayment.Myself: WhereToSendPayment), originalCacheMap)
        result.data mustBe Map(
          WhereToSendPaymentId.toString -> Json.toJson(WhereToSendPayment.Myself: WhereToSendPayment)
        )
      }

      "(Address Correct) remove is payment address correct when selecting 'Nominee'" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          WhereToSendPaymentId.toString -> Json.toJson(WhereToSendPayment.Myself: WhereToSendPayment),
          PaymentAddressCorrectId.toString -> Json.toJson(true)

        ))
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert(WhereToSendPaymentId.toString, Json.toJson(WhereToSendPayment.Nominee: WhereToSendPayment), originalCacheMap)
        result.data mustBe Map(
          WhereToSendPaymentId.toString -> Json.toJson(WhereToSendPayment.Nominee: WhereToSendPayment)
        )
      }

      "(UK) remove is payment address correct when selecting 'Nominee'" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          WhereToSendPaymentId.toString -> Json.toJson(WhereToSendPayment.Myself: WhereToSendPayment),
          PaymentAddressCorrectId.toString -> Json.toJson(false),
          IsPaymentAddressInTheUKId.toString -> Json.toJson(true),
          PaymentUKAddressId.toString -> Json.toJson(UkAddress("Road", "Town", None, None, None, "NE1 1LX"))
        ))
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert(WhereToSendPaymentId.toString, Json.toJson(WhereToSendPayment.Nominee: WhereToSendPayment), originalCacheMap)
        result.data mustBe Map(
          WhereToSendPaymentId.toString -> Json.toJson(WhereToSendPayment.Nominee: WhereToSendPayment)
        )
      }

      "(International) remove is payment address correct when selecting 'Nominee'" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          WhereToSendPaymentId.toString -> Json.toJson(WhereToSendPayment.Myself: WhereToSendPayment),
          PaymentAddressCorrectId.toString -> Json.toJson(false),
          IsPaymentAddressInTheUKId.toString -> Json.toJson(false),
          PaymentInternationalAddressId.toString ->
            Json.toJson(InternationalAddress("Line 1", "Line 2", Some("Line 3"), Some("Line 4"), Some("Line 5"), "Line 6"))
        ))
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert(WhereToSendPaymentId.toString, Json.toJson(WhereToSendPayment.Nominee: WhereToSendPayment), originalCacheMap)
        result.data mustBe Map(
          WhereToSendPaymentId.toString -> Json.toJson(WhereToSendPayment.Nominee: WhereToSendPayment)
        )
      }
    }

    "(UK) remove details when 'yes' is selected on is Correct Address" in {
      val originalCacheMap = new CacheMap(id = "test", Map(
        PaymentAddressCorrectId.toString -> Json.toJson(false),
        IsPaymentAddressInTheUKId.toString -> Json.toJson(true),
        PaymentUKAddressId.toString -> Json.toJson(UkAddress("Road", "Town", None, None, None, "NE1 1LX"))
      ))
      val cascadeUpsert = new CascadeUpsert
      val result = cascadeUpsert(PaymentAddressCorrectId.toString, JsBoolean(true), originalCacheMap)
      result.data mustBe Map(
        PaymentAddressCorrectId.toString -> Json.toJson(true)
      )
    }

    "(International) remove details when 'yes' is selected on is Correct Address" in {
      val originalCacheMap = new CacheMap(id = "test", Map(
        PaymentAddressCorrectId.toString -> Json.toJson(false),
        IsPaymentAddressInTheUKId.toString -> Json.toJson(false),
        PaymentInternationalAddressId.toString ->
          Json.toJson(InternationalAddress("Line 1", "Line 2", Some("Line 3"), Some("Line 4"), Some("Line 5"), "Line 6"))
      ))
      val cascadeUpsert = new CascadeUpsert
      val result = cascadeUpsert(PaymentAddressCorrectId.toString, JsBoolean(true), originalCacheMap)
      result.data mustBe Map(
        PaymentAddressCorrectId.toString -> Json.toJson(true)
      )
    }

    "remove is address in the UK when 'yes' is selected on is Correct Address" in {
      val originalCacheMap = new CacheMap(id = "test", Map(
        PaymentAddressCorrectId.toString -> Json.toJson(false),
        IsPaymentAddressInTheUKId.toString -> Json.toJson(true)
      ))
      val cascadeUpsert = new CascadeUpsert
      val result = cascadeUpsert(PaymentAddressCorrectId.toString, JsBoolean(true), originalCacheMap)
      result.data mustBe Map(
        PaymentAddressCorrectId.toString -> Json.toJson(true)
      )
    }

    "for address in the UK" must {
      "remove UK address details when 'No' is selected" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          IsPaymentAddressInTheUKId.toString -> Json.toJson(true),
          PaymentUKAddressId.toString -> Json.toJson(UkAddress("Road", "Town", None, None, None, "NE1 1LX"))
        ))
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert(IsPaymentAddressInTheUKId.toString, JsBoolean(false), originalCacheMap)
        result.data mustBe Map(
          IsPaymentAddressInTheUKId.toString -> Json.toJson(false)
        )
      }

      "remove international address details when 'Yes' is selected" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          IsPaymentAddressInTheUKId.toString -> Json.toJson(false),
          PaymentInternationalAddressId.toString ->
            Json.toJson(InternationalAddress("Line 1", "Line 2", Some("Line 3"), Some("Line 4"), Some("Line 5"), "Line 6"))
        ))
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert(IsPaymentAddressInTheUKId.toString, JsBoolean(true), originalCacheMap)
        result.data mustBe Map(
          IsPaymentAddressInTheUKId.toString -> Json.toJson(true)
        )
      }
    }
  }
}