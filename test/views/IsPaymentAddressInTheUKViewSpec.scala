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
import play.api.data.Form
import views.behaviours.YesNoViewBehaviours
import views.html.isPaymentAddressInTheUK

class IsPaymentAddressInTheUKViewSpec extends YesNoViewBehaviours {

  val messageKeyPrefix = "isPaymentAddressInTheUK"

  override val form = new BooleanForm()()

  def createView = () => isPaymentAddressInTheUK(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => isPaymentAddressInTheUK(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "IsPaymentAddressInTheUK view" must {

    behave like normalPage(createView, messageKeyPrefix, None)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    behave like yesNoPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.IsPaymentAddressInTheUKController.onSubmit(NormalMode).url,
      expectedHintTextKey = None
    )
  }
}
