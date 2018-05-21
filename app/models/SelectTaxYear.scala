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

package models

import uk.gov.hmrc.time.TaxYearResolver
import utils.{Enumerable, WithName}

sealed trait SelectTaxYear {
  def year: Int
  def asString: String
}

object SelectTaxYear extends Enumerable[SelectTaxYear] {

  val dateFormat = "dd MMMM YYYY"

  case object CYMinus2 extends WithName("current-year-minus-2") with SelectTaxYear {
    override def year: Int = TaxYearResolver.startOfCurrentTaxYear.minusYears(2).getYear
    override def asString: String = TaxYearResolver.startOfCurrentTaxYear.minusYears(2).toString(dateFormat) + " to " +
      TaxYearResolver.endOfCurrentTaxYear.minusYears(2).toString(dateFormat)
  }

  case object CYMinus3 extends WithName("current-year-minus-3") with SelectTaxYear {
    override def year: Int = TaxYearResolver.startOfCurrentTaxYear.minusYears(3).getYear
    override def asString: String = TaxYearResolver.startOfCurrentTaxYear.minusYears(3).toString(dateFormat) + " to " +
      TaxYearResolver.endOfCurrentTaxYear.minusYears(3).toString(dateFormat)
  }

  case object CYMinus4 extends WithName("current-year-minus-4") with SelectTaxYear {
    override def year: Int = TaxYearResolver.startOfCurrentTaxYear.minusYears(4).getYear
    override def asString: String = TaxYearResolver.startOfCurrentTaxYear.minusYears(4).toString(dateFormat) + " to " +
      TaxYearResolver.endOfCurrentTaxYear.minusYears(4).toString(dateFormat)
  }

  case object CYMinus5 extends WithName("current-year-minus-5") with SelectTaxYear {
    override def year: Int = TaxYearResolver.startOfCurrentTaxYear.minusYears(5).getYear
    override def asString: String = TaxYearResolver.startOfCurrentTaxYear.minusYears(5).toString(dateFormat) + " to " +
      TaxYearResolver.endOfCurrentTaxYear.minusYears(5).toString(dateFormat)
  }

  lazy val values: Set[SelectTaxYear] = Set(
    CYMinus2, CYMinus3, CYMinus4, CYMinus5
  )
}
