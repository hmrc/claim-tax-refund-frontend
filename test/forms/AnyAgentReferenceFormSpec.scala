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

package forms

import forms.mappings.AgentRefMapping
import generators.Generators
import models.AnyAgentRef
import org.scalatest.prop.PropertyChecks
import play.api.data.FormError


class AnyAgentReferenceFormSpec extends FormSpec with AgentRefMapping with PropertyChecks with Generators {

  "AnyAgentRef form" must {
    val form = new AnyAgentReferenceForm()("","anyAgentRef.blankAgentRef")
    val anyAgentRef = "anyAgentRef"
    val agentRef = "agentRef"
    val testAgentRef = "AB1234"

    "bind successfully when yes and agentRef is valid" in {
      val result = form.bind(Map(anyAgentRef -> "true", agentRef -> testAgentRef))
      result.errors.size shouldBe 0
      result.get shouldBe AnyAgentRef.Yes(testAgentRef)
    }

    "bind successfully when no" in {
      val result = form.bind(Map(anyAgentRef -> "false"))
      result.errors.size shouldBe 0
      result.get shouldBe AnyAgentRef.No
    }

    "fail to bind" when {
      "yes is selected but agentRef is not provided" in {
        val result = form.bind(Map(anyAgentRef -> "true"))
        result.errors shouldBe Seq(FormError(agentRef, "anyAgentRef.blankAgentRef"))
      }
      "reason is more than max length" in {
        val maxLength = 160
        forAll(stringsLongerThan(maxLength) -> "longString") {
          string =>
            val result = form.bind(Map(anyAgentRef -> "true", agentRef -> string))
            result.errors shouldBe Seq(FormError(agentRef, "anyAgentRef.maxLength", Seq(maxLength)))
        }
      }
    }

    "Successfully unbind 'AgentRef.Yes'" in {
      val result = form.fill(AnyAgentRef.Yes(testAgentRef)).data
      result should contain(anyAgentRef -> "true")
      result should contain(agentRef -> testAgentRef)
    }
    "Successfully unbind 'AgentRef.No'" in {
      val result = form.fill(AnyAgentRef.No).data
      result should contain(anyAgentRef -> "false")
    }
  }
}
