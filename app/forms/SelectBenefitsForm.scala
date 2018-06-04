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

package forms

import models.Benefits
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formatter
import play.api.data.validation.{Constraint, Invalid, Valid}

object SelectBenefitsForm extends FormErrorHelper {

  private def selectBenefitsFormatter = new Formatter[Benefits.Value] {
    def bind(key: String, data: Map[String, String]) = data.get(key) match {
      case Some(s) if optionIsValid(s) => Right(Benefits.withName(s))
      case None => produceError(key, "selectBenefits.blank")
      case _ => produceError(key, "error.unknown")
    }

    def unbind(key: String, value: Benefits.Value) = Map(key -> value.toString)
  }

  private def optionIsValid(value: String): Boolean = options.values.toSeq.contains(value)

  private def constraint: Constraint[Set[Benefits.Value]] = Constraint {
    case set if set.nonEmpty =>
      Valid
    case _ =>
      Invalid("selectBenefits.blank")
  }

  def apply(): Form[Set[Benefits.Value]] =
    Form(
      "value" -> set(of(selectBenefitsFormatter)).verifying(constraint)
    )

  def options: Map[String, String] = Benefits.values.map {
    value =>
      s"selectBenefits.$value" -> value.toString
  }.toMap
}
