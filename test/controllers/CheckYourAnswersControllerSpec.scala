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

import connectors.{DataCacheConnector, FakeDataCacheConnector}
import controllers.actions.{DataRequiredActionImpl, DataRetrievalAction, FakeAuthAction}
import models.{SubmissionFailed, SubmissionSuccessful}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import play.api.test.Helpers._
import services.SubmissionService
import utils.WireMockHelper

import scala.concurrent.{ExecutionContext, Future}

class CheckYourAnswersControllerSpec extends ControllerSpecBase with WireMockHelper with ScalaFutures{
  implicit val ec: ExecutionContext = mock[ExecutionContext]
  implicit val dataCacheConnector: DataCacheConnector = mock[DataCacheConnector]

  private val mockSubmissionService: SubmissionService = mock[SubmissionService]

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap,
                 submissionService: SubmissionService = mockSubmissionService) =
    new CheckYourAnswersController(
      frontendAppConfig, messagesApi,
      FakeDataCacheConnector, FakeAuthAction,
      dataRetrievalAction,
      new DataRequiredActionImpl,
      submissionService,
      formPartialRetriever,
      templateRenderer
    )

  "Check Your Answers Controller" must {

    "return 200 and the correct view for a GET" in {
      val result = controller(someData).onPageLoad()(fakeRequest)
      status(result) mustBe OK
    }

    "redirect to Session Expired for a GET if not existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad()(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "return RuntimeException" in {
      when(dataCacheConnector.save(any(), any(), any())(any())) thenReturn Future.failed(new RuntimeException)
      val result = controller(someData).onSubmit()(fakeRequest)

      whenReady(result.failed) {
        result =>
          result mustBe a[RuntimeException]
      }
    }

    "Redirect to Confirmation page on a POST when submission is successful" in {
      when(mockSubmissionService.ctrSubmission(any())(any())) thenReturn Future.successful(SubmissionSuccessful)
      val result = controller(someData).onSubmit()(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.ConfirmationController.onPageLoad().url)
    }

    "Redirect to Failed to submit on a POST when submission fails" in {
      when(mockSubmissionService.ctrSubmission(any())(any())) thenReturn Future.successful(SubmissionFailed)
      val result = controller(someData).onSubmit()(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }


  }
}
