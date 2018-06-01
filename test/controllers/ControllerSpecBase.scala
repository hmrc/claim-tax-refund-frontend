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

package controllers


import base.SpecBase
import controllers.actions.{DataRetrievalAction, FakeDataRetrievalAction}
import identifiers.SelectTaxYearId
import models.SelectTaxYear
import models.requests.{AuthenticatedRequest, OptionalDataRequest}
import play.api.libs.json.Json
import uk.gov.hmrc.auth.core.retrieve.{ItmpAddress, ItmpName}
import uk.gov.hmrc.http.cache.client.CacheMap
import utils.{MockUserAnswers, UserAnswers}

import scala.concurrent.Future

trait ControllerSpecBase extends SpecBase {

  val cacheMapId = "id"

  def emptyCacheMap = CacheMap(cacheMapId, Map())

  def getEmptyCacheMap = new FakeDataRetrievalAction(Some(emptyCacheMap))

  def dontGetAnyData = new FakeDataRetrievalAction(None)

  def someData = new FakeDataRetrievalAction(
    Some(CacheMap(cacheMapId, Map(SelectTaxYearId.toString -> Json.toJson(SelectTaxYear.CYMinus2)))))

  def fakeDataRetrievalAction(mockUserAnswers: UserAnswers = MockUserAnswers.yourDetailsUserAnswers) = new DataRetrievalAction {
    override protected def transform[A](request: AuthenticatedRequest[A]): Future[OptionalDataRequest[A]] = {
      Future.successful(OptionalDataRequest(request, "123123", ItmpName(Some("sdadsad"), Some("sdfasfad"), Some("adfsdfa")), "AB123456A",
        ItmpAddress(None, None, None, None, None, None, None, None),
        Some(mockUserAnswers)))
    }
  }
}
