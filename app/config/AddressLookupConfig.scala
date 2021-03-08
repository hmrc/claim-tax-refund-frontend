/*
 * Copyright 2021 HM Revenue & Customs
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

  def config(continueUrl:String, accessibilityFooterUrl: String)(implicit language: Lang) = {

    val cy = Lang("CY")
    val v2Config = s"""{
                      |  "version": 2,
                      |  "options": {
                      |    "continueUrl": "$continueUrl",
                      |    "accessibilityFooterUrl": "$accessibilityFooterUrl",
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
                      |          "navTitle": "${messagesApi("index.title")}"
                      |        },
                      |        "lookupPageLabels": {
                      |          "title": "${messagesApi("addressLookup.lookupPage.title")}",
                      |          "heading": "${messagesApi("addressLookup.lookupPage.heading")}"
                      |        },
                      |        "editPageLabels": {
                      |          "title": "${messagesApi("addressLookup.editPage.title")}",
                      |          "heading": "${messagesApi("addressLookup.editPage.heading")}"
                      |        }
                      |      },
                      |      "cy": {
                      |        "appLevelLabels": {
                      |          "navTitle": "${messagesApi("index.title")(cy)}"
                      |        },
                      |        "lookupPageLabels": {
                      |          "title": "${messagesApi("addressLookup.lookupPage.title")(cy)}",
                      |          "heading": "${messagesApi("addressLookup.lookupPage.heading")(cy)}"
                      |        },
                      |        "editPageLabels": {
                      |          "title": "${messagesApi("addressLookup.editPage.title")(cy)}",
                      |          "heading": "${messagesApi("addressLookup.editPage.heading")(cy)}"
                      |        }
                      |      }
                      |    }
                      |  }""".stripMargin

    Json.parse(v2Config).as[JsObject]
  }
}