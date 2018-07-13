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
import identifiers._
import models.{AnyTaxPaid, TaxableIncome}
import org.scalacheck.{Gen, Shrink}
import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap
import org.scalatest.prop.PropertyChecks

class TaxableBenefitsCascadeUpsertSpec extends SpecBase with PropertyChecks {

  implicit def dontShrink[A]: Shrink[A] = Shrink.shrinkAny

  private val allTaxableBenefits = new CacheMap(id ="test", Map(
    AnyTaxableIncomeId.toString -> Json.toJson(true),
    SelectTaxableIncomeId.toString -> Json.toJson(TaxableIncome.sortedTaxableIncome),
    HowMuchRentalIncomeId.toString ->  JsString("1234"),
    AnyTaxableRentalIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
    HowMuchBankInterestId.toString ->  JsString("1234"),
    AnyTaxableBankInterestId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
    HowMuchInvestmentsId.toString ->  JsString("1234"),
    AnyTaxableInvestmentsId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
    HowMuchForeignIncomeId.toString ->  JsString("1234"),
    AnyTaxableForeignIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
    OtherTaxableIncomeNameId.toString ->  JsString("qwerty"),
    HowMuchOtherTaxableIncomeId.toString -> Json.toJson("123"),
    AnyTaxableOtherIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
    AnyOtherTaxableIncomeId.toString -> JsBoolean(false)
  ))
  private val cascadeUpsert = new CascadeUpsert

  private val arbitraryTaxableIncome: Gen[Seq[TaxableIncome.Value]] =
    Gen.containerOf[Seq, TaxableIncome.Value](Gen.oneOf(
      TaxableIncome.RENTAL_INCOME,
      TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
      TaxableIncome.INVESTMENT_OR_DIVIDENDS,
      TaxableIncome.FOREIGN_INCOME,
      TaxableIncome.OTHER_TAXABLE_INCOME
    ))

  "answering 'no' to taxable income" must {
    "remove all associated data" in {
      forAll(arbitraryTaxableIncome) {
        taxableIncome =>
          val originalCacheMap = new CacheMap(id ="test", Map(
            AnyTaxableIncomeId.toString -> Json.toJson(true),
            SelectTaxableIncomeId.toString -> Json.toJson(taxableIncome),
            HowMuchRentalIncomeId.toString ->  JsString("1234"),
            AnyTaxableRentalIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
            HowMuchBankInterestId.toString ->  JsString("1234"),
            AnyTaxableBankInterestId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
            HowMuchInvestmentsId.toString ->  JsString("1234"),
            AnyTaxableInvestmentsId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
            HowMuchForeignIncomeId.toString ->  JsString("1234"),
            AnyTaxableForeignIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
            OtherTaxableIncomeNameId.toString ->  JsString("qwerty"),
            HowMuchOtherTaxableIncomeId.toString -> Json.toJson("123"),
            AnyTaxableOtherIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
            AnyOtherTaxableIncomeId.toString -> JsBoolean(false)
          ))
          val result = cascadeUpsert(AnyTaxableIncomeId.toString, JsBoolean(false), originalCacheMap)
          result.data mustBe Map(
            AnyTaxableIncomeId.toString -> Json.toJson(false)
          )
      }
    }
  }

