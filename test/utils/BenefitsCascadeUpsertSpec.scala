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
import models.{Benefits, OtherTaxableIncome}
import org.scalacheck.{Gen, Shrink}
import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap
import org.scalatest.prop.PropertyChecks

class BenefitsCascadeUpsertSpec extends SpecBase with PropertyChecks {

  implicit def dontShrink[A]: Shrink[A] = Shrink.shrinkAny

  private val cascadeUpsert = new CascadeUpsert
  private val allBenefits = new CacheMap(id = "test", Map(
    AnyBenefitsId.toString -> Json.toJson(true),
    SelectBenefitsId.toString -> Json.toJson(Benefits.sortedBenefits),
    HowMuchBereavementAllowanceId.toString -> JsString("1234"),
    HowMuchCarersAllowanceId.toString -> JsString("1234"),
    HowMuchJobseekersAllowanceId.toString -> JsString("1234"),
    HowMuchIncapacityBenefitId.toString -> JsString("1234"),
    HowMuchEmploymentAndSupportAllowanceId.toString -> JsString("1234"),
    HowMuchStatePensionId.toString -> JsString("1234"),
    OtherBenefitId.toString -> JsString("qwerty"),
    AnyOtherBenefitsId.toString -> JsBoolean(false)
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

  "answering 'no' to anyBenefits" must {
    "remove all associated data" in {
      forAll(arbitraryBenefits) {
        benefits =>
          val originalCacheMap = new CacheMap("id", Map(
            AnyBenefitsId.toString -> Json.toJson(true),
            SelectBenefitsId.toString -> Json.toJson(benefits),
            HowMuchBereavementAllowanceId.toString -> JsString("1234"),
            HowMuchCarersAllowanceId.toString -> JsString("1234"),
            HowMuchJobseekersAllowanceId.toString -> JsString("1234"),
            HowMuchIncapacityBenefitId.toString -> JsString("1234"),
            HowMuchEmploymentAndSupportAllowanceId.toString -> JsString("1234"),
            HowMuchStatePensionId.toString -> JsString("1234"),
            OtherBenefitId.toString -> JsString("qwerty"),
            AnyOtherBenefitsId.toString -> JsString("1234")
          ))
          val result = cascadeUpsert(AnyBenefitsId.toString, JsBoolean(false), originalCacheMap)
          result.data mustBe Map(
            AnyBenefitsId.toString -> Json.toJson(false)
          )
      }
    }
  }

  "SelectBenefits" must {
    "removing bereavment and its associated howMuch" in {
      val result = cascadeUpsert(SelectBenefitsId.toString, Seq(
        Benefits.CARERS_ALLOWANCE,
        Benefits.JOBSEEKERS_ALLOWANCE,
        Benefits.INCAPACITY_BENEFIT,
        Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
        Benefits.STATE_PENSION,
        Benefits.OTHER_TAXABLE_BENEFIT
      ), allBenefits)
      result.data mustBe Map(
        AnyBenefitsId.toString -> Json.toJson(true),
        SelectBenefitsId.toString -> Json.toJson(Seq(
          Benefits.CARERS_ALLOWANCE,
          Benefits.JOBSEEKERS_ALLOWANCE,
          Benefits.INCAPACITY_BENEFIT,
          Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
          Benefits.STATE_PENSION,
          Benefits.OTHER_TAXABLE_BENEFIT
        )),
        HowMuchCarersAllowanceId.toString -> JsString("1234"),
        HowMuchJobseekersAllowanceId.toString -> JsString("1234"),
        HowMuchIncapacityBenefitId.toString -> JsString("1234"),
        HowMuchEmploymentAndSupportAllowanceId.toString -> JsString("1234"),
        HowMuchStatePensionId.toString -> JsString("1234"),
        OtherBenefitId.toString -> JsString("qwerty"),
        AnyOtherBenefitsId.toString -> JsBoolean(false)
      )
    }


    "removing careers and its associated howMuch" in {
      val result = cascadeUpsert(SelectBenefitsId.toString, Seq(
        Benefits.BEREAVEMENT_ALLOWANCE,
        Benefits.JOBSEEKERS_ALLOWANCE,
        Benefits.INCAPACITY_BENEFIT,
        Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
        Benefits.STATE_PENSION,
        Benefits.OTHER_TAXABLE_BENEFIT
      ), allBenefits)
      result.data mustBe Map(
        AnyBenefitsId.toString -> Json.toJson(true),
        SelectBenefitsId.toString -> Json.toJson(Seq(
          Benefits.BEREAVEMENT_ALLOWANCE,
          Benefits.JOBSEEKERS_ALLOWANCE,
          Benefits.INCAPACITY_BENEFIT,
          Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
          Benefits.STATE_PENSION,
          Benefits.OTHER_TAXABLE_BENEFIT
        )),
        HowMuchBereavementAllowanceId.toString -> JsString("1234"),
        HowMuchJobseekersAllowanceId.toString -> JsString("1234"),
        HowMuchIncapacityBenefitId.toString -> JsString("1234"),
        HowMuchEmploymentAndSupportAllowanceId.toString -> JsString("1234"),
        HowMuchStatePensionId.toString -> JsString("1234"),
        OtherBenefitId.toString -> JsString("qwerty"),
        AnyOtherBenefitsId.toString -> JsBoolean(false)
      )
    }

    "removing jobseekers and its associated howMuch" in {
      val result = cascadeUpsert(SelectBenefitsId.toString, Seq(
        Benefits.BEREAVEMENT_ALLOWANCE,
        Benefits.CARERS_ALLOWANCE,
        Benefits.INCAPACITY_BENEFIT,
        Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
        Benefits.STATE_PENSION,
        Benefits.OTHER_TAXABLE_BENEFIT
      ), allBenefits)
      result.data mustBe Map(
        AnyBenefitsId.toString -> Json.toJson(true),
        SelectBenefitsId.toString -> Json.toJson(Seq(
          Benefits.BEREAVEMENT_ALLOWANCE,
          Benefits.CARERS_ALLOWANCE,
          Benefits.INCAPACITY_BENEFIT,
          Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
          Benefits.STATE_PENSION,
          Benefits.OTHER_TAXABLE_BENEFIT
        )),
        HowMuchBereavementAllowanceId.toString -> JsString("1234"),
        HowMuchCarersAllowanceId.toString -> JsString("1234"),
        HowMuchIncapacityBenefitId.toString -> JsString("1234"),
        HowMuchEmploymentAndSupportAllowanceId.toString -> JsString("1234"),
        HowMuchStatePensionId.toString -> JsString("1234"),
        OtherBenefitId.toString -> JsString("qwerty"),
        AnyOtherBenefitsId.toString -> JsBoolean(false)
      )
    }

    "removing incapacity and its associated howMuch" in {
      val result = cascadeUpsert(SelectBenefitsId.toString, Seq(
        Benefits.BEREAVEMENT_ALLOWANCE,
        Benefits.CARERS_ALLOWANCE,
        Benefits.JOBSEEKERS_ALLOWANCE,
        Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
        Benefits.STATE_PENSION,
        Benefits.OTHER_TAXABLE_BENEFIT
      ), allBenefits)
      result.data mustBe Map(
        AnyBenefitsId.toString -> Json.toJson(true),
        SelectBenefitsId.toString -> Json.toJson(Seq(
          Benefits.BEREAVEMENT_ALLOWANCE,
          Benefits.CARERS_ALLOWANCE,
          Benefits.JOBSEEKERS_ALLOWANCE,
          Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
          Benefits.STATE_PENSION,
          Benefits.OTHER_TAXABLE_BENEFIT
        )),
        HowMuchBereavementAllowanceId.toString -> JsString("1234"),
        HowMuchCarersAllowanceId.toString -> JsString("1234"),
        HowMuchJobseekersAllowanceId.toString -> JsString("1234"),
        HowMuchEmploymentAndSupportAllowanceId.toString -> JsString("1234"),
        HowMuchStatePensionId.toString -> JsString("1234"),
        OtherBenefitId.toString -> JsString("qwerty"),
        AnyOtherBenefitsId.toString -> JsBoolean(false)
      )
    }

    "removing employment and its associated howMuch" in {
      val result = cascadeUpsert(SelectBenefitsId.toString, Seq(
        Benefits.BEREAVEMENT_ALLOWANCE,
        Benefits.CARERS_ALLOWANCE,
        Benefits.JOBSEEKERS_ALLOWANCE,
        Benefits.INCAPACITY_BENEFIT,
        Benefits.STATE_PENSION,
        Benefits.OTHER_TAXABLE_BENEFIT
      ), allBenefits)
      result.data mustBe Map(
        AnyBenefitsId.toString -> Json.toJson(true),
        SelectBenefitsId.toString -> Json.toJson(Seq(
          Benefits.BEREAVEMENT_ALLOWANCE,
          Benefits.CARERS_ALLOWANCE,
          Benefits.JOBSEEKERS_ALLOWANCE,
          Benefits.INCAPACITY_BENEFIT,
          Benefits.STATE_PENSION,
          Benefits.OTHER_TAXABLE_BENEFIT
        )),
        HowMuchBereavementAllowanceId.toString -> JsString("1234"),
        HowMuchCarersAllowanceId.toString -> JsString("1234"),
        HowMuchJobseekersAllowanceId.toString -> JsString("1234"),
        HowMuchIncapacityBenefitId.toString -> JsString("1234"),
        HowMuchStatePensionId.toString -> JsString("1234"),
        OtherBenefitId.toString -> JsString("qwerty"),
        AnyOtherBenefitsId.toString -> JsBoolean(false)
      )
    }

    "removing state pension its associated howMuch" in {
      val result = cascadeUpsert(SelectBenefitsId.toString, Seq(
        Benefits.BEREAVEMENT_ALLOWANCE,
        Benefits.CARERS_ALLOWANCE,
        Benefits.JOBSEEKERS_ALLOWANCE,
        Benefits.INCAPACITY_BENEFIT,
        Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
        Benefits.OTHER_TAXABLE_BENEFIT
      ), allBenefits)
      result.data mustBe Map(
        AnyBenefitsId.toString -> Json.toJson(true),
        SelectBenefitsId.toString -> Json.toJson(Seq(
          Benefits.BEREAVEMENT_ALLOWANCE,
          Benefits.CARERS_ALLOWANCE,
          Benefits.JOBSEEKERS_ALLOWANCE,
          Benefits.INCAPACITY_BENEFIT,
          Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
          Benefits.OTHER_TAXABLE_BENEFIT
        )),
        HowMuchBereavementAllowanceId.toString -> JsString("1234"),
        HowMuchCarersAllowanceId.toString -> JsString("1234"),
        HowMuchJobseekersAllowanceId.toString -> JsString("1234"),
        HowMuchIncapacityBenefitId.toString -> JsString("1234"),
        HowMuchEmploymentAndSupportAllowanceId.toString -> JsString("1234"),
        OtherBenefitId.toString -> JsString("qwerty"),
        AnyOtherBenefitsId.toString -> JsBoolean(false)
      )
    }

    "removing otherTaxable and its associated howMuch" in {
      val result = cascadeUpsert(SelectBenefitsId.toString, Seq(
        Benefits.BEREAVEMENT_ALLOWANCE,
        Benefits.CARERS_ALLOWANCE,
        Benefits.JOBSEEKERS_ALLOWANCE,
        Benefits.INCAPACITY_BENEFIT,
        Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
        Benefits.STATE_PENSION
      ), allBenefits)
      result.data mustBe Map(
        AnyBenefitsId.toString -> Json.toJson(true),
        SelectBenefitsId.toString -> Json.toJson(Seq(
          Benefits.BEREAVEMENT_ALLOWANCE,
          Benefits.CARERS_ALLOWANCE,
          Benefits.JOBSEEKERS_ALLOWANCE,
          Benefits.INCAPACITY_BENEFIT,
          Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
          Benefits.STATE_PENSION
        )),
        HowMuchBereavementAllowanceId.toString -> JsString("1234"),
        HowMuchCarersAllowanceId.toString -> JsString("1234"),
        HowMuchJobseekersAllowanceId.toString -> JsString("1234"),
        HowMuchIncapacityBenefitId.toString -> JsString("1234"),
        HowMuchEmploymentAndSupportAllowanceId.toString -> JsString("1234"),
        HowMuchStatePensionId.toString -> JsString("1234")
      )
    }

    "removing careers and jobseekers remove associated howMuch" in {
      val result = cascadeUpsert(SelectBenefitsId.toString, Seq(
        Benefits.BEREAVEMENT_ALLOWANCE,
        Benefits.INCAPACITY_BENEFIT,
        Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
        Benefits.STATE_PENSION,
        Benefits.OTHER_TAXABLE_BENEFIT
      ), allBenefits)
      result.data mustBe Map(
        AnyBenefitsId.toString -> Json.toJson(true),
        SelectBenefitsId.toString -> Json.toJson(Seq(
          Benefits.BEREAVEMENT_ALLOWANCE,
          Benefits.INCAPACITY_BENEFIT,
          Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
          Benefits.STATE_PENSION,
          Benefits.OTHER_TAXABLE_BENEFIT
        )),
        HowMuchBereavementAllowanceId.toString -> JsString("1234"),
        HowMuchIncapacityBenefitId.toString -> JsString("1234"),
        HowMuchEmploymentAndSupportAllowanceId.toString -> JsString("1234"),
        HowMuchStatePensionId.toString -> JsString("1234"),
        OtherBenefitId.toString -> JsString("qwerty"),
        AnyOtherBenefitsId.toString -> JsBoolean(false)
      )
    }

    "removing careers, jobseekers and other remove associated howMuch" in {
      val result = cascadeUpsert(SelectBenefitsId.toString, Seq(
        Benefits.BEREAVEMENT_ALLOWANCE,
        Benefits.INCAPACITY_BENEFIT,
        Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
        Benefits.STATE_PENSION
      ), allBenefits)
      result.data mustBe Map(
        AnyBenefitsId.toString -> Json.toJson(true),
        SelectBenefitsId.toString -> Json.toJson(Seq(
          Benefits.BEREAVEMENT_ALLOWANCE,
          Benefits.INCAPACITY_BENEFIT,
          Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
          Benefits.STATE_PENSION
        )),
        HowMuchBereavementAllowanceId.toString -> JsString("1234"),
        HowMuchIncapacityBenefitId.toString -> JsString("1234"),
        HowMuchEmploymentAndSupportAllowanceId.toString -> JsString("1234"),
        HowMuchStatePensionId.toString -> JsString("1234")
      )
    }

    "Remove all other benefits" must {
      "when answering 'Yes' remove 'Other taxable benefit' from the selection list" in {
				val originalCacheMap = new CacheMap(id = "test", Map(
					SelectBenefitsId.toString -> Json.toJson(Seq(
						Benefits.BEREAVEMENT_ALLOWANCE,
						Benefits.INCAPACITY_BENEFIT,
						Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
						Benefits.STATE_PENSION,
						Benefits.OTHER_TAXABLE_BENEFIT)
					),
					OtherTaxableIncomeId.toString -> Json.toJson(Seq(OtherTaxableIncome("qwerty","1234"))),
					EmploymentDetailsId.toString -> Json.toJson(false),
					EnterPayeReferenceId.toString -> Json.toJson("123/AB1234"),
					DetailsOfEmploymentOrPensionId.toString -> Json.toJson("some details")
				))

      }

    }
  }
}
