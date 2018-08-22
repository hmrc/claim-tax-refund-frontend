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

import models._
import models.templates.xml.robots
import play.api.i18n.Messages

import scala.xml.Elem
import scala.xml.XML._

class RobotsXmlHelper(userAnswers: UserAnswers)(implicit messages: Messages) {

  def selectTaxYear: String = userAnswers.selectTaxYear.get.asString

  def incomeAmount(userAnswer: AnyTaxPaid): String = userAnswer match {
    case AnyTaxPaid.Yes(amount) => s"$amount"
    case _ => ""
  }

  def anyIncome(userAnswer: AnyTaxPaid): String = userAnswer match {
    case AnyTaxPaid.Yes(_) => Messages("site.yes")
    case _ => Messages("site.no")
  }

  def anyAgentRef(userAnswer: AnyAgentRef): String = userAnswer match {
    case AnyAgentRef.Yes(_) => Messages("site.yes")
    case _ => Messages("site.no")
  }

  def anyAgentReference(userAnswer: AnyAgentRef): String = userAnswer match {
    case AnyAgentRef.Yes(reference) => s"$reference"
    case _ => ""
  }

  def anyTelephone(userAnswer: TelephoneOption): String = userAnswer match {
    case TelephoneOption.Yes(_) => Messages("site.yes")
    case _ => Messages("site.no")
  }

  def anyTelephoneNumber(userAnswer: TelephoneOption): String = userAnswer match {
    case TelephoneOption.Yes(number) => s"$number"
    case _ => ""
  }

  def otherBenefits(userAnswer: Seq[OtherBenefit]): Seq[Elem] = userAnswer.map {
    value =>
      <otherBenefit><name>{value.name}</name><amount>{value.amount}</amount></otherBenefit>
  }

  def otherCompanyBenefits(userAnswer: Seq[OtherCompanyBenefit]): Seq[Elem] = userAnswer.map {
    value =>
      <companyBenefit><name>{value.name}</name><amount>{value.amount}</amount></companyBenefit>
  }

  def otherTaxableIncome(userAnswer: Seq[OtherTaxableIncome]): Seq[Elem] = userAnswer.map {
    value =>
      <taxableIncome><name>{value.name}</name><amount>{value.amount}</amount></taxableIncome>
  }

  def internationalAddress: Elem = InternationalAddress.toXml(userAnswers.paymentInternationalAddress.get)

  def ukAddress: Elem = UkAddress.toXml(userAnswers.paymentUKAddress.get)

  def lookupAddress: Elem = AddressLookup.toXml(userAnswers.paymentLookupAddress.get)

  def formattedXml: Elem = {
    val xmlString = robots(userAnswers, this)
    val FormattedXmlString = xmlString.toString.replaceAll("\t|\n", "")
    loadString(FormattedXmlString)
  }


}
