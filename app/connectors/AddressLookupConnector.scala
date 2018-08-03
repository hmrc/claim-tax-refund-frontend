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

import javax.inject.Inject
import config.FrontendAppConfig
import models.AddressLookup
import play.api.Logger
import play.api.i18n.MessagesApi
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Request
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

class AddressLookupConnector @Inject()(appConfig: FrontendAppConfig, http: HttpClient, messagesApi: MessagesApi, dataCacheConnector: DataCacheConnector) {

  def initialise(continueUrl: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[String]] = {

    val addressLookupUrl = s"${appConfig.addressLookupUrl}/api/init"
    val addressConfig = Json.toJson(config(continueUrl = s"$continueUrl"))
    http.POST(addressLookupUrl, body = addressConfig).map {
      response =>
        response.status match {
          case 202 =>
            Some(response.header(key = "Location")
              .getOrElse(s"[AddressLookupConnector][initialise] - Failed to obtain location from $addressLookupUrl"))
          case other =>
            Logger.warn(s"[AddressLookupConnector][initialise] - received HTTP status $other from $addressLookupUrl")
            None
        }
    }.recover {
      case _: Exception =>
        Logger.warn(s"[AddressLookupConnector][initialise] - connection to $addressLookupUrl failed")
        None
    }
  }

  def getAddress(cacheId: String, saveKey: String)(implicit hc: HeaderCarrier, ec: ExecutionContext, request: Request[_]) = {
    request.getQueryString(key = "id") match {
      case Some(id) => {
        val getAddressUrl = s"${appConfig.addressLookupUrl}/api/confirmed?id=$id"
        val address: Future[AddressLookup] = for {
          address <-http.GET[AddressLookup](getAddressUrl)
        } yield {
          address
        }

        address.map {
          address =>
            dataCacheConnector.save(cacheId,saveKey,address)
        }
      }
      case None => Future.successful(None)
    }
  }

  def config(continueUrl: String): JsObject = {
    Json.obj(
      fields = "continueUrl" -> s"$continueUrl",
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

}
