/*
 * Copyright 2023 HM Revenue & Customs
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

package views.behaviours

import org.jsoup.Jsoup
import play.twirl.api.{Html, HtmlFormat}
import views.NewViewSpecBase

trait NewViewBehaviours extends NewViewSpecBase {

  def normalPage(view: Html,
                 messageKeyPrefix: String,
                 expectedGuidanceKeys: Option[String],
                 args: Any*) = {

    "behave like a normal page" when {
      "rendered" must {
        "display the correct browser title" in {
          val doc = asDocument(view)
          assertRenderedByCssSelector(doc, "div.govuk-header__content")
        }

        "display the correct page title" in {
          val doc = asDocument(view)
          assertPageTitleEqualsMessage(doc, s"$messageKeyPrefix.heading", args: _*)
        }

        "display the correct guidance" in {
          val doc = asDocument(view)
          for (key <- expectedGuidanceKeys) assertContainsText(doc, messages(s"$messageKeyPrefix.$key"))
        }
      }
    }
  }

  def pageWithBackLink(view: Html) = {

    "behave like a page with a back link" must {
      "have a back link" in {
        val doc = asDocument(view)
        assertRenderedByCssSelector(doc, ".govuk-back-link")
      }
    }
  }

  def pageWithSecondaryHeader(view: Html,
                              heading: String) = {

    "behave like a page with a secondary header" in {
      Jsoup.parse(view.toString()).getElementsByClass("govuk-caption-xl").text() must include(heading)
    }
  }

  def pageWithList(view: () => HtmlFormat.Appendable,
                   pageKey: String,
                   bulletList: Seq[String],
                   messageKeyPrefix: String) = {

    "behave like a page with a list" must {
      "have a list" in {
        val doc = asDocument(view())
        bulletList.foreach {
          x => assertRenderedById(doc, s"bullet-$x")
        }
      }

      "have correct values" in {
        val doc = asDocument(view())
        bulletList.foreach{
          x => assertContainsMessages(doc, s"$messageKeyPrefix.$x")
        }
      }
    }
  }
}
