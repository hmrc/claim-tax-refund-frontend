/*
 * Copyright 2023 HM Revenue & Customs
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

import play.api.libs.json.{Json, OFormat}

import scala.xml.Elem

case class OtherBenefit(name: String, amount: String)

object OtherBenefit {
  implicit val format: OFormat[OtherBenefit] = Json.format[OtherBenefit]
  implicit val collectionId: String = "otherBenefits"

  def toXml(userAnswer: Seq[OtherBenefit]): Seq[Elem] = userAnswer.map {
    value =>
      <otherBenefit><name>{value.name}</name><amount>{value.amount}</amount></otherBenefit>
  }
}
