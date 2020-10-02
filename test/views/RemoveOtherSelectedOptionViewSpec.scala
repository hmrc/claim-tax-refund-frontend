/*
 * Copyright 2020 HM Revenue & Customs
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
import models.{NormalMode, OtherBenefit}
import models.SelectTaxYear.CustomTaxYear
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.twirl.api.Html
import views.html.removeOtherSelectedOption

class RemoveOtherSelectedOptionViewSpec extends YesNoViewBehaviours with GuiceOneAppPerSuite {

  private val messageKeyPrefix = "RemoveOtherSelectedOption"
  private val taxYear = CustomTaxYear(2017)
  private val collectionId = OtherBenefit.collectionId

  override val form = new BooleanForm()()
  val removeOtherSelectedOption: removeOtherSelectedOption = fakeApplication.injector.instanceOf[removeOtherSelectedOption]

  def createView: () => Html = () =>
    removeOtherSelectedOption(frontendAppConfig, form, NormalMode, taxYear, collectionId)(fakeRequest, messages, formPartialRetriever, templateRenderer)

  def createViewUsingForm: Form[_] => Html = (form: Form[_]) =>
    removeOtherSelectedOption(frontendAppConfig, form, NormalMode, taxYear, collectionId)(fakeRequest, messages, formPartialRetriever, templateRenderer)

  "RemoveOtherSelectedOption view" must {

    behave like normalPage(createView, messageKeyPrefix, None, messages(collectionId))

    behave like pageWithBackLink(createView)

    behave like yesNoPage(
      createViewUsingForm,
      messageKeyPrefix,
      routes.RemoveOtherSelectedOptionController.onSubmit(NormalMode, collectionId).url,
      None,
      messages(collectionId)
    )
  }
}
