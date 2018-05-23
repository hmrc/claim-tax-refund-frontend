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

import play.api.libs.json._
import utils.InputOption

sealed trait AgentRef

object AgentRef {

  case class Yes(agentRef: String) extends AgentRef
  case object No extends AgentRef

  def options: Seq[InputOption] = Seq(
    InputOption("true", "site.yes", Some("agentRef_agentRef-form")),
    InputOption("false", "site.no")
  )

  implicit val reads: Reads[AgentRef] = {
    (JsPath \ "anyAgentRef").read[Boolean].flatMap {
      case true =>
        (JsPath \ "agentRef").read[String]
          .map[AgentRef](Yes.apply)
          .orElse(Reads[AgentRef](_ => JsError("AgentRef value expected")))
      case false =>
        Reads.pure(No)
    }
  }

  implicit lazy val writes = new Writes[AgentRef] {
    def writes(o: AgentRef): JsObject = {
      o match {
        case Yes(agentRef) =>
          Json.obj("anyAgentRef" -> true, "agentRef" -> agentRef)
        case _ =>
          Json.obj("anyAgentRef" ->false)
      }
    }
  }
}
