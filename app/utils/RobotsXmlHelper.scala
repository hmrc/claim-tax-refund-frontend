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

  def anyTaxPaid(userAnswer: AnyTaxPaid): String = userAnswer match {
    case AnyTaxPaid.Yes(_) => messages("site.yes")
    case _ => messages("site.no")
  }

  def taxPaidAmount(userAnswer: AnyTaxPaid): String = userAnswer match {
    case AnyTaxPaid.Yes(amount) => s"$amount"
    case _ => ""
  }

  def anyAgentRef(userAnswer: AnyAgentRef): String = userAnswer match {
    case AnyAgentRef.Yes(_) => messages("site.yes")
    case _ => messages("site.no")
  }

  def agentRef(userAnswer: AnyAgentRef): String = userAnswer match {
    case AnyAgentRef.Yes(reference) => s"$reference"
    case _ => ""
  }

  def anyTelephone(userAnswer: TelephoneOption): String = userAnswer match {
    case TelephoneOption.Yes(_) => messages("site.yes")
    case _ => messages("site.no")
  }

  def telephoneNumber(userAnswer: TelephoneOption): String = userAnswer match {
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

  def formattedXml: Elem = loadString(robots(userAnswers, this).toString.replaceAll("\t|\n", ""))
}
