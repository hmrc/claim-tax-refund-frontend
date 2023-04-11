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
import forms.OtherTaxableIncomeForm
import models.SelectTaxYear.CustomTaxYear
import models.{NormalMode, OtherTaxableIncome}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.NewQuestionViewBehaviours
import views.html.otherTaxableIncome

class OtherTaxableIncomeViewSpec extends NewQuestionViewBehaviours[OtherTaxableIncome] with MockitoSugar with GuiceOneAppPerSuite {

  private val messageKeyPrefix = "otherTaxableIncome"
  private val taxYear = CustomTaxYear(2017)

  override val form: Form[OtherTaxableIncome] = new OtherTaxableIncomeForm(frontendAppConfig)(Seq.empty, 0)
  val otherTaxableIncome: otherTaxableIncome = fakeApplication().injector.instanceOf[otherTaxableIncome]

  def createView: () =>
    HtmlFormat.Appendable = () =>
      otherTaxableIncome(form, NormalMode, 0, taxYear)(fakeRequest, messages)

  def createViewUsingForm: Form[OtherTaxableIncome] => HtmlFormat.Appendable = (form: Form[OtherTaxableIncome]) =>
    otherTaxableIncome(form, NormalMode, 0, taxYear)(fakeRequest, messages)

  "OtherTaxableIncome view" must {
    behave like normalPage(createView, messageKeyPrefix, None, "1", taxYear.asString(messages))

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    behave like pageWithTextFields(
      createViewUsingForm, messageKeyPrefix, routes.OtherTaxableIncomeController.onSubmit(NormalMode, 0).url, "name", "amount"
    )

  }
}
