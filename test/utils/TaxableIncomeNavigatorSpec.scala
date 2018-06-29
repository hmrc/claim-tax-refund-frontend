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

package utils

import base.SpecBase
import controllers.routes
import identifiers._
import models.{TaxableIncome, NormalMode}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

class TaxableIncomeNavigatorSpec extends SpecBase with MockitoSugar {


  val navigator = new Navigator


  "TaxableIncomeNavigator" when {
    "in normal mode" when {
      "Navigating from SelectTaxableIncome" must {
        "go to HowMuchRentalIncome when rental-income checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectTaxableIncome) thenReturn Some(
            Seq(
              TaxableIncome.RENTAL_INCOME,
              TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
              TaxableIncome.INVESTMENT_OR_DIVIDENDS,
              TaxableIncome.FOREIGN_INCOME,
              TaxableIncome.OTHER_TAXABLE_INCOME
            )
          )

          navigator.nextPage(SelectTaxableIncomeId, NormalMode)(answers) mustBe routes.HowMuchRentalIncomeController.onPageLoad(NormalMode)
        }

        "go to HowMuchBankOrBuildingSociety when bank-or-building-society-interest checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectTaxableIncome) thenReturn Some(
            Seq(
              TaxableIncome.RENTAL_INCOME,
              TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
              TaxableIncome.INVESTMENT_OR_DIVIDENDS,
              TaxableIncome.FOREIGN_INCOME,
              TaxableIncome.OTHER_TAXABLE_INCOME
            )
          )

          navigator.nextPage(SelectTaxableIncomeId, NormalMode)(answers) mustBe routes.HowMuchBankInterestController.onPageLoad(NormalMode)
        }

        "go to HowMuchInvestmentOrDividends when investment-or-dividends checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectTaxableIncome) thenReturn Some(
            Seq(
              TaxableIncome.RENTAL_INCOME,
              TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
              TaxableIncome.INVESTMENT_OR_DIVIDENDS,
              TaxableIncome.FOREIGN_INCOME,
              TaxableIncome.OTHER_TAXABLE_INCOME
            )
          )

          navigator.nextPage(SelectTaxableIncomeId, NormalMode)(answers) mustBe routes.HowMuchInvestmentOrDividendController.onPageLoad(NormalMode)
        }

        "go to HowMuchForeignIncome when foreign-income checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectTaxableIncome) thenReturn Some(
            Seq(
              TaxableIncome.RENTAL_INCOME,
              TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
              TaxableIncome.INVESTMENT_OR_DIVIDENDS,
              TaxableIncome.FOREIGN_INCOME,
              TaxableIncome.OTHER_TAXABLE_INCOME
            )
          )

          navigator.nextPage(SelectTaxableIncomeId, NormalMode)(answers) mustBe routes.HowMuchForeignIncomeController.onPageLoad(NormalMode)
        }

        "go to HowMuchOtherTaxableIncome when other-taxable-income is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectTaxableIncome) thenReturn Some(
            Seq(
              TaxableIncome.RENTAL_INCOME,
              TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
              TaxableIncome.INVESTMENT_OR_DIVIDENDS,
              TaxableIncome.FOREIGN_INCOME,
              TaxableIncome.OTHER_TAXABLE_INCOME
            )
          )

          navigator.nextPage(SelectTaxableIncomeId, NormalMode)(answers) mustBe routes.OtherTaxableIncomeNameController.onPageLoad(NormalMode)
        }
      }
    }
  }

}
