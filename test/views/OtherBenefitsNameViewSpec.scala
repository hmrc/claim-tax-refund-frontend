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
import play.api.data.Form
import controllers.routes
import forms.OtherBenefitsNameForm
import models.{Index, NormalMode}
import models.SelectTaxYear.CYMinus2
import org.scalatest.mockito.MockitoSugar
import play.api.i18n.Messages
import views.behaviours.StringViewBehaviours
import views.html.otherBenefitsName

class OtherBenefitsNameViewSpec(implicit messages: Messages) extends StringViewBehaviours with MockitoSugar {

  val messageKeyPrefix = "otherBenefitsName"
  private val taxYear = CYMinus2

  val appConfig: FrontendAppConfig = mock[FrontendAppConfig]

  override val form: Form[String] = new OtherBenefitsNameForm(appConfig)()

  def createView = () => otherBenefitsName(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) => otherBenefitsName(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  "OtherBenefitsName view" must {
    behave like normalPageWithDynamicHeader(createView, messageKeyPrefix, s"${taxYear.asString}?")

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    behave like stringPage(createViewUsingForm, messageKeyPrefix, routes.OtherBenefitsNameController.onSubmit(NormalMode, 1).url)
  }
}
