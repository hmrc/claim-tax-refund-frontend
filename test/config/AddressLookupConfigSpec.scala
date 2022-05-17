/*
 * Copyright 2022 HM Revenue & Customs
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

import base.SpecBase
import play.api.i18n.Lang
import play.api.libs.json.JsString

class AddressLookupConfigSpec extends SpecBase {
  val languages: Seq[Lang] = Seq(
    Lang("en"),
    Lang("cy")
  )

  "config" must {

    for (lang <- languages) {
      s" have ${lang.code} labels " must {
        val addressConfig = addressLookupConfig.config(continueUrl = s"", accessibilityFooterUrl = "")(lang)
        "have appsLevel labels" must {
          s"""have correct nav Title of "${messagesApi("index.title")(lang)}" """ in {
            val element = addressConfig \ "labels" \ lang.code \ "appLevelLabels" \ "navTitle"
            element.get mustBe JsString(messagesApi("index.title")(lang))
          }
        }

        "have lookupPageLabels section" must {
          s"""have correct title of "${messagesApi("addressLookup.lookupPage.title")(lang)}" """ in {
            val element = addressConfig \ "labels" \ lang.code \ "lookupPageLabels" \ "title"
            element.get mustBe JsString(messagesApi("addressLookup.lookupPage.title")(lang))
          }
          s"""have correct heading of "${messagesApi("addressLookup.lookupPage.heading")(lang)}" """ in {
            val element = addressConfig \ "labels" \ lang.code \ "lookupPageLabels" \ "heading"
            element.get mustBe JsString(messagesApi("addressLookup.lookupPage.heading")(lang))
          }
        }
        "have editPageLabels" must {
          s"""have correct title of "${messagesApi("addressLookup.editPage.title")(lang)}" """ in {
            val element = addressConfig \ "labels" \ lang.code \ "editPageLabels" \ "title"
            element.get mustBe JsString(messagesApi("addressLookup.editPage.title")(lang))
          }
          s"""have correct heading of "${messagesApi("addressLookup.editPage.heading")(lang)}" """ in {
            val element = addressConfig \ "labels" \ lang.code \ "editPageLabels" \ "heading"
            element.get mustBe JsString(messagesApi("addressLookup.editPage.heading")(lang))
          }
        }
      }
    }
  }

}