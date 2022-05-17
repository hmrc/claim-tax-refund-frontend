/*
 * Copyright 2022 HM Revenue & Customs
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
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class CompanyBenefitsCascadeUpsertSpec extends SpecBase with ScalaCheckPropertyChecks {

  implicit def dontShrink[A]: Shrink[A] = Shrink.shrinkAny

  private val allCompanyBenefits = new CacheMap(id = "test", Map(
    AnyCompanyBenefitsId.toString -> Json.toJson(true),
    SelectCompanyBenefitsId.toString -> Json.toJson(CompanyBenefits.sortedCompanyBenefits),
    HowMuchCarBenefitsId.toString -> JsString("1234"),
    HowMuchFuelBenefitId.toString -> JsString("1234"),
    HowMuchMedicalBenefitsId.toString -> JsString("1234"),
    OtherCompanyBenefitId.toString -> JsString("qwerty"),
    AnyOtherCompanyBenefitsId.toString -> JsBoolean(false)
  ))
  private val cascadeUpsert = new CascadeUpsert

  private val arbitraryCompanyBenefits: Gen[Seq[CompanyBenefits.Value]] =
    Gen.containerOf[Seq, CompanyBenefits.Value](Gen.oneOf(
      CompanyBenefits.COMPANY_CAR_BENEFIT,
      CompanyBenefits.FUEL_BENEFIT,
      CompanyBenefits.MEDICAL_BENEFIT,
      CompanyBenefits.OTHER_COMPANY_BENEFIT
    ))

  "answering 'no' to anyCompanyBenefits" must {
    "remove all associated data" in {
      forAll(arbitraryCompanyBenefits) {
        companyBenefits =>
          val originalCacheMap = new CacheMap(id = "test", Map(
            AnyCompanyBenefitsId.toString -> Json.toJson(true),
            SelectCompanyBenefitsId.toString -> Json.toJson(companyBenefits),
            HowMuchCarBenefitsId.toString -> JsString("1234"),
            HowMuchFuelBenefitId.toString -> JsString("1234"),
            HowMuchMedicalBenefitsId.toString -> JsString("1234"),
            OtherCompanyBenefitId.toString -> JsString("qwerty"),
            AnyOtherCompanyBenefitsId.toString -> JsBoolean(false)
          ))
          val result = cascadeUpsert(AnyCompanyBenefitsId.toString, JsBoolean(false), originalCacheMap)
          result.data mustBe Map(
            AnyCompanyBenefitsId.toString.toString -> Json.toJson(false)
          )
      }
    }
  }

  "SelectCompanyBenefits" must {
    "un-selecting COMPANY_CAR to remove associated data" in {
      val result = cascadeUpsert(SelectCompanyBenefitsId.toString, Seq(
        CompanyBenefits.FUEL_BENEFIT,
        CompanyBenefits.MEDICAL_BENEFIT,
        CompanyBenefits.OTHER_COMPANY_BENEFIT)
        , allCompanyBenefits)
      result.data mustBe Map(
        AnyCompanyBenefitsId.toString -> Json.toJson(true),
        SelectCompanyBenefitsId.toString -> Json.toJson(Seq(
          CompanyBenefits.FUEL_BENEFIT,
          CompanyBenefits.MEDICAL_BENEFIT,
          CompanyBenefits.OTHER_COMPANY_BENEFIT
        )),
        HowMuchFuelBenefitId.toString -> JsString("1234"),
        HowMuchMedicalBenefitsId.toString -> JsString("1234"),
        OtherCompanyBenefitId.toString -> JsString("qwerty"),
        AnyOtherCompanyBenefitsId.toString -> JsBoolean(false)
      )
    }

    "un-selecting FUEL to remove associated data" in {
      val result = cascadeUpsert(SelectCompanyBenefitsId.toString, Seq(
        CompanyBenefits.COMPANY_CAR_BENEFIT,
        CompanyBenefits.MEDICAL_BENEFIT,
        CompanyBenefits.OTHER_COMPANY_BENEFIT)
        , allCompanyBenefits)
      result.data mustBe Map(
        AnyCompanyBenefitsId.toString -> Json.toJson(true),
        SelectCompanyBenefitsId.toString -> Json.toJson(Seq(
          CompanyBenefits.COMPANY_CAR_BENEFIT,
          CompanyBenefits.MEDICAL_BENEFIT,
          CompanyBenefits.OTHER_COMPANY_BENEFIT
        )),
        HowMuchCarBenefitsId.toString -> JsString("1234"),
        HowMuchMedicalBenefitsId.toString -> JsString("1234"),
        OtherCompanyBenefitId.toString -> JsString("qwerty"),
        AnyOtherCompanyBenefitsId.toString -> JsBoolean(false)
      )
    }

    "un-selecting MEDICAL to remove associated data" in {
      val result = cascadeUpsert(SelectCompanyBenefitsId.toString, Seq(
        CompanyBenefits.COMPANY_CAR_BENEFIT,
        CompanyBenefits.FUEL_BENEFIT,
        CompanyBenefits.OTHER_COMPANY_BENEFIT)
        , allCompanyBenefits)
      result.data mustBe Map(
        AnyCompanyBenefitsId.toString -> Json.toJson(true),
        SelectCompanyBenefitsId.toString -> Json.toJson(Seq(
          CompanyBenefits.COMPANY_CAR_BENEFIT,
          CompanyBenefits.FUEL_BENEFIT,
          CompanyBenefits.OTHER_COMPANY_BENEFIT
        )),
        HowMuchCarBenefitsId.toString -> JsString("1234"),
        HowMuchFuelBenefitId.toString -> JsString("1234"),
        OtherCompanyBenefitId.toString -> JsString("qwerty"),
        AnyOtherCompanyBenefitsId.toString -> JsBoolean(false)
      )
    }

    "un-selecting OTHER_COMPANY to remove associated data" in {
      val result = cascadeUpsert(SelectCompanyBenefitsId.toString, Seq(
        CompanyBenefits.COMPANY_CAR_BENEFIT,
        CompanyBenefits.FUEL_BENEFIT,
        CompanyBenefits.MEDICAL_BENEFIT
      ), allCompanyBenefits)
      result.data mustBe Map(
        AnyCompanyBenefitsId.toString -> Json.toJson(true),
        SelectCompanyBenefitsId.toString -> Json.toJson(Seq(
          CompanyBenefits.COMPANY_CAR_BENEFIT,
          CompanyBenefits.FUEL_BENEFIT,
          CompanyBenefits.MEDICAL_BENEFIT
        )),
        HowMuchCarBenefitsId.toString -> JsString("1234"),
        HowMuchFuelBenefitId.toString -> JsString("1234"),
        HowMuchMedicalBenefitsId.toString -> JsString("1234")
      )
    }

    "un-selecting COMPANY_CAR and MEDICAL to remove associated data" in {
      val result = cascadeUpsert(SelectCompanyBenefitsId.toString, Seq(
        CompanyBenefits.FUEL_BENEFIT,
        CompanyBenefits.OTHER_COMPANY_BENEFIT)
        , allCompanyBenefits)
      result.data mustBe Map(
        AnyCompanyBenefitsId.toString -> Json.toJson(true),
        SelectCompanyBenefitsId.toString -> Json.toJson(Seq(
          CompanyBenefits.FUEL_BENEFIT,
          CompanyBenefits.OTHER_COMPANY_BENEFIT
        )),
        HowMuchFuelBenefitId.toString -> JsString("1234"),
        OtherCompanyBenefitId.toString -> JsString("qwerty"),
        AnyOtherCompanyBenefitsId.toString -> JsBoolean(false)
      )
    }

    "un-selecting COMPANY_CAR, MEDICAL and OTHER to remove associated data" in {
      val result = cascadeUpsert(SelectCompanyBenefitsId.toString, Seq(
        CompanyBenefits.FUEL_BENEFIT)
        , allCompanyBenefits)
      result.data mustBe Map(
        AnyCompanyBenefitsId.toString -> Json.toJson(true),
        SelectCompanyBenefitsId.toString -> Json.toJson(Seq(
          CompanyBenefits.FUEL_BENEFIT
        )),
        HowMuchFuelBenefitId.toString -> JsString("1234")
      )
    }
  }
}
