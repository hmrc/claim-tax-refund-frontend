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

package models

import play.api.i18n.Messages
import uk.gov.hmrc.time.TaxYear
import utils._

trait SelectTaxYear {
  def year: Int
  def asString(implicit messages: Messages): String
}

object SelectTaxYear extends Enumerable[SelectTaxYear] {

  val dateFormat = "YYYY"

  case object CYMinus1 extends WithName("current-year-minus-1") with SelectTaxYear {
    override def year: Int = TaxYear.current.back(1).currentYear
    override def asString(implicit messages: Messages): String =
      messages(
        s"global.${CYMinus1.toString}",
        TaxYear.current.back(1).startYear.toString.format(dateFormat),
        TaxYear.current.back(1).finishYear.toString.format(dateFormat)
      )
  }
  case object CYMinus2 extends WithName("current-year-minus-2") with SelectTaxYear {
    override def year: Int = TaxYear.current.back(2).currentYear
    override def asString(implicit messages: Messages): String =
      messages(
        s"global.${CYMinus2.toString}",
        TaxYear.current.back(2).startYear.toString.format(dateFormat),
        TaxYear.current.back(2).finishYear.toString.format(dateFormat)
      )
  }
  case object CYMinus3 extends WithName("current-year-minus-3") with SelectTaxYear {
    override def year: Int = TaxYear.current.back(3).currentYear
    override def asString(implicit messages: Messages): String =
      messages(
        s"global.${CYMinus3.toString}",
        TaxYear.current.back(3).startYear.toString.format(dateFormat),
        TaxYear.current.back(3).finishYear.toString.format(dateFormat)
      )
  }
  case object CYMinus4 extends WithName("current-year-minus-4") with SelectTaxYear {
    override def year: Int = TaxYear.current.back(4).currentYear
    override def asString(implicit messages: Messages): String =
      messages(
        s"global.${CYMinus4.toString}",
        TaxYear.current.back(4).startYear.toString.format(dateFormat),
        TaxYear.current.back(4).finishYear.toString.format(dateFormat)
      )
  }

  case class CustomTaxYear(year: Int) extends SelectTaxYear {
    override def asString(implicit messages: Messages): String = s"6 April $year to 5 April ${year + 1}"
  }

  private val regex = "CustomTaxYear\\(([0-9]+)\\)".r
  def harnessReads: PartialFunction[String, SelectTaxYear] = PartialFunction[String, SelectTaxYear] {
    case regex(y) => CustomTaxYear(y.toInt)
  }

  def options: Seq[RadioOption] = Seq(
    taxYearRadioOption(TaxYear.current.back(1), CYMinus1),
    taxYearRadioOption(TaxYear.current.back(2), CYMinus2),
    taxYearRadioOption(TaxYear.current.back(3), CYMinus3),
    taxYearRadioOption(TaxYear.current.back(4), CYMinus4)
  )

  private def taxYearRadioOption(taxYear: TaxYear, option: SelectTaxYear) =
    RadioOption(
      keyPrefix   = "global",
      option      = option.toString,
      messageArgs = Seq(taxYear.startYear.toString.format(dateFormat), taxYear.finishYear.toString.format(dateFormat)): _*
    )

  def values: Set[SelectTaxYear] = Set(
    CYMinus1, CYMinus2, CYMinus3, CYMinus4
  )
}
