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

package models.templates

import play.api.libs.json.{Json, Writes}

case class AddressLookup (continueUrl: String)

object AddressLookup{
  implicit val writes:Writes[AddressLookup] =
    Writes {
      addressLookup =>
        Json.obj(
          //{
          "continueUrl" -> addressLookup.continueUrl,
          "homeNavHref" -> "http://www.hmrc.gov.uk/",
          "navTitle" -> "Address Lookup",
          "showPhaseBanner" -> false,
          "alphaPhase" -> false,
          "phaseFeedbackLink" -> "/help/alpha",
          "phaseBannerHtml" -> "This is a new service – your <a href='/help/alpha'>feedback</a> will help us to improve it.",
          "showBackButtons"-> true,
          "includeHMRCBranding" -> true,
          "deskProServiceName" -> "",
          "lookupPage" -> Json.obj (
            "title" -> "Find the address",
            "heading" -> "Find the address",
            "filterLabel" -> "Property name or number",
            "postcodeLabel" -> "UK Postcode",
            "submitLabel" -> "Find das address",
            "noResultsFoundMessage" -> "Sorry, we couldn't find anything for that postcode.",
            "resultLimitExceededMessage" -> "There were too many results. Please add additional details to limit the number of results.",
            "manualAddressLinkText" -> "The address doesn't have a UK postcode"
          ),
          "selectPage" -> Json.obj (
            "title" -> "Choose the address",
            "heading" -> "Choose the address",
            "proposalListLabel" -> "Please select one of the following addresses",
            "submitLabel" -> "Continue",
            "proposalListLimit" -> 50,
            "showSearchAgainLink" -> false,
            "searchAgainLinkText" -> "Search again",
            "editAddressLinkText" -> "Enter address manually"
          ),
          "confirmPage" -> Json.obj (
            "title" -> "Confirm the address",
            "heading" -> "Review and confirm",
            "infoSubheading" -> "Your selected address",
            "infoMessage" -> "This is how your address will look. Please double-check it and, if accurate, click on the <kbd>Confirm</kbd> button.",
            "submitLabel" -> "Confirm and continue",
            "showSearchAgainLink" -> false,
            "searchAgainLinkText" -> "Search again",
            "changeLinkText" -> "Edit address"
          ),
          "editPage" -> Json.obj (
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
          "timeout" -> Json.obj (
            "timeoutAmount" -> 900,
            "timeoutUrl" -> "http://service/timeout-uri"
          ),
          "ukMode"-> false
        )
    }
}
