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

sealed trait SelectTaxYear {
  def year: Int
  def asString(implicit messages: Messages): String
}

object SelectTaxYear extends Enumerable[SelectTaxYear] {

  val dateFormat = "YYYY"

  case object CYMinus1 extends WithName("current-year-minus-1") with SelectTaxYear {
    override def year: Int = TaxYear.current.back(1).currentYear
    override def asString(implicit messages: Messages): String =
      messages(s"global.${CYMinus1.toString}", TaxYear.current.back(1).startYear, TaxYear.current.back(1).finishYear)
  }
  case object CYMinus2 extends WithName("current-year-minus-2") with SelectTaxYear {
    override def year: Int = TaxYear.current.back(2).currentYear
    override def asString(implicit messages: Messages): String =
      messages(s"global.${CYMinus2.toString}", TaxYear.current.back(2).startYear, TaxYear.current.back(2).finishYear)
  }
  case object CYMinus3 extends WithName("current-year-minus-3") with SelectTaxYear {
    override def year: Int = TaxYear.current.back(3).currentYear
    override def asString(implicit messages: Messages): String =
      messages(s"global.${CYMinus3.toString}", TaxYear.current.back(3).startYear, TaxYear.current.back(3).finishYear)
  }
  case object CYMinus4 extends WithName("current-year-minus-4") with SelectTaxYear {
    override def year: Int = TaxYear.current.back(4).currentYear
    override def asString(implicit messages: Messages): String =
      messages(s"global.${CYMinus4.toString}", TaxYear.current.back(4).startYear, TaxYear.current.back(4).finishYear)
  }
  case object CYMinus5 extends WithName("current-year-minus-5") with SelectTaxYear {
    override def year: Int = TaxYear.current.back(5).currentYear
    override def asString(implicit messages: Messages): String =
      messages(s"global.${CYMinus5.toString}", TaxYear.current.back(5).startYear, TaxYear.current.back(5).finishYear)
  }

  def options: List[RadioOption] = List(
    taxYearRadioOption(TaxYear.current.back(1), CYMinus1),
    taxYearRadioOption(TaxYear.current.back(2), CYMinus2),
    taxYearRadioOption(TaxYear.current.back(3), CYMinus3),
    taxYearRadioOption(TaxYear.current.back(4), CYMinus4),
    taxYearRadioOption(TaxYear.current.back(5), CYMinus5)
  )

  private def taxYearRadioOption(taxYear: TaxYear, option: SelectTaxYear) =
    new RadioOption(
      id = option.toString,
      value = option.toString,
      message = Message(s"global.$option", taxYear.startYear.toString, taxYear.finishYear.toString)
    )

  val mappings: Map[String, SelectTaxYear] = Map(
    CYMinus1.toString -> CYMinus1,
    CYMinus2.toString -> CYMinus2,
    CYMinus3.toString -> CYMinus3,
    CYMinus4.toString -> CYMinus4,
    CYMinus5.toString -> CYMinus5
  )

  lazy val values: Set[SelectTaxYear] = Set(
    CYMinus1, CYMinus2, CYMinus3, CYMinus4, CYMinus5
  )
}
