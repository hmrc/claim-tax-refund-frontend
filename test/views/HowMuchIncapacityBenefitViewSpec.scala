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
import forms.HowMuchIncapacityBenefitForm
import models.NormalMode
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form
import views.behaviours.StringViewBehaviours
import views.html.howMuchIncapacityBenefit

class HowMuchIncapacityBenefitViewSpec extends StringViewBehaviours with MockitoSugar {

  val messageKeyPrefix = "howMuchIncapacityBenefit"

  val appConfig: FrontendAppConfig = mock[FrontendAppConfig]

  override val form: Form[String] = new HowMuchIncapacityBenefitForm(appConfig)()

  def createView = () => howMuchIncapacityBenefit(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) => howMuchIncapacityBenefit(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "HowMuchIncapacityBenefit view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like stringPage(createViewUsingForm, messageKeyPrefix,
      routes.HowMuchIncapacityBenefitController.onSubmit(NormalMode).url, None, None, Some(messages("global.poundSign")))
  }
}
