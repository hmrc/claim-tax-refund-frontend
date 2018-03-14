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

import utils.{WithName, Enumerable}

sealed trait SelectTaxYear

object SelectTaxYear extends Enumerable[SelectTaxYear] {

  case object CYMinus2 extends WithName("current-year-minus-2") with SelectTaxYear
  case object CYMinus3 extends WithName("current-year-minus-3") with SelectTaxYear
  case object CYMinus4 extends WithName("current-year-minus-4") with SelectTaxYear
  case object CYMinus5 extends WithName("current-year-minus-5") with SelectTaxYear

  lazy val values: Set[SelectTaxYear] = Set(
    CYMinus2, CYMinus3, CYMinus4 , CYMinus5
  )
}
