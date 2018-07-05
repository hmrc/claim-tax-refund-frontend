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
import org.scalacheck.{Gen, Shrink}
import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap
import org.scalatest.prop.PropertyChecks

class CompanyBenefitsCascadeUpsertSpec extends SpecBase with PropertyChecks {

  implicit def dontShrink[A]: Shrink[A] = Shrink.shrinkAny

  private val arbitraryCompanyBenefits: Gen[Seq[CompanyBenefits.Value]] =
    Gen.containerOf[Seq, CompanyBenefits.Value](Gen.oneOf(
      CompanyBenefits.FUEL_BENEFIT,
      CompanyBenefits.MEDICAL_BENEFIT,
      CompanyBenefits.COMPANY_CAR_BENEFIT,
      CompanyBenefits.OTHER_COMPANY_BENEFIT
    ))

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
}