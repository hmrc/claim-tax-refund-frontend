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
import forms.PaymentUKAddressForm
import models.SelectTaxYear.CYMinus1
import models.{NormalMode, UkAddress}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import views.behaviours.QuestionViewBehaviours
import views.html.paymentUKAddress

class PaymentUKAddressViewSpec extends QuestionViewBehaviours[UkAddress] with MockitoSugar with GuiceOneAppPerSuite {

  private val messageKeyPrefix = "paymentUKAddress"
  private val taxYear = CYMinus1

  override val form: Form[UkAddress] = new PaymentUKAddressForm(frontendAppConfig)()
  val paymentUKAddress: paymentUKAddress = fakeApplication.injector.instanceOf[paymentUKAddress]

  def createView = () =>
    paymentUKAddress(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages, formPartialRetriever, templateRenderer)

  def createViewUsingForm = (form: Form[UkAddress]) =>
    paymentUKAddress(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages, formPartialRetriever, templateRenderer)

  "PaymentUKAddress view" must {

    behave like normalPage(createView, messageKeyPrefix, None)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    behave like pageWithTextFields(createViewUsingForm, messageKeyPrefix, routes.PaymentUKAddressController.onSubmit(NormalMode)
      .url, "addressLine1", "addressLine2", "addressLine3", "addressLine4", "addressLine5", "postcode")
  }
}
