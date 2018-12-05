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

import forms.SelectCompanyBenefitsForm
import models.SelectTaxYear.CYMinus2
import models.{CompanyBenefits, NormalMode}
import play.api.data.Form
import play.twirl.api.Html
import views.behaviours.{CheckboxViewBehaviours, ViewBehaviours}
import views.html.selectCompanyBenefits

class SelectCompanyBenefitsViewSpec extends ViewBehaviours with CheckboxViewBehaviours[CompanyBenefits.Value] {

  val messageKeyPrefix = "selectCompanyBenefits"
  val fieldKey = "value"
  val errorMessage = "error.invalid"
  private val taxYear = CYMinus2

  val values: Seq[(String, String)] = SelectCompanyBenefitsForm.options

  def form: Form[Seq[CompanyBenefits.Value]] = SelectCompanyBenefitsForm()

  override def createView(): Html = createView(form)

  def createView(form: Form[Seq[CompanyBenefits.Value]]): Html =
    selectCompanyBenefits(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages, formPartialRetriever, scalate)

  def createViewUsingForm = (form: Form[_]) => selectCompanyBenefits(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages, formPartialRetriever, scalate)

  "SelectCompanyBenefits view" must {
    behave like normalPage(createView, messageKeyPrefix, None, taxYear.asString(messages))

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    behave like checkboxPage(legend = Some(messages(s"$messageKeyPrefix.heading", taxYear.asString(messages))))

  }
}
