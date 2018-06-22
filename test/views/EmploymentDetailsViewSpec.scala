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
import controllers.routes
import forms.BooleanForm
import models.SelectTaxYear.CYMinus2
import views.behaviours.YesNoViewBehaviours
import models.{Employment, NormalMode}
import play.api.i18n.Messages
import views.html.employmentDetails

class EmploymentDetailsViewSpec(implicit messages: Messages) extends YesNoViewBehaviours {

  val messageKeyPrefix = "employmentDetails"

  override val form = new BooleanForm()()

  val fakeEmployments = Seq(Employment("AVIVA PENSIONS", "754", "AZ00070"))

  private val taxYear = CYMinus2


  def createViewUsingForm = (form: Form[_]) => employmentDetails(frontendAppConfig, form, NormalMode, fakeEmployments, taxYear)(fakeRequest, messages)

  def createView = () => employmentDetails(frontendAppConfig, form, NormalMode, fakeEmployments, taxYear)(fakeRequest, messages)

  "EmploymentDetails view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.EmploymentDetailsController.onSubmit(NormalMode).url)
  }
}
