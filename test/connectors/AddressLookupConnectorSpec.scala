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
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

class AddressLookupConnectorSpec extends SpecBase with MockitoSugar with ScalaFutures{

  implicit val hc: HeaderCarrier = HeaderCarrier()
  implicit val ec: ExecutionContext = mock[ExecutionContext]



  "AddressLookupConnector" must {

    "Json must form correctly" in {
      val httpMock = mock[HttpClient]
      val connector = new AddressLookupConnector(frontendAppConfig, httpMock)
      val json = connector.config("api/location")
      json mustBe testIntialiseJson
    }

    "return a location when addressLookup.intialise" in {
      val httpMock = mock[HttpClient]
      val connector = new AddressLookupConnector(frontendAppConfig, httpMock)

      when(httpMock.POST[JsValue, HttpResponse](any(), any(), any())(any(), any(), any(), any()))
        .thenReturn(Future.successful(
          HttpResponse(
            202,
            Some(Json.toJson("")),
            responseHeaders = Map("Location" -> List("api/location"))
            )
          )
        )

      val futureResult = connector.initialise
      whenReady(futureResult) {
        result =>
          result mustBe Some("api/location")
      }
    }

    "return None when HTTP call fails" in {
      val httpMock = mock[HttpClient]
      val connector = new AddressLookupConnector(frontendAppConfig, httpMock)
      when(httpMock.POST[JsValue, HttpResponse](any(), any(), any())(any(), any(), any(), any()))
        .thenReturn(Future.successful(HttpResponse(400))
      )

      val futureResult = connector.initialise
      whenReady(futureResult) {
        result =>
          result mustBe None
      }
    }
  }

  "return address when passing an ID" in {
    val httpMock = mock[HttpClient]

    when(httpMock.GET[AddressLookup](any())(any(), any(), any()))
      .thenReturn (Future.successful(testReponseAddress.as[AddressLookup])
    )
    val connector = new AddressLookupConnector(frontendAppConfig, httpMock)
    val id = "2ba429b4-7f4b-4105-a9ac-808e04c6ac3c"
    val futureResult = connector.getAddress(id)
    val testAddress = testReponseAddress.as[AddressLookup]
    whenReady(futureResult){
      result =>
        result mustBe testAddress
    }

  }

  val testIntialiseJson = {
    Json.obj(
    "continueUrl" -> s"api/location",
    "homeNavHref" -> "http://www.hmrc.gov.uk/",
    "navTitle" -> "Address Lookup",
    "showPhaseBanner" -> false,
    "alphaPhase" -> false,
    "phaseFeedbackLink" -> "/help/alpha",
    "phaseBannerHtml" -> "This is a new service – your <a href='/help/alpha'>feedback</a> will help us to improve it.",
    "showBackButtons" -> true,
    "includeHMRCBranding" -> true,
    "deskProServiceName" -> "",
    "lookupPage" -> Json.obj(
      "title" -> "Find the address",
      "heading" -> "Find the address",
      "filterLabel" -> "Property name or number",
      "postcodeLabel" -> "UK Postcode",
      "submitLabel" -> "Find das address",
      "noResultsFoundMessage" -> "Sorry, we couldn't find anything for that postcode.",
      "resultLimitExceededMessage" -> "There were too many results. Please add additional details to limit the number of results.",
      "manualAddressLinkText" -> "The address doesn't have a UK postcode"
    ),
    "selectPage" -> Json.obj(
      "title" -> "Choose the address",
      "heading" -> "Choose the address",
      "proposalListLabel" -> "Please select one of the following addresses",
      "submitLabel" -> "Continue",
      "proposalListLimit" -> 50,
      "showSearchAgainLink" -> false,
      "searchAgainLinkText" -> "Search again",
      "editAddressLinkText" -> "Enter address manually"
    ),
    "confirmPage" -> Json.obj(
      "title" -> "Confirm the address",
      "heading" -> "Review and confirm",
      "infoSubheading" -> "Your selected address",
      "infoMessage" -> "This is how your address will look. Please double-check it and, if accurate, click on the <kbd>Confirm</kbd> button.",
      "submitLabel" -> "Confirm and continue",
      "showSearchAgainLink" -> false,
      "searchAgainLinkText" -> "Search again",
      "changeLinkText" -> "Edit address"
    ),
    "editPage" -> Json.obj(
      "title" -> "Enter the address",
      "heading" -> "Enter the address",
      "line1Label" -> "Address line 1",
      "line2Label" -> "Address line 2 (optional)",
      "line3Label" -> "Address line 3 (optional)",
      "townLabel" -> "Town/City",
      "postcodeLabel" -> "Postal code (optional)",
      "countryLabel" -> "Country",
      "submitLabel" -> "Next",
      "showSearchAgainLink" -> false,
      "searchAgainLinkText" -> "Search again"
    ),
    "timeout" -> Json.obj(
      "timeoutAmount" -> 900,
      "timeoutUrl" -> "http://service/timeout-uri"
    ),
    "ukMode" -> false
  )
}


  val testReponseAddress = {
    Json.parse("{\n    \"auditRef\": \"e9e2fb3f-268f-4c4c-b928-3dc0b17259f2\",\n    \"address\": {\n        \"lines\": [\n            \"Line1\",\n            \"Line2\",\n            \"Line3\",\n            \"Line4\"\n        ],\n        \"postcode\": \"NE1 1LX\",\n        \"country\": {\n            \"code\": \"GB\",\n            \"name\": \"United Kingdom\"\n        }\n    }\n}")
  }


}