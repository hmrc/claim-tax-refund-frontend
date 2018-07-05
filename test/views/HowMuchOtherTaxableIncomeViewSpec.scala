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
import forms.HowMuchOtherTaxableIncomeForm
import models.NormalMode
import models.SelectTaxYear.CYMinus2
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form
import views.behaviours.StringViewBehaviours
import views.html.howMuchOtherTaxableIncome

class HowMuchOtherTaxableIncomeViewSpec extends StringViewBehaviours with MockitoSugar {

  private val messageKeyPrefix = "howMuchOtherTaxableIncome"
  private val taxYear = CYMinus2
  private val testIncome = "Test income"

  override val form: Form[String] = new HowMuchOtherTaxableIncomeForm(frontendAppConfig)()

  def createView = () => howMuchOtherTaxableIncome(frontendAppConfig, form, NormalMode, taxYear, testIncome)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) => howMuchOtherTaxableIncome(frontendAppConfig, form, NormalMode, taxYear, testIncome)(fakeRequest, messages)

  "howMuchOtherTaxableIncome view" must {
    behave like normalPage(createView, messageKeyPrefix, None, testIncome, taxYear.asString(messages))

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    behave like stringPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.HowMuchOtherTaxableIncomeController.onSubmit(NormalMode).url,
      expectedHintKeyLine1 = None,
      expectedHintKeyLine2 = None,
      expectedPrefix = Some(messages("global.poundSign")),
      args = taxYear.asString(messages)
      )

  }
}
