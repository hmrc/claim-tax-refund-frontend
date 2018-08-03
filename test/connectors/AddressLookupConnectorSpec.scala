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
import models.AddressLookup
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.Request
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

class AddressLookupConnectorSpec extends SpecBase with MockitoSugar with ScalaFutures {

  implicit val hc: HeaderCarrier = HeaderCarrier()
  implicit val ec: ExecutionContext = mock[ExecutionContext]
  implicit val request: Request[_] = mock[Request[_]]
  implicit val dataCacheConnector = mock[DataCacheConnector]


  "AddressLookupConnector" must {

    "form Json correctly" in {
      val httpMock = mock[HttpClient]
      val connector = new AddressLookupConnector(frontendAppConfig, httpMock, messagesApi, dataCacheConnector)
      val json = connector.config(continueUrl = "api/location")
      json mustBe testIntialiseJson
    }

    "return a location when addressLookup.intialise" in {
      val httpMock = mock[HttpClient]
      val connector = new AddressLookupConnector(frontendAppConfig, httpMock, messagesApi, dataCacheConnector)

      when(httpMock.POST[JsValue, HttpResponse](any(), any(), any())(any(), any(), any(), any()))
        .thenReturn(Future.successful(
          HttpResponse(
            202,
            Some(Json.toJson("")),
            responseHeaders = Map("Location" -> List("api/location"))
          )
        )
        )

      val futureResult = connector.initialise(continueUrl = "")
      whenReady(futureResult) {
        result =>
          result mustBe Some("api/location")
      }
    }

    "return error when there is no Location" in {
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
    }
  }

  "return address when called from telephone controller with ID" in {
    val httpMock = mock[HttpClient]
    when(httpMock.GET[AddressLookup](any())(any(), any(), any()))
      .thenReturn(Future.successful(testReponseAddress.as[AddressLookup])
      )
    when(request.getQueryString(key = "id")).thenReturn(Some("123456789"))
    val connector = new AddressLookupConnector(frontendAppConfig, httpMock, messagesApi, dataCacheConnector)
    val futureResult = connector.getAddress
    val testAddress = testReponseAddress.as[AddressLookup]
    whenReady(futureResult) {
      result =>
        result mustBe Some(testAddress)
    }
  }

  "return none when no ID is in the URL" in {
    val httpMock = mock[HttpClient]
    when(request.getQueryString(key = "id")).thenReturn(None)
    val connector = new AddressLookupConnector(frontendAppConfig, httpMock, messagesApi, dataCacheConnector)
    val futureResult = connector.getAddress
    whenReady(futureResult) {
      result =>
        result mustBe None
    }
  }

  val testIntialiseJson = {
    Json.obj(
      fields = "continueUrl" -> "api/location",
      "homeNavHref" -> "http://www.hmrc.gov.uk/",
      "navTitle" -> messagesApi("index.title"),
      "showPhaseBanner" -> false,
      "alphaPhase" -> false,
      "phaseFeedbackLink" -> "/help/alpha",
      "phaseBannerHtml" -> "This is a new service – your <a href='/help/alpha'>feedback</a> will help us to improve it.",
      "showBackButtons" -> true,
      "includeHMRCBranding" -> true,
      "deskProServiceName" -> "",
      "lookupPage" -> Json.obj(
        fields = "title" -> messagesApi("addressLookup.lookupPage.title"),
        "heading" -> messagesApi("addressLookup.lookupPage.heading"),
        "filterLabel" -> messagesApi("addressLookup.lookupPage.filterLabel"),
        "postcodeLabel" -> messagesApi("addressLookup.lookupPage.postcodeLabel"),
        "submitLabel" -> messagesApi("addressLookup.lookupPage.submitLabel"),
        "noResultsFoundMessage" -> messagesApi("addressLookup.lookupPage.noResultsFoundMessage"),
        "resultLimitExceededMessage" -> messagesApi("addressLookup.lookupPage.resultLimitExceededMessage"),
        "manualAddressLinkText" -> messagesApi("addressLookup.lookupPage.manualAddressLinkText")
      ),
      "selectPage" -> Json.obj(
        fields = "title" -> messagesApi("addressLookup.selectPage.title"),
        "heading" -> messagesApi("addressLookup.selectPage.heading"),
        "proposalListLabel" -> messagesApi("addressLookup.selectPage.proposalListLabel"),
        "submitLabel" -> messagesApi("addressLookup.selectPage.submitLabel"),
        "proposalListLimit" -> 50,
        "showSearchAgainLink" -> false,
        "searchAgainLinkText" -> messagesApi("addressLookup.selectPage.searchAgainLinkText"),
        "editAddressLinkText" -> messagesApi("addressLookup.selectPage.editAddressLinkText")
      ),
      "confirmPage" -> Json.obj(
        fields = "title" -> messagesApi("addressLookup.confirmPage.title"),
        "heading" -> messagesApi("addressLookup.confirmPage.heading"),
        "infoSubheading" -> messagesApi("addressLookup.confirmPage.infoSubheading"),
        "infoMessage" -> messagesApi("addressLookup.confirmPage.infoMessage"),
        "submitLabel" -> messagesApi("addressLookup.confirmPage.submitLabel"),
        "showSearchAgainLink" -> false,
        "searchAgainLinkText" -> messagesApi("addressLookup.confirmPage.searchAgainLinkText"),
        "changeLinkText" -> messagesApi("addressLookup.confirmPage.changeLinkText")
      ),
      "editPage" -> Json.obj(
        fields = "title" -> messagesApi("addressLookup.editPage.title"),
        "heading" -> messagesApi("addressLookup.editPage.heading"),
        "line1Label" -> messagesApi("addressLookup.editPage.line1Label"),
        "line2Label" -> messagesApi("addressLookup.editPage.line2Label"),
        "line3Label" -> messagesApi("addressLookup.editPage.line3Label"),
        "townLabel" -> messagesApi("addressLookup.editPage.townLabel"),
        "postcodeLabel" -> messagesApi("addressLookup.editPage.postcodeLabel"),
        "countryLabel" -> messagesApi("addressLookup.editPage.countryLabel"),
        "submitLabel" -> messagesApi("addressLookup.editPage.submitLabel"),
        "showSearchAgainLink" -> false,
        "searchAgainLinkText" -> messagesApi("addressLookup.editPage.searchAgainLinkText")
      ),
      "timeout" -> Json.obj(
        fields = "timeoutAmount" -> 900,
        "timeoutUrl" -> "http://service/timeout-uri"
      ),
      "ukMode" -> false
    )
  }


  val testReponseAddress = {
    Json.parse(input = "{\n    \"auditRef\": \"e9e2fb3f-268f-4c4c-b928-3dc0b17259f2\",\n    \"address\": {\n        \"lines\": [\n            \"Line1\",\n            \"Line2\",\n            \"Line3\",\n            \"Line4\"\n        ],\n        \"postcode\": \"NE1 1LX\",\n        \"country\": {\n            \"code\": \"GB\",\n            \"name\": \"United Kingdom\"\n        }\n    }\n}")
  }


}