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

package connectors

import com.github.tomakehurst.wiremock.client.WireMock._
import base.SpecBase
import config.FrontendAppConfig
import models.requests.DataRequest
import uk.gov.hmrc.http.HeaderCarrier
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar

import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Request
import utils.WireMockHelper
import scala.concurrent._

import scala.concurrent.ExecutionContext

class AddressLookupConnectorSpec extends SpecBase with MockitoSugar with WireMockHelper with GuiceOneAppPerSuite with ScalaFutures {

  override implicit lazy val app: Application =
    new GuiceApplicationBuilder()
      .configure(
        conf = "microservice.services.address-lookup-frontend.port" -> server.port
      )
      .build()

  implicit val hc: HeaderCarrier = HeaderCarrier()
  implicit val ec: ExecutionContext = mock[ExecutionContext]
  implicit val request: Request[_] = mock[Request[_]]
  implicit val dataCacheConnector = mock[DataCacheConnector]
  implicit val dataRequest = mock[DataRequest[_]]
  implicit val appConfig = mock[FrontendAppConfig]


  private lazy val connector: AddressLookupConnector = app.injector.instanceOf[AddressLookupConnector]

  "AddressLookupConnector" must {

    "return a location when addressLookup.intialise" in {
      val addressLookupUrl = "/api/init"
      server.stubFor(
        post(urlEqualTo(addressLookupUrl))
          .willReturn(
            aResponse()
              .withStatus(202)
              .withHeader("Location", "/api/location")
          )
      )

      /*val result = Await.result(connector.initialise(continueUrl = ""), 1.second)
      result mustBe Some("/api/location")*/

      whenReady(connector.initialise(continueUrl = "")){
        result =>
          result mustBe Some("/api/location")
      }
    }

  /*  "return error when there is no Location" in {
      val httpMock = mock[HttpClient]
      val connector = new AddressLookupConnector(frontendAppConfig, httpMock, messagesApi, dataCacheConnector)

      when(httpMock.POST[JsValue, HttpResponse](any(), any(), any())(any(), any(), any(), any()))
        .thenReturn(Future.successful(
          HttpResponse(
            202,
            Some(Json.toJson("")),
            responseHeaders = Map("" -> List(""))
          )
        )
        )

      val futureResult = connector.initialise(continueUrl = "")
      whenReady(futureResult) {
        result =>
          result mustBe Some("[AddressLookupConnector][initialise] - Failed to obtain location from http://localhost:9028/api/init")
      }
    }

    "get None when there is an error" in {
      val httpMock = mock[HttpClient]
      val connector = new AddressLookupConnector(frontendAppConfig, httpMock, messagesApi, dataCacheConnector)


      when(httpMock.POST[JsValue, HttpResponse](any(), any(), any())(any(), any(), any(), any()))
        .thenReturn(Future.failed(new Exception())
        )


      val futureResult = connector.initialise(continueUrl = "")
      whenReady(futureResult) {
        result =>
          result mustBe None
      }
    }

    "return None when HTTP call fails" in {
      val httpMock = mock[HttpClient]
      val connector = new AddressLookupConnector(frontendAppConfig, httpMock, messagesApi, dataCacheConnector)
      when(httpMock.POST[JsValue, HttpResponse](any(), any(), any())(any(), any(), any(), any()))
        .thenReturn(Future.successful(HttpResponse(400))
        )

      val futureResult = connector.initialise(continueUrl = "")
      whenReady(futureResult) {
        result =>
          result mustBe None
      }
    }*/
  }

/*
  "return cacheMap when called with ID" in {
/*
    val testAddress = testReponseAddress.as[AddressLookup]

    val getAddressUrl = s"${appConfig.addressLookupUrl}/api/confirmed?id=123456789"

    when(httpMock.GET[AddressLookup](Matchers.eq(getAddressUrl))(Matchers.any(), Matchers.any(), Matchers.any()))
      .thenReturn(Future.successful(testAddress))*/

    val httpMock = mock[HttpClient]
    val connector = new AddressLookupConnector(appConfig,httpMock, messagesApi, dataCacheConnector)
    val futureResult: Future[UserAnswers] = connector.getAddress(cacheId = "12345", saveKey = "saveKey", id = "123456789")

    whenReady(futureResult){
      result =>
        result mustBe ""
    }
  }

  "return none when no ID is in the URL" in {
    val httpMock = mock[HttpClient]
    when(request.getQueryString(key = "id")).thenReturn(None)
    val connector = new AddressLookupConnector(frontendAppConfig, httpMock, messagesApi, dataCacheConnector)
    val futureResult = connector.getAddress(cacheId = "", saveKey = "", id = "")
    whenReady(futureResult) {
      result =>
        result mustBe None
    }
  }
*/
  /*
  val testReponseAddress = {
    Json.parse(input = "{\n    \"auditRef\": \"e9e2fb3f-268f-4c4c-b928-3dc0b17259f2\",\n    \"address\": {\n        \"lines\": [\n            \"Line1\",\n            \"Line2\",\n            \"Line3\",\n            \"Line4\"\n        ],\n        \"postcode\": \"NE1 1LX\",\n        \"country\": {\n            \"code\": \"GB\",\n            \"name\": \"United Kingdom\"\n        }\n    }\n}")
  }
*/
}

