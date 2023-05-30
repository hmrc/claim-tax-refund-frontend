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
import forms.BooleanForm
import models.NormalMode
import models.SelectTaxYear.CustomTaxYear
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.NewYesNoViewBehaviours
import views.html.anyTaxableIncome

class AnyTaxableIncomeViewSpec extends NewYesNoViewBehaviours with GuiceOneAppPerSuite {

  private val messageKeyPrefix = "anyTaxableIncome"
  private val listMessageKeyPrefix = "selectTaxableIncome"
  private val taxYear = CustomTaxYear(2017)

  override val form = new BooleanForm()()
  val anyTaxableIncome: anyTaxableIncome = fakeApplication().injector.instanceOf[anyTaxableIncome]

  def createView: () =>
    HtmlFormat.Appendable = () =>
      anyTaxableIncome(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  def createViewUsingForm: Form[_] =>
    HtmlFormat.Appendable = (form: Form[_]) =>
      anyTaxableIncome(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  "AnyTaxableIncome view" must {

    behave like normalPage(createView(), messageKeyPrefix, None)

    behave like pageWithBackLink(createView())

    behave like pageWithSecondaryHeader(createView(), messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    behave like yesNoPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.AnyTaxableIncomeController.onSubmit(NormalMode).url,
      expectedHintTextKey = None
    )

    behave like pageWithList(createView, messageKeyPrefix,
      Seq(
        "bank-or-building-society-interest",
        "foreign-income",
        "investment-or-dividends",
        "rental-income"
      ), listMessageKeyPrefix)

    "contain a listHeader" in {
      val doc = asDocument(createViewUsingForm(form))
      val employerName = doc.getElementById("listHeading").text
      employerName mustBe messages("anyTaxableIncome.listHeading")
    }
  }
}
