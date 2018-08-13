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
import forms.NomineeFullNameForm
import models.NormalMode
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form
import views.behaviours.StringViewBehaviours
import views.html.nomineeFullName

class NomineeFullNameViewSpec extends StringViewBehaviours with MockitoSugar {

  private val messageKeyPrefix = "nomineeFullName"

  override val form: Form[String] = new NomineeFullNameForm(frontendAppConfig)()

  def createView = () => nomineeFullName(frontendAppConfig, form, NormalMode)(fakeRequest, messages, formPartialRetriever, templateRenderer)

  def createViewUsingForm = (form: Form[String]) => nomineeFullName(frontendAppConfig, form, NormalMode)(fakeRequest, messages, formPartialRetriever, templateRenderer)

  "NomineeFullName view" must {
    behave like normalPage(createView, messageKeyPrefix, None)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    behave like stringPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.NomineeFullNameController.onSubmit(NormalMode).url,
      expectedHintKeyLine1 = None,
      expectedHintKeyLine2 = None,
      expectedPrefix = None
    )

  }
}
