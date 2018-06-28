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
import forms.HowMuchStatePensionForm
import models.NormalMode
import models.SelectTaxYear.CYMinus2
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form
import play.api.i18n.Messages
import views.behaviours.StringViewBehaviours
import views.html.howMuchStatePension

class HowMuchStatePensionViewSpec(implicit messages: Messages) extends StringViewBehaviours with MockitoSugar {

  private val messageKeyPrefix = "howMuchStatePension"
  private val taxYear = CYMinus2
  private val appConfig: FrontendAppConfig = mock[FrontendAppConfig]

  override val form: Form[String] = new HowMuchStatePensionForm(appConfig)()

  def createView = () => howMuchStatePension(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) => howMuchStatePension(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  "HowMuchStatePension view" must {
    behave like normalPage(createView, messageKeyPrefix, taxYear.asString)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    behave like stringPage(createViewUsingForm, messageKeyPrefix,
      routes.HowMuchStatePensionController.onSubmit(NormalMode).url, None, None, Some(messages("global.poundSign")))
  }
}
