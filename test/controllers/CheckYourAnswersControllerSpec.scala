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

import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, get, urlEqualTo}
import connectors.{AddressLookupConnector, DataCacheConnector, FakeDataCacheConnector}
import controllers.actions.{DataRequiredActionImpl, DataRetrievalAction, FakeAuthAction}
import models.{SubmissionFailed, SubmissionSuccessful}
import org.scalatest.mockito.MockitoSugar
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import services.SubmissionService
import uk.gov.hmrc.http.HeaderCarrier
import utils.{UserAnswers, WireMockHelper}

import scala.concurrent.{ExecutionContext, Future}

class CheckYourAnswersControllerSpec extends ControllerSpecBase with MockitoSugar with WireMockHelper{
  implicit val ec: ExecutionContext = mock[ExecutionContext]
  implicit val dataCacheConnector: DataCacheConnector = mock[DataCacheConnector]


  override implicit lazy val app: Application =
    new GuiceApplicationBuilder()
      .configure(
        conf = "microservice.services.address-lookup-frontend.port" -> server.port
      )
      .build()

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap,
                 addressLookupConnector: AddressLookupConnector, submissionService: SubmissionService = FakeSuccessfulSubmissionService) =
    new CheckYourAnswersController(frontendAppConfig, messagesApi, FakeDataCacheConnector, FakeAuthAction, dataRetrievalAction,
       new DataRequiredActionImpl, submissionService, formPartialRetriever, templateRenderer)

  private lazy implicit val addressLookupConnector: AddressLookupConnector = app.injector.instanceOf[AddressLookupConnector]


  "Check Your Answers Controller" must {

    "return 200 and the correct view for a GET" in {
      val result = controller(someData, addressLookupConnector).onPageLoad()(fakeRequest)
      status(result) mustBe OK
    }

    "redirect to Session Expired for a GET if not existing data is found" in {
      val result = controller(dontGetAnyData, addressLookupConnector).onPageLoad()(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "build when new answers are present in the url" in {
      server.stubFor(
        get(urlEqualTo("/api/confirmed?id=123456789"))
          .willReturn(
            aResponse()
              .withStatus(200)
              .withBody(testResponseAddress.toString)
          )
      )
      val result = controller(someData, addressLookupConnector).onPageLoad(Some("123456789"))(fakeRequest)
      result.map {
        res =>
          res mustBe OK
      }
      status(result) mustBe OK
    }

    "Redirect to Confimration page on a POST when submission is successful" in {
      val result = controller(someData, addressLookupConnector, FakeSuccessfulSubmissionService).onSubmit()(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "Redirect to Failed to submit on a POST when submission fails" in {
      val result = controller(someData, addressLookupConnector, FakeFailingSubmissionService).onSubmit()(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }
  }
}

object FakeSuccessfulSubmissionService extends SubmissionService {
  override def ctrSubmission(answers: UserAnswers)(implicit hc: HeaderCarrier) : Future[SubmissionSuccessful.type] = Future.successful(SubmissionSuccessful)
}

object FakeFailingSubmissionService extends SubmissionService {
  override def ctrSubmission(answers: UserAnswers)(implicit hc: HeaderCarrier) : Future[SubmissionFailed.type] = Future.successful(SubmissionFailed)
}
