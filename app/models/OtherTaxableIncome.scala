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

import play.api.libs.json.{Format, Json}

import scala.xml.Elem

case class OtherTaxableIncome(name: String, amount: String, anyTaxPaid: Option[AnyTaxPaid] = None)

object OtherTaxableIncome {
  implicit val format: Format[OtherTaxableIncome] = Json.format[OtherTaxableIncome]
  implicit val collectionId = "otherTaxableIncome"


  def toXml(userAnswer: Seq[OtherTaxableIncome]): Seq[Elem] = userAnswer.map {
    value =>
      <taxableIncome><name>{value.name}</name><amount>{value.amount}</amount></taxableIncome>
  }
}