  "removing values from taxable income checkbox" when {
    "un-selecting RENTAL_INCOME to remove all associated data" in {
      val result = cascadeUpsert(SelectTaxableIncomeId.toString, Seq(
        TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
        TaxableIncome.INVESTMENT_OR_DIVIDENDS,
        TaxableIncome.FOREIGN_INCOME,
        TaxableIncome.OTHER_TAXABLE_INCOME
      ), allTaxableBenefits)
      result.data mustBe Map(
        AnyTaxableIncomeId.toString -> Json.toJson(true),
        SelectTaxableIncomeId.toString -> Json.toJson(Seq(
          TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
          TaxableIncome.INVESTMENT_OR_DIVIDENDS,
          TaxableIncome.FOREIGN_INCOME,
          TaxableIncome.OTHER_TAXABLE_INCOME
        )),
        HowMuchBankInterestId.toString ->  JsString("1234"),
        AnyTaxableBankInterestId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        HowMuchInvestmentsId.toString ->  JsString("1234"),
        AnyTaxableInvestmentsId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        HowMuchForeignIncomeId.toString ->  JsString("1234"),
        AnyTaxableForeignIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        OtherTaxableIncomeNameId.toString ->  JsString("qwerty"),
        HowMuchOtherTaxableIncomeId.toString -> Json.toJson("123"),
        AnyTaxableOtherIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        AnyOtherTaxableIncomeId.toString -> JsBoolean(false)
      )
    }

    "un-selecting BANK_OR_BUILDING_SOCIETY_INTEREST to remove all associated data" in {
      val result = cascadeUpsert(SelectTaxableIncomeId.toString, Seq(
        TaxableIncome.RENTAL_INCOME,
        TaxableIncome.INVESTMENT_OR_DIVIDENDS,
        TaxableIncome.FOREIGN_INCOME,
        TaxableIncome.OTHER_TAXABLE_INCOME
      ), allTaxableBenefits)
      result.data mustBe Map(
        AnyTaxableIncomeId.toString -> Json.toJson(true),
        SelectTaxableIncomeId.toString -> Json.toJson(Seq(
          TaxableIncome.RENTAL_INCOME,
          TaxableIncome.INVESTMENT_OR_DIVIDENDS,
          TaxableIncome.FOREIGN_INCOME,
          TaxableIncome.OTHER_TAXABLE_INCOME
        )),
        HowMuchRentalIncomeId.toString ->  JsString("1234"),
        AnyTaxableRentalIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        HowMuchInvestmentsId.toString ->  JsString("1234"),
        AnyTaxableInvestmentsId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        HowMuchForeignIncomeId.toString ->  JsString("1234"),
        AnyTaxableForeignIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        OtherTaxableIncomeNameId.toString ->  JsString("qwerty"),
        HowMuchOtherTaxableIncomeId.toString -> Json.toJson("123"),
        AnyTaxableOtherIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        AnyOtherTaxableIncomeId.toString -> JsBoolean(false)
      )
    }

    "un-selecting INVESTMENT_OR_DIVIDENDS to remove all associated data" in {
      val result = cascadeUpsert(SelectTaxableIncomeId.toString, Seq(
        TaxableIncome.RENTAL_INCOME,
        TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
        TaxableIncome.FOREIGN_INCOME,
        TaxableIncome.OTHER_TAXABLE_INCOME
      ), allTaxableBenefits)
      result.data mustBe Map(
        AnyTaxableIncomeId.toString -> Json.toJson(true),
        SelectTaxableIncomeId.toString -> Json.toJson(Seq(
          TaxableIncome.RENTAL_INCOME,
          TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
          TaxableIncome.FOREIGN_INCOME,
          TaxableIncome.OTHER_TAXABLE_INCOME
        )),
        HowMuchRentalIncomeId.toString ->  JsString("1234"),
        AnyTaxableRentalIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        HowMuchBankInterestId.toString ->  JsString("1234"),
        AnyTaxableBankInterestId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        HowMuchForeignIncomeId.toString ->  JsString("1234"),
        AnyTaxableForeignIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        OtherTaxableIncomeNameId.toString ->  JsString("qwerty"),
        HowMuchOtherTaxableIncomeId.toString -> Json.toJson("123"),
        AnyTaxableOtherIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        AnyOtherTaxableIncomeId.toString -> JsBoolean(false)
      )
    }

    "un-selecting FOREIGN_INCOME to remove all associated data" in {
      val result = cascadeUpsert(SelectTaxableIncomeId.toString, Seq(
        TaxableIncome.RENTAL_INCOME,
        TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
        TaxableIncome.INVESTMENT_OR_DIVIDENDS,
        TaxableIncome.OTHER_TAXABLE_INCOME
      ), allTaxableBenefits)
      result.data mustBe Map(
        AnyTaxableIncomeId.toString -> Json.toJson(true),
        SelectTaxableIncomeId.toString -> Json.toJson(Seq(
          TaxableIncome.RENTAL_INCOME,
          TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
          TaxableIncome.INVESTMENT_OR_DIVIDENDS,
          TaxableIncome.OTHER_TAXABLE_INCOME
        )),
        HowMuchRentalIncomeId.toString ->  JsString("1234"),
        AnyTaxableRentalIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        HowMuchBankInterestId.toString ->  JsString("1234"),
        AnyTaxableBankInterestId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        HowMuchInvestmentsId.toString ->  JsString("1234"),
        AnyTaxableInvestmentsId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        OtherTaxableIncomeNameId.toString ->  JsString("qwerty"),
        HowMuchOtherTaxableIncomeId.toString -> Json.toJson("123"),
        AnyTaxableOtherIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        AnyOtherTaxableIncomeId.toString -> JsBoolean(false)
      )
    }

    "un-selecting OTHER_TAXABLE_INCOME to remove all associated data" in {
      val result = cascadeUpsert(SelectTaxableIncomeId.toString, Seq(
        TaxableIncome.RENTAL_INCOME,
        TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
        TaxableIncome.INVESTMENT_OR_DIVIDENDS,
        TaxableIncome.FOREIGN_INCOME
      ), allTaxableBenefits)
      result.data mustBe Map(
        AnyTaxableIncomeId.toString -> Json.toJson(true),
        SelectTaxableIncomeId.toString -> Json.toJson(Seq(
          TaxableIncome.RENTAL_INCOME,
          TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
          TaxableIncome.INVESTMENT_OR_DIVIDENDS,
          TaxableIncome.FOREIGN_INCOME
        )),
        HowMuchRentalIncomeId.toString ->  JsString("1234"),
        AnyTaxableRentalIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        HowMuchBankInterestId.toString ->  JsString("1234"),
        AnyTaxableBankInterestId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        HowMuchInvestmentsId.toString ->  JsString("1234"),
        AnyTaxableInvestmentsId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        HowMuchForeignIncomeId.toString ->  JsString("1234"),
        AnyTaxableForeignIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123"))
      )
    }

    "un-selecting BANK_OR_BUILDING_SOCIETY_INTEREST and INVESTMENT_OR_DIVIDENDS to remove all associated data" in {
      val result = cascadeUpsert(SelectTaxableIncomeId.toString, Seq(
        TaxableIncome.RENTAL_INCOME,
        TaxableIncome.FOREIGN_INCOME,
        TaxableIncome.OTHER_TAXABLE_INCOME
      ), allTaxableBenefits)
      result.data mustBe Map(
        AnyTaxableIncomeId.toString -> Json.toJson(true),
        SelectTaxableIncomeId.toString -> Json.toJson(Seq(
          TaxableIncome.RENTAL_INCOME,
          TaxableIncome.FOREIGN_INCOME,
          TaxableIncome.OTHER_TAXABLE_INCOME
        )),
        HowMuchRentalIncomeId.toString ->  JsString("1234"),
        AnyTaxableRentalIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        HowMuchForeignIncomeId.toString ->  JsString("1234"),
        AnyTaxableForeignIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        OtherTaxableIncomeNameId.toString ->  JsString("qwerty"),
        HowMuchOtherTaxableIncomeId.toString -> Json.toJson("123"),
        AnyTaxableOtherIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        AnyOtherTaxableIncomeId.toString -> JsBoolean(false)
      )
    }

    "un-selecting BANK_OR_BUILDING_SOCIETY_INTEREST, INVESTMENT_OR_DIVIDENDS and OTHER_TAXABLE_INCOME to remove all associated data" in {
      val result = cascadeUpsert(SelectTaxableIncomeId.toString, Seq(
        TaxableIncome.RENTAL_INCOME,
        TaxableIncome.FOREIGN_INCOME
      ), allTaxableBenefits)
      result.data mustBe Map(
        AnyTaxableIncomeId.toString -> Json.toJson(true),
        SelectTaxableIncomeId.toString -> Json.toJson(Seq(
          TaxableIncome.RENTAL_INCOME,
          TaxableIncome.FOREIGN_INCOME
        )),
        HowMuchRentalIncomeId.toString ->  JsString("1234"),
        AnyTaxableRentalIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
        HowMuchForeignIncomeId.toString ->  JsString("1234"),
        AnyTaxableForeignIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123"))
      )
    }
  }
}
