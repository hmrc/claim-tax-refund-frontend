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

import controllers.routes
import forms.TelephoneNumberForm
import models.{NormalMode, TelephoneOption}
import play.api.data.{Form, FormError}
import play.twirl.api.HtmlFormat
import views.behaviours.QuestionViewBehaviours
import views.html.telephoneNumber

class TelephoneNumberViewSpec extends QuestionViewBehaviours[TelephoneOption]{

  private val messageKeyPrefix = "telephoneNumber"
  private val testPhoneNumber = "0191 1111 111"

  val formProvider = new TelephoneNumberForm()
  val form = formProvider()

  def createView = () => telephoneNumber(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => telephoneNumber(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "TelephoneNumber view" must {
    behave like normalPage(createView, messageKeyPrefix, None)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("index.title"))

    yesNoPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.TelephoneNumberController.onSubmit(NormalMode).url,
      expectedHintTextKey = None
    )

    def yesNoPage(createView: (Form[TelephoneOption]) => HtmlFormat.Appendable,
                  messageKeyPrefix: String,
                  expectedFormAction: String,
                  expectedHintTextKey: Option[String],
                  args: Any*) = {

      "behave like a page with a Yes/No question and revealing content" when {
        "rendered" must {
          "contain a legend for the question" in {
            val doc = asDocument(createView(form))
            val legends = doc.getElementsByTag("legend")
            legends.size mustBe 1
          }

          "contain a heading" in {
            val doc = asDocument(createView(form))
            assertContainsText(doc, messages(s"$messageKeyPrefix.heading", args: _*))
          }

          if(expectedHintTextKey.isDefined){
            "contain a label for the value" in {
              val doc = asDocument(createView(form))
              val expectedHintTextLine1 = expectedHintTextKey map (k => messages(k))
              assertYesNoHint(doc, expectedHintTextLine1)
            }
          }

          "contain an input for the value" in {
            val doc = asDocument(createView(form))
            assertRenderedById(doc, "anyTelephoneNumber-yes")
            assertRenderedById(doc, "anyTelephoneNumber-no")
            assertRenderedById(doc, "yesTelephoneNumber")
            assertRenderedById(doc, "noTelephoneNumber")
          }

          "have no values checked when rendered with no form" in {
            val doc = asDocument(createView(form))
            assert(!doc.getElementById("anyTelephoneNumber-yes").hasAttr("checked"))
            assert(!doc.getElementById("anyTelephoneNumber-no").hasAttr("checked"))
          }

          "include the form's value in the value input" in {
            val doc = asDocument(createView(form.fill(TelephoneOption.Yes(testPhoneNumber))))
            doc.getElementById("telephoneNumber").attr("value") mustBe testPhoneNumber
          }

          "not render an error summary" in {
            val doc = asDocument(createView(form))
            assertNotRenderedById(doc, "error-summary_header")
          }
        }

        "rendered with a value of true" must {
          behave like answeredYesNoPage(createView, true)
        }

        "rendered with a value of false" must {
          behave like answeredYesNoPage(createView, false)
        }

        "rendered with an error" must {
          "show an error summary" in {
            val doc = asDocument(createView(form.withError(error)))
            assertRenderedById(doc, "error-summary-heading")
          }

          "show an error in the value field's label" in {
            val doc = asDocument(createView(form.withError(FormError("anyTelephoneNumber", "Please enter a valid number"))))
            val errorSpan = doc.getElementsByClass("error-notification").first
            errorSpan.text mustBe messages(errorMessage)
          }
        }
      }
    }


    def answeredYesNoPage(createView: (Form[TelephoneOption]) => HtmlFormat.Appendable, answer: Boolean) = {

      "have only the correct value checked when yes selected" in {
        val doc = asDocument(createView(form.fill(TelephoneOption.Yes(testPhoneNumber))))
        assert(doc.getElementById("anyTelephoneNumber-yes").hasAttr("checked"))
        assert(!doc.getElementById("anyTelephoneNumber-no").hasAttr("checked"))
      }

      "have only the correct value checked when no selected" in {
        val doc = asDocument(createView(form.fill(TelephoneOption.No)))
        assert(!doc.getElementById("anyTelephoneNumber-yes").hasAttr("checked"))
        assert(doc.getElementById("anyTelephoneNumber-no").hasAttr("checked"))
      }

      "display hint text when no is selected" in {
        val doc = asDocument(createView(form.fill(TelephoneOption.No)))
        assertContainsText(doc, messages(s"$messageKeyPrefix.hintPara1"))
      }

      "not render an error summary" in {
        val doc = asDocument(createView(form.fill(TelephoneOption.Yes(testPhoneNumber))))
        assertNotRenderedById(doc, "error-summary_header")
      }
    }
  }
}
