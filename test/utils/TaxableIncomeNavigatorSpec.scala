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
              TaxableIncome.OTHER_TAXABLE_INCOME
            )
          )

          navigator.nextPage(SelectTaxableIncomeId, NormalMode)(answers) mustBe routes.OtherTaxableIncomeNameController.onPageLoad(NormalMode)
        }

        "go to AnyTaxableRentalIncome from HowMuchRentalIncome" in {
          val answers = mock[UserAnswers]
          navigator.nextPage(HowMuchRentalIncomeId, NormalMode)(answers) mustBe routes.AnyTaxableRentalIncomeController.onPageLoad(NormalMode)
        }

        "go to AnyTaxableBankInterest from HowMuchBankInterest" in {
          val answers = mock[UserAnswers]
          navigator.nextPage(HowMuchBankInterestId, NormalMode)(answers) mustBe routes.AnyTaxableBankInterestController.onPageLoad(NormalMode)
        }

        "go to AnyTaxableInvestmentOrDividends from HowMuchInvestmentOrDividends" in {
          val answers = mock[UserAnswers]
          navigator.nextPage(HowMuchInvestmentOrDividendId, NormalMode)(answers) mustBe routes.AnyTaxableInvestmentsController.onPageLoad(NormalMode)
        }

        "go to AnyTaxableForeignIncome from HowMuchForeignIncome" in {
          val answers = mock[UserAnswers]
          navigator.nextPage(HowMuchForeignIncomeId, NormalMode)(answers) mustBe routes.AnyTaxableForeignIncomeController.onPageLoad(NormalMode)
        }

        "go to HowMuchOtherTaxableIncome from OtherTaxableIncomeName" in {
          val answers = mock[UserAnswers]
          navigator.nextPage(OtherTaxableIncomeNameId, NormalMode)(answers) mustBe routes.HowMuchOtherTaxableIncomeController.onPageLoad(NormalMode)
        }

        "go to AnyTaxableOtherIncome from HowMuchOtherTaxableIncome" in {
          val answers = mock[UserAnswers]
          navigator.nextPage(HowMuchOtherTaxableIncomeId, NormalMode)(answers) mustBe routes.AnyOtherTaxableIncomeController.onPageLoad(NormalMode)
        }

        "go to WhereToSendPayment once all taxableIncome have been completed" in {
          val answers = mock[UserAnswers]
          when(answers.selectTaxableIncome) thenReturn Some(Seq(
            TaxableIncome.RENTAL_INCOME,
            TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
            TaxableIncome.INVESTMENT_OR_DIVIDENDS,
            TaxableIncome.FOREIGN_INCOME
          ))
          navigator.nextPage(AnyTaxableForeignIncomeId, NormalMode)(answers) mustBe routes.WhereToSendPaymentController.onPageLoad(NormalMode)
        }

        "go to SessionExpired if answers.selectTaxableIncome is None on SelectTaxableIncome page" in {
          val answers = mock[UserAnswers]
          when(answers.selectTaxableIncome) thenReturn None
          navigator.nextPage(SelectTaxableIncomeId, NormalMode)(answers) mustBe routes.SessionExpiredController.onPageLoad()
        }
      }

      "Navigating from AnyTaxableRentalIncome" must {
        "go to HowMuchBankOrBuildingSociety if this option was selected on SelectTaxableIncome" in {
          val answers = mock[UserAnswers]
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST))
          navigator.nextPage(AnyTaxableRentalIncomeId, NormalMode)(answers) mustBe routes.HowMuchBankInterestController.onPageLoad(NormalMode)
        }

        "go to HowMuchInvestmentOrDividends if this option was selected on SelectTaxableIncome" in {
          val answers = mock[UserAnswers]
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.INVESTMENT_OR_DIVIDENDS))
          navigator.nextPage(AnyTaxableRentalIncomeId, NormalMode)(answers) mustBe routes.HowMuchInvestmentOrDividendController.onPageLoad(NormalMode)
        }

        "go to HowMuchForeignIncome if this option was selected on SelectTaxableIncome" in {
          val answers = mock[UserAnswers]
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.FOREIGN_INCOME))
          navigator.nextPage(AnyTaxableRentalIncomeId, NormalMode)(answers) mustBe routes.HowMuchForeignIncomeController.onPageLoad(NormalMode)
        }

        "go to HowMuchOtherTaxableIncome if this option was selected on SelectTaxableIncome" in {
          val answers = mock[UserAnswers]
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.OTHER_TAXABLE_INCOME))
          navigator.nextPage(AnyTaxableRentalIncomeId, NormalMode)(answers) mustBe routes.OtherTaxableIncomeNameController.onPageLoad(NormalMode)
        }
      }

      "Navigating from AnyTaxableBankOrBuildingSociety" must {
        "go to HowMuchInvestmentOrDividends if this option was selected on SelectTaxableIncome" in {
          val answers = mock[UserAnswers]
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.INVESTMENT_OR_DIVIDENDS))
          navigator.nextPage(AnyTaxableBankInterestId, NormalMode)(answers) mustBe routes.HowMuchInvestmentOrDividendController.onPageLoad(NormalMode)
        }

        "go to HowMuchForeignIncome if this option was selected on SelectTaxableIncome" in {
          val answers = mock[UserAnswers]
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.FOREIGN_INCOME))
          navigator.nextPage(AnyTaxableBankInterestId, NormalMode)(answers) mustBe routes.HowMuchForeignIncomeController.onPageLoad(NormalMode)
        }

        "go to HowMuchOtherTaxableIncome if this option was selected on SelectTaxableIncome" in {
          val answers = mock[UserAnswers]
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.OTHER_TAXABLE_INCOME))
          navigator.nextPage(AnyTaxableBankInterestId, NormalMode)(answers) mustBe routes.OtherTaxableIncomeNameController.onPageLoad(NormalMode)
        }
      }

      "Navigating from AnyTaxableInvestments" must {
        "go to HowMuchForeignIncome if this option was selected on SelectTaxableIncome" in {
          val answers = mock[UserAnswers]
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.FOREIGN_INCOME))
          navigator.nextPage(AnyTaxableInvestmentsId, NormalMode)(answers) mustBe routes.HowMuchForeignIncomeController.onPageLoad(NormalMode)
        }

        "go to HowMuchOtherTaxableIncome if this option was selected on SelectTaxableIncome" in {
          val answers = mock[UserAnswers]
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.OTHER_TAXABLE_INCOME))
          navigator.nextPage(AnyTaxableInvestmentsId, NormalMode)(answers) mustBe routes.OtherTaxableIncomeNameController.onPageLoad(NormalMode)
        }
      }
    }
  }

}
