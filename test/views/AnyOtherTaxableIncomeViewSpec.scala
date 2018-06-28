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
import play.api.i18n.Messages
import views.behaviours.YesNoViewBehaviours
import views.html.anyOtherTaxableIncome

class AnyOtherTaxableIncomeViewSpec(implicit messages: Messages) extends YesNoViewBehaviours {

  private val messageKeyPrefix = "anyOtherTaxableIncome"
  private val taxYear = CYMinus2

  override val form = new BooleanForm()()

  def createView = () => anyOtherTaxableIncome(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => anyOtherTaxableIncome(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  "AnyOtherTaxableIncome view" must {

    behave like normalPageWithDynamicHeader(createView, messageKeyPrefix, taxYear.asString)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.AnyOtherTaxableIncomeController.onSubmit(NormalMode).url)
  }
}
