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

import config.FrontendAppConfig
import play.api.data.Form
import controllers.routes
import forms.OtherCompanyBenefitsDetailsForm
import models.NormalMode
import models.SelectTaxYear.CYMinus2
import org.scalatest.mockito.MockitoSugar
import views.behaviours.StringViewBehaviours
import views.html.otherCompanyBenefitsDetails

class OtherCompanyBenefitsDetailsViewSpec extends StringViewBehaviours with MockitoSugar {

  val messageKeyPrefix = "otherCompanyBenefitsDetails"

  private val taxYear = CYMinus2.asString

  val appConfig: FrontendAppConfig = mock[FrontendAppConfig]

  override val form: Form[String] = new OtherCompanyBenefitsDetailsForm(appConfig)()

  def createView = () => otherCompanyBenefitsDetails(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) => otherCompanyBenefitsDetails(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages)

  "OtherCompanyBenefitsDetails view" must {
    behave like normalPageWithDynamicHeader(createView, messageKeyPrefix, " " + taxYear, messages("global.questionMark"))

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    behave like stringPage(createViewUsingForm, messageKeyPrefix, routes.OtherCompanyBenefitsDetailsController.onSubmit(NormalMode).url)
  }
}