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

package views

import controllers.routes
import forms.BooleanForm
import models.{AnyTaxPaid, NormalMode, OtherTaxableIncome}
import models.SelectTaxYear.CustomTaxYear
import org.jsoup.nodes.Document
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.{NewYesNoViewBehaviours, YesNoViewBehaviours}
import views.html.anyOtherTaxableIncome

class AnyOtherTaxableIncomeViewSpec extends NewYesNoViewBehaviours with GuiceOneAppPerSuite {

	private val messageKeyPrefix = "anyOtherTaxableIncome"
	private val taxYear = CustomTaxYear(2017)

	val completeSeq: Seq[(OtherTaxableIncome, Int)] = Seq((OtherTaxableIncome("qwerty1", "1234", Some(AnyTaxPaid.Yes("1234"))), 0))
	val incompleteSeq: Seq[(OtherTaxableIncome, Int)] = Seq((OtherTaxableIncome("qwerty2", "1234", None), 1))

	override val form = new BooleanForm()()
	val anyOtherTaxableIncome: anyOtherTaxableIncome = fakeApplication.injector.instanceOf[anyOtherTaxableIncome]

	def createView: () => HtmlFormat.Appendable = () =>
		anyOtherTaxableIncome(frontendAppConfig,
			form,
			NormalMode,
			taxYear,
			completeSeq,
			incompleteSeq)(fakeRequest, messages, templateRenderer, ec)

	def createViewUsingForm(complete: Seq[(OtherTaxableIncome, Int)], incomplete: Seq[(OtherTaxableIncome, Int)]): Form[_] =>
		HtmlFormat.Appendable = (form: Form[_]) =>
		anyOtherTaxableIncome(frontendAppConfig,
			form,
			NormalMode,
			taxYear,
			complete,
			incomplete)(fakeRequest, messages, templateRenderer, ec
		)

	override def assertPageTitleEqualsMessage(doc: Document, expectedMessageKey: String, args: Any*) = {
		val headers = doc.getElementsByClass("govuk-caption-xl heading-secondary")
		headers.size mustBe 1
		headers.first.text.replaceAll("\u00a0", " ") mustBe messages("site.service_name.with_tax_year", args: _*).replaceAll("&nbsp;", " ")
	}

	"AnyOtherTaxableIncome view" must {

		behave like normalPage(createView, messageKeyPrefix, None, taxYear.asString(messages))

		behave like pageWithBackLink(createView)

		behave like pageWithSecondaryHeader(createView, messages("site.service_name.with_tax_year", taxYear.asString(messages)))

		behave like yesNoPage(
			createView = createViewUsingForm(completeSeq, Seq.empty),
			messageKeyPrefix = messageKeyPrefix,
			expectedFormAction = routes.AnyOtherTaxableIncomeController.onSubmit(NormalMode).url,
			expectedHintTextKey = None
		)
	}

	"complete and incomplete lists are passed" must {
		val doc = asDocument(createViewUsingForm(completeSeq, incompleteSeq)(form))
		"show the complete list heading and incomplete heading and contain lists" in {
			doc.getElementById("add-to-list-complete-h2").text.contains(messages("global.addToList.complete")) mustBe true
			doc.getElementById("add-to-list-incomplete-h2").text.contains(messages("global.addToList.incomplete")) mustBe true
			doc.getElementById("complete-component-answer-list") != null
			doc.getElementById("incomplete-component-answer-list") != null
		}
	}

	"complete list is passed" must {
		val doc = asDocument(createViewUsingForm(completeSeq, Seq.empty)(form))
		"show the complete list and hide the complete heading" in {
			doc.getElementById("add-to-list-complete-h2") mustBe null
			doc.getElementById("complete-component-answer-list") != null
		}

		"not show the incomplete heading or contain the list" in {
			doc.getElementById("add-to-list-incomplete-h2") mustBe null
			doc.getElementById("incomplete-component-answer-list") mustBe null
		}

		"show the continue message" in {
			doc.getElementsByClass("govuk-button").text.contains(messages("site.continue")) mustBe true
		}
	}

	"incomplete list is passed" must {
		val doc = asDocument(createViewUsingForm(Seq.empty, incompleteSeq)(form))
		"show the incomplete list and heading" in {
			doc.getElementById("add-to-list-incomplete-h2") != null
			doc.getElementById("incomplete-component-answer-list") != null
		}

		"not show the complete heading or contain the list" in {
			doc.getElementById("add-to-list-complete-h2") mustBe null
			doc.getElementById("complete-component-answer-list") mustBe null
		}
	}

	"display 'You have told us about:' section" must {
		val doc: Document = asDocument(createView())
		"display list of created taxable benefits" in {
			doc.getElementById("add-list-0-answer").text.contains("qwerty") mustBe true
		}

		"list item must have change buttons" in {
			doc.getElementById("add-list-0-change").text.contains("Change") mustBe true
		}

		"list item must have a remove button" in {
			doc.getElementById("add-list-0-remove").text.contains("Remove") mustBe true
		}
	}
}
