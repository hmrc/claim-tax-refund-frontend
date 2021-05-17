/*
 * Copyright 2021 HM Revenue & Customs
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

import forms.SelectTaxableIncomeForm
import models.SelectTaxYear.CustomTaxYear
import models.{NormalMode, TaxableIncome}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.twirl.api.Html
import views.behaviours.{CheckboxViewBehaviours, ViewBehaviours}
import views.html.selectTaxableIncome

class SelectTaxableIncomeViewSpec extends ViewBehaviours with CheckboxViewBehaviours[TaxableIncome.Value] with GuiceOneAppPerSuite {

  val messageKeyPrefix = "selectTaxableIncome"
  val fieldKey = "value"
  val errorMessage = "error.invalid"
  private val taxYear = CustomTaxYear(2017)

  val values: Seq[(String, String)] = SelectTaxableIncomeForm.options

  val selectTaxableIncome: selectTaxableIncome = fakeApplication.injector.instanceOf[selectTaxableIncome]

  def form: Form[Seq[TaxableIncome.Value]] = SelectTaxableIncomeForm()

  override def createView(): Html = createView(form)

  def createView(form: Form[Seq[TaxableIncome.Value]]): Html =
    selectTaxableIncome(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages, templateRenderer, ec)

  def createViewUsingForm = (form: Form[_]) => selectTaxableIncome(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages, templateRenderer, ec)

  "SelectTaxableIncome view" must {
    behave like normalPage(createView, messageKeyPrefix, None, taxYear.asString(messages))

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    behave like checkboxPage(legend = Some(messages(s"$messageKeyPrefix.heading", taxYear.asString(messages))))

  }
}
