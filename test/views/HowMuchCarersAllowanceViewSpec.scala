/*
 * Copyright 2023 HM Revenue & Customs
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
import forms.HowMuchCarersAllowanceForm
import models.NormalMode
import models.SelectTaxYear.CustomTaxYear
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import views.behaviours.NewStringViewBehaviours
import views.html.howMuchCarersAllowance

class HowMuchCarersAllowanceViewSpec extends NewStringViewBehaviours with MockitoSugar with GuiceOneAppPerSuite {

  private val messageKeyPrefix = "howMuchCarersAllowance"
  private val taxYear = CustomTaxYear(2017)

  override val form: Form[String] = new HowMuchCarersAllowanceForm(frontendAppConfig)()
  val howMuchCarersAllowance: howMuchCarersAllowance = fakeApplication().injector.instanceOf[howMuchCarersAllowance]

  def createView = () => howMuchCarersAllowance(form, NormalMode, taxYear)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) => howMuchCarersAllowance(form, NormalMode, taxYear)(fakeRequest, messages)

  "HowMuchCarersAllowance view" must {

    behave like normalPage(createView(), messageKeyPrefix, None)

    behave like pageWithBackLink(createView())

    behave like pageWithSecondaryHeader(createView(), messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    behave like stringPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.HowMuchCarersAllowanceController.onSubmit(NormalMode).url,
      expectedHintKeyLine1 = None,
      expectedHintKeyLine2 = None
    )

  }
}
