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

import models.SelectTaxYear
import models.SelectTaxYear.{CYMinus1, CYMinus2, CYMinus3, CYMinus4, CYMinus5}
import play.api.data.{Form, FormError}
import play.api.data.Forms._
import play.api.data.format.Formatter
import play.api.i18n.Messages
import uk.gov.hmrc.time.TaxYearResolver
import utils.RadioOption

object SelectTaxYearForm extends FormErrorHelper {

  val dateFormat = "YYYY"

  def apply(): Form[SelectTaxYear] =
    Form(single("value" -> of(selectTaxYearFormatter)))

  def options(implicit messages: Messages): Seq[RadioOption] = Seq(
    RadioOption(
      CYMinus1.toString, CYMinus1.toString, messages(s"selectTaxYear.${CYMinus1.toString}",
        TaxYearResolver.startOfCurrentTaxYear.minusYears(1).toString(dateFormat),
        TaxYearResolver.endOfCurrentTaxYear.minusYears(1).toString(dateFormat))),
    RadioOption(
      CYMinus2.toString, CYMinus2.toString, messages(s"selectTaxYear.${CYMinus2.toString}",
        TaxYearResolver.startOfCurrentTaxYear.minusYears(2).toString(dateFormat),
        TaxYearResolver.endOfCurrentTaxYear.minusYears(2).toString(dateFormat))),
    RadioOption(
      CYMinus3.toString, CYMinus3.toString, messages(s"selectTaxYear.${CYMinus3.toString}",
        TaxYearResolver.startOfCurrentTaxYear.minusYears(3).toString(dateFormat),
        TaxYearResolver.endOfCurrentTaxYear.minusYears(3).toString(dateFormat))),
    RadioOption(
      CYMinus4.toString, CYMinus4.toString, messages(s"selectTaxYear.${CYMinus4.toString}",
        TaxYearResolver.startOfCurrentTaxYear.minusYears(4).toString(dateFormat),
        TaxYearResolver.endOfCurrentTaxYear.minusYears(4).toString(dateFormat))),
    RadioOption(
      CYMinus5.toString, CYMinus5.toString, messages(s"selectTaxYear.${CYMinus5.toString}",
        TaxYearResolver.startOfCurrentTaxYear.minusYears(5).toString(dateFormat),
        TaxYearResolver.endOfCurrentTaxYear.minusYears(5).toString(dateFormat)))
  )

  private def selectTaxYearFormatter: Formatter[SelectTaxYear] = new Formatter[SelectTaxYear] {

    private val errorKeyBlank = "selectTaxYear.blank"

    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], SelectTaxYear] = data.get(key) match {
      case Some(s) => SelectTaxYear.withName(s)
        .map(Right.apply)
        .getOrElse(produceError(key, "error.unknown"))
      case None => produceError(key, errorKeyBlank)
    }

    def unbind(key: String, value: SelectTaxYear) = Map(key -> value.toString)
  }
}
