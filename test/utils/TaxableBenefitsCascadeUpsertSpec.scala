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
          val originalCacheMap = new CacheMap("id", Map(
            SelectTaxableIncomeId.toString -> Json.toJson(taxableIncome),
            HowMuchRentalIncomeId.toString ->  JsString("1234"),
            AnyTaxableRentalIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
            HowMuchBankInterestId.toString ->  JsString("1234"),
            AnyTaxableBankInterestId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
            HowMuchInvestmentOrDividendId.toString ->  JsString("1234"),
            AnyTaxableInvestmentsId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
            HowMuchForeignIncomeId.toString ->  JsString("1234"),
            AnyTaxableForeignIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
            OtherTaxableIncomeNameId.toString ->  JsString("qwerty"),
            HowMuchOtherTaxableIncomeId.toString -> Json.toJson("123"),
            AnyTaxableOtherIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes("123")),
            AnyOtherTaxableIncomeId.toString -> JsBoolean(false)
          ))
          val cascadeUpsert = new CascadeUpsert
          val result = cascadeUpsert(AnyTaxableIncomeId.toString, JsBoolean(false), originalCacheMap)
          result.data.size mustBe 1
          result.data.contains(SelectTaxableIncomeId.toString) mustBe false
          result.data.contains(HowMuchRentalIncomeId.toString) mustBe false
          result.data.contains(HowMuchBankInterestId.toString) mustBe false
          result.data.contains(AnyTaxableBankInterestId.toString) mustBe false
          result.data.contains(HowMuchInvestmentOrDividendId.toString) mustBe false
          result.data.contains(AnyTaxableInvestmentsId.toString) mustBe false
          result.data.contains(HowMuchForeignIncomeId.toString) mustBe false
          result.data.contains(AnyTaxableForeignIncomeId.toString) mustBe false
          result.data.contains(OtherTaxableIncomeNameId.toString) mustBe false
          result.data.contains(HowMuchOtherTaxableIncomeId.toString) mustBe false
          result.data.contains(AnyTaxableOtherIncomeId.toString) mustBe false
          result.data.contains(AnyOtherTaxableIncomeId.toString) mustBe false
      }
    }
  }

  "removing values from taxable income checkbox" when {
    "unselecting RENTAL_INCOME" must {
      "remove the figure, name and AnyTaxable values from userAnswers" in {
        forAll(Gen.numStr, arbitraryTaxableIncome) {
          (amount, taxableIncome) =>
            val originalCacheMap = new CacheMap("", Map(
              SelectTaxableIncomeId.toString -> Json.toJson(taxableIncome :+ TaxableIncome.RENTAL_INCOME),
              HowMuchRentalIncomeId.toString -> JsString(amount),
              AnyTaxableRentalIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes(amount))
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectTaxableIncomeId.toString, taxableIncome.filterNot(_ == TaxableIncome.RENTAL_INCOME), originalCacheMap)
            result.data mustEqual Map(SelectTaxableIncomeId.toString -> Json.toJson(taxableIncome.filterNot(_ == TaxableIncome.RENTAL_INCOME)))
        }
      }
    }

    "unselecting BANK_OR_BUILDING_SOCIETY_INTEREST" must {
      "remove the figure, name and AnyTaxable values from userAnswers" in {
        forAll(Gen.numStr, arbitraryTaxableIncome) {
          (amount, taxableIncome) =>
            val originalCacheMap = new CacheMap("", Map(
              SelectTaxableIncomeId.toString -> Json.toJson(taxableIncome :+ TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST),
              HowMuchBankInterestId.toString -> JsString(amount),
              AnyTaxableBankInterestId.toString -> Json.toJson(AnyTaxPaid.Yes(amount))
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectTaxableIncomeId.toString,
              taxableIncome.filterNot(_ == TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST), originalCacheMap)
            result.data mustEqual Map(SelectTaxableIncomeId.toString ->
              Json.toJson(taxableIncome.filterNot(_ == TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST)))
        }
      }
    }

    "unselecting INVESTMENT_OR_DIVIDENDS" must {
      "remove the figure, name and AnyTaxable values from userAnswers" in {
        forAll(Gen.numStr, arbitraryTaxableIncome) {
          (amount, taxableIncome) =>
            val originalCacheMap = new CacheMap("", Map(
              SelectTaxableIncomeId.toString -> Json.toJson(taxableIncome :+ TaxableIncome.INVESTMENT_OR_DIVIDENDS),
              HowMuchInvestmentOrDividendId.toString -> JsString(amount),
              AnyTaxableInvestmentsId.toString -> Json.toJson(AnyTaxPaid.Yes(amount))
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectTaxableIncomeId.toString,
              taxableIncome.filterNot(_ == TaxableIncome.INVESTMENT_OR_DIVIDENDS), originalCacheMap)
            result.data mustEqual Map(SelectTaxableIncomeId.toString ->
              Json.toJson(taxableIncome.filterNot(_ == TaxableIncome.INVESTMENT_OR_DIVIDENDS)))
        }
      }
    }

    "unselecting FOREIGN_INCOME" must {
      "remove the figure, name and AnyTaxable values from userAnswers" in {
        forAll(Gen.numStr, arbitraryTaxableIncome) {
          (amount, taxableIncome) =>
            val originalCacheMap = new CacheMap("", Map(
              SelectTaxableIncomeId.toString -> Json.toJson(taxableIncome :+ TaxableIncome.FOREIGN_INCOME),
              HowMuchForeignIncomeId.toString -> JsString(amount),
              AnyTaxableForeignIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes(amount))
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectTaxableIncomeId.toString,
              taxableIncome.filterNot(_ == TaxableIncome.FOREIGN_INCOME), originalCacheMap)
            result.data mustEqual Map(SelectTaxableIncomeId.toString ->
              Json.toJson(taxableIncome.filterNot(_ == TaxableIncome.FOREIGN_INCOME)))
        }
      }
    }

    "unselecting OTHER_TAXABLE_INCOME" must {
      "remove the figure, name and AnyTaxable values from userAnswers" in {
        forAll(Gen.numStr, arbitraryTaxableIncome) {
          (amount, taxableIncome) =>
            val originalCacheMap = new CacheMap("", Map(
              SelectTaxableIncomeId.toString -> Json.toJson(taxableIncome :+ TaxableIncome.OTHER_TAXABLE_INCOME),
              OtherTaxableIncomeNameId.toString -> JsString("qwerty"),
              HowMuchOtherTaxableIncomeId.toString -> Json.toJson(AnyTaxPaid.Yes(amount)),
              AnyOtherTaxableIncomeId.toString -> JsBoolean(false)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectTaxableIncomeId.toString,
              taxableIncome.filterNot(_ == TaxableIncome.OTHER_TAXABLE_INCOME), originalCacheMap)
            result.data mustEqual Map(SelectTaxableIncomeId.toString ->
              Json.toJson(taxableIncome.filterNot(_ == TaxableIncome.OTHER_TAXABLE_INCOME)))
        }
      }
    }
  }
}