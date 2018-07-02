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

import javax.inject.Singleton
import identifiers._
import models.{Benefits, CompanyBenefits}
import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap

@Singleton
class CascadeUpsert {

  val funcMap: Map[String, (JsValue, CacheMap) => CacheMap] =
    Map(
      SelectCompanyBenefitsId.toString -> storeCompanyBenefit,
      SelectBenefitsId.toString -> storeBenefit,
      AnyCompanyBenefitsId.toString -> anyCompanyBenefits
    )

  def apply[A](key: String, value: A, originalCacheMap: CacheMap)(implicit fmt: Format[A]): CacheMap =
    funcMap.get(key).fold(store(key, value, originalCacheMap)) { fn => fn(Json.toJson(value), originalCacheMap) }

  def addRepeatedValue[A](key: String, value: A, originalCacheMap: CacheMap)(implicit fmt: Format[A]): CacheMap = {
    val values = originalCacheMap.getEntry[Seq[A]](key).getOrElse(Seq()) :+ value
    originalCacheMap copy (data = originalCacheMap.data + (key -> Json.toJson(values)))
  }

  private def store[A](key: String, value: A, cacheMap: CacheMap)(implicit fmt: Format[A]) =
    cacheMap copy (data = cacheMap.data + (key -> Json.toJson(value)))

  private def clearIfFalse[A](key: String, value: A, keysToRemove: Seq[String], cacheMap: CacheMap)(implicit fmt: Format[A]): CacheMap = {
    val mapToStore = value match {
      case JsBoolean(false) => cacheMap copy (data = cacheMap.data.filterKeys(s => !keysToRemove.contains(s)))
      case _ => cacheMap
    }
    store(key, value, mapToStore)
  }

  private def anyCompanyBenefits(value: JsValue, cacheMap: CacheMap): CacheMap =
    if (value.as[Boolean]) {
      store(AnyCompanyBenefitsId.toString, value, cacheMap)
    } else {
      store(AnyCompanyBenefitsId.toString, value, cacheMap.copy(data = cacheMap.data - (
        SelectCompanyBenefitsId.toString,
        HowMuchCarBenefitsId.toString,
        HowMuchFuelBenefitId.toString,
        HowMuchMedicalBenefitsId.toString,
        OtherCompanyBenefitsNameId.toString,
        HowMuchOtherCompanyBenefitId.toString,
        AnyOtherCompanyBenefitsId.toString
      )))
    }

  private def storeCompanyBenefit(selectedBenefits: JsValue, cacheMap: CacheMap): CacheMap = {

    val mapToStore = cacheMap.data.get(SelectCompanyBenefitsId.toString).map {
      _.as[JsArray].value.foldLeft(cacheMap) {
        (cm, benefit) =>
          if (!selectedBenefits.as[JsArray].value.contains(benefit) && benefit != JsString(CompanyBenefits.OTHER_COMPANY_BENEFIT.toString)) {
            cm copy (data = cacheMap.data - CompanyBenefits.getIdString(benefit.as[String]))
          } else if (!selectedBenefits.as[JsArray].value.contains(JsString(CompanyBenefits.OTHER_COMPANY_BENEFIT.toString))) {
            cm copy (data = cacheMap.data - (OtherCompanyBenefitsNameId.toString, HowMuchOtherCompanyBenefitId.toString, AnyOtherCompanyBenefitsId.toString))
          } else {
            cm
          }
      }
    }.getOrElse(cacheMap)

    store(SelectCompanyBenefitsId.toString, selectedBenefits, mapToStore)
  }

  private def storeBenefit(selectedBenefits: JsValue, cacheMap: CacheMap): CacheMap = {

    val mapToStore = cacheMap.data.get(SelectBenefitsId.toString).map {
      _.as[JsArray].value.foldLeft(cacheMap) {
        (cm, benefit) =>
          if (!selectedBenefits.as[JsArray].value.contains(benefit) && benefit != JsString(Benefits.OTHER_TAXABLE_BENEFIT.toString)) {
            cm copy (data = cacheMap.data - Benefits.getIdString(benefit.as[String]))
          } else if (!selectedBenefits.as[JsArray].value.contains(JsString(Benefits.OTHER_TAXABLE_BENEFIT.toString))) {
            cm copy (data = cacheMap.data - (OtherTaxableBenefitsNameId.toString, HowMuchOtherTaxableBenefitId.toString, AnyOtherTaxableBenefitsId.toString))
          } else {
            cm
          }
      }
    }.getOrElse(cacheMap)

    store(SelectBenefitsId.toString, selectedBenefits, mapToStore)
  }
}
