/*
 * Copyright 2019 HM Revenue & Customs
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

import forms.SelectTaxYearForm
import models.{NormalMode, SelectTaxYear}
import play.api.data.Form
import play.api.i18n.Messages
import utils.RadioOption
import views.behaviours.ViewBehaviours
import views.html.selectTaxYear

class SelectTaxYearViewSpec extends ViewBehaviours {

  private val messageKeyPrefix = "selectTaxYear"

  def createView = () => selectTaxYear(frontendAppConfig, SelectTaxYearForm(), NormalMode)(fakeRequest, messages, formPartialRetriever, scalate)

  def createViewUsingForm = (form: Form[_]) => selectTaxYear(frontendAppConfig, form, NormalMode)(fakeRequest, messages, formPartialRetriever, scalate)

  def radioButtonOptions(implicit messages: Messages): Seq[RadioOption] = SelectTaxYear.options


  "SelectTaxYear view" must {
    behave like normalPage(createView, messageKeyPrefix, None)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))
  }

  "SelectTaxYear view" when {
    "rendered" must {
      "contain radio buttons for the value" in {
        val doc = asDocument(createViewUsingForm(SelectTaxYearForm()))
        for (option <- radioButtonOptions(messages)) {
          assertContainsRadioButton(doc, option.id, "value", option.value, false)
        }
      }
    }

    for (option <- radioButtonOptions(messages)) {
      s"rendered with a value of '${option.value}'" must {
        s"have the '${option.value}' radio button selected" in {
          val doc = asDocument(createViewUsingForm(SelectTaxYearForm().bind(Map("value" -> s"${option.value}"))))
          assertContainsRadioButton(doc, option.id, "value", option.value, true)

          for (unselectedOption <- radioButtonOptions(messages).filterNot(o => o == option)) {
            assertContainsRadioButton(doc, unselectedOption.id, "value", unselectedOption.value, false)
          }
        }
      }
    }
  }
}
