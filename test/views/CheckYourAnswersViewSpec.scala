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
import utils.{CheckYourAnswersHelper, CheckYourAnswersSections, MockUserAnswers, UserAnswers}
import views.behaviours.ViewBehaviours
import views.html.check_your_answers

class CheckYourAnswersViewSpec (implicit messages: Messages) extends SpecBase with ViewBehaviours with MockitoSugar {

  val messageKeyPrefix = "checkYourAnswers"

  def view = () => check_your_answers(frontendAppConfig, sections)(fakeRequest, messages)

  private var answers = mock[UserAnswers]
  answers = MockUserAnswers.minimalValidUserAnswers

  val helper = new CheckYourAnswersHelper(answers)
  val section = new CheckYourAnswersSections(helper, MockUserAnswers.minimalValidUserAnswers)
  val sections = Seq(section.contactDetails)

  "Check you answers view" must {
    behave like normalPage(view, messageKeyPrefix)
  }

  "Page should display correct sections" in {
    val doc = asDocument(view())
    assertContainsText(doc, messagesApi("checkYourAnswers.yourDetailsSection"))
    assertContainsText(doc, messagesApi("checkYourAnswers.contactDetailsSection"))
  }
}
