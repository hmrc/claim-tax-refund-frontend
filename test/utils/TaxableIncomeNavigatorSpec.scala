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
import models.{CheckMode, NormalMode, TaxableIncome}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

class TaxableIncomeNavigatorSpec extends SpecBase with MockitoSugar {

  private val navigator = new Navigator
  private val answers = MockUserAnswers.nothingAnswered

  "TaxableIncomeNavigator" when {
    "in normal mode" when {
      "go to SelectTaxableIncome from AnyTaxableIncome when answer is yes" in {
        when(answers.anyTaxableIncome) thenReturn Some(true)
        navigator.nextPage(AnyTaxableIncomeId, NormalMode)(answers) mustBe routes.SelectTaxableIncomeController.onPageLoad(NormalMode)
      }

      "Navigating from SelectTaxableIncome" must {
        "go to HowMuchRentalIncome when rental-income checkbox is the first answer selected" in {
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
          when(answers.selectTaxableIncome) thenReturn Some(
            Seq(
              TaxableIncome.FOREIGN_INCOME,
              TaxableIncome.OTHER_TAXABLE_INCOME
            )
          )

          navigator.nextPage(SelectTaxableIncomeId, NormalMode)(answers) mustBe routes.HowMuchForeignIncomeController.onPageLoad(NormalMode)
        }

        "go to OtherTaxableIncomeName when other-taxable-income is the first answer selected" in {
          when(answers.selectTaxableIncome) thenReturn Some(
            Seq(
              TaxableIncome.OTHER_TAXABLE_INCOME
            )
          )

          navigator.nextPage(SelectTaxableIncomeId, NormalMode)(answers) mustBe routes.OtherTaxableIncomeNameController.onPageLoad(NormalMode)
        }

        "go to AnyTaxableRentalIncome from HowMuchRentalIncome" in {
          navigator.nextPage(HowMuchRentalIncomeId, NormalMode)(answers) mustBe routes.AnyTaxableRentalIncomeController.onPageLoad(NormalMode)
        }

        "go to AnyTaxableBankInterest from HowMuchBankInterest" in {
          navigator.nextPage(HowMuchBankInterestId, NormalMode)(answers) mustBe routes.AnyTaxableBankInterestController.onPageLoad(NormalMode)
        }

        "go to AnyTaxableInvestmentOrDividends from HowMuchInvestmentOrDividends" in {
          navigator.nextPage(HowMuchInvestmentsId, NormalMode)(answers) mustBe routes.AnyTaxableInvestmentsController.onPageLoad(NormalMode)
        }

        "go to AnyTaxableForeignIncome from HowMuchForeignIncome" in {
          navigator.nextPage(HowMuchForeignIncomeId, NormalMode)(answers) mustBe routes.AnyTaxableForeignIncomeController.onPageLoad(NormalMode)
        }

        "go to HowMuchOtherTaxableIncome from OtherTaxableIncomeName" in {
          navigator.nextPage(OtherTaxableIncomeId, NormalMode)(answers) mustBe routes.HowMuchOtherTaxableIncomeController.onPageLoad(NormalMode)
        }

        "go to AnyTaxableOtherIncome from HowMuchOtherTaxableIncome" in {
          navigator.nextPage(HowMuchOtherTaxableIncomeId, NormalMode)(answers) mustBe routes.AnyTaxableOtherIncomeController.onPageLoad(NormalMode)
        }

        "go to AnyOtherTaxableIncome from AnyTaxableOtherIncome" in {
          navigator.nextPage(AnyTaxableOtherIncomeId, NormalMode)(answers) mustBe routes.AnyOtherTaxableIncomeController.onPageLoad(NormalMode)
        }

        "go to WhereToSendPayment once all taxableIncome have been completed" in {
          val answers = MockUserAnswers.taxableIncomeUserAnswers
          navigator.nextPage(AnyTaxableForeignIncomeId, NormalMode)(answers) mustBe routes.WhereToSendPaymentController.onPageLoad(NormalMode)
        }

        "go to SessionExpired if answers.selectTaxableIncome is None on SelectTaxableIncome page" in {
          when(answers.selectTaxableIncome) thenReturn None
          navigator.nextPage(SelectTaxableIncomeId, NormalMode)(answers) mustBe routes.SessionExpiredController.onPageLoad()
        }
      }

      "Navigating from AnyTaxableRentalIncome" must {
        "go to HowMuchBankOrBuildingSociety if this option was selected on SelectTaxableIncome" in {
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST))
          navigator.nextPage(AnyTaxableRentalIncomeId, NormalMode)(answers) mustBe routes.HowMuchBankInterestController.onPageLoad(NormalMode)
        }

        "go to HowMuchInvestmentOrDividends if this option was selected on SelectTaxableIncome" in {
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.INVESTMENT_OR_DIVIDENDS))
          navigator.nextPage(AnyTaxableRentalIncomeId, NormalMode)(answers) mustBe routes.HowMuchInvestmentOrDividendController.onPageLoad(NormalMode)
        }

        "go to HowMuchForeignIncome if this option was selected on SelectTaxableIncome" in {
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.FOREIGN_INCOME))
          navigator.nextPage(AnyTaxableRentalIncomeId, NormalMode)(answers) mustBe routes.HowMuchForeignIncomeController.onPageLoad(NormalMode)
        }

        "go to OtherTaxableIncomeName if this option was selected on SelectTaxableIncome" in {
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.OTHER_TAXABLE_INCOME))
          navigator.nextPage(AnyTaxableRentalIncomeId, NormalMode)(answers) mustBe routes.OtherTaxableIncomeNameController.onPageLoad(NormalMode)
        }
      }

      "Navigating from AnyTaxableBankOrBuildingSociety" must {
        "go to HowMuchInvestmentOrDividends if this option was selected on SelectTaxableIncome" in {
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.INVESTMENT_OR_DIVIDENDS))
          navigator.nextPage(AnyTaxableBankInterestId, NormalMode)(answers) mustBe routes.HowMuchInvestmentOrDividendController.onPageLoad(NormalMode)
        }

        "go to HowMuchForeignIncome if this option was selected on SelectTaxableIncome" in {
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.FOREIGN_INCOME))
          navigator.nextPage(AnyTaxableBankInterestId, NormalMode)(answers) mustBe routes.HowMuchForeignIncomeController.onPageLoad(NormalMode)
        }

        "go to OtherTaxableIncomeName if this option was selected on SelectTaxableIncome" in {
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.OTHER_TAXABLE_INCOME))
          navigator.nextPage(AnyTaxableBankInterestId, NormalMode)(answers) mustBe routes.OtherTaxableIncomeNameController.onPageLoad(NormalMode)
        }
      }

      "Navigating from AnyTaxableInvestments" must {
        "go to HowMuchForeignIncome if this option was selected on SelectTaxableIncome" in {
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.FOREIGN_INCOME))
          navigator.nextPage(AnyTaxableInvestmentsId, NormalMode)(answers) mustBe routes.HowMuchForeignIncomeController.onPageLoad(NormalMode)
        }

        "go to OtherTaxableIncomeName if this option was selected on SelectTaxableIncome" in {
          when(answers.selectTaxableIncome) thenReturn Some(Seq(TaxableIncome.OTHER_TAXABLE_INCOME))
          navigator.nextPage(AnyTaxableInvestmentsId, NormalMode)(answers) mustBe routes.OtherTaxableIncomeNameController.onPageLoad(NormalMode)
        }
      }
    }

    //------------------Check Mode-------------------------//

    "in check mode" when {

      "Navigating from AnyTaxableIncome" must {
        "go to CheckYourAnswersController from AnyTaxableIncome when Yes is selected and taxable income section complete" in {
          val answers = MockUserAnswers.taxableIncomeUserAnswers
          navigator.nextPage(AnyTaxableIncomeId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }

        "go to SelectTaxableIncome from AnyTaxableIncome when Yes is selected and no taxable incomes selected" in {
          val answers = MockUserAnswers.taxableIncomeUserAnswers
          when(answers.selectTaxableIncome) thenReturn None
          navigator.nextPage(AnyTaxableIncomeId, CheckMode)(answers) mustBe routes.SelectTaxableIncomeController.onPageLoad(CheckMode)
        }

        "go to CheckYourAnswersController from AnyTaxableIncome when No is selected" in {
          val answers = MockUserAnswers.taxableIncomeUserAnswers
          when(answers.anyTaxableIncome) thenReturn Some(false)
          navigator.nextPage(AnyTaxableIncomeId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }
      }

      "Navigating from SelectTaxableIncome" must {
        "go to CheckYourAnswers when all answer selected and all have an associated amount" in {
          val answers = MockUserAnswers.taxableIncomeUserAnswers
          navigator.nextPage(SelectTaxableIncomeId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }

        "go to HowMuchRentalIncome when rental income selected but no associated amount" in {
          val answers = MockUserAnswers.taxableIncomeUserAnswers
          when(answers.howMuchRentalIncome) thenReturn None

          navigator.nextPage(SelectTaxableIncomeId, CheckMode)(answers) mustBe routes.HowMuchRentalIncomeController.onPageLoad(CheckMode)
        }

        "go to HowMuchBankInterest when bank interest selected but no associated amount" in {
          val answers = MockUserAnswers.taxableIncomeUserAnswers
          when(answers.howMuchBankInterest) thenReturn None

          navigator.nextPage(SelectTaxableIncomeId, CheckMode)(answers) mustBe routes.HowMuchBankInterestController.onPageLoad(CheckMode)
        }

        "go to HowMuchInvestment when investment selected but no associated amount" in {
          val answers = MockUserAnswers.taxableIncomeUserAnswers
          when(answers.howMuchInvestmentOrDividend) thenReturn None

          navigator.nextPage(SelectTaxableIncomeId, CheckMode)(answers) mustBe routes.HowMuchInvestmentOrDividendController.onPageLoad(CheckMode)
        }

        "go to HowMuchForeignIncome when foreign income selected but no associated amount" in {
          val answers = MockUserAnswers.taxableIncomeUserAnswers
          when(answers.howMuchForeignIncome) thenReturn None

          navigator.nextPage(SelectTaxableIncomeId, CheckMode)(answers) mustBe routes.HowMuchForeignIncomeController.onPageLoad(CheckMode)
        }

        "go to OtherTaxableIncomeName when other taxable income selected but no associated name" in {
          val answers = MockUserAnswers.taxableIncomeUserAnswers
          when(answers.otherTaxableIncomeName) thenReturn None

          navigator.nextPage(SelectTaxableIncomeId, CheckMode)(answers) mustBe routes.OtherTaxableIncomeNameController.onPageLoad(CheckMode)
        }
      }

      "Navigating RentalIncome" when {
        "HowMuchRentalIncome" must {
          "go to CheckYourAnswersController when all selected taxable incomes have associated values" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            navigator.nextPage(HowMuchRentalIncomeId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
          }

          "go to AnyTaxableRentalIncome when any taxable rental income not selected" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.anyTaxableRentalIncome) thenReturn None

            navigator.nextPage(HowMuchRentalIncomeId, CheckMode)(answers) mustBe routes.AnyTaxableRentalIncomeController.onPageLoad(CheckMode)
          }
        }

        "AnyTaxableRentalIncome" must {
          "go to CheckYourAnswersController when all selected taxable incomes have associated values" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            navigator.nextPage(AnyTaxableRentalIncomeId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
          }

          "go to HowMuchRentalIncome when rental income selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchRentalIncome) thenReturn None

            navigator.nextPage(AnyTaxableRentalIncomeId, CheckMode)(answers) mustBe routes.HowMuchRentalIncomeController.onPageLoad(CheckMode)
          }

          "go to HowMuchBankInterest when bank interest selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchBankInterest) thenReturn None

            navigator.nextPage(AnyTaxableRentalIncomeId, CheckMode)(answers) mustBe routes.HowMuchBankInterestController.onPageLoad(CheckMode)
          }

          "go to HowMuchInvestment when investment selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchInvestmentOrDividend) thenReturn None

            navigator.nextPage(AnyTaxableRentalIncomeId, CheckMode)(answers) mustBe routes.HowMuchInvestmentOrDividendController.onPageLoad(CheckMode)
          }

          "go to HowMuchForeignIncome when foreign income selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchForeignIncome) thenReturn None

            navigator.nextPage(AnyTaxableRentalIncomeId, CheckMode)(answers) mustBe routes.HowMuchForeignIncomeController.onPageLoad(CheckMode)
          }

          "go to OtherTaxableIncomeName when other taxable income selected without an associated name" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.otherTaxableIncomeName) thenReturn None

            navigator.nextPage(AnyTaxableRentalIncomeId, CheckMode)(answers) mustBe routes.OtherTaxableIncomeNameController.onPageLoad(CheckMode)
          }
        }
      }

      "Navigating BankInterest" when {
        "HowMuchBankInterest" must {
          "go to CheckYourAnswersController when all selected taxable incomes have associated values" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            navigator.nextPage(HowMuchBankInterestId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
          }

          "go to AnyTaxableBankInterest when any taxable bank interest not selected" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.anyTaxableBankInterest) thenReturn None

            navigator.nextPage(HowMuchBankInterestId, CheckMode)(answers) mustBe routes.AnyTaxableBankInterestController.onPageLoad(CheckMode)
          }
        }

        "AnyTaxableBankInterest" must {
          "go to CheckYourAnswersController when all selected taxable incomes have associated values" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            navigator.nextPage(AnyTaxableBankInterestId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
          }

          "go to HowMuchRental when rental income selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchRentalIncome) thenReturn None

            navigator.nextPage(AnyTaxableBankInterestId, CheckMode)(answers) mustBe routes.HowMuchRentalIncomeController.onPageLoad(CheckMode)
          }

          "go to HowMuchBankInterest when bank interest selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchBankInterest) thenReturn None

            navigator.nextPage(AnyTaxableBankInterestId, CheckMode)(answers) mustBe routes.HowMuchBankInterestController.onPageLoad(CheckMode)
          }

          "go to HowMuchInvestment when investment selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchInvestmentOrDividend) thenReturn None

            navigator.nextPage(AnyTaxableBankInterestId, CheckMode)(answers) mustBe routes.HowMuchInvestmentOrDividendController.onPageLoad(CheckMode)
          }

          "go to HowMuchForeignIncome when foreign income selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchForeignIncome) thenReturn None

            navigator.nextPage(AnyTaxableBankInterestId, CheckMode)(answers) mustBe routes.HowMuchForeignIncomeController.onPageLoad(CheckMode)
          }

          "go to OtherTaxableIncomeName when other taxable income selected without an associated name" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.otherTaxableIncomeName) thenReturn None

            navigator.nextPage(AnyTaxableBankInterestId, CheckMode)(answers) mustBe routes.OtherTaxableIncomeNameController.onPageLoad(CheckMode)
          }
        }
      }

      "Navigating Investments" when {
        "HowMuchInvestments" must {
          "go to CheckYourAnswersController when all selected taxable incomes have associated values" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            navigator.nextPage(HowMuchInvestmentsId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
          }

          "go to AnyTaxableInvestments when any taxable investments not selected" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.anyTaxableInvestments) thenReturn None

            navigator.nextPage(HowMuchInvestmentsId, CheckMode)(answers) mustBe routes.AnyTaxableInvestmentsController.onPageLoad(CheckMode)
          }
        }

        "AnyTaxableInvestments" must {
          "go to CheckYourAnswersController when all selected taxable incomes have associated values" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            navigator.nextPage(AnyTaxableInvestmentsId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
          }

          "go to HowMuchRental when rental income selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchRentalIncome) thenReturn None

            navigator.nextPage(AnyTaxableInvestmentsId, CheckMode)(answers) mustBe routes.HowMuchRentalIncomeController.onPageLoad(CheckMode)
          }

          "go to HowMuchBankInterest when bank interest selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchBankInterest) thenReturn None

            navigator.nextPage(AnyTaxableInvestmentsId, CheckMode)(answers) mustBe routes.HowMuchBankInterestController.onPageLoad(CheckMode)
          }

          "go to HowMuchInvestment when investment selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchInvestmentOrDividend) thenReturn None

            navigator.nextPage(AnyTaxableInvestmentsId, CheckMode)(answers) mustBe routes.HowMuchInvestmentOrDividendController.onPageLoad(CheckMode)
          }

          "go to HowMuchForeignIncome when foreign income selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchForeignIncome) thenReturn None

            navigator.nextPage(AnyTaxableInvestmentsId, CheckMode)(answers) mustBe routes.HowMuchForeignIncomeController.onPageLoad(CheckMode)
          }

          "go to OtherTaxableIncomeName when other taxable income selected without an associated name" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.otherTaxableIncomeName) thenReturn None

            navigator.nextPage(AnyTaxableInvestmentsId, CheckMode)(answers) mustBe routes.OtherTaxableIncomeNameController.onPageLoad(CheckMode)
          }
        }
      }

      "Navigating ForeignIncome" when {
        "HowMuchForeignIncome" must {
          "go to CheckYourAnswersController when all selected taxable incomes have associated values" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            navigator.nextPage(HowMuchForeignIncomeId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
          }

          "go to AnyTaxableForeignIncome when any taxable foreign income not selected" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.anyTaxableForeignIncome) thenReturn None

            navigator.nextPage(HowMuchForeignIncomeId, CheckMode)(answers) mustBe routes.AnyTaxableForeignIncomeController.onPageLoad(CheckMode)
          }
        }

        "AnyTaxableForeignIncome" must {
          "go to CheckYourAnswersController when all selected taxable incomes have associated values" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            navigator.nextPage(AnyTaxableForeignIncomeId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
          }

          "go to HowMuchRental when rental income selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchRentalIncome) thenReturn None

            navigator.nextPage(AnyTaxableForeignIncomeId, CheckMode)(answers) mustBe routes.HowMuchRentalIncomeController.onPageLoad(CheckMode)
          }

          "go to HowMuchBankInterest when bank interest selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchBankInterest) thenReturn None

            navigator.nextPage(AnyTaxableForeignIncomeId, CheckMode)(answers) mustBe routes.HowMuchBankInterestController.onPageLoad(CheckMode)
          }

          "go to HowMuchInvestment when investment selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchInvestmentOrDividend) thenReturn None

            navigator.nextPage(AnyTaxableForeignIncomeId, CheckMode)(answers) mustBe routes.HowMuchInvestmentOrDividendController.onPageLoad(CheckMode)
          }

          "go to HowMuchForeignIncome when foreign income selected without an associated value" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchForeignIncome) thenReturn None

            navigator.nextPage(AnyTaxableForeignIncomeId, CheckMode)(answers) mustBe routes.HowMuchForeignIncomeController.onPageLoad(CheckMode)
          }

          "go to OtherTaxableIncomeName when other taxable income selected without an associated name" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.otherTaxableIncomeName) thenReturn None

            navigator.nextPage(AnyTaxableForeignIncomeId, CheckMode)(answers) mustBe routes.OtherTaxableIncomeNameController.onPageLoad(CheckMode)
          }
        }
      }

      "Navigating OtherIncome" when {
        "OtherTaxableIncomeName" must {
          "go to CheckYourAnswersController when all selected taxable incomes have associated values" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            navigator.nextPage(OtherTaxableIncomeId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
          }

          "go to HowMuchOtherIncome when how much other income does not have an associated amount" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.howMuchOtherTaxableIncome) thenReturn None

            navigator.nextPage(OtherTaxableIncomeId, CheckMode)(answers) mustBe routes.HowMuchOtherTaxableIncomeController.onPageLoad(CheckMode)
          }

          "go to AnyTaxableOtherIncome when any taxable other income not selected" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.anyTaxableOtherIncome) thenReturn None

            navigator.nextPage(OtherTaxableIncomeId, CheckMode)(answers) mustBe routes.AnyTaxableOtherIncomeController.onPageLoad(CheckMode)
          }
        }

        "HowMuchOtherIncome" must {
          "go to CheckYourAnswersController when all selected taxable incomes have associated values" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            navigator.nextPage(HowMuchOtherTaxableIncomeId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
          }

          "go to AnyTaxableOtherIncome when any taxable other income not selected" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            when(answers.anyTaxableOtherIncome) thenReturn None

            navigator.nextPage(HowMuchOtherTaxableIncomeId, CheckMode)(answers) mustBe routes.AnyTaxableOtherIncomeController.onPageLoad(CheckMode)
          }
        }

        "AnyTaxableOtherIncome" must {
          "go to CheckYourAnswersController when all selected taxable incomes have associated values" in {
            val answers = MockUserAnswers.taxableIncomeUserAnswers
            navigator.nextPage(AnyTaxableOtherIncomeId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
          }
        }
      }
    }
  }
}