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

package utils

import models.{AnyTaxPaid, OtherBenefit, OtherCompanyBenefit, OtherTaxableIncome}
import play.api.i18n.Messages

import scala.xml.Elem

class RobotsXmlHelper(userAnswers: UserAnswers)(implicit messages: Messages) {

  def getSelectTaxYear: String = userAnswers.selectTaxYear.get.asString

  def getIncomeAmount(userAnswer: AnyTaxPaid): String = userAnswer match {
    case AnyTaxPaid.Yes(amount) =>
      s"$amount"
    case _ => ""
  }

  def getAnyIncome(userAnswer: AnyTaxPaid): String = userAnswer match {
    case AnyTaxPaid.Yes(amount) =>
      Messages("site.yes")
    case _ => Messages("site.no")
  }

  def getOtherBenefits(userAnswer: Seq[OtherBenefit]): Elem = {
    val data = <otherTaxableIncome>{userAnswer.map(o => o.toXml)}</otherTaxableIncome>
    data
  }

  def getOtherCompanyBenefits(userAnswer: Seq[OtherCompanyBenefit]): Elem = {
    val data = <otherTaxableIncome>{userAnswer.map(o => o.toXml)}</otherTaxableIncome>
    data
  }

  def getOtherTaxableIncome(userAnswer: Seq[OtherTaxableIncome]): Elem = {
    val data = <otherTaxableIncome>{userAnswer.map(o => o.toXml)}</otherTaxableIncome>
    data
  }

}
