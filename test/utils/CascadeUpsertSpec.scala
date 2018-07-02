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
import models.{AnyTaxPaid, Benefits, CompanyBenefits, TaxableIncome}
import org.scalacheck.{Gen, Shrink}
import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap
import org.scalatest.prop.PropertyChecks

class CascadeUpsertSpec extends SpecBase with PropertyChecks {

  implicit def dontShrink[A]: Shrink[A] = Shrink.shrinkAny

  private val arbitraryCompanyBenefits: Gen[Seq[CompanyBenefits.Value]] =
    Gen.containerOf[Seq, CompanyBenefits.Value](Gen.oneOf(
      CompanyBenefits.FUEL_BENEFIT,
      CompanyBenefits.MEDICAL_BENEFIT,
      CompanyBenefits.COMPANY_CAR_BENEFIT,
      CompanyBenefits.OTHER_COMPANY_BENEFIT
    ))

  private val arbitraryBenefits: Gen[Seq[Benefits.Value]] =
    Gen.containerOf[Seq, Benefits.Value](Gen.oneOf(
      Benefits.BEREAVEMENT_ALLOWANCE,
      Benefits.CARERS_ALLOWANCE,
      Benefits.JOBSEEKERS_ALLOWANCE,
      Benefits.INCAPACITY_BENEFIT,
      Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
      Benefits.STATE_PENSION,
      Benefits.OTHER_TAXABLE_BENEFIT
    ))

  private val arbitraryTaxableIncome: Gen[Seq[TaxableIncome.Value]] =
    Gen.containerOf[Seq, TaxableIncome.Value](Gen.oneOf(
      TaxableIncome.RENTAL_INCOME,
      TaxableIncome.BANK_OR_BUILDING_SOCIETY_INTEREST,
      TaxableIncome.INVESTMENT_OR_DIVIDENDS,
      TaxableIncome.FOREIGN_INCOME,
      TaxableIncome.OTHER_TAXABLE_INCOME
    ))

  "using the apply method for a key that has no special function" when {
    "the key doesn't already exists" must {
      "add the key to the cache map" in {
        val originalCacheMap = new CacheMap("id", Map())
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert("key", "value", originalCacheMap)
        result.data mustBe Map("key" -> JsString("value"))
      }
    }

    "data already exists for that key" must {
      "replace the value held against the key" in {
        val originalCacheMap = new CacheMap("id", Map("key" -> JsString("original value")))
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert("key", "new value", originalCacheMap)
        result.data mustBe Map("key" -> JsString("new value"))
      }
    }
  }

