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

package models

import base.SpecBase
import identifiers.{AgentRefId, AnyAgentRefId}
import play.api.data.validation.ValidationError
import play.api.libs.json._

class AgentRefSpec extends SpecBase {

  "write" must {

    "contain true and agent ref" in {
      Json.toJson(AgentRef.Yes("AB1234")) mustBe
        Json.obj(AnyAgentRefId.toString -> JsBoolean(true), AgentRefId.toString -> JsString("AB1234"))
    }

    "contain false and no agent ref" in {
      Json.toJson(AgentRef.No) mustBe Json.obj(AnyAgentRefId.toString -> JsBoolean(false))
    }
  }

  "reads" must {

    "successfully read true" in {
      val json = Json.obj("anyAgentRef" -> true, "agentRef" -> "1234567")

      Json.fromJson[AgentRef](json).asOpt.value mustEqual AgentRef.Yes("1234567")
    }

    "successfully read false" in {
      val json = Json.obj("anyAgentRef" -> false)

      Json.fromJson[AgentRef](json).asOpt.value mustEqual AgentRef.No
    }

    "return failure for true without agent ref" in {
      val json = Json.obj("anyAgentRef" -> true)

      Json.fromJson[AgentRef](json) mustEqual JsError("AgentRef value expected")
    }

    "return failure for when no input given" in {
      val json = Json.obj("anyAgentRef" -> "notABoolean")
      Json.fromJson[AgentRef](json) mustEqual JsError(Seq((JsPath \ "anyAgentRef", Seq(ValidationError(Seq("error.expected.jsboolean"))))))
    }
  }
}
