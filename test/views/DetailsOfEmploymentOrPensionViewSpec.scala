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
import forms.DetailsOfEmploymentOrPensionForm
import models.NormalMode
import models.SelectTaxYear.CustomTaxYear
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import views.behaviours.NewStringViewBehaviours
import views.html.detailsOfEmploymentOrPension

class DetailsOfEmploymentOrPensionViewSpec extends NewStringViewBehaviours with MockitoSugar with GuiceOneAppPerSuite {

  private val messageKeyPrefix = "detailsOfEmploymentOrPension"
  private val taxYear = CustomTaxYear(2017)
  private val characterLimit = 500

  override val form: Form[String] = new DetailsOfEmploymentOrPensionForm(frontendAppConfig)()
  val detailsOfEmploymentOrPension: detailsOfEmploymentOrPension = fakeApplication().injector.instanceOf[detailsOfEmploymentOrPension]

  def createView = () =>
    detailsOfEmploymentOrPension(frontendAppConfig, form, NormalMode, taxYear, characterLimit)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) =>
    detailsOfEmploymentOrPension(frontendAppConfig, form, NormalMode, taxYear, characterLimit)(fakeRequest, messages)

  "DetailsOfEmploymentOrPension view" must {
    behave like normalPage(createView(), messageKeyPrefix, None, taxYear.asString(messages))

    behave like pageWithBackLink(createView())

    behave like pageWithSecondaryHeader(createView(), messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    behave like stringPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.DetailsOfEmploymentOrPensionController.onSubmit(NormalMode).url,
      expectedHintKeyLine1 = Some("detailsOfEmploymentOrPension.hintText"),
      expectedHintKeyLine2 = None
    )
  }

}
