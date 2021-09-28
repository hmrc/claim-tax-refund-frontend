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
import forms.BooleanForm
import models.NormalMode
import models.SelectTaxYear.CustomTaxYear
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import views.behaviours.NewYesNoViewBehaviours
import views.html.isPaymentAddressInTheUK

class IsPaymentAddressInTheUKViewSpec extends NewYesNoViewBehaviours with GuiceOneAppPerSuite {

  val messageKeyPrefix = "isPaymentAddressInTheUK"
  private val taxYear = CustomTaxYear(2017)

  override val form = new BooleanForm()()
  val isPaymentAddressInTheUK: isPaymentAddressInTheUK = fakeApplication.injector.instanceOf[isPaymentAddressInTheUK]

  def createView = () => isPaymentAddressInTheUK(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => isPaymentAddressInTheUK(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  "IsPaymentAddressInTheUK view" must {

    behave like normalPage(createView, messageKeyPrefix, None)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    behave like yesNoPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.IsPaymentAddressInTheUKController.onSubmit(NormalMode).url,
      expectedHintTextKey = None
    )
  }
}
