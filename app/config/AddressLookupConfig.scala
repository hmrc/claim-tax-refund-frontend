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

package config

import com.google.inject.Inject
import models.requests.DataRequest
import play.api.i18n.{Lang, MessagesApi}
import play.api.libs.json.{JsObject, Json}
import uk.gov.hmrc.play.language.LanguageUtils

class AddressLookupConfig @Inject()(messagesApi: MessagesApi) {



  def config(continueUrl: String, language: Lang): JsObject = {
      //val language = LanguageUtils.getCurrentLang(request)
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
        fields = "title" -> messagesApi("addressLookup.lookupPage.title")(language),
        "heading" -> messagesApi("addressLookup.lookupPage.heading")(language),
        "filterLabel" -> messagesApi("addressLookup.lookupPage.filterLabel")(language),
        "postcodeLabel" -> messagesApi("addressLookup.lookupPage.postcodeLabel")(language),
        "submitLabel" -> messagesApi("addressLookup.lookupPage.submitLabel")(language),
        "noResultsFoundMessage" -> messagesApi("addressLookup.lookupPage.noResultsFoundMessage")(language),
        "resultLimitExceededMessage" -> messagesApi("addressLookup.lookupPage.resultLimitExceededMessage")(language),
        "manualAddressLinkText" -> messagesApi("addressLookup.lookupPage.manualAddressLinkText")(language)
      ),
      "selectPage" -> Json.obj(
        fields = "title" -> messagesApi("addressLookup.selectPage.title")(language),
        "heading" -> messagesApi("addressLookup.selectPage.heading")(language),
        "proposalListLabel" -> messagesApi("addressLookup.selectPage.proposalListLabel")(language),
        "submitLabel" -> messagesApi("addressLookup.selectPage.submitLabel")(language),
        "proposalListLimit" -> 50,
        "showSearchAgainLink" -> false,
        "searchAgainLinkText" -> messagesApi("addressLookup.selectPage.searchAgainLinkText")(language),
        "editAddressLinkText" -> messagesApi("addressLookup.selectPage.editAddressLinkText")(language)
      ),
      "confirmPage" -> Json.obj(
        fields = "title" -> messagesApi("addressLookup.confirmPage.title")(language),
        "heading" -> messagesApi("addressLookup.confirmPage.heading")(language),
        "infoSubheading" -> messagesApi("addressLookup.confirmPage.infoSubheading")(language),
        "infoMessage" -> messagesApi("addressLookup.confirmPage.infoMessage")(language),
        "submitLabel" -> messagesApi("addressLookup.confirmPage.submitLabel")(language),
        "showSearchAgainLink" -> false,
        "searchAgainLinkText" -> messagesApi("addressLookup.confirmPage.searchAgainLinkText")(language),
        "changeLinkText" -> messagesApi("addressLookup.confirmPage.changeLinkText")(language)
      ),
      "editPage" -> Json.obj(
        fields = "title" -> messagesApi("addressLookup.editPage.title")(language),
        "heading" -> messagesApi("addressLookup.editPage.heading")(language),
        "line1Label" -> messagesApi("addressLookup.editPage.line1Label")(language),
        "line2Label" -> messagesApi("addressLookup.editPage.line2Label")(language),
        "line3Label" -> messagesApi("addressLookup.editPage.line3Label")(language),
        "townLabel" -> messagesApi("addressLookup.editPage.townLabel")(language),
        "postcodeLabel" -> messagesApi("addressLookup.editPage.postcodeLabel")(language),
        "countryLabel" -> messagesApi("addressLookup.editPage.countryLabel")(language),
        "submitLabel" -> messagesApi("addressLookup.editPage.submitLabel")(language),
        "showSearchAgainLink" -> false,
        "searchAgainLinkText" -> messagesApi("addressLookup.editPage.searchAgainLinkText")(language)
      ),
      "timeout" -> Json.obj(
        fields = "timeoutAmount" -> 900,
        "timeoutUrl" -> "http://service/timeout-uri"
      ),
      "ukMode" -> false
    )
  }
}
