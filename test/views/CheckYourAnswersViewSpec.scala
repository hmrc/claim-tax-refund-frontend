/*
 * Copyright 2021 HM Revenue & Customs
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

import base.SpecBase
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.i18n.Messages
import utils.{CheckYourAnswersHelper, CheckYourAnswersSections, MockUserAnswers}
import views.behaviours.ViewBehaviours
import views.html.check_your_answers

class CheckYourAnswersViewSpec extends SpecBase with ViewBehaviours with MockitoSugar with GuiceOneAppPerSuite {

  private val messageKeyPrefix = "checkYourAnswers"
  private val answers = MockUserAnswers.fullValidUserAnswers()
  private val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
  private val cyaSection = new CheckYourAnswersSections(helper, answers)
  private val sections = cyaSection.sections
  private val checkYourAnswers: check_your_answers = fakeApplication.injector.instanceOf[check_your_answers]

  def view = () => checkYourAnswers(frontendAppConfig, sections)(fakeRequest, messages: Messages, templateRenderer, ec)

  "Check your answers view" must {
    behave like normalPage(view, messageKeyPrefix, None)
  }

  "Page should display correct sections" in {
    val doc = asDocument(view())
    assertContainsText(doc, messages("checkYourAnswers.claimSection"))
    assertContainsText(doc, messages("checkYourAnswers.benefitSection"))
    assertContainsText(doc, messages("checkYourAnswers.companyBenefitSection"))
    assertContainsText(doc, messages("checkYourAnswers.taxableIncomeSection"))
    assertContainsText(doc, messages("checkYourAnswers.paymentSection"))
    assertContainsText(doc, messages("checkYourAnswers.contactSection"))
  }

  "Page should have correct hidden messages for change links in Taxable benefit details section" in {
    val doc = asDocument(view())
    assertContainsText(doc, messages("howMuchBereavementAllowance.changeLabel"))
    assertContainsText(doc, messages("howMuchCarersAllowance.changeLabel"))
    assertContainsText(doc, messages("howMuchJobseekersAllowance.changeLabel"))
    assertContainsText(doc, messages("howMuchEmploymentAndSupportAllowance.changeLabel"))
    assertContainsText(doc, messages("howMuchIncapacityBenefit.changeLabel"))
    assertContainsText(doc, messages("howMuchStatePension.changeLabel"))
  }

  "Page should have correct hidden messages for change links in Taxable company benefit details section" in {
    val doc = asDocument(view())
    assertContainsText(doc, messages("howMuchCarBenefits.changeLabel"))
    assertContainsText(doc, messages("howMuchFuelBenefit.changeLabel"))
    assertContainsText(doc, messages("howMuchMedicalBenefits.changeLabel"))
  }

  "Page should have correct hidden messages for change links in Taxable income details section" in {
    val doc = asDocument(view())
    assertContainsText(doc, messages("howMuchBankInterest.changeLabel"))
    assertContainsText(doc, messages("howMuchForeignIncome.changeLabel"))
    assertContainsText(doc, messages("howMuchInvestmentOrDividend.changeLabel"))
    assertContainsText(doc, messages("howMuchRentalIncome.changeLabel"))
  }
}
