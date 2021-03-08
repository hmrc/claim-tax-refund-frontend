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

import play.api.libs.json._

import scala.xml.NodeSeq

sealed trait AnyAgentRef

object AnyAgentRef {

  case class Yes(agentRef: String) extends AnyAgentRef
  case object No extends AnyAgentRef

  implicit val reads: Reads[AnyAgentRef] = {
    (JsPath \ "anyAgentRef").read[Boolean].flatMap {
      case true =>
        (JsPath \ "agentRef").read[String]
          .map[AnyAgentRef](Yes.apply)
          .orElse(Reads[AnyAgentRef](_ => JsError("AgentRef value expected")))
      case false =>
        Reads.pure(No)
    }
  }

  implicit lazy val writes: Writes[AnyAgentRef] = new Writes[AnyAgentRef] {
    def writes(agentRefObject: AnyAgentRef): JsObject = {
      agentRefObject match {
        case Yes(agentRef) =>
          Json.obj("anyAgentRef" -> true, "agentRef" -> agentRef)
        case _ =>
          Json.obj("anyAgentRef" ->false)
      }
    }
  }

  def toXml(userAnswer: AnyAgentRef): NodeSeq = userAnswer match {
    case AnyAgentRef.Yes(agentRef) => <anyAgentRef>Yes</anyAgentRef><agentReference>{agentRef}</agentReference>
    case _ => <anyAgentRef>No</anyAgentRef>
  }
}
