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

package views

import views.behaviours.ViewBehaviours
import views.html.accessibility

class AccessibilityViewSpec extends ViewBehaviours {

  def view = () => accessibility(frontendAppConfig)(fakeRequest, messages, formPartialRetriever, scalate)

  "Accessibility view" should {

    "have the correct title" in {
     assert(asDocument(view()).title == "Accessibility statement for Claim a tax refund")
    }

    "have a h1 heading" which {
      val heading = asDocument(view()).select("h1")
      "should be the only h1" in {
        assert(heading.size == 1)
      }
      "should have the correct text" in {
        assert(heading.text == "Accessibility statement for Claim a tax refund")
      }
    }

    "have h2 headings" which {
      val heading = asDocument(view()).select("h2")
      "have seven h2 headings" in {
        assert(heading.size() == 7)
      }
      "should have the correct text" in {
        for (i <- 0 until heading.size()) {
          assert(heading.get(i).text == messages(s"accessibility.subheading0${i + 1}"))
        }
      }
    }

    "have h3 headings" which {
      val heading = asDocument(view()).select("h3")
      "have a h3 heading" in {
        assert(heading.size() == 1)
      }
      "should have the correct text" in {
          assert(heading.get(0).text == messages("Non-accessible content"))
      }
    }

    "have h4 headings" which {
      val heading = asDocument(view()).select("h4")
      "have a h4 heading" in {
        assert(heading.size() == 1)
      }
      "should have the correct text" in {
          assert(heading.get(0).text == messages("Non-compliance with the accessibility regulations"))
      }
    }

    "have 22 paragraphs" in {
      assert(asDocument(view()).select("p").size() == 24)
    }
  }
}
