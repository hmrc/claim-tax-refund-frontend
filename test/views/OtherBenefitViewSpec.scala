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
import forms.OtherBenefitForm
import models.{NormalMode, OtherBenefit}
import models.SelectTaxYear.CustomTaxYear
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.NewQuestionViewBehaviours
import views.html.otherBenefit

class OtherBenefitViewSpec extends NewQuestionViewBehaviours[OtherBenefit] with MockitoSugar with GuiceOneAppPerSuite {

  private val messageKeyPrefix = "otherBenefit"
  private val taxYear = CustomTaxYear(2017)

  override val form: Form[OtherBenefit] = new OtherBenefitForm(frontendAppConfig)(Seq.empty, 0)
  val otherBenefit: otherBenefit = fakeApplication().injector.instanceOf[otherBenefit]

  def createView: () =>
    HtmlFormat.Appendable = () =>
    otherBenefit(frontendAppConfig, form, NormalMode, 0, taxYear)(fakeRequest, messages)

  def createViewUsingForm: Form[OtherBenefit] => HtmlFormat.Appendable = (form: Form[OtherBenefit]) =>
    otherBenefit(frontendAppConfig, form, NormalMode, 0, taxYear)(fakeRequest, messages)

  "OtherBenefit view" must {
    behave like normalPage(createView(), messageKeyPrefix, None, "1", taxYear.asString(messages))

    behave like pageWithBackLink(createView())

    behave like pageWithSecondaryHeader(createView(), messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    behave like pageWithTextFields(
      createViewUsingForm, messageKeyPrefix, routes.OtherBenefitController.onSubmit(NormalMode, 0).url, "name", "amount"
    )
  }
}
