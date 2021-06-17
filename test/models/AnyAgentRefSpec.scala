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
import identifiers.{AgentRefId, AnyAgentRefId}
import models.AnyAgentRef.Yes
import play.api.libs.json._

class AnyAgentRefSpec extends SpecBase {

  "write" must {


    "contain true and agent ref" in {
      Json.toJson(AnyAgentRef.Yes("AB1234"): AnyAgentRef) mustBe
        Json.obj(AnyAgentRefId.toString -> JsBoolean(true), AgentRefId.toString -> JsString("AB1234"))
    }

    "contain false and no agent ref" in {
      Json.toJson(AnyAgentRef.No: AnyAgentRef) mustBe Json.obj(AnyAgentRefId.toString -> JsBoolean(false))
    }
  }

  "reads" must {

    "successfully read true" in {
      val json = Json.obj("anyAgentRef" -> true, "agentRef" -> "1234567")

      Json.fromJson[AnyAgentRef](json).asOpt.value mustEqual AnyAgentRef.Yes("1234567")
    }

    "successfully read false" in {
      val json = Json.obj("anyAgentRef" -> false)

      Json.fromJson[AnyAgentRef](json).asOpt.value mustEqual AnyAgentRef.No
    }

    "return failure for true without agent ref" in {
      val json = Json.obj("anyAgentRef" -> true)

      Json.fromJson[AnyAgentRef](json) mustEqual JsError("AgentRef value expected")
    }

    "return failure for when no input given" in {
      val json = Json.obj("anyAgentRef" -> "notABoolean")
      Json.fromJson[AnyAgentRef](json) mustEqual JsError(Seq((JsPath \ "anyAgentRef", Seq(JsonValidationError(Seq("error.expected.jsboolean"))))))
    }
  }
}
