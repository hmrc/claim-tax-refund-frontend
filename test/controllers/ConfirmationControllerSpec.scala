/*
 * Copyright 2020 HM Revenue & Customs
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

import connectors.DataCacheConnector
import controllers.actions._
import models.NormalMode
import org.scalatest.concurrent.ScalaFutures
import play.api.test.Helpers._
import views.html.confirmation
import org.mockito.Mockito._
import org.mockito.Matchers._
import play.api.mvc.Call
import uk.gov.hmrc.http.cache.client.CacheMap
import utils.{MockUserAnswers, UserAnswers}

import scala.concurrent.ExecutionContext.Implicits.global

class ConfirmationControllerSpec extends ControllerSpecBase with ScalaFutures{
	val mockDataCacheConnector = mock[DataCacheConnector]
  def onwardRoute: Call = routes.IndexController.onPageLoad()

  def controller(dataRetrievalAction: DataRetrievalAction = someData()) =
    new ConfirmationController(frontendAppConfig, messagesApi, FakeAuthAction(authConnector, frontendAppConfig),
      dataRetrievalAction, new DataRequiredActionImpl(messagesControllerComponents), messagesControllerComponents, mockDataCacheConnector, formPartialRetriever, scalate)

  private val submissionReference = "ABC-1234-DEF"


  def viewAsString: String = confirmation(frontendAppConfig, submissionReference)(fakeRequest, messages, formPartialRetriever, scalate).toString

  "Confirmation Controller" must {

    "return OK and the correct view for a GET" in {
      val mockUserAnswers: UserAnswers = MockUserAnswers.minimalValidUserAnswers()
			when(mockUserAnswers.cacheMap) thenReturn CacheMap(cacheMapId, Map())
			val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)
			status(result) mustBe OK
			contentAsString(result) mustBe viewAsString
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(NormalMode)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)

      whenReady(result) {
        _ =>
          verify(mockDataCacheConnector, times(1)).removeAll(any())
      }
    }
  }
}
