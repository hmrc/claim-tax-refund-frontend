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

import config.FrontendAppConfig
import controllers.routes
import forms.PaymentUKAddressForm
import models.{NormalMode, UkAddress}
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form
import views.behaviours.QuestionViewBehaviours
import views.html.paymentUKAddress

class PaymentUKAddressViewSpec extends QuestionViewBehaviours[UkAddress] with MockitoSugar {

  val messageKeyPrefix = "paymentUKAddress"

  val appConfig: FrontendAppConfig = mock[FrontendAppConfig]

  override val form: Form[UkAddress] = new PaymentUKAddressForm(appConfig)()

  def createView = () => paymentUKAddress(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[UkAddress]) => paymentUKAddress(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "PaymentUKAddress view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    behave like pageWithTextFields(createViewUsingForm, messageKeyPrefix, routes.PaymentUKAddressController.onSubmit(NormalMode)
      .url, "addressLine1", "addressLine2", "addressLine3", "addressLine4", "addressLine5", "postcode")
  }
}