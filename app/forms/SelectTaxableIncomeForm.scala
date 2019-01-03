/*
 * Copyright 2019 HM Revenue & Customs
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

package forms

import models.TaxableIncome
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formatter
import play.api.data.validation.{Constraint, Invalid, Valid}

object SelectTaxableIncomeForm extends FormErrorHelper {

  private def selectBenefitsFormatter = new Formatter[TaxableIncome.Value] {
    def bind(key: String, data: Map[String, String]) = data.get(key) match {
      case Some(s) if optionIsValid(s) => Right(TaxableIncome.withName(s))
      case _ => produceError(key, "error.unknown")
    }

    def unbind(key: String, value: TaxableIncome.Value) = Map(key -> value.toString)
  }

  private def optionIsValid(value: String): Boolean = TaxableIncome.sortedTaxableIncome.map(_.toString).contains(value)

  private def constraint: Constraint[Seq[TaxableIncome.Value]] = Constraint {
    case set if set.nonEmpty =>
      Valid
    case _ =>
      Invalid("selectTaxableIncome.blank")
  }

  def apply(): Form[Seq[TaxableIncome.Value]] =
    Form(
      "value" -> seq(of(selectBenefitsFormatter)).verifying(constraint)
    )

  def options: Seq[(String, String)] = TaxableIncome.sortedTaxableIncome.map {
    value =>
      s"selectTaxableIncome.$value" -> value.toString
  }

}
