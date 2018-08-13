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
import forms.BooleanForm
import models.NormalMode
import models.SelectTaxYear.CYMinus2
import play.api.data.Form
import views.behaviours.YesNoViewBehaviours
import views.html.anyTaxableIncome

class AnyTaxableIncomeViewSpec extends YesNoViewBehaviours {

  private val messageKeyPrefix = "anyTaxableIncome"
  private val listMessageKeyPrefix = "selectTaxableIncome"
  private val taxYear = CYMinus2

  override val form = new BooleanForm()()

  def createView = () => anyTaxableIncome(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages, formPartialRetriever, templateRenderer)

  def createViewUsingForm = (form: Form[_]) => anyTaxableIncome(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages, formPartialRetriever, templateRenderer)

  "AnyTaxableIncome view" must {

    behave like normalPage(createView, messageKeyPrefix, None, taxYear.asString(messages))

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    behave like yesNoPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.AnyTaxableIncomeController.onSubmit(NormalMode).url,
      expectedHintTextKey = None,
      args = taxYear.asString(messages)
    )

    behave like pageWithList(createView, messageKeyPrefix,
      Seq(
        "rental-income",
        "bank-or-building-society-interest",
        "investment-or-dividends",
        "foreign-income",
        "other-taxable-income"
      ), listMessageKeyPrefix)

    "contain a listHeader" in {
      val doc = asDocument(createViewUsingForm(form))
      val employerName = doc.getElementById("listHeading").text
      employerName mustBe messages("anyTaxableIncome.listHeading")
    }
  }
}
