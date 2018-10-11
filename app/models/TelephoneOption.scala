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

import scala.xml.NodeSeq

sealed trait TelephoneOption

object TelephoneOption {

  case class Yes(number: String) extends TelephoneOption
  case object No extends TelephoneOption

  implicit val reads: Reads[TelephoneOption] = {
    (JsPath \ "anyTelephone").read[Boolean].flatMap {
      case true =>
        (JsPath \ "telephoneNumber").read[String]
          .map[TelephoneOption](Yes.apply)
          .orElse(Reads[TelephoneOption](_ => JsError("TelephoneOption value expected")))
      case false =>
        Reads.pure(No)
    }
  }

  implicit lazy val writes: Writes[TelephoneOption] = new Writes[TelephoneOption] {
    def writes(telephoneObject: TelephoneOption): JsObject = {
      telephoneObject match {
        case Yes(number) =>
          Json.obj("anyTelephone" -> true, "telephoneNumber" -> number)
        case _ =>
          Json.obj("anyTelephone" ->false)
      }
    }
  }

  def toXml(userAnswer: TelephoneOption): NodeSeq = userAnswer match {
    case TelephoneOption.Yes(telephoneNumber) => <anyTelephoneNumber>Yes</anyTelephoneNumber><telephoneNumber>{telephoneNumber}</telephoneNumber>
    case _ => <anyTelephoneNumber>No</anyTelephoneNumber>
  }

  def asString(userAnswer: TelephoneOption): String = userAnswer match {
    case TelephoneOption.Yes(telephoneNumber) => telephoneNumber
    case _ => "No"
  }

}
