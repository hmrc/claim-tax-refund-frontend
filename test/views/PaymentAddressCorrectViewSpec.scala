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
import forms.BooleanForm
import models.NormalMode
import models.SelectTaxYear.CYMinus1
import play.api.data.Form
import uk.gov.hmrc.auth.core.retrieve.ItmpAddress
import views.behaviours.YesNoViewBehaviours
import views.html.paymentAddressCorrect

class PaymentAddressCorrectViewSpec extends YesNoViewBehaviours {

  private val messageKeyPrefix = "paymentAddressCorrect"
  private val taxYear = CYMinus1
  private val testAddress = ItmpAddress(
    Some("Line 1"),
    Some("Line 2"),
    Some("Line 3"),
    Some("Line 4"),
    Some("Line 5"),
    Some("AB12 3AB"),
    Some("England"),
    Some("UK")
  )

  override val form = new BooleanForm()()

  def createView = () =>
    paymentAddressCorrect(frontendAppConfig, form, NormalMode, testAddress, taxYear)(fakeRequest, messages, formPartialRetriever, scalate)

  def createViewUsingForm = (form: Form[_]) =>
    paymentAddressCorrect(frontendAppConfig, form, NormalMode, testAddress, taxYear)(fakeRequest, messages, formPartialRetriever, scalate)

  "PaymentAddressCorrect view" must {

    behave like normalPage(createView, messageKeyPrefix, None)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    behave like yesNoPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.PaymentAddressCorrectController.onSubmit(NormalMode).url,
      expectedHintTextKey = None
    )

    "render the section for the users address" in {
      val doc = asDocument(createViewUsingForm(form))
      assertRenderedById(doc, "usersAddress")
    }

    "contain the correct users address" in {
      val doc = asDocument(createViewUsingForm(form))
      assertContainsText(doc, testAddress.line1.get)
      assertContainsText(doc, testAddress.line2.get)
      assertContainsText(doc, testAddress.line3.get)
      assertContainsText(doc, testAddress.line4.get)
      assertContainsText(doc, testAddress.line5.get)
      assertContainsText(doc, testAddress.postCode.get)
      assertContainsText(doc, testAddress.countryName.get)
      assertContainsText(doc, testAddress.countryCode.get)
    }

  }
}
