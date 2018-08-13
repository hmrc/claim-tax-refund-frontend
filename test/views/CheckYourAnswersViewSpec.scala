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

import base.SpecBase
import org.scalatest.mockito.MockitoSugar
import play.api.i18n.Messages
import utils.{CheckYourAnswersHelper, CheckYourAnswersSections, MockUserAnswers}
import views.behaviours.ViewBehaviours
import views.html.check_your_answers

class CheckYourAnswersViewSpec extends SpecBase with ViewBehaviours with MockitoSugar {

  private val messageKeyPrefix = "checkYourAnswers"
  private val answers = MockUserAnswers.minimalValidUserAnswers
  private val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
  private val cyaSection = new CheckYourAnswersSections(helper, answers)
  private val sections = cyaSection.sections

  def view = () => check_your_answers(frontendAppConfig, sections)(fakeRequest, messages: Messages, formPartialRetriever, templateRenderer)

  "Check your answers view" must {
    behave like normalPage(view, messageKeyPrefix, None)
  }

  "Page should display correct sections" in {
    val doc = asDocument(view())
    assertContainsText(doc, messagesApi("checkYourAnswers.claimSection"))
    assertContainsText(doc, messagesApi("checkYourAnswers.benefitSection"))
    assertContainsText(doc, messagesApi("checkYourAnswers.companyBenefitSection"))
    assertContainsText(doc, messagesApi("checkYourAnswers.taxableIncomeSection"))
    assertContainsText(doc, messagesApi("checkYourAnswers.paymentSection"))
    assertContainsText(doc, messagesApi("checkYourAnswers.contactSection"))
  }
}
