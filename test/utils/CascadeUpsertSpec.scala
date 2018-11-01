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
import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap
import org.scalatest.prop.PropertyChecks

class CascadeUpsertSpec extends SpecBase with PropertyChecks {

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
  }

  "Claim details section" must {
    "Answering 'Yes' on EmploymentDetailsController" must {
      "remove data from next 2 screens" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          EmploymentDetailsId.toString -> Json.toJson(false),
          EnterPayeReferenceId.toString -> Json.toJson("123/AB1234"),
          DetailsOfEmploymentOrPensionId.toString -> Json.toJson("some details")
        ))
        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert(EmploymentDetailsId.toString, JsBoolean(true), originalCacheMap)
        result.data mustBe Map(
          EmploymentDetailsId.toString -> Json.toJson(true)
        )
      }
    }
  }

  "Other Benefits section" must {
    "Answering 'No' on RemoveOtherSelectedOption" must {
      "remove selectBenefits and OtherBenefitId from CacheMap and change AnyBenefits to false when OtherBenefit is empty" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          AnyBenefitsId.toString -> Json.toJson(true),
          SelectBenefitsId.toString -> Json.toJson(Seq("other-taxable-benefit")),
          OtherBenefitId.toString -> Json.toJson(Seq("value")),
          AnyOtherBenefitsId.toString -> Json.toJson(false)
        ))

        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert("otherBenefits", JsBoolean(false), originalCacheMap)

        result.data mustBe Map(
          AnyBenefitsId.toString -> Json.toJson(false),
          RemoveOtherSelectedOptionId.toString -> Json.toJson(false)
        )
      }

      "remove other-taxable-benefit from SelectBenefits when OtherBenefit is not empty" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          AnyBenefitsId.toString -> Json.toJson(true),
          SelectBenefitsId.toString -> Json.toJson(Seq("bereavement-allowance", "other-taxable-benefit")),
          OtherBenefitId.toString -> Json.toJson(Seq("value")),
          AnyOtherBenefitsId.toString -> Json.toJson(false)
        ))

        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert("otherBenefits", JsBoolean(false), originalCacheMap)

        result.data mustBe Map(
          AnyBenefitsId.toString -> Json.toJson(true),
          SelectBenefitsId.toString -> Json.toJson(Seq("bereavement-allowance")),
          RemoveOtherSelectedOptionId.toString -> Json.toJson(false)
        )
      }
    }

    "Answering 'Yes' on RemoveOtherSelectedOption" must {
      "return original CacheMap" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          AnyBenefitsId.toString -> Json.toJson(true),
          SelectBenefitsId.toString -> Json.toJson(Seq("bereavement-allowance", "other-taxable-benefit")),
          OtherBenefitId.toString -> Json.toJson(Seq("value")),
          AnyOtherBenefitsId.toString -> Json.toJson(false)
        ))

        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert("otherBenefits", JsBoolean(true), originalCacheMap)

        result.data mustBe Map(
          AnyBenefitsId.toString -> Json.toJson(true),
          SelectBenefitsId.toString -> Json.toJson(Seq("bereavement-allowance", "other-taxable-benefit")),
          OtherBenefitId.toString -> Json.toJson(Seq("value")),
          AnyOtherBenefitsId.toString -> Json.toJson(false),
          RemoveOtherSelectedOptionId.toString -> Json.toJson(true)
        )
      }
    }
  }

  "Other Company Benefits section" must {
    "Answering 'No' on RemoveOtherSelectedOption" must {
      "remove selectCompanyBenefits and OtherCompanyBenefits from CacheMap and change AnyCompanyBenefits to false when OtherCompanyBenefit is empty" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          AnyCompanyBenefitsId.toString -> Json.toJson(true),
          SelectCompanyBenefitsId.toString -> Json.toJson(Seq("other-company-benefit")),
          OtherCompanyBenefitId.toString -> Json.toJson(Seq("value")),
          AnyOtherCompanyBenefitsId.toString -> Json.toJson(false)
        ))

        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert("otherCompanyBenefits", JsBoolean(false), originalCacheMap)

        result.data mustBe Map(
          AnyCompanyBenefitsId.toString -> Json.toJson(false),
          RemoveOtherSelectedOptionId.toString -> Json.toJson(false)
        )
      }

      "remove other-company-benefit from selectCompanyBenefits when OtherCompanyBenefit is not empty" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          AnyCompanyBenefitsId.toString -> Json.toJson(true),
          SelectCompanyBenefitsId.toString -> Json.toJson(Seq("company-car-benefit", "other-company-benefit")),
          OtherCompanyBenefitId.toString -> Json.toJson(Seq("value")),
          AnyOtherCompanyBenefitsId.toString -> Json.toJson(false)
        ))

        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert("otherCompanyBenefits", JsBoolean(false), originalCacheMap)

        result.data mustBe Map(
          AnyCompanyBenefitsId.toString -> Json.toJson(true),
          SelectCompanyBenefitsId.toString -> Json.toJson(Seq("company-car-benefit")),
          RemoveOtherSelectedOptionId.toString -> Json.toJson(false)
        )
      }

      "Answering 'Yes' on RemoveOtherSelectedOption" must {
        "return original CacheMap" in {
          val originalCacheMap = new CacheMap(id = "test", Map(
            AnyCompanyBenefitsId.toString -> Json.toJson(true),
            SelectCompanyBenefitsId.toString -> Json.toJson(Seq("company-car-benefit", "other-company-benefit")),
            OtherCompanyBenefitId.toString -> Json.toJson(Seq("value")),
            AnyOtherCompanyBenefitsId.toString -> Json.toJson(false)
          ))

          val cascadeUpsert = new CascadeUpsert
          val result = cascadeUpsert("otherCompanyBenefits", JsBoolean(true), originalCacheMap)

          result.data mustBe Map(
            AnyCompanyBenefitsId.toString -> Json.toJson(true),
            SelectCompanyBenefitsId.toString -> Json.toJson(Seq("company-car-benefit", "other-company-benefit")),
            OtherCompanyBenefitId.toString -> Json.toJson(Seq("value")),
            AnyOtherCompanyBenefitsId.toString -> Json.toJson(false),
            RemoveOtherSelectedOptionId.toString -> Json.toJson(true)
          )
        }
      }
    }
  }

  "Other Taxable Income section" must {
    "Answering 'No' on RemoveOtherSelectedOption" must {
      "remove selectTaxableIncome and OtherTaxableIncome from CacheMap and change AnyTaxableIncome to false when OtherTaxableIncome is empty" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          AnyTaxableIncomeId.toString -> Json.toJson(true),
          SelectTaxableIncomeId.toString -> Json.toJson(Seq("other-taxable-income")),
          OtherTaxableIncomeId.toString -> Json.toJson(Seq("value")),
          AnyOtherTaxableIncomeId.toString -> Json.toJson(false)
        ))

        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert("otherTaxableIncome", JsBoolean(false), originalCacheMap)

        result.data mustBe Map(
          AnyTaxableIncomeId.toString -> Json.toJson(false),
          RemoveOtherSelectedOptionId.toString -> Json.toJson(false)
        )
      }

      "remove other-taxable-income from selectTaxableIncome when OtherTaxableIncome is not empty" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          AnyTaxableIncomeId.toString -> Json.toJson(true),
          SelectTaxableIncomeId.toString -> Json.toJson(Seq("rental-income", "other-taxable-income")),
          OtherTaxableIncomeId.toString -> Json.toJson(Seq("value")),
          AnyOtherTaxableIncomeId.toString -> Json.toJson(false)
        ))

        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert("otherTaxableIncome", JsBoolean(false), originalCacheMap)

        result.data mustBe Map(
          AnyTaxableIncomeId.toString -> Json.toJson(true),
          SelectTaxableIncomeId.toString -> Json.toJson(Seq("rental-income")),
          RemoveOtherSelectedOptionId.toString -> Json.toJson(false)
        )
      }
    }

    "Answering 'Yes' on RemoveOtherSelectedOption" must {
      "return original CacheMap" in {
        val originalCacheMap = new CacheMap(id = "test", Map(
          AnyTaxableIncomeId.toString -> Json.toJson(true),
          SelectTaxableIncomeId.toString -> Json.toJson(Seq("rental-income", "other-taxable-income")),
          OtherTaxableIncomeId.toString -> Json.toJson(Seq("value")),
          AnyOtherTaxableIncomeId.toString -> Json.toJson(false)
        ))

        val cascadeUpsert = new CascadeUpsert
        val result = cascadeUpsert("otherTaxableIncome", JsBoolean(true), originalCacheMap)

        result.data mustBe Map(
          AnyTaxableIncomeId.toString -> Json.toJson(true),
          SelectTaxableIncomeId.toString -> Json.toJson(Seq("rental-income", "other-taxable-income")),
          OtherTaxableIncomeId.toString -> Json.toJson(Seq("value")),
          AnyOtherTaxableIncomeId.toString -> Json.toJson(false),
          RemoveOtherSelectedOptionId.toString -> Json.toJson(true)
        )
      }
    }
  }

  "SelectTaxYear" must {
    "clear employment details if selectTaxYear has changed" in {
      val originalCacheMap = new CacheMap(id = "test", Map(
        SelectTaxYearId.toString -> Json.toJson("current-year-minus-3"),
        EmploymentDetailsId.toString -> Json.toJson(true),
        EnterPayeReferenceId.toString -> Json.toJson("123/AB456"),
        DetailsOfEmploymentOrPensionId.toString -> Json.toJson("value")
      ))

      val cascadeUpsert = new CascadeUpsert
      val result = cascadeUpsert("selectTaxYear", Json.toJson("current-year-minus-2"), originalCacheMap)

      result.data mustBe Map(
        SelectTaxYearId.toString -> Json.toJson("current-year-minus-2")
      )
    }

    "change nothing if selectTaxYear is the same" in {
      val originalCacheMap = new CacheMap(id = "test", Map(
        SelectTaxYearId.toString -> Json.toJson("current-year-minus-2"),
        EmploymentDetailsId.toString -> Json.toJson(true),
        EnterPayeReferenceId.toString -> Json.toJson("123/AB456"),
        DetailsOfEmploymentOrPensionId.toString -> Json.toJson("value")
      ))

      val cascadeUpsert = new CascadeUpsert
      val result = cascadeUpsert("selectTaxYear", Json.toJson("current-year-minus-2"), originalCacheMap)

      result.data mustBe Map(
        SelectTaxYearId.toString -> Json.toJson("current-year-minus-2"),
        EmploymentDetailsId.toString -> Json.toJson(true),
        EnterPayeReferenceId.toString -> Json.toJson("123/AB456"),
        DetailsOfEmploymentOrPensionId.toString -> Json.toJson("value")
      )
    }
  }
}
