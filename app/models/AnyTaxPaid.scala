/*
 * Copyright 2020 HM Revenue & Customs
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

trait AnyTaxPaid

object AnyTaxPaid {

  case class Yes(taxPaidAmount: String) extends AnyTaxPaid
  case object No extends AnyTaxPaid


  implicit val reads: Reads[AnyTaxPaid] = {
    (JsPath \ "anyTaxPaid").read[Boolean].flatMap {
      case true =>
        (JsPath \ "taxPaidAmount").read[String]
          .map[AnyTaxPaid](Yes.apply)
          .orElse(Reads[AnyTaxPaid](_ => JsError("AnyTaxPaid value expected")))
      case false =>
        Reads.pure(No)
    }
  }

  implicit lazy val writes: Writes[AnyTaxPaid] = new Writes[AnyTaxPaid] {
    def writes(anyTaxPaidObject: AnyTaxPaid): JsObject = {
      anyTaxPaidObject match {
        case Yes(taxPaidAmount) =>
          Json.obj("anyTaxPaid" -> true, "taxPaidAmount" -> taxPaidAmount)
        case _ =>
          Json.obj("anyTaxPaid" -> false)
      }
    }
  }

  def toXml(userAnswer: AnyTaxPaid): NodeSeq = userAnswer match {
    case AnyTaxPaid.Yes(amount) => <anyTaxPaid>Yes</anyTaxPaid><taxPaid>{amount}</taxPaid>
    case _ => <anyTaxPaid>No</anyTaxPaid>
  }
}
