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
import models.Benefits
import org.scalacheck.{Gen, Shrink}
import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap
import org.scalatest.prop.PropertyChecks

class BenefitsCascadeUpsertSpec extends SpecBase with PropertyChecks {

  implicit def dontShrink[A]: Shrink[A] = Shrink.shrinkAny

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
}