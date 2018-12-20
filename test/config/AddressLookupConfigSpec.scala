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
import play.api.libs.json.{JsString, Json}
import uk.gov.hmrc.play.language.LanguageUtils

class AddressLookupConfigSpec extends SpecBase {
	val languages: Seq[Lang] = Seq(
		LanguageUtils.English,
		LanguageUtils.Welsh
	)

	"config" must {
		for (lang <- languages) {
			val addressConfig = Json.toJson(addressLookupConfig.config(continueUrl = s"")(lang))
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
					s"""have correct title of "${messagesApi("addressLookup.selectPage.title")(lang)}""" in {
						val element = addressConfig \ "selectPage" \ "title"
						element.get mustBe JsString(messagesApi("addressLookup.selectPage.title")(lang))
					}

					s"""have correct heading of "${messagesApi("addressLookup.selectPage.heading")(lang)}""" in {
						val element = addressConfig \ "selectPage" \ "heading"
						element.get mustBe JsString(messagesApi("addressLookup.selectPage.heading")(lang))
					}

					s"""have correct proposalListLabel of "${messagesApi("addressLookup.selectPage.proposalListLabel")(lang)}""" in {
						val element = addressConfig \ "selectPage" \ "proposalListLabel"
						element.get mustBe JsString(messagesApi("addressLookup.selectPage.proposalListLabel")(lang))
					}

					s"""have correct submitLabel of "${messagesApi("addressLookup.selectPage.submitLabel")(lang)}""" in {
						val element = addressConfig \ "selectPage" \ "submitLabel"
						element.get mustBe JsString(messagesApi("addressLookup.selectPage.submitLabel")(lang))
					}

					s"""have correct searchAgainLinkText of "${messagesApi("addressLookup.selectPage.searchAgainLinkText")(lang)}""" in {
						val element = addressConfig \ "selectPage" \ "searchAgainLinkText"
						element.get mustBe JsString(messagesApi("addressLookup.selectPage.searchAgainLinkText")(lang))
					}

					s"""have correct editAddressLinkText of "${messagesApi("addressLookup.selectPage.editAddressLinkText")(lang)}""" in {
						val element = addressConfig \ "selectPage" \ "editAddressLinkText"
						element.get mustBe JsString(messagesApi("addressLookup.selectPage.editAddressLinkText")(lang))
					}
				}

				"have confirmPage section" must {
					s"""have correct title of "${messagesApi("addressLookup.confirmPage.title")(lang)}""" in {
						val element = addressConfig \ "confirmPage" \ "title"
						element.get mustBe JsString(messagesApi("addressLookup.confirmPage.title")(lang))
					}

					s"""have correct heading of "${messagesApi("addressLookup.confirmPage.heading")(lang)}""" in {
						val element = addressConfig \ "confirmPage" \ "heading"
						element.get mustBe JsString(messagesApi("addressLookup.confirmPage.heading")(lang))
					}

					s"""have correct infoSubheading of "${messagesApi("addressLookup.confirmPage.infoSubheading")(lang)}""" in {
						val element = addressConfig \ "confirmPage" \ "infoSubheading"
						element.get mustBe JsString(messagesApi("addressLookup.confirmPage.infoSubheading")(lang))
					}

					s"""have correct infoMessage of "${messagesApi("addressLookup.confirmPage.infoMessage")(lang)}""" in {
						val element = addressConfig \ "confirmPage" \ "infoMessage"
						element.get mustBe JsString(messagesApi("addressLookup.confirmPage.infoMessage")(lang))
					}

					s"""have correct submitLabel of "${messagesApi("addressLookup.confirmPage.submitLabel")(lang)}""" in {
						val element = addressConfig \ "confirmPage" \ "submitLabel"
						element.get mustBe JsString(messagesApi("addressLookup.confirmPage.submitLabel")(lang))
					}

					s"""have correct searchAgainLinkText of "${messagesApi("addressLookup.confirmPage.searchAgainLinkText")(lang)}""" in {
						val element = addressConfig \ "confirmPage" \ "searchAgainLinkText"
						element.get mustBe JsString(messagesApi("addressLookup.confirmPage.searchAgainLinkText")(lang))
					}

					s"""have correct changeLinkText of "${messagesApi("addressLookup.confirmPage.changeLinkText")(lang)}""" in {
						val element = addressConfig \ "confirmPage" \ "changeLinkText"
						element.get mustBe JsString(messagesApi("addressLookup.confirmPage.changeLinkText")(lang))
					}

				}

				"have editPage section" must {
					s"""have correct title of "${messagesApi("addressLookup.editPage.title")(lang)}""" in {
						val element = addressConfig \ "editPage" \ "title"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.title")(lang))
					}

					s"""have correct heading of "${messagesApi("addressLookup.editPage.heading")(lang)}""" in {
						val element = addressConfig \ "editPage" \ "heading"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.heading")(lang))
					}

					s"""have correct line1Label of "${messagesApi("addressLookup.editPage.line1Label")(lang)}""" in {
						val element = addressConfig \ "editPage" \ "line1Label"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.line1Label")(lang))
					}

					s"""have correct line2Label of "${messagesApi("addressLookup.editPage.line2Label")(lang)}""" in {
						val element = addressConfig \ "editPage" \ "line2Label"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.line2Label")(lang))
					}

					s"""have correct line3Label of "${messagesApi("addressLookup.editPage.line3Label")(lang)}""" in {
						val element = addressConfig \ "editPage" \ "line3Label"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.line3Label")(lang))
					}

					s"""have correct townLabel of "${messagesApi("addressLookup.editPage.townLabel")(lang)}""" in {
						val element = addressConfig \ "editPage" \ "townLabel"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.townLabel")(lang))
					}

					s"""have correct postcodeLabel of "${messagesApi("addressLookup.editPage.postcodeLabel")(lang)}""" in {
						val element = addressConfig \ "editPage" \ "postcodeLabel"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.postcodeLabel")(lang))
					}

					s"""have correct countryLabel of "${messagesApi("addressLookup.editPage.countryLabel")(lang)}""" in {
						val element = addressConfig \ "editPage" \ "countryLabel"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.countryLabel")(lang))
					}

					s"""have correct submitLabel of "${messagesApi("addressLookup.editPage.submitLabel")(lang)}""" in {
						val element = addressConfig \ "editPage" \ "submitLabel"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.submitLabel")(lang))
					}

					s"""have correct searchAgainLinkText of "${messagesApi("addressLookup.editPage.searchAgainLinkText")(lang)}""" in {
						val element = addressConfig \ "editPage" \ "searchAgainLinkText"
						element.get mustBe JsString(messagesApi("addressLookup.editPage.searchAgainLinkText")(lang))
					}
				}
			}
		}
	}

}
