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

import forms.SelectTaxableIncomeForm
import models.SelectTaxYear.CYMinus2
import models.{NormalMode, TaxableIncome}
import play.api.data.Form
import play.api.i18n.Messages
import play.twirl.api.Html
import views.behaviours.{CheckboxViewBehaviours, ViewBehaviours}
import views.html.selectTaxableIncome

class SelectTaxableIncomeViewSpec(implicit messages: Messages) extends ViewBehaviours with CheckboxViewBehaviours[TaxableIncome.Value] {

  val messageKeyPrefix = "selectTaxableIncome"
  val fieldKey = "value"
  val errorMessage = "error.invalid"
  private val taxYear = CYMinus2


  val values: Map[String, TaxableIncome.Value] =
    SelectTaxableIncomeForm.options.map {
      case (k, v) => k -> TaxableIncome.withName(v)
    }

  def form: Form[Set[TaxableIncome.Value]] = SelectTaxableIncomeForm()

  override def createView(): Html = createView(form)

  def createView(form: Form[Set[TaxableIncome.Value]]): Html =
    selectTaxableIncome(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => selectTaxableIncome(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  "SelectTaxableIncome view" must {
    behave like normalPageWithDynamicHeader(createView, messageKeyPrefix, s"${taxYear.asString}?")

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    behave like checkboxPage(legend = Some(messages(s"$messageKeyPrefix.heading")))

  }
}
