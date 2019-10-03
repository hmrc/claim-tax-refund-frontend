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

import base.SpecBase
import play.api.i18n.Lang
import play.api.libs.json.{JsString}
import uk.gov.hmrc.play.language.LanguageUtils

class AddressLookupConfigSpec extends SpecBase {
	val languages: Seq[Lang] = Seq(
		LanguageUtils.English,
		LanguageUtils.Welsh
	)

	"config" must {

		for (lang <- languages) {
			val addressConfig = (addressLookupConfig.config(continueUrl = s"")(lang))
			s"return ${lang.code} based config when passed ${lang.code} Language" must {
				s"""homeNavHref must be "${messagesApi("index.title")(lang)}"""" in {
					val element = addressConfig \ "options" \ "homeNavHref"
					element.get mustBe JsString(messagesApi("index.title")(lang))
				}
			}

			s" have ${lang.code} labels " must {
				val addressConfig = (addressLookupConfig.config(continueUrl = s"")(lang))
				"have appsLevel labels" must {
					s"""have correct nav Title of "${messagesApi("index.title")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "appLevelLabels" \ "navTitle"
						element.get mustBe JsString(messagesApi("index.title")(lang))
					}
				}
				"have selectPageLabels section" must {
					s"""have correct title of "${messagesApi("addressLookup.selectPage.title")(lang)} """ in {
						val element = addressConfig \ "labels" \ lang.code \ "selectPageLabels" \ "title"
						element.get mustBe JsString(messagesApi("addressLookup.selectPage.title")(lang))
					}
					s""" have correct heading of "${messagesApi("addressLookup.selectPage.heading")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "selectPageLabels" \ "heading"
						element.get mustBe JsString(messagesApi("addressLookup.selectPage.heading")(lang))
					}
					s"""have correct heading with Postcode of "${messagesApi("addressLookup.selectPage.heading")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "selectPageLabels" \ "heading"
						element.get mustBe JsString(messagesApi("addressLookup.selectPage.heading")(lang))
					}
					s"""have correct proposal List Label of "${messagesApi("addressLookup.selectPage.proposalListLabel")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "selectPageLabels" \ "proposalListLabel"
						element.get mustBe JsString(messagesApi("addressLookup.selectPage.proposalListLabel")(lang))
					}
					s"""have correct submit Label of "${messagesApi("addressLookup.selectPage.submitLabel")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "selectPageLabels" \ "submitLabel"
						element.get mustBe JsString(messagesApi("addressLookup.selectPage.submitLabel")(lang))
					}
					s"""have correct search Again Link Text of "${messagesApi("addressLookup.selectPage.searchAgainLinkText")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "selectPageLabels" \ "searchAgainLinkText"
						element.get mustBe JsString(messagesApi("addressLookup.selectPage.searchAgainLinkText")(lang))
					}
					s"""have correct edit Address Link Text of "${messagesApi("addressLookup.selectPage.editAddressLinkText")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "selectPageLabels" \ "editAddressLinkText"
						element.get mustBe JsString(messagesApi("addressLookup.selectPage.editAddressLinkText")(lang))
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
					s"""have correct filter Label of "${messagesApi("addressLookup.lookupPage.filterLabel")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "lookupPageLabels" \ "filterLabel"
						element.get mustBe JsString(messagesApi("addressLookup.lookupPage.filterLabel")(lang))
					}
					s"""have correct postcode Label of "${messagesApi("addressLookup.lookupPage.postcodeLabel")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "lookupPageLabels" \ "postcodeLabel"
						element.get mustBe JsString(messagesApi("addressLookup.lookupPage.postcodeLabel")(lang))
					}
					s"""have correct submit Label of "${messagesApi("addressLookup.lookupPage.submitLabel")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "lookupPageLabels" \ "submitLabel"
						element.get mustBe JsString(messagesApi("addressLookup.lookupPage.submitLabel")(lang))
					}
					s"""have correct no Results Found Message of "${messagesApi("addressLookup.lookupPage.noResultsFoundMessage")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "lookupPageLabels" \ "noResultsFoundMessage"
						element.get mustBe JsString(messagesApi("addressLookup.lookupPage.noResultsFoundMessage")(lang))
					}
					s"""have correct result Limit Exceeded Message of "${messagesApi("addressLookup.lookupPage.resultLimitExceededMessage")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "lookupPageLabels" \ "resultLimitExceededMessage"
						element.get mustBe JsString(messagesApi("addressLookup.lookupPage.resultLimitExceededMessage")(lang))
					}
					s"""have correct manual Address Link Text of "${messagesApi("addressLookup.lookupPage.manualAddressLinkText")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "lookupPageLabels" \ "manualAddressLinkText"
						element.get mustBe JsString(messagesApi("addressLookup.lookupPage.manualAddressLinkText")(lang))
					}
				}
				"have confirmPageLabels" must {
					s"""have correct title of "${messagesApi("addressLookup.confirmPage.title")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "confirmPageLabels" \ "title"
						element.get mustBe JsString(messagesApi("addressLookup.confirmPage.title")(lang))
					}
					s"""have correct heading of "${messagesApi("addressLookup.confirmPage.heading")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "confirmPageLabels" \ "heading"
						element.get mustBe JsString(messagesApi("addressLookup.confirmPage.heading")(lang))
					}
					s"""have correct info Sub heading of "${messagesApi("addressLookup.confirmPage.infoSubheading")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "confirmPageLabels" \ "infoSubheading"
						element.get mustBe JsString(messagesApi("addressLookup.confirmPage.infoSubheading")(lang))
					}
					s"""have correct info Message of "${messagesApi("addressLookup.confirmPage.infoMessage")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "confirmPageLabels" \ "infoMessage"
						element.get mustBe JsString(messagesApi("addressLookup.confirmPage.infoMessage")(lang))
					}
					s"""have correct submit Label of "${messagesApi("addressLookup.confirmPage.submitLabel")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "confirmPageLabels" \ "submitLabel"
						element.get mustBe JsString(messagesApi("addressLookup.confirmPage.submitLabel")(lang))
					}
					s"""have correct search Again Link Text of "${messagesApi("addressLookup.confirmPage.searchAgainLinkText")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "confirmPageLabels" \ "searchAgainLinkText"
						element.get mustBe JsString(messagesApi("addressLookup.confirmPage.searchAgainLinkText")(lang))
					}
					s"""have correct change Link Text of "${messagesApi("addressLookup.confirmPage.changeLinkText")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "confirmPageLabels" \ "changeLinkText"
						element.get mustBe JsString(messagesApi("addressLookup.confirmPage.changeLinkText")(lang))
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
					s"""have correct line1 Label of "${messagesApi("addressLookup.editPage.line1Label")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "editPageLabels" \ "line1Label"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.line1Label")(lang))
					}
					s"""have correct line2 Label of "${messagesApi("addressLookup.editPage.line2Label")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "editPageLabels" \ "line2Label"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.line2Label")(lang))
					}
					s"""have correct line3 Label of "${messagesApi("addressLookup.editPage.line3Label")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "editPageLabels" \ "line3Label"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.line3Label")(lang))
					}
					s"""have correct town Label of "${messagesApi("addressLookup.editPage.townLabel")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "editPageLabels" \ "townLabel"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.townLabel")(lang))
					}
					s"""have correct postcode Label of "${messagesApi("addressLookup.editPage.postcodeLabel")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "editPageLabels" \ "postcodeLabel"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.postcodeLabel")(lang))
					}
					s"""have correct country Label of "${messagesApi("addressLookup.editPage.countryLabel")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "editPageLabels" \ "countryLabel"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.countryLabel")(lang))
					}
					s"""have correct submit Labelof "${messagesApi("addressLookup.editPage.submitLabel")(lang)}" """ in {
						val element = addressConfig \ "labels" \ lang.code \ "editPageLabels" \ "submitLabel"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.submitLabel")(lang))
					}
				}
			}
		}
	}

}
