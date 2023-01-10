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
import org.jsoup.nodes.Document
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.twirl.api.Html
import utils.{CheckYourAnswersHelper, CheckYourAnswersSections, MockUserAnswers}
import viewmodels.AnswerSection
import views.behaviours.NewYesNoViewBehaviours
import views.html.anyOtherBenefits

class AnyOtherBenefitsViewSpec extends NewYesNoViewBehaviours with GuiceOneAppPerSuite {

  private val messageKeyPrefix = "anyOtherBenefits"
  private val taxYear = CustomTaxYear(2017)

	private val cya: CheckYourAnswersHelper = new CheckYourAnswersHelper(MockUserAnswers.fullValidUserAnswers())(messages)
	private val otherBenefits: AnswerSection = new CheckYourAnswersSections(cya, MockUserAnswers.fullValidUserAnswers()).otherBenefitsAddToListNormalMode

  override val form = new BooleanForm()()
  val anyOtherBenefits: anyOtherBenefits = fakeApplication.injector.instanceOf[anyOtherBenefits]

  def createView: () => Html = () =>
    anyOtherBenefits(frontendAppConfig, form, NormalMode, taxYear, otherBenefits)(fakeRequest, messages)

  def createViewUsingForm: Form[_] => Html = (form: Form[_]) =>
    anyOtherBenefits(frontendAppConfig, form, NormalMode, taxYear, otherBenefits)(fakeRequest, messages)

  "AnyOtherBenefits view" must {

    behave like normalPage(createView, messageKeyPrefix, None, taxYear.asString(messages))

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    behave like yesNoPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.AnyOtherBenefitsController.onSubmit(NormalMode).url,
      expectedHintTextKey = None
    )

		"any benefits list" must {
      val doc: Document = asDocument(createView())
      "display list of created benefits" in {
        doc.getElementsByClass("govuk-summary-list").text.contains("qwerty") mustBe true
      }

      "list item must have change buttons" in {
        doc.getElementsByClass("govuk-summary-list__actions-list-item").text.contains("Change qwerty") mustBe true
      }

			"list item must have a remove button" in {
				doc.getElementsByClass("govuk-summary-list__actions-list-item").text.contains("Remove qwerty") mustBe true
			}
		}
  }
}
