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

  def getSelectTaxYear: String = userAnswers.selectTaxYear.get.asString

  def getIncomeAmount(userAnswer: AnyTaxPaid): String = userAnswer match {
    case AnyTaxPaid.Yes(amount) => s"$amount"
    case _ => ""
  }

  def getAnyIncome(userAnswer: AnyTaxPaid): String = userAnswer match {
    case AnyTaxPaid.Yes(_) => Messages("site.yes")
    case _ => Messages("site.no")
  }

  def getAnyAgentRef(userAnswer: AnyAgentRef): String = userAnswer match {
    case AnyAgentRef.Yes(_) => Messages("site.yes")
    case _ => Messages("site.no")
  }

  def getAnyAgentReference(userAnswer: AnyAgentRef): String = userAnswer match {
    case AnyAgentRef.Yes(reference) => s"$reference"
    case _ => ""
  }

  def getAnyTelephone(userAnswer: TelephoneOption): String = userAnswer match {
    case TelephoneOption.Yes(_) => Messages("site.yes")
    case _ => Messages("site.no")
  }

  def getAnyTelephoneNumber(userAnswer: TelephoneOption): String = userAnswer match {
    case TelephoneOption.Yes(number) => s"$number"
    case _ => ""
  }

  def showBenefits: Boolean = userAnswers.anyBenefits match{
    case Some(true) => true
    case _ => false
  }

  def getOtherBenefits(userAnswer: Seq[OtherBenefit]): Seq[Elem] = {
    val data: Seq[Elem] = userAnswer.map {
      value =>
        loadString(s"<otherBenefit><name>${value.name}</name><amount>${value.amount}</amount></otherBenefit>")
    }
    data
  }

  def showCompanyBenefits: Boolean = userAnswers.anyCompanyBenefits match{
    case Some(true) => true
    case _ => false
  }

  def getOtherCompanyBenefits(userAnswer: Seq[OtherCompanyBenefit]): Seq[Elem] = {
    val data: Seq[Elem] = userAnswer.map {
      value =>
        loadString(s"<companyBenefit><name>${value.name}</name><amount>${value.amount}</amount></companyBenefit>")
    }
    data
  }

  def showTaxableIncome: Boolean = userAnswers.anyTaxableIncome match{
    case Some(true) => true
    case _ => false
  }

  def getOtherTaxableIncome(userAnswer: Seq[OtherTaxableIncome]): Seq[Elem] = {
    val data: Seq[Elem] = userAnswer.map {
      value =>
        loadString(s"<taxableIncome><name>${value.name}</name><amount>${value.amount}</amount></taxableIncome>")
    }
    data
  }

  def xmlCheckBoxFormatter[A](userAnswer: Seq[A]): String = {
    val data = userAnswer.mkString(", ")
    data
  }

  def getInternationalAddress: Elem = InternationalAddress.toXml(userAnswers.paymentInternationalAddress.get)

  def getUkAddress: Elem = UkAddress.toXml(userAnswers.paymentUKAddress.get)

  def getLookupAddress: Elem = AddressLookup.toXml(userAnswers.paymentLookupAddress.get)

  def formatedXml: Elem = {
    val xmlString = robots(userAnswers, this)
    val FormatedXmlString = xmlString.toString.replaceAll("\t|\n", "")
    loadString(FormatedXmlString)
  }


}
