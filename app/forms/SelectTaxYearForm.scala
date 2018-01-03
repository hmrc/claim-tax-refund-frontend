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
import utils.RadioOption
import models.SelectTaxYear

object SelectTaxYearForm extends FormErrorHelper {

  def apply(): Form[SelectTaxYear] =
    Form(single("value" -> of(selectTaxYearFormatter)))

  def options = SelectTaxYear.values.map {
    value =>
      RadioOption("selectTaxYear", value.toString)
  }

  private def selectTaxYearFormatter = new Formatter[SelectTaxYear] {

    private val errorKeyBlank = "selectTaxYear.blank"

    def bind(key: String, data: Map[String, String]) = data.get(key) match {
      case Some(s) => SelectTaxYear.withName(s)
        .map(Right.apply)
        .getOrElse(produceError(key, "error.unknown"))
      case None => produceError(key, errorKeyBlank)
    }

    def unbind(key: String, value: SelectTaxYear) = Map(key -> value.toString)
  }

  private def optionIsValid(value: String) = options.exists(o => o.value == value)
}
