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
import uk.gov.hmrc.auth.core.retrieve.ItmpName
import utils.{CheckYourAnswersHelper, CheckYourAnswersSections, MockUserAnswers}
import views.behaviours.ViewBehaviours
import views.html.pdf_check_your_answers

class PdfCheckYourAnswersViewSpec extends SpecBase with ViewBehaviours with MockitoSugar {

  private val messageKeyPrefix = "checkYourAnswers"
  private val answers = MockUserAnswers.minimalValidUserAnswers
  private val helper = new CheckYourAnswersHelper(answers)(messages: Messages)
  private val cyaSection = new CheckYourAnswersSections(helper, answers)
  private val sections = cyaSection.sections
  private val nino = "AB123456A"
  private val itmpName: Option[ItmpName] = Some(ItmpName(Some("First"), Some("Middle"), Some("Last")))

  def view = () => pdf_check_your_answers(frontendAppConfig, sections, nino, itmpName)(fakeRequest, messages: Messages)

  "PDF Check your answers view" must {
    "display the correct browser title" in {
      val doc = asDocument(view())
      assertEqualsMessage(doc, "title", s"checkYourAnswers.pdfHeader")
    }

    "display the correct page title" in {
      val doc = asDocument(view())
      assertPageTitleEqualsMessage(doc, s"checkYourAnswers.pdfHeader")
    }

    "display correct sections" in {
      val doc = asDocument(view())
      assertContainsText(doc, messagesApi("checkYourAnswers.userSection"))
      assertContainsText(doc, messagesApi("checkYourAnswers.claimSection"))
      assertContainsText(doc, messagesApi("checkYourAnswers.benefitsSection"))
      assertContainsText(doc, messagesApi("checkYourAnswers.companyBenefitsSection"))
      assertContainsText(doc, messagesApi("checkYourAnswers.taxableIncomeSection"))
      assertContainsText(doc, messagesApi("checkYourAnswers.paymentSection"))
      assertContainsText(doc, messagesApi("checkYourAnswers.contactSection"))
    }

    "display nino" in {
      val doc = asDocument(view())
      assertContainsText(doc, nino)
    }

    "display name when provided by auth" in {
      val doc = asDocument(view())
      assertContainsText(doc, itmpName.get.givenName.get)
      assertContainsText(doc, itmpName.get.middleName.get)
      assertContainsText(doc, itmpName.get.familyName.get)
    }

    "display correct names when given name missing" in {
      val noGivenName = Some(ItmpName(None, Some("middle"), Some("last")))
      val doc = asDocument(pdf_check_your_answers(frontendAppConfig, sections, nino, noGivenName)(fakeRequest, messages: Messages))
      assertContainsText(doc, noGivenName.get.middleName.get)
      assertContainsText(doc, noGivenName.get.familyName.get)
    }

    "display correct names when middle name missing" in {
      val noMiddleName = Some(ItmpName(Some("first"), None, Some("last")))
      val doc = asDocument(pdf_check_your_answers(frontendAppConfig, sections, nino, noMiddleName)(fakeRequest, messages: Messages))
      assertContainsText(doc, noMiddleName.get.givenName.get)
      assertContainsText(doc, noMiddleName.get.familyName.get)
    }

    "display correct names when last name missing" in {
      val noLastName = Some(ItmpName(Some("first"), Some("middle"), None))
      val doc = asDocument(pdf_check_your_answers(frontendAppConfig, sections, nino, noLastName)(fakeRequest, messages: Messages))
      assertContainsText(doc, noLastName.get.givenName.get)
      assertContainsText(doc, noLastName.get.middleName.get)
    }

    "not display name when not provided by auth" in {
      val doc = asDocument(pdf_check_your_answers(frontendAppConfig, sections, nino, None)(fakeRequest, messages: Messages))
      assertNotRenderedById(doc, "cya-name-answer")
    }

  }
}
