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

import base.SpecBase
import play.api.i18n.Lang
import play.api.libs.json.{JsString, JsValue, Json}
import uk.gov.hmrc.play.language.LanguageUtils

class AddressLookupConfigSpec extends SpecBase {
	val languages: Seq[Lang] = Seq(
		LanguageUtils.English,
		LanguageUtils.Welsh
	)

	"config" must {
		for (lang <- languages) {
			val addressConfig = Json.toJson(addressLookupConfig.config(continueUrl = s"", lang))

			s"return ${lang.language} based config when passed ${lang.language} Language" must {

				s"""navTitle must be "${messagesApi("index.title")(lang)}"""" in {
					val element = addressConfig \ "navTitle"
					element.get mustBe JsString(messagesApi("index.title")(lang))
				}

				"have lookup section" must {
					s"""have correct title of "${messagesApi("addressLookup.lookupPage.title")(lang)}""" in {
						val element = addressConfig \ "lookupPage" \ "title"
						element.get mustBe JsString(messagesApi("addressLookup.lookupPage.title")(lang))
					}

					s"""have correct heading of "${messagesApi("addressLookup.lookupPage.heading")(lang)}""" in {
						val element = addressConfig \ "lookupPage" \ "heading"
						element.get mustBe JsString(messagesApi("addressLookup.lookupPage.heading")(lang))
					}

					s"""have correct filterLabel of "${messagesApi("addressLookup.lookupPage.filterLabel")(lang)}"""" in {
						val element = addressConfig \ "lookupPage" \ "filterLabel"
						element.get mustBe JsString(messagesApi("addressLookup.lookupPage.filterLabel")(lang))
					}

					s"""have correct postcodeLabel of "${messagesApi("addressLookup.lookupPage.postcodeLabel")(lang)}""" in {
						val element = addressConfig \ "lookupPage" \ "postcodeLabel"
						element.get mustBe JsString(messagesApi("addressLookup.lookupPage.postcodeLabel")(lang))
					}

					s"""have correct submitLabel of "${messagesApi("addressLookup.lookupPage.submitLabel")(lang)}""" in {
						val element = addressConfig \ "lookupPage" \ "submitLabel"
						element.get mustBe JsString(messagesApi("addressLookup.lookupPage.submitLabel")(lang))
					}

					s"""have correct noResultsFoundMessage of "${messagesApi("addressLookup.lookupPage.noResultsFoundMessage")(lang)}""" in {
						val element = addressConfig \ "lookupPage" \ "noResultsFoundMessage"
						element.get mustBe JsString(messagesApi("addressLookup.lookupPage.noResultsFoundMessage")(lang))
					}

					s"""have correct resultLimitExceededMessage of "${messagesApi("addressLookup.lookupPage.resultLimitExceededMessage")(lang)}""" in {
						val element = addressConfig \ "lookupPage" \ "resultLimitExceededMessage"
						element.get mustBe JsString(messagesApi("addressLookup.lookupPage.resultLimitExceededMessage")(lang))
					}

					s"""have correct manualAddressLinkText of "${messagesApi("addressLookup.lookupPage.manualAddressLinkText")(lang)}""" in {
						val element = addressConfig \ "lookupPage" \ "manualAddressLinkText"
						element.get mustBe JsString(messagesApi("addressLookup.lookupPage.manualAddressLinkText")(lang))
					}

				}

				"have selectPage section" must {

				}

				"have confirmPage section" must {

				}

				"have editPAge section" must {

				}

			}
		}
	}

}
