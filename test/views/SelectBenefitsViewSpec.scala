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

import play.api.data.Form
import forms.SelectBenefitsForm
import models.SelectTaxYear.CYMinus2
import models.{Benefits, NormalMode}
import play.twirl.api.Html
import views.behaviours.{CheckboxViewBehaviours, ViewBehaviours}
import views.html.selectBenefits

class SelectBenefitsViewSpec extends ViewBehaviours with CheckboxViewBehaviours[Benefits.Value] {

  val messageKeyPrefix = "selectBenefits"
  val fieldKey = "value"
  val errorMessage = "error.invalid"
  val taxYear: String = CYMinus2.asString

  val values: Map[String, Benefits.Value] =
    SelectBenefitsForm.options.map {
      case (k, v) => k -> Benefits.withName(v)
    }

  def form: Form[Set[Benefits.Value]] = SelectBenefitsForm()

  override def createView(): Html = createView(form)

  def createView(form: Form[Set[Benefits.Value]]): Html =
    selectBenefits(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => selectBenefits(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  "SelectBenefits view" must {
    behave like normalPageWithDynamicHeader(createView, messageKeyPrefix, " " + taxYear, messages("global.questionMark"))

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    behave like checkboxPage(legend = Some(messages(s"$messageKeyPrefix.heading")))
  }
}