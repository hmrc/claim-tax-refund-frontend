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

package views

import org.jsoup.select.NodeFilter
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.twirl.api.HtmlFormat
import views.behaviours.{NewViewBehaviours, ViewBehaviours}
import views.html.confirmation

class ConfirmationViewSpec extends NewViewBehaviours with MockitoSugar with GuiceOneAppPerSuite {

  private val messageKeyPrefix = "confirmation"
  private val submissionReference = " ABC-1234-DEF"  //this contains normal hyphens
  private val formattedSubmissionReference = " ABC‑1234‑DEF" //this contains non-breaking hyphens "‑"
  private val confirmation: confirmation = fakeApplication.injector.instanceOf[confirmation]

  def createView: () =>
		HtmlFormat.Appendable = () =>
			confirmation(frontendAppConfig, submissionReference)(fakeRequest, messages)

  "Confirmation view" must {
    behave like normalPage(createView, messageKeyPrefix, None)

    "have its title in a confirmation govuk panel" in {
      val doc = asDocument(createView())
      val h1 = doc.getElementsByTag("h1").first
      h1.parent.hasClass("govuk-panel--confirmation") mustBe true
    }

    "have a reference with non-breaking hyphen in a highlight box" in {
      val doc = asDocument(createView())
      val referenceText = doc.getElementById("reference")
      referenceText.text mustBe messages("confirmation.reference") + formattedSubmissionReference
      referenceText.parent.hasClass("govuk-panel--confirmation") mustBe true
    }

    "have what happens next header and text" in {
      val doc = asDocument(createView())
      assertContainsText(doc, messages("confirmation.whatHappensNext.title"))
      assertContainsText(doc, messages("confirmation.whatHappensNext.line1"))
    }

    "have a link to an exit survey" in {
      val doc = asDocument(createView())
      val feedback = doc.select("a[href*=/feedback/CTR]")
      feedback.isEmpty mustBe false
      feedback.text.contains(messages("confirmation.survey.linkText")) mustBe true
    }
  }
}
