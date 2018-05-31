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

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formatter
import play.api.data.validation.{Constraint, Invalid, Valid}
import models.CompanyBenefits

object SelectCompanyBenefitsForm extends FormErrorHelper {

  private def selectCompanyBenefitsFormatter = new Formatter[CompanyBenefits.Value] {
    def bind(key: String, data: Map[String, String]) = data.get(key) match {
      case Some(s) if optionIsValid(s) => Right(CompanyBenefits.withName(s))
      case None => produceError(key, "selectCompanyBenefits.blank")
      case _ => produceError(key, "error.unknown")
    }

    def unbind(key: String, value: CompanyBenefits.Value) = Map(key -> value.toString)
  }

  private def optionIsValid(value: String): Boolean = options.values.toSeq.contains(value)

  private def constraint: Constraint[Set[CompanyBenefits.Value]] = Constraint {
    case set if set.nonEmpty =>
      Valid
    case _ =>
      Invalid("selectCompanyBenefits.blank")
  }

  def apply(): Form[Set[CompanyBenefits.Value]] =
    Form(
      "value" -> set(of(selectCompanyBenefitsFormatter)).verifying(constraint)
    )

  def options: Map[String, String] = CompanyBenefits.values.map {
    value =>
      s"selectCompanyBenefits.$value" -> value.toString
  }.toMap
}
