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
import models.TelephoneOption
import org.scalatest.mockito.MockitoSugar
import play.api.i18n.Messages
import play.twirl.api.Html
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
  private val telephoneNumber: Option[TelephoneOption] = Some(TelephoneOption.Yes("01234567890"))

  def view: () => Html = () => pdf_check_your_answers(
    appConfig = frontendAppConfig,
    answerSections = sections,
    nino = nino,
    name = itmpName,
    address = Some(itmpAddress),
    telephone = telephoneNumber)(fakeRequest, messages: Messages)

  "PDF Check your answers view" must {
    "display the correct page title" in {
      val doc = asDocument(view())
      assertPageTitleEqualsMessage(doc, "checkYourAnswers.pdfHeader")
    }

    "display correct sections" in {
      val doc = asDocument(view())
      assertContainsText(doc, messagesApi("checkYourAnswers.userSection"))
      assertContainsText(doc, messagesApi("checkYourAnswers.claimSection"))
      assertContainsText(doc, messagesApi("checkYourAnswers.benefitSection"))
      assertContainsText(doc, messagesApi("checkYourAnswers.companyBenefitSection"))
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

    "display address when provided by auth" in {
      val doc = asDocument(view())
      assertContainsText(doc, itmpAddress.line1.get)
      assertContainsText(doc, itmpAddress.line2.get)
      assertContainsText(doc, itmpAddress.line3.get)
      assertContainsText(doc, itmpAddress.line4.get)
      assertContainsText(doc, itmpAddress.line5.get)
      assertContainsText(doc, itmpAddress.postCode.get)
      assertContainsText(doc, itmpAddress.countryName.get)
      assertContainsText(doc, itmpAddress.countryCode.get)
    }

    "display telephone number when provided by user" in {
      val doc = asDocument(view())
      assertContainsText(doc, TelephoneOption.asString(telephoneNumber.get))
    }

    "not display name when not provided by auth" in {
      val doc = asDocument(pdf_check_your_answers(frontendAppConfig, sections, nino, None, None, None)(fakeRequest, messages: Messages))
      assertNotRenderedById(doc, "cya-name-answer")
      assertNotRenderedById(doc, "cya-address-answer")
      assertNotRenderedById(doc, "cya-telephone-answer")
    }

  }
}
