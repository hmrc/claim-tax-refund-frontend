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

import models.SelectTaxYear
import play.api.data.Forms._
import play.api.data.format.Formatter
import play.api.data.{Form, FormError}

object SelectTaxYearForm extends FormErrorHelper {

  val dateFormat = "YYYY"

  def apply(): Form[SelectTaxYear] =
    Form(single("value" -> of(selectTaxYearFormatter)))

  private def selectTaxYearFormatter: Formatter[SelectTaxYear] = new Formatter[SelectTaxYear] {

    private val errorKeyBlank = "selectTaxYear.blank"

    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], SelectTaxYear] = data.get(key) match {
      case Some(s) => SelectTaxYear.withName(s)
        .map(Right.apply)
        .getOrElse(produceError(key, "error.unknown"))
      case None => produceError(key, errorKeyBlank)
    }

    def unbind(key: String, value: SelectTaxYear): Map[String, String] = Map(key -> value.toString)
  }
}
