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

import play.api.data.Form
import controllers.routes
import forms.BooleanForm
import views.behaviours.YesNoViewBehaviours
import models.NormalMode
import play.api.i18n.Messages
import uk.gov.hmrc.auth.core.retrieve.ItmpAddress
import views.html.paymentAddressCorrect

class PaymentAddressCorrectViewSpec(implicit messages: Messages) extends YesNoViewBehaviours {

  private val messageKeyPrefix = "paymentAddressCorrect"
  private val testAddress = ItmpAddress(
    Some("Line 1"),
    Some("Line 2"),
    Some("Line 3"),
    None,
    None,
    Some("AB12 3AB"),
    None,
    None
  )

  override val form = new BooleanForm()()

  def createView = () => paymentAddressCorrect(frontendAppConfig, form, NormalMode, testAddress)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => paymentAddressCorrect(frontendAppConfig, form, NormalMode, testAddress)(fakeRequest, messages)

  "PaymentAddressCorrect view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.PaymentAddressCorrectController.onSubmit(NormalMode).url)

    "render the section for the users address" in {
      val doc = asDocument(createViewUsingForm(form))
      assertRenderedById(doc, "usersAddress")
    }

    "contain the correct users address" in {
      val doc = asDocument(createViewUsingForm(form))
      assertContainsText(doc, testAddress.toString)
    }

  }
}
