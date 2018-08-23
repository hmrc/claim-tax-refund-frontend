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
import forms.PaymentInternationalAddressForm
import models.SelectTaxYear.CYMinus1
import models.{InternationalAddress, NormalMode}
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form
import views.behaviours.QuestionViewBehaviours
import views.html.paymentInternationalAddress

class PaymentInternationalAddressViewSpec extends QuestionViewBehaviours[InternationalAddress] with MockitoSugar {

  private val messageKeyPrefix = "paymentInternationalAddress"
  private val taxYear = CYMinus1

  override val form: Form[InternationalAddress] = new PaymentInternationalAddressForm(frontendAppConfig)()

  def createView = () =>
    paymentInternationalAddress(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages, formPartialRetriever, templateRenderer)

  def createViewUsingForm = (form: Form[InternationalAddress]) =>
    paymentInternationalAddress(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages, formPartialRetriever, templateRenderer)

  "PaymentInternationalAddress view" must {

    behave like normalPage(createView, messageKeyPrefix, None)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    behave like pageWithTextFields(
      createViewUsingForm,
      messageKeyPrefix,
      routes.PaymentInternationalAddressController.onSubmit(NormalMode).url,
      "addressLine1",
      "addressLine2",
      "addressLine3",
      "addressLine4", "addressLine5",
      "country"
    )
  }
}
