/*
 * Copyright 2019 HM Revenue & Customs
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

import models.AnyAgentRef
import play.api.data.Forms.tuple
import uk.gov.voa.play.form.ConditionalMappings.mandatoryIfTrue
import play.api.data.Mapping

trait AgentRefMapping extends Mappings {

  def agentRefMapping(requiredMessage: String,
                      requiredAgentRefMessage: String,
                      agentRefLengthKey: String):
  Mapping[AnyAgentRef] = {
    val agentRefMaxLength = 160

    def fromAgentRef(anyAgentRef: AnyAgentRef): (Boolean, Option[String]) = {
      anyAgentRef match {
        case AnyAgentRef.Yes(agentRef) => (true, Some(agentRef))
        case AnyAgentRef.No =>  (false, None)
      }
    }

    def toAgentRef(agentRefTuple: (Boolean, Option[String])) = {

      agentRefTuple match {
        case (true, Some(agentRef))  => AnyAgentRef.Yes(agentRef)
        case (false, None)  => AnyAgentRef.No
        case _ => throw new RuntimeException("Invalid selection")
      }
    }

    tuple("anyAgentRef" -> boolean(requiredMessage),
      "agentRef" -> mandatoryIfTrue(
        "anyAgentRef",
        text(requiredAgentRefMessage).verifying(maxLength(agentRefMaxLength, agentRefLengthKey))
      )
    )
      .transform(toAgentRef, fromAgentRef)
  }

}
