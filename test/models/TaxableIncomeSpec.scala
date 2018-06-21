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

class TaxableIncomeSpec extends WordSpec with MustMatchers with MockitoSugar {

  "TaxableIncome model" must {
    "Return map in correct order" in {

      TaxableIncome.sortedTaxableIncome.head._2 mustBe TaxableIncome.RENTAL_INCOME.toString
      TaxableIncome.sortedTaxableIncome.toSeq(1)._2 mustBe TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST.toString
      TaxableIncome.sortedTaxableIncome.toSeq(2)._2 mustBe TaxableIncome.INVESTMENT_OR_DIVIDENDS.toString
      TaxableIncome.sortedTaxableIncome.toSeq(3)._2 mustBe TaxableIncome.FOREIGN_INCOME.toString
      TaxableIncome.sortedTaxableIncome.toSeq(4)._2 mustBe TaxableIncome.OTHER_TAXABLE_INCOME.toString
    }
  }
}
