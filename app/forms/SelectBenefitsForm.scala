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

package forms

import models.Benefits
import play.api.data.Forms._
import play.api.data.format.Formatter
import play.api.data.validation.{Constraint, Invalid, Valid}
import play.api.data.{Form, FormError}

object SelectBenefitsForm extends FormErrorHelper {

  private def selectBenefitsFormatter: Formatter[Benefits.Value] = new Formatter[Benefits.Value] {
    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Benefits.Value] = data.get(key) match {
      case Some(s) if optionIsValid(s) => Right(Benefits.withName(s))
      case None => produceError(key, "selectBenefits.blank")
      case _ => produceError(key, "error.unknown")
    }

    def unbind(key: String, value: Benefits.Value) = Map(key -> value.toString)
  }

  private def optionIsValid(value: String): Boolean = Benefits.sortedBenefits.map(_.toString).contains(value)

  private def constraint: Constraint[Seq[Benefits.Value]] = Constraint {
    case set if set.nonEmpty =>
      Valid
    case _ =>
      Invalid("selectBenefits.blank")
  }

  def apply(): Form[Seq[Benefits.Value]] =
    Form(
      "value" -> seq(of(selectBenefitsFormatter)).verifying(constraint)
    )

  def options: Seq[(String, String)] = Benefits.sortedBenefits.map {
    value =>
      s"selectBenefits.$value" -> value.toString
  }
}
