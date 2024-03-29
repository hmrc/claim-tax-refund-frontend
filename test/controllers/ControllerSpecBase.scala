/*
 * Copyright 2023 HM Revenue & Customs
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
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.auth.core.retrieve.ItmpName
import models.CacheMap
import utils.{MockUserAnswers, UserAnswers}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

trait ControllerSpecBase extends SpecBase {

  val cacheMapId = "id"

  def emptyCacheMap = CacheMap(cacheMapId, Map())

  def getEmptyCacheMap = new FakeDataRetrievalAction(Some(emptyCacheMap))

  def dontGetAnyData = new FakeDataRetrievalAction(None)

  def someData(year: Int = 2017) = new FakeDataRetrievalAction(
    Some(CacheMap(cacheMapId, Map(SelectTaxYearId.toString -> Json.toJson(SelectTaxYear.CustomTaxYear(year): SelectTaxYear))))
  )

  implicit lazy val cc: MessagesControllerComponents = messagesControllerComponents

  def fakeDataRetrievalAction(mockUserAnswers: UserAnswers = MockUserAnswers.minimalValidUserAnswers()): DataRetrievalAction =
    new DataRetrievalAction {
      override protected def transform[A](request: AuthenticatedRequest[A]): Future[OptionalDataRequest[A]] = {
        Future.successful(OptionalDataRequest(
          request = request,
          externalId = "123123",
          nino = "AB123456A",
          name = Some(ItmpName(Some("sdadsad"), None, None)),
          address = Some(itmpAddress),
          userAnswers = Some(mockUserAnswers)
        ))
      }

      override protected def executionContext: ExecutionContext = implicitly[ExecutionContext]
    }
}
