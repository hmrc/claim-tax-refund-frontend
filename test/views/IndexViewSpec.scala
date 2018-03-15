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

package views

import controllers.routes
import models.NormalMode
import views.behaviours.ViewBehaviours
import views.html.index

class IndexViewSpec extends ViewBehaviours {

  def view = () => index(frontendAppConfig, call)(fakeRequest, messages)

  val call = routes.UserDetailsController.onPageLoad(NormalMode)

  "Index view" must {

    behave like normalPage(view, "index")
  }

  "link should direct the user to full name page" in {
    val doc = asDocument(view())
    doc.getElementById("start-now").attr("href") must include("/user-details")
  }

  "Page should have title and heading Claim a tax refund" in {
    val doc = asDocument(view())
    assertContainsText(doc, messagesApi("index.title"))
    assertContainsText(doc, messagesApi("index.heading"))
  }

  "Page should contain all use form content" in {
    val doc = asDocument(view())
    assertContainsText(doc, messagesApi("index.useForm.title"))
    assertContainsText(doc, messagesApi("index.useForm.item1"))
    assertContainsText(doc, messagesApi("index.useForm.item2"))
    assertContainsText(doc, messagesApi("index.useForm.item3"))
  }

  "Page should contain before you start content" in {
    val doc = asDocument(view())
    assertContainsText(doc, messagesApi("index.beforeYouStart.heading"))
    assertContainsText(doc, messagesApi("index.beforeYouStart.title"))
    assertContainsText(doc, messagesApi("index.beforeYouStart.item1"))
    assertContainsText(doc, messagesApi("index.beforeYouStart.p60Title"))
    assertContainsText(doc, messagesApi("index.beforeYouStart.item2"))
    assertContainsText(doc, messagesApi("index.beforeYouStart.taxableBenefitsTitle"))
    assertContainsText(doc, messagesApi("index.beforeYouStart.taxableIncomeTitle"))
    assertContainsText(doc, messagesApi("index.beforeYouStart.item2.2"))
    assertContainsText(doc, messagesApi("index.beforeYouStart.line2"))
    assertContainsText(doc, messagesApi("index.beforeYouStart.line3"))
    assertContainsText(doc, messagesApi("index.beforeYouStart.personalTaxAccountTitle"))
  }

  "Page links should link to correct locations" in {
    val doc = asDocument(view())
    doc.getElementById("p60-link").attr("href") must include(messagesApi("index.beforeYouStart.p60Link"))
    doc.getElementById("taxable-benefits-link").attr("href") must include(messagesApi("index.beforeYouStart.taxableBenefitsLink"))
    doc.getElementById("taxable-income-link").attr("href") must include(messagesApi("index.beforeYouStart.taxableIncomeLink"))
    doc.getElementById("personal-tax-account-link").attr("href") must include(messagesApi("index.beforeYouStart.personalTaxAccountLink"))
  }
}
