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

import java.util.Locale

import com.google.inject.Inject
import play.api.i18n.{Lang, MessagesApi}
import play.api.libs.json.{JsObject, Json}

class AddressLookupConfig @Inject()(messagesApi: MessagesApi) {

  def config(continueUrl:String)(implicit language: Lang) = {

    val en = Lang("EN")
    val cy = Lang("CY")

    val v2Config = s"""{
                     |  "version": 2,
                     |  "options": {
                     |    "continueUrl": "$continueUrl",
                     |    "homeNavHref": "${messagesApi("index.title")}",
                     |    "phaseFeedbackLink": "/help/alpha",
                     |    "showPhaseBanner": false,
                     |    "alphaPhase": false,
                     |    "showBackButtons": true,
                     |    "includeHMRCBranding": true,
                     |    "ukMode": false,
                     |    "selectPageConfig": {
                     |      "proposalListLimit": 50,
                     |      "showSearchLinkAgain": true
                     |    },
                     |    "confirmPageConfig": {
                     |      "showChangeLink": true,
                     |      "showSubHeadingAndInfo": true,
                     |      "showSearchAgainLink": false,
                     |      "showConfirmChangeText": true
                     |    },
                     |    "timeoutConfig": {
                     |      "timeoutAmount": 890,
                     |      "timeoutUrl": "http://tax.service.gov.uk/claim-tax-refund/sign-out"
                     |    }
                     |},
                     |    "labels": {
                     |      "en": {
                     |        "appLevelLabels": {
                     |          "navTitle": "${messagesApi("index.title")}",
                     |          "phaseBannerHtml": "This is a new service – your <a href='/help/alpha'>feedback</a> will help us to improve it."
                     |        },
                     |        "selectPageLabels": {
                     |          "title": "${messagesApi("addressLookup.selectPage.title")}",
                     |          "heading": "${messagesApi("addressLookup.selectPage.heading")}",
                     |          "headingWithPostcode": "${messagesApi("addressLookup.selectPage.heading")}",
                     |          "proposalListLabel": "${messagesApi("addressLookup.selectPage.proposalListLabel")}",
                     |          "submitLabel": "${messagesApi("addressLookup.selectPage.submitLabel")}",
                     |          "searchAgainLinkText": "${messagesApi("addressLookup.selectPage.searchAgainLinkText")}",
                     |          "editAddressLinkText": "${messagesApi("addressLookup.selectPage.editAddressLinkText")}"
                     |        },
                     |        "lookupPageLabels": {
                     |          "title": "${messagesApi("addressLookup.lookupPage.title")}",
                     |          "heading": "${messagesApi("addressLookup.lookupPage.heading")}",
                     |          "filterLabel": "${messagesApi("addressLookup.lookupPage.filterLabel")}",
                     |          "postcodeLabel": "${messagesApi("addressLookup.lookupPage.postcodeLabel")}",
                     |          "submitLabel": "${messagesApi("addressLookup.lookupPage.submitLabel")}",
                     |          "noResultsFoundMessage": "${messagesApi("addressLookup.lookupPage.noResultsFoundMessage")}",
                     |          "resultLimitExceededMessage": "${messagesApi("addressLookup.lookupPage.resultLimitExceededMessage")}",
                     |          "manualAddressLinkText": "${messagesApi("addressLookup.lookupPage.manualAddressLinkText")}"
                     |        },
                     |        "confirmPageLabels": {
                     |          "title": "${messagesApi("addressLookup.confirmPage.title")}",
                     |          "heading": "${messagesApi("addressLookup.confirmPage.heading")}",
                     |          "infoSubheading": "${ messagesApi("addressLookup.confirmPage.infoSubheading")}",
                     |          "infoMessage": "${messagesApi("addressLookup.confirmPage.infoMessage")}",
                     |          "submitLabel": "${messagesApi("addressLookup.confirmPage.submitLabel")}",
                     |          "searchAgainLinkText": "${messagesApi("addressLookup.confirmPage.searchAgainLinkText")}",
                     |          "changeLinkText": "${messagesApi("addressLookup.confirmPage.changeLinkText")}"
                     |        },
                     |        "editPageLabels": {
                     |          "title": "${messagesApi("addressLookup.editPage.title")}",
                     |          "heading": "${messagesApi("addressLookup.editPage.heading")}",
                     |          "line1Label": "${messagesApi("addressLookup.editPage.line1Label")}",
                     |          "line2Label": "${messagesApi("addressLookup.editPage.line2Label")}",
                     |          "line3Label": "${messagesApi("addressLookup.editPage.line3Label")}",
                     |          "townLabel": "${messagesApi("addressLookup.editPage.townLabel")}",
                     |          "postcodeLabel": "${messagesApi("addressLookup.editPage.postcodeLabel")}",
                     |          "countryLabel": "${messagesApi("addressLookup.editPage.countryLabel")}",
                     |          "submitLabel": "${messagesApi("addressLookup.editPage.submitLabel")}"
                     |        }
                     |      },
                     |      "cy": {
                     |        "appLevelLabels": {
                     |          "navTitle": "${messagesApi("index.title")(cy)}",
                     |          "phaseBannerHtml": ""
                     |        },
                     |        "selectPageLabels": {
                     |          "title": "${messagesApi("addressLookup.selectPage.title")(cy)}",
                     |          "heading": "${messagesApi("addressLookup.selectPage.heading")(cy)}",
                     |          "headingWithPostcode": "${messagesApi("addressLookup.selectPage.heading")(cy)}",
                     |          "proposalListLabel": "${messagesApi("addressLookup.selectPage.proposalListLabel")(cy)}",
                     |          "submitLabel": "${messagesApi("addressLookup.selectPage.submitLabel")(cy)}",
                     |          "searchAgainLinkText": "${messagesApi("addressLookup.selectPage.searchAgainLinkText")(cy)}",
                     |          "editAddressLinkText": "${messagesApi("addressLookup.selectPage.editAddressLinkText")(cy)}"
                     |        },
                     |        "lookupPageLabels": {
                     |          "title": "${messagesApi("addressLookup.lookupPage.title")(cy)}",
                     |          "heading": "${messagesApi("addressLookup.lookupPage.heading")(cy)}",
                     |          "filterLabel": "${messagesApi("addressLookup.lookupPage.filterLabel")(cy)}",
                     |          "postcodeLabel": "${messagesApi("addressLookup.lookupPage.postcodeLabel")(cy)}",
                     |          "submitLabel": "${messagesApi("addressLookup.lookupPage.submitLabel")(cy)}",
                     |          "noResultsFoundMessage": "${messagesApi("addressLookup.lookupPage.noResultsFoundMessage")(cy)}",
                     |          "resultLimitExceededMessage": "${messagesApi("addressLookup.lookupPage.resultLimitExceededMessage")(cy)}",
                     |          "manualAddressLinkText": "${messagesApi("addressLookup.lookupPage.manualAddressLinkText")(cy)}"
                     |        },
                     |        "confirmPageLabels": {
                     |          "title": "${messagesApi("addressLookup.confirmPage.title")(cy)}",
                     |          "heading": "${messagesApi("addressLookup.confirmPage.heading")(cy)}",
                     |          "infoSubheading": "${ messagesApi("addressLookup.confirmPage.infoSubheading")(cy)}",
                     |          "infoMessage": "${messagesApi("addressLookup.confirmPage.infoMessage")(cy)}",
                     |          "submitLabel": "${messagesApi("addressLookup.confirmPage.submitLabel")(cy)}",
                     |          "searchAgainLinkText": "${messagesApi("addressLookup.confirmPage.searchAgainLinkText")(cy)}",
                     |          "changeLinkText": "${messagesApi("addressLookup.confirmPage.changeLinkText")(cy)}"
                     |        },
                     |        "editPageLabels": {
                     |          "title": "${messagesApi("addressLookup.editPage.title")(cy)}",
                     |          "heading": "${messagesApi("addressLookup.editPage.heading")(cy)}",
                     |          "line1Label": "${messagesApi("addressLookup.editPage.line1Label")(cy)}",
                     |          "line2Label": "${messagesApi("addressLookup.editPage.line2Label")(cy)}",
                     |          "line3Label": "${messagesApi("addressLookup.editPage.line3Label")(cy)}",
                     |          "townLabel": "${messagesApi("addressLookup.editPage.townLabel")(cy)}",
                     |          "postcodeLabel": "${messagesApi("addressLookup.editPage.postcodeLabel")(cy)}",
                     |          "countryLabel": "${messagesApi("addressLookup.editPage.countryLabel")(cy)}",
                     |          "submitLabel": "${messagesApi("addressLookup.editPage.submitLabel")(cy)}"
                     |        }
                     |      }
                     |    }
                     |  }""".stripMargin

    Json.parse(v2Config).as[JsObject]
  }

}
