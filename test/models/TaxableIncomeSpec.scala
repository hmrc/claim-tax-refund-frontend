/*
 * Copyright 2023 HM Revenue & Customs
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

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.mockito.MockitoSugar

class TaxableIncomeSpec extends AnyWordSpecLike with Matchers with MockitoSugar {

  "TaxableIncome model" must {
    "Return map in correct order" in {

      TaxableIncome.sortedTaxableIncome.head mustBe TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST
      TaxableIncome.sortedTaxableIncome(1) mustBe TaxableIncome.FOREIGN_INCOME
      TaxableIncome.sortedTaxableIncome(2) mustBe TaxableIncome.INVESTMENT_OR_DIVIDENDS
      TaxableIncome.sortedTaxableIncome(3) mustBe TaxableIncome.RENTAL_INCOME
      TaxableIncome.sortedTaxableIncome(4) mustBe TaxableIncome.OTHER_TAXABLE_INCOME
    }
  }
}
