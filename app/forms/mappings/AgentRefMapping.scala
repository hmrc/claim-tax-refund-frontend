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

package forms.mappings

import models.AgentRef
import play.api.data.Forms.tuple
import uk.gov.voa.play.form.ConditionalMappings.mandatoryIfTrue
import play.api.data.Mapping

trait AgentRefMapping extends Mappings {

  def agentRefMapping(requiredKey: String,
                    requiredAgentRefKey: String,
                    agentRefLengthKey: String):
  Mapping[AgentRef] = {
    val agentRefMaxLength = 160

    def fromAgentRef(agentRef: AgentRef): (Boolean, Option[String]) = {
      agentRef match {
        case AgentRef.Yes(agentRefNo) => (true, Some(agentRefNo))
        case AgentRef.No =>  (false, None)
      }
    }

    def toAgentRef(agentRefTuple: (Boolean, Option[String])) = {

      agentRefTuple match {
        case (true, Some(agentRef))  => AgentRef.Yes(agentRef)
        case (false, None)  => AgentRef.No
        case _ => throw new RuntimeException("Invalid selection")
      }
    }

    tuple("anyAgentRef" -> boolean(requiredKey),
      "agentRef" -> mandatoryIfTrue("agentRef.anyAgentRef", text(requiredAgentRefKey).verifying(maxLength(agentRefMaxLength,agentRefLengthKey))))
      .transform(toAgentRef, fromAgentRef)
  }

}
