/*
 * Copyright 2020 HM Revenue & Customs
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
import forms.EnterPayeReferenceForm
import models.NormalMode
import models.SelectTaxYear.CYMinus4
import org.scalatestplus.mockito.MockitoSugar
import views.behaviours.StringViewBehaviours
import views.html.enterPayeReference

class EnterPayeReferenceViewSpec extends StringViewBehaviours with MockitoSugar {

  private val messageKeyPrefix = "enterPayeReference"
  private val appConfig: FrontendAppConfig = mock[FrontendAppConfig]
  private val taxYear = CYMinus4

  override val form: Form[String] = new EnterPayeReferenceForm(appConfig)()

  def createView = () => enterPayeReference(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages, formPartialRetriever, scalate)

  def createViewUsingForm = (form: Form[String]) => enterPayeReference(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages, formPartialRetriever, scalate)

  "EnterPayeReference view" must {
    behave like normalPage(createView, messageKeyPrefix, None)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    behave like stringPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.EnterPayeReferenceController.onSubmit(NormalMode).url,
      expectedHintKeyLine1 = Some("enterPayeReference.hint"),
      expectedHintKeyLine2 = None
    )
  }
}
