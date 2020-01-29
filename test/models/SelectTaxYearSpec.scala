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

package models

import models.SelectTaxYear._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatest.{MustMatchers, WordSpec}
import play.api.i18n.Messages
import uk.gov.hmrc.time.TaxYear


class SelectTaxYearSpec(implicit messages: Messages) extends WordSpec with MustMatchers with MockitoSugar {

  "SelectTaxYear model" must {
    "return the currect tax year for CYMinus1" in {
      val taxYear = SelectTaxYear.CYMinus1
      taxYear.year mustBe TaxYear.current.back(1).currentYear
    }
    "return the currect tax year for CYMinus2" in {
      val taxYear = SelectTaxYear.CYMinus2
      taxYear.year mustBe TaxYear.current.back(2).currentYear
    }
    "return the currect tax year for CYMinus3" in {
      val taxYear = SelectTaxYear.CYMinus3
      taxYear.year mustBe TaxYear.current.back(3).currentYear
    }
    "return the currect tax year for CYMinus4" in {
      val taxYear = SelectTaxYear.CYMinus4
      taxYear.year mustBe TaxYear.current.back(4).currentYear
    }

    "return the currect tax year for CYMinus1 as String" in {
      val taxYear = SelectTaxYear.CYMinus1
      taxYear.asString mustBe messages(s"global.${CYMinus1.toString}", TaxYear.current.back(1).startYear, TaxYear.current.back(1).finishYear)
    }
    "return the currect tax year for CYMinus2 as String" in {
      val taxYear = SelectTaxYear.CYMinus2
      taxYear.asString mustBe messages(s"global.${CYMinus2.toString}", TaxYear.current.back(2).startYear, TaxYear.current.back(2).finishYear)
    }
    "return the currect tax year for CYMinus3 as String" in {
      val taxYear = SelectTaxYear.CYMinus3
      taxYear.asString mustBe messages(s"global.${CYMinus3.toString}", TaxYear.current.back(3).startYear, TaxYear.current.back(3).finishYear)
    }
    "return the currect tax year for CYMinus4 as String" in {
      val taxYear = SelectTaxYear.CYMinus4
      taxYear.asString mustBe messages(s"global.${CYMinus4.toString}", TaxYear.current.back(4).startYear, TaxYear.current.back(4).finishYear)
    }
  }
}
