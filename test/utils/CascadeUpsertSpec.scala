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

package utils

import base.SpecBase
import identifiers._
import models._
import org.scalacheck.{Gen, Shrink}
import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap
import org.scalatest.prop.PropertyChecks

class CascadeUpsertSpec extends SpecBase with PropertyChecks {

  implicit def dontShrink[A]: Shrink[A] = Shrink.shrinkAny

  "using the apply method for a key that has no special function" when {
    "the key doesn't already exists" must {
      "add the key to the cache map" in {
        val originalCacheMap = new CacheMap("id", Map())
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert("key", "value", originalCacheMap)
        result.data mustBe Map("key" -> JsString("value"))
      }
    }

    "data already exists for that key" must {
      "replace the value held against the key" in {
        val originalCacheMap = new CacheMap("id", Map("key" -> JsString("original value")))
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert("key", "new value", originalCacheMap)
        result.data mustBe Map("key" -> JsString("new value"))
      }
    }
  }

  "addRepeatedValue" when {
    "the key doesn't already exist" must {
      "add the key to the cache map and save the value in a sequence" in {
        val originalCacheMap = new CacheMap("id", Map())
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert.addRepeatedValue("key", "value", originalCacheMap)
        result.data mustBe Map("key" -> Json.toJson(Seq("value")))
      }
    }

    "the key already exists" must {
      "add the new value to the existing sequence" in {
        val originalCacheMap = new CacheMap("id", Map("key" -> Json.toJson(Seq("value"))))
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert.addRepeatedValue("key", "new value", originalCacheMap)
        result.data mustBe Map("key" -> Json.toJson(Seq("value", "new value")))
      }
    }
  }

  //Claim details section

  "Claim details section" must {
    "Answering 'Yes' on EmploymentDetailsController" must {
      "remove data from next 2 screens" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          EmploymentDetailsId.toString -> Json.toJson(false),
          EnterPayeReferenceId.toString -> Json.toJson("123/AB1234"),
          DetailsOfEmploymentOrPensionId.toString -> Json.toJson("some details")
        ))
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert(EmploymentDetailsId.toString, JsBoolean(true), originalCacheMap)
        result.data.size mustBe 1
        result.data.contains(EmploymentDetailsId.toString) mustBe true
        result.data.contains(EnterPayeReferenceId.toString) mustBe false
        result.data.contains(DetailsOfEmploymentOrPensionId.toString) mustBe false
      }
    }
  }

  //Payment details section

  "Payment details section" must {
    "when answering where to send payment" must {
      "remove nominee name and agent reference answers when selecting 'Self'" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          WhereToSendPaymentId.toString -> Json.toJson(WhereToSendPayment.Nominee),
          NomineeFullNameId.toString -> Json.toJson("Test Name"),
          AgentRefId.toString -> Json.toJson(AnyAgentRef.Yes("12345"))
        ))
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert (WhereToSendPaymentId.toString, Json.toJson(WhereToSendPayment.Myself), originalCacheMap)
        result.data.size mustBe 1
        result.data.contains(WhereToSendPaymentId.toString) mustBe true
        result.data.contains(NomineeFullNameId.toString) mustBe false
        result.data.contains(AgentRefId.toString) mustBe false
      }

      "remove is payment address correct when selecting 'Nominee'" in {
        1 mustBe 2
      }
    }

    "remove remaining journey when 'yes' is selected"in {
        1 mustBe 2

    }

    "for address in the UK" must {
      "remove UK address details when 'No' is selected" in {
        1 mustBe 2
      }

      "remove international address details when 'Yes' is selected" in {
        1 mustBe 2
      }
    }





  }
}
