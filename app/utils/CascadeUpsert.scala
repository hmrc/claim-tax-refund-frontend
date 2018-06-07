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
import models.CompanyBenefits
import play.api.libs.json._
import uk.gov.hmrc.http.cache.client.CacheMap

@Singleton
class CascadeUpsert {

  val funcMap: Map[String, (JsValue, CacheMap) => CacheMap] =
    Map(
      SelectCompanyBenefitsId.toString -> storeCompanyBenefit
    )

  def apply[A](key: String, value: A, originalCacheMap: CacheMap)(implicit fmt: Format[A]): CacheMap =
    funcMap.get(key).fold(store(key, value, originalCacheMap)) { fn => fn(Json.toJson(value), originalCacheMap) }

  def addRepeatedValue[A](key: String, value: A, originalCacheMap: CacheMap)(implicit fmt: Format[A]): CacheMap = {
    val values = originalCacheMap.getEntry[Seq[A]](key).getOrElse(Seq()) :+ value
    originalCacheMap copy (data = originalCacheMap.data + (key -> Json.toJson(values)))
  }

  private def store[A](key: String, value: A, cacheMap: CacheMap)(implicit fmt: Format[A]) =
    cacheMap copy (data = cacheMap.data + (key -> Json.toJson(value)))

  private def clearIfFalse[A](key: String, value: A, keysToRemove: Set[String], cacheMap: CacheMap)(implicit fmt: Format[A]): CacheMap = {
    val mapToStore = value match {
      case JsBoolean(false) => cacheMap copy (data = cacheMap.data.filterKeys(s => !keysToRemove.contains(s)))
      case _ => cacheMap
    }
    store(key, value, mapToStore)
  }

  private def storeCompanyBenefit(value: JsValue, cacheMap: CacheMap): CacheMap = {
    cacheMap.data.get(SelectCompanyBenefitsId.toString) match {
      case Some(benefits) if benefits != value =>
        var mapToStore = cacheMap
        benefits.as[JsArray].value.foreach {
          x=> if(!value.as[JsArray].value.contains(x)){
            mapToStore = cacheMap copy (data = cacheMap.data - CompanyBenefits.getIdString(x.as[String]).toString)
            store(SelectCompanyBenefitsId.toString, value, mapToStore)
          }
        }
        store(SelectCompanyBenefitsId.toString, value, mapToStore)
      case _ =>
        store(SelectCompanyBenefitsId.toString, value, cacheMap)
    }

  }
}
