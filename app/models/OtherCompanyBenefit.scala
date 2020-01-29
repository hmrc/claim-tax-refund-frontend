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

import play.api.libs.json.Json

import scala.xml.Elem

case class OtherCompanyBenefit(name: String, amount: String)

object OtherCompanyBenefit {
  implicit val format = Json.format[OtherCompanyBenefit]
  implicit val collectionId = "otherCompanyBenefits"

  def toXml(userAnswer: Seq[OtherCompanyBenefit]): Seq[Elem] = userAnswer.map {
    value =>
      <companyBenefit><name>{value.name}</name><amount>{value.amount}</amount></companyBenefit>
  }
}
