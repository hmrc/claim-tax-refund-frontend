/*
 * Copyright 2019 HM Revenue & Customs
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

package config

import com.google.inject.Inject
import play.api.i18n.{Lang, MessagesApi}
import play.api.libs.json.{JsObject, Json}

class AddressLookupConfig @Inject()(messagesApi: MessagesApi) {



  def config(continueUrl: String)(implicit language: Lang): JsObject = {
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
        "heading" -> messagesApi("addressLookup.confirmPage.title"),
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
        fields = "timeoutAmount" -> 890,
        "timeoutUrl" -> "http://tax.service.gov.uk/claim-tax-refund/sign-out"
      ),
      "ukMode" -> false
    )
  }
}
