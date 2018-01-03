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
import forms.PartialClaimAmountForm
import models.NormalMode
import org.scalatest.mockito.MockitoSugar
import views.behaviours.StringViewBehaviours
import views.html.partialClaimAmount

class PartialClaimAmountViewSpec extends StringViewBehaviours with MockitoSugar {

  val messageKeyPrefix = "partialClaimAmount"
  val testRegex = """(?=.)^\$?(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+)?(\.[0-9]{1,2})?$"""

  val appConfig: FrontendAppConfig = mock[FrontendAppConfig]

  override val form: Form[String] = new PartialClaimAmountForm(appConfig)()

  def createView = () => partialClaimAmount(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) => partialClaimAmount(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "PartialClaimAmount view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like stringPage(createViewUsingForm, messageKeyPrefix, routes.PartialClaimAmountController.onSubmit(NormalMode).url,
      Some(s"$messageKeyPrefix.hintLine1"), None, Some(messages("global.poundSign")))
  }
}
