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
import models.CompanyBenefits
import org.scalacheck.Gen
import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap
import org.scalatest.prop.PropertyChecks

class CascadeUpsertSpec extends SpecBase with PropertyChecks {

  private val arbitraryBenefits: Gen[Set[CompanyBenefits.Value]] =
    Gen.containerOf[Set, CompanyBenefits.Value](Gen.oneOf(
      CompanyBenefits.FUEL_BENEFIT,
      CompanyBenefits.MEDICAL_BENEFIT,
      CompanyBenefits.COMPANY_CAR_BENEFIT,
      CompanyBenefits.OTHER_COMPANY_BENEFIT
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

    "unselecting fuel benefit" must {

      "remove amount of fuel benefit" in {

        forAll(Gen.numStr, arbitraryBenefits) {
          (fuelBenefitAmount, benefits) =>

            val originalCacheMap = new CacheMap("id", Map(
              SelectCompanyBenefitsId.toString -> Json.toJson(benefits + CompanyBenefits.FUEL_BENEFIT),
              HowMuchFuelBenefitId.toString -> JsString(fuelBenefitAmount)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectCompanyBenefitsId.toString, benefits - CompanyBenefits.FUEL_BENEFIT, originalCacheMap)
            result.data mustEqual Map(SelectCompanyBenefitsId.toString -> Json.toJson(benefits - CompanyBenefits.FUEL_BENEFIT))
        }
      }
    }

    "unselecting company car benefit" must {

      "remove amount of company car benefit" in {

        forAll(Gen.numStr, arbitraryBenefits) {
          (carBenefitAmount, benefits) =>

            val originalCacheMap = new CacheMap("id", Map(
              SelectCompanyBenefitsId.toString -> Json.toJson(benefits + CompanyBenefits.COMPANY_CAR_BENEFIT),
              HowMuchCarBenefitsId.toString -> JsString(carBenefitAmount)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectCompanyBenefitsId.toString, benefits - CompanyBenefits.COMPANY_CAR_BENEFIT, originalCacheMap)
            result.data mustEqual Map(SelectCompanyBenefitsId.toString -> Json.toJson(benefits - CompanyBenefits.COMPANY_CAR_BENEFIT))
        }
      }
    }

    "unselecting medical benefit" must {

      "remove amount of medical benefit" in {

        forAll(Gen.numStr, arbitraryBenefits) {
          (medicalBenefitAmount, benefits) =>

            val originalCacheMap = new CacheMap("id", Map(
              SelectCompanyBenefitsId.toString -> Json.toJson(benefits + CompanyBenefits.MEDICAL_BENEFIT),
              HowMuchMedicalBenefitsId.toString -> JsString(medicalBenefitAmount)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectCompanyBenefitsId.toString, benefits - CompanyBenefits.MEDICAL_BENEFIT, originalCacheMap)
            result.data mustEqual Map(SelectCompanyBenefitsId.toString -> Json.toJson(benefits - CompanyBenefits.MEDICAL_BENEFIT))
        }
      }
    }

    "unselecting other company benefit" must {

      "remove amount of other company benefit" in {

        forAll(Gen.numStr, arbitraryBenefits) {
          (otherCompanyBenefitAmount, benefits) =>

            val originalCacheMap = new CacheMap("id", Map(
              SelectCompanyBenefitsId.toString -> Json.toJson(benefits + CompanyBenefits.OTHER_COMPANY_BENEFIT),
              HowMuchOtherCompanyBenefitId.toString -> JsString(otherCompanyBenefitAmount)
            ))
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectCompanyBenefitsId.toString, benefits - CompanyBenefits.OTHER_COMPANY_BENEFIT, originalCacheMap)
            result.data mustEqual Map(SelectCompanyBenefitsId.toString -> Json.toJson(benefits - CompanyBenefits.OTHER_COMPANY_BENEFIT))
        }
      }
    }

    "select company benefit when no data currently stored" must {

      "store the data" in {

        forAll(arbitraryBenefits) {
          (benefits) =>

            val originalCacheMap = new CacheMap("id", Map())
            val cascadeUpsert = new CascadeUpsert
            val result = cascadeUpsert(SelectCompanyBenefitsId.toString, benefits, originalCacheMap)
            result.data mustEqual Map(SelectCompanyBenefitsId.toString -> Json.toJson(benefits))
        }
      }
    }
  }
}
