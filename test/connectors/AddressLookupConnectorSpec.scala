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

import base.SpecBase
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.http.Fault
import config.FrontendAppConfig
import models.requests.DataRequest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsString, JsValue, Json}
import play.api.mvc.Request
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.cache.client.CacheMap
import utils.{MockUserAnswers, UserAnswers, WireMockHelper}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

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
  implicit val dataCacheConnector: DataCacheConnector = mock[DataCacheConnector]
  implicit val dataRequest: DataRequest[_] = mock[DataRequest[_]]
  implicit val appConfig: FrontendAppConfig = mock[FrontendAppConfig]

  private val mockUserAnswers = MockUserAnswers.claimDetailsUserAnswers
  private lazy val connector: AddressLookupConnector = app.injector.instanceOf[AddressLookupConnector]

  "AddressLookupConnector" must {

    "return a location when addressLookup.intialise" in {
      server.stubFor(
        post(urlEqualTo("/api/init"))
          .willReturn(
            aResponse()
              .withStatus(202)
              .withHeader("Location", "/api/location")
          )
      )

      val result: Option[String] = Await.result(connector.initialise(continueUrl = ""), 200.millisecond)
      result mustBe Some("/api/location")

    }

    "return error when there is no Location" in {
      server.stubFor(
        post(urlEqualTo("/api/init"))
          .willReturn(
            aResponse()
              .withStatus(202)
              .withBody("")
              .withHeader("", "")
          )
      )

      val result: Option[String] = Await.result(connector.initialise(""), 200.millisecond)
      result mustBe Some(s"[AddressLookupConnector][initialise] - Failed to obtain location from http://localhost:${server.port}/api/init")
    }

    "get None when there is an error" in {
      server.stubFor(
        post(urlEqualTo("/api/init"))
          .willReturn(
            aResponse().withFault(Fault.EMPTY_RESPONSE)
          )
      )

      val result: Option[String] = Await.result(connector.initialise(""), 200.millisecond)
      result mustBe None

    }

    "return None when HTTP call fails" in {
      server.stubFor(
        post(urlEqualTo("/api/init"))
          .willReturn(
            aResponse()
              .withStatus(400)
          )
      )

      val result: Option[String] = Await.result(connector.initialise(""), 200.millisecond)
      result mustBe None
    }
  }

  "return cacheMap when called with ID" in {
    val testAddress: String = testResponseAddress.toString

    server.stubFor(
      get(urlEqualTo("/api/confirmed?id=123456789"))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withBody(testResponseAddress.toString)
        )
    )

    val result: UserAnswers = Await.result(connector.getAddress(cacheId = "12345", saveKey = "saveKey", id = "123456789"), 2.second)
    result.cacheMap mustBe CacheMap("12345", Map("saveKey" -> testResponseAddress))
  }

  "return none when no ID is in the URL" in {
    val testAddress: String = testResponseAddress.toString

    server.stubFor(
      get(urlEqualTo("/api/confirmed?id="))
        .willReturn(aResponse()
          .withStatus(404)
          .withBody("")
        )
    )

    val result: UserAnswers = Await.result(connector.getAddress(cacheId = "12345", saveKey = "saveKey", id = ""), 5.second)
    result.cacheMap.getEntry[JsValue]("saveKey") mustBe ""
  }

//  "return none when no ID is in the URL" in {
//    val httpMock = mock[HttpClient]
//    when(request.getQueryString(key = "id")).thenReturn(None)
//    val connector = new AddressLookupConnector(frontendAppConfig, httpMock, messagesApi, dataCacheConnector)
//    val futureResult = connector.getAddress(cacheId = "", saveKey = "", id = "")
//    whenReady(futureResult) {
//      result =>
//        result mustBe None
//    }
//  }

  val testResponseAddress: JsValue = {
    Json.parse(input = "{\n    \"auditRef\": \"e9e2fb3f-268f-4c4c-b928-3dc0b17259f2\",\n    \"address\": {\n        \"lines\": [\n            \"Line1\",\n            \"Line2\",\n            \"Line3\",\n            \"Line4\"\n        ],\n        \"postcode\": \"NE1 1LX\",\n        \"country\": {\n            \"code\": \"GB\",\n            \"name\": \"United Kingdom\"\n        }\n    }\n}")
  }

}

