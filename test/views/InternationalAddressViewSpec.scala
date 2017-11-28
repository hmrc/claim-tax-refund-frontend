/*
 * Copyright 2017 HM Revenue & Customs
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
import play.api.data.Form
import controllers.routes
import forms.InternationalAddressForm
import models.{InternationalAddress, NormalMode}
import org.scalatest.mockito.MockitoSugar
import views.behaviours.QuestionViewBehaviours
import views.html.internationalAddress

class InternationalAddressViewSpec extends QuestionViewBehaviours[InternationalAddress] with MockitoSugar{

  val addressLineMaxLength = 35
  val countryMaxLength = 20

  val messageKeyPrefix = "internationalAddress"

  val appConfig: FrontendAppConfig = mock[FrontendAppConfig]

  override val form: Form[InternationalAddress] = new InternationalAddressForm(appConfig)()

  def createView = () =>
    internationalAddress(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[InternationalAddress]) => internationalAddress(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "InternationalAddress view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like pageWithTextFields(createViewUsingForm, messageKeyPrefix,
      routes.InternationalAddressController.onSubmit(NormalMode).url, "addressLine1", "addressLine2", "addressLine3", "addressLine4","addressLine5","country")
  }
}
