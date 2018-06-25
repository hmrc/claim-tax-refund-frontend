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
import views.behaviours.YesNoViewBehaviours
import models.{Benefits, NormalMode}
import models.SelectTaxYear.CYMinus2
import play.api.i18n.Messages
import views.html.anyCompanyBenefits

class AnyCompanyBenefitsViewSpec(implicit messages: Messages) extends YesNoViewBehaviours {

  val messageKeyPrefix = "anyCompanyBenefits"

  override val form = new BooleanForm()()

  private val taxYear = CYMinus2

  def createView = () => anyCompanyBenefits(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => anyCompanyBenefits(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  "AnyCompanyBenefits view" must {

    behave like normalPageWithDynamicHeader(createView, messageKeyPrefix, s"${taxYear.asString}?")

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.AnyBenefitsController.onSubmit(NormalMode).url, Some(s"$messageKeyPrefix.hint"))

    behave like pageWithList(createView, messageKeyPrefix,
      Seq(
        "company-car-benefit",
        "fuel-benefit",
        "medical-benefit",
        "other-company-benefit"
      )
    )
  }
}
