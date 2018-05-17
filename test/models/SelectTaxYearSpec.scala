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

import org.scalatest.{MustMatchers, WordSpec}
import org.scalatest.mockito.MockitoSugar
import uk.gov.hmrc.time.TaxYearResolver


class SelectTaxYearSpec extends WordSpec with MustMatchers with MockitoSugar {

  val mockSelectTaxYear = mock[SelectTaxYear]

  "SelectTaxYear model" must {
    "return the currect tax year for CYMinus2" in {
      val taxYear = SelectTaxYear.CYMinus2
      taxYear.year mustBe TaxYearResolver.startOfCurrentTaxYear.minusYears(2).getYear
    }
    "return the currect tax year for CYMinus3" in {
      val taxYear = SelectTaxYear.CYMinus3
      taxYear.year mustBe TaxYearResolver.startOfCurrentTaxYear.minusYears(3).getYear
    }
    "return the currect tax year for CYMinus4" in {
      val taxYear = SelectTaxYear.CYMinus4
      taxYear.year mustBe TaxYearResolver.startOfCurrentTaxYear.minusYears(4).getYear
    }
    "return the currect tax year for CYMinus5" in {
      val taxYear = SelectTaxYear.CYMinus5
      taxYear.year mustBe TaxYearResolver.startOfCurrentTaxYear.minusYears(5).getYear
    }
  }
}
