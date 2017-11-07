/*
 * Copyright 2017 HM Revenue & Customs
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

import views.behaviours.ViewBehaviours
import views.html.confirmation

class ConfirmationViewSpec extends ViewBehaviours {

  def view = () => confirmation(frontendAppConfig)(fakeRequest, messages)

  "Confirmation view" must {
    behave like normalPage(view, "confirmation", "whatHappensNext", "guidance1", "guidance2", "contactUs", "contactTelephone", "openingHours")

    "have its title in a highlight box" in {
      val doc = asDocument(view())
      val h1 = doc.getElementsByTag("h1").first
      h1.parent.hasClass("govuk-box-highlight") mustBe true
    }

    "have a link to an exit survey" in {
      val doc = asDocument(view())
      val link = doc.getElementById("survey-link")
      link.text mustBe messages("confirmation.survey.linkText")
      link.attr("href") must include("/feedback-survey/?origin=SCC")
    }

    "have a link to GOV.UK" in {
      val doc = asDocument(view())
      val link = doc.getElementById("gov-uk-link")
      link.attr("href") mustBe "https://www.gov.uk"
      link.text mustBe messages("confirmation.gov.uk.linkText")
    }
  }
}