  "addRepeatedValue" when {
    "the key doesn't already exist" must {
      "add the key to the cache map and save the value in a sequence" in {
        val originalCacheMap = new CacheMap("id", Map())
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert.addRepeatedValue("key", "value", originalCacheMap)
        result.data mustBe Map("key" -> Json.toJson(Seq("value")))
      }
    }

    "the key already exists" must {
      "add the new value to the existing sequence" in {
        val originalCacheMap = new CacheMap("id", Map("key" -> Json.toJson(Seq("value"))))
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert.addRepeatedValue("key", "new value", originalCacheMap)
        result.data mustBe Map("key" -> Json.toJson(Seq("value", "new value")))
      }
    }

    "answering 'no' to anyBenefits" must {
      "remove all associated data" in {
        forAll(arbitraryBenefits) {
          benefits =>
            val originalCacheMap = new CacheMap("id", Map(
              SelectBenefitsId.toString -> Json.toJson(benefits),
              HowMuchBereavementAllowanceId.toString -> JsString("1234"),
              HowMuchCarersAllowanceId.toString -> JsString("1234"),
              HowMuchJobseekersAllowanceId.toString -> JsString("1234"),
              HowMuchIncapacityBenefitId.toString -> JsString("1234"),
              HowMuchEmploymentAndSupportAllowanceId.toString -> JsString("1234"),
              HowMuchStatePensionId.toString -> JsString("1234"),
              OtherBenefitsNameId.toString -> JsString("qwerty"),
              HowMuchOtherBenefitId.toString -> JsString("1234"),
              AnyOtherBenefitsId.toString -> JsString("1234")
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(AnyBenefitsId.toString, JsBoolean(false), originalCacheMap)
            result.data.size mustBe 1
            result.data.contains(SelectBenefitsId.toString) mustBe false
            result.data.contains(HowMuchBereavementAllowanceId.toString) mustBe false
            result.data.contains(HowMuchCarersAllowanceId.toString) mustBe false
            result.data.contains(HowMuchJobseekersAllowanceId.toString) mustBe false
            result.data.contains(HowMuchIncapacityBenefitId.toString) mustBe false
            result.data.contains(HowMuchEmploymentAndSupportAllowanceId.toString) mustBe false
            result.data.contains(HowMuchStatePensionId.toString) mustBe false
            result.data.contains(OtherBenefitsNameId.toString) mustBe false
            result.data.contains(HowMuchOtherBenefitId.toString) mustBe false
            result.data.contains(AnyOtherBenefitsId.toString) mustBe false
        }
      }
    }

    "answering 'no' to anyCompanyBenefits" must {
      "remove all associated data" in {
        forAll(arbitraryCompanyBenefits) {
          companyBenefits =>
            val originalCacheMap = new CacheMap("id", Map(
              SelectCompanyBenefitsId.toString -> Json.toJson(companyBenefits),
              HowMuchCarBenefitsId.toString -> JsString("1234"),
              HowMuchFuelBenefitId.toString -> JsString("1234"),
              HowMuchMedicalBenefitsId.toString -> JsString("1234"),
              OtherCompanyBenefitsNameId.toString -> JsString("qwerty"),
              HowMuchOtherCompanyBenefitId.toString ->  JsString("1234"),
              AnyOtherCompanyBenefitsId.toString -> JsBoolean(false)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(AnyCompanyBenefitsId.toString, JsBoolean(false), originalCacheMap)
            result.data.size mustBe 1
            result.data.contains(SelectCompanyBenefitsId.toString) mustBe false
            result.data.contains(HowMuchCarBenefitsId.toString) mustBe false
            result.data.contains(HowMuchFuelBenefitId.toString) mustBe false
            result.data.contains(HowMuchMedicalBenefitsId.toString) mustBe false
            result.data.contains(OtherCompanyBenefitsNameId.toString) mustBe false
            result.data.contains(HowMuchOtherCompanyBenefitId.toString) mustBe false
            result.data.contains(AnyOtherCompanyBenefitsId.toString) mustBe false
        }
      }
    }

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

    "unselecting fuel benefit" must {

      "remove amount of fuel benefit" in {

        forAll(Gen.numStr, arbitraryCompanyBenefits) {
          (fuelBenefitAmount, companyBenefits) =>

            val originalCacheMap = new CacheMap("id", Map(
              SelectCompanyBenefitsId.toString -> Json.toJson(companyBenefits :+ CompanyBenefits.FUEL_BENEFIT),
              HowMuchFuelBenefitId.toString -> JsString(fuelBenefitAmount)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectCompanyBenefitsId.toString, companyBenefits.filterNot(_ == CompanyBenefits.FUEL_BENEFIT), originalCacheMap)
            result.data mustEqual Map(SelectCompanyBenefitsId.toString -> Json.toJson(companyBenefits.filterNot(_ == CompanyBenefits.FUEL_BENEFIT)))
        }
      }
    }

    "unselecting company car benefit" must {

      "remove amount of company car benefit" in {

        forAll(Gen.numStr, arbitraryCompanyBenefits) {
          (carBenefitAmount, companyBenefits) =>

            val originalCacheMap = new CacheMap("id", Map(
              SelectCompanyBenefitsId.toString -> Json.toJson(companyBenefits :+ CompanyBenefits.COMPANY_CAR_BENEFIT),
              HowMuchCarBenefitsId.toString -> JsString(carBenefitAmount)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectCompanyBenefitsId.toString, companyBenefits.filterNot(_ == CompanyBenefits.COMPANY_CAR_BENEFIT), originalCacheMap)
            result.data mustEqual Map(SelectCompanyBenefitsId.toString -> Json.toJson(companyBenefits.filterNot(_ == CompanyBenefits.COMPANY_CAR_BENEFIT)))
        }
      }
    }

    "unselecting medical benefit" must {

      "remove amount of medical benefit" in {

        forAll(Gen.numStr, arbitraryCompanyBenefits) {
          (medicalBenefitAmount, companyBenefits) =>

            val originalCacheMap = new CacheMap("id", Map(
              SelectCompanyBenefitsId.toString -> Json.toJson(companyBenefits :+ CompanyBenefits.MEDICAL_BENEFIT),
              HowMuchMedicalBenefitsId.toString -> JsString(medicalBenefitAmount)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectCompanyBenefitsId.toString, companyBenefits.filterNot(_ == CompanyBenefits.MEDICAL_BENEFIT), originalCacheMap)
            result.data mustEqual Map(SelectCompanyBenefitsId.toString -> Json.toJson(companyBenefits.filterNot(_ == CompanyBenefits.MEDICAL_BENEFIT)))
        }
      }
    }

    "unselecting other company benefit" must {

      "remove amount and name of other company benefit" in {

        forAll(Gen.numStr, arbitraryCompanyBenefits) {
          (amount, companyBenefits) =>

            val originalCacheMap = new CacheMap("id", Map(
              SelectCompanyBenefitsId.toString -> Json.toJson(companyBenefits :+ CompanyBenefits.OTHER_COMPANY_BENEFIT),
              OtherCompanyBenefitsNameId.toString -> JsString("qwerty"),
              HowMuchOtherCompanyBenefitId.toString -> JsString(amount),
              AnyOtherCompanyBenefitsId.toString -> JsBoolean(false)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectCompanyBenefitsId.toString, companyBenefits.filterNot(_ == CompanyBenefits.OTHER_COMPANY_BENEFIT), originalCacheMap)
            result.data mustEqual Map(
              SelectCompanyBenefitsId.toString -> Json.toJson(companyBenefits.filterNot(_ == CompanyBenefits.OTHER_COMPANY_BENEFIT))
            )
        }
      }
    }

    "unselecting BEREAVEMENT_ALLOWANCE" must {
      "remove the figure from userAnswers" in {
        forAll(Gen.numStr, arbitraryBenefits) {
          (amount, benefits) =>

            val originalCacheMap = new CacheMap("", Map(
              SelectBenefitsId.toString -> Json.toJson(benefits :+ Benefits.BEREAVEMENT_ALLOWANCE),
              HowMuchBereavementAllowanceId.toString -> JsString(amount)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectBenefitsId.toString, benefits.filterNot(_ == Benefits.BEREAVEMENT_ALLOWANCE), originalCacheMap)
            result.data mustEqual Map(SelectBenefitsId.toString -> Json.toJson(benefits.filterNot(_ == Benefits.BEREAVEMENT_ALLOWANCE)))
        }
      }
    }

    "unselecting CARERS_ALLOWANCE" must {
      "remove the figure from userAnswers" in {
        forAll(Gen.numStr, arbitraryBenefits) {
          (amount, benefits) =>

            val originalCacheMap = new CacheMap("", Map(
              SelectBenefitsId.toString -> Json.toJson(benefits :+ Benefits.CARERS_ALLOWANCE),
              HowMuchCarersAllowanceId.toString -> JsString(amount)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectBenefitsId.toString, benefits.filterNot(_ == Benefits.CARERS_ALLOWANCE), originalCacheMap)
            result.data mustEqual Map(SelectBenefitsId.toString -> Json.toJson(benefits.filterNot(_ == Benefits.CARERS_ALLOWANCE)))
        }
      }
    }

    "unselecting JOBSEEKERS_ALLOWANCE" must {
      "remove the figure from userAnswers" in {
        forAll(Gen.numStr, arbitraryBenefits) {
          (amount, benefits) =>

            val originalCacheMap = new CacheMap("", Map(
              SelectBenefitsId.toString -> Json.toJson(benefits :+ Benefits.JOBSEEKERS_ALLOWANCE),
              HowMuchJobseekersAllowanceId.toString -> JsString(amount)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectBenefitsId.toString, benefits.filterNot(_ == Benefits.JOBSEEKERS_ALLOWANCE), originalCacheMap)
            result.data mustEqual Map(SelectBenefitsId.toString -> Json.toJson(benefits.filterNot(_ == Benefits.JOBSEEKERS_ALLOWANCE)))
        }
      }
    }

    "unselecting INCAPACITY_BENEFIT" must {
      "remove the figure from userAnswers" in {
        forAll(Gen.numStr, arbitraryBenefits) {
          (amount, benefits) =>

            val originalCacheMap = new CacheMap("", Map(
              SelectBenefitsId.toString -> Json.toJson(benefits :+ Benefits.INCAPACITY_BENEFIT),
              HowMuchIncapacityBenefitId.toString -> JsString(amount)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectBenefitsId.toString, benefits.filterNot(_ == Benefits.INCAPACITY_BENEFIT), originalCacheMap)
            result.data mustEqual Map(SelectBenefitsId.toString -> Json.toJson(benefits.filterNot(_ == Benefits.INCAPACITY_BENEFIT)))
        }
      }
    }

    "unselecting EMPLOYMENT_AND_SUPPORT_ALLOWANCE" must {
      "remove the figure from userAnswers" in {
        forAll(Gen.numStr, arbitraryBenefits) {
          (amount, benefits) =>

            val originalCacheMap = new CacheMap("", Map(
              SelectBenefitsId.toString -> Json.toJson(benefits :+ Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE),
              HowMuchEmploymentAndSupportAllowanceId.toString -> JsString(amount)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectBenefitsId.toString, benefits.filterNot(_ == Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE), originalCacheMap)
            result.data mustEqual Map(SelectBenefitsId.toString -> Json.toJson(benefits.filterNot(_ == Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE)))
        }
      }
    }

    "unselecting STATE_PENSION" must {
      "remove the figure from userAnswers" in {
        forAll(Gen.numStr, arbitraryBenefits) {
          (amount, benefits) =>

            val originalCacheMap = new CacheMap("", Map(
              SelectBenefitsId.toString -> Json.toJson(benefits :+ Benefits.STATE_PENSION),
              HowMuchStatePensionId.toString -> JsString(amount)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectBenefitsId.toString, benefits.filterNot(_ == Benefits.STATE_PENSION), originalCacheMap)
            result.data mustEqual Map(SelectBenefitsId.toString -> Json.toJson(benefits.filterNot(_ == Benefits.STATE_PENSION)))
        }
      }
    }

    "unselecting OTHER_TAXABLE_BENEFIT" must {
      "remove the figure and name from userAnswers" in {
        forAll(Gen.numStr, arbitraryBenefits) {
          (amount, benefits) =>

            val originalCacheMap = new CacheMap("", Map(
              SelectBenefitsId.toString -> Json.toJson(benefits :+ Benefits.OTHER_TAXABLE_BENEFIT),
              OtherBenefitsNameId.toString -> JsString("qwerty"),
              HowMuchOtherBenefitId.toString -> JsString(amount),
              AnyOtherBenefitsId.toString -> JsBoolean(false)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectBenefitsId.toString, benefits.filterNot(_ == Benefits.OTHER_TAXABLE_BENEFIT), originalCacheMap)
            result.data mustEqual Map(SelectBenefitsId.toString -> Json.toJson(benefits.filterNot(_ == Benefits.OTHER_TAXABLE_BENEFIT)))
        }
      }
    }

    "select company benefit when no data currently stored" must {

      "store the data" in {

        forAll(arbitraryCompanyBenefits) {
          companyBenefits =>

            val originalCacheMap = new CacheMap("id", Map())
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectCompanyBenefitsId.toString, companyBenefits, originalCacheMap)
            result.data mustEqual Map(SelectCompanyBenefitsId.toString -> Json.toJson(companyBenefits))
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
}
