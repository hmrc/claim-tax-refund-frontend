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

import models.SelectTaxYear.{CYMinus1, CYMinus2, CYMinus3, CYMinus4, CYMinus5, dateFormat}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpec}
import play.api.i18n.Messages
import uk.gov.hmrc.time.TaxYearResolver


class SelectTaxYearSpec(implicit messages: Messages) extends WordSpec with MustMatchers with MockitoSugar {

  "SelectTaxYear model" must {
    "return the currect tax year for CYMinus1" in {
      val taxYear = SelectTaxYear.CYMinus1
      taxYear.year mustBe TaxYearResolver.startOfCurrentTaxYear.minusYears(1).getYear
    }
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

    "return the currect tax year for CYMinus1 as String" in {
      val taxYear = SelectTaxYear.CYMinus1
      taxYear.asString mustBe messages(s"selectTaxYear.${CYMinus1.toString}",
        TaxYearResolver.startOfCurrentTaxYear.minusYears(1).toString(dateFormat),
        TaxYearResolver.endOfCurrentTaxYear.minusYears(1).toString(dateFormat))
    }
    "return the currect tax year for CYMinus2 as String" in {
      val taxYear = SelectTaxYear.CYMinus2
      taxYear.asString mustBe messages(s"selectTaxYear.${CYMinus2.toString}",
        TaxYearResolver.startOfCurrentTaxYear.minusYears(2).toString(dateFormat),
        TaxYearResolver.endOfCurrentTaxYear.minusYears(2).toString(dateFormat))
    }
    "return the currect tax year for CYMinus3 as String" in {
      val taxYear = SelectTaxYear.CYMinus3
      taxYear.asString mustBe messages(s"selectTaxYear.${CYMinus3.toString}",
        TaxYearResolver.startOfCurrentTaxYear.minusYears(3).toString(dateFormat),
        TaxYearResolver.endOfCurrentTaxYear.minusYears(3).toString(dateFormat))
    }
    "return the currect tax year for CYMinus4 as String" in {
      val taxYear = SelectTaxYear.CYMinus4
      taxYear.asString mustBe messages(s"selectTaxYear.${CYMinus4.toString}",
        TaxYearResolver.startOfCurrentTaxYear.minusYears(4).toString(dateFormat),
        TaxYearResolver.endOfCurrentTaxYear.minusYears(4).toString(dateFormat))
    }
    "return the currect tax year for CYMinus5 as String" in {
      val taxYear = SelectTaxYear.CYMinus5
      taxYear.asString mustBe messages(s"selectTaxYear.${CYMinus5.toString}",
        TaxYearResolver.startOfCurrentTaxYear.minusYears(5).toString(dateFormat),
        TaxYearResolver.endOfCurrentTaxYear.minusYears(5).toString(dateFormat))
    }
  }
}
