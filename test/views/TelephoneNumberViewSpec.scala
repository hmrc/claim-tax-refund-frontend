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
import forms.TelephoneNumberForm
import models.SelectTaxYear.CYMinus1
import models.{NormalMode, TelephoneOption}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.{Form, FormError}
import play.twirl.api.HtmlFormat
import views.behaviours.{NewQuestionViewBehaviours, QuestionViewBehaviours}
import views.html.telephoneNumber

class TelephoneNumberViewSpec extends NewQuestionViewBehaviours[TelephoneOption] with GuiceOneAppPerSuite {

  private val messageKeyPrefix = "telephoneNumber"
  private val testPhoneNumber = "0191 1111 111"
  private val taxYear = CYMinus1

  val formProvider = new TelephoneNumberForm()
  val form = formProvider()
  val telephoneNumber: telephoneNumber = fakeApplication().injector.instanceOf[telephoneNumber]

  def createView = () =>
    telephoneNumber(form, NormalMode, taxYear)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) =>
    telephoneNumber(form, NormalMode, taxYear)(fakeRequest, messages)

  "TelephoneNumber view" must {
    behave like normalPage(createView(), messageKeyPrefix, None)

    behave like pageWithBackLink(createView())

    behave like pageWithSecondaryHeader(createView(), messages("site.service_name.with_tax_year", taxYear.asString(messages)))

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
            "render a hint" in {
              val doc = asDocument(createView(form))
              assertYesNoHint(doc, expectedHintTextKey)
            }
          } else {
            "not render a hint" in {
              val doc = asDocument(createView(form))
              assertNotRenderedByCssSelector(doc, ".form-hint")
            }
          }

          "display hint para 1" in {
            val doc = asDocument(createView(form))
            assertContainsText(doc, messages(s"$messageKeyPrefix.hintPara1"))
          }

          "contain 2 radioButtons" in {
            val doc = asDocument(createView(form))
            assert(doc.select("input[type=\"radio\"]").size == 2)
            assertRenderedById(doc, "anyTelephoneNumber-2")
            assertRenderedById(doc, "anyTelephoneNumber")
          }

          "contain a conditionally-revealing text input" which {
            val doc = asDocument(createView(form))
            val telInput = doc.getElementById("telephoneNumber")

            "should have an input type of 'tel" in {
              assert(telInput.attr("type") == "tel")
            }

            "should have the autocomplete attribute" in {
              assert(telInput.hasAttr("autocomplete"))
              assert(telInput.attr("autocomplete") == "tel")
            }
          }

          "have no values checked when rendered with no form" in {
            val doc = asDocument(createView(form))
            assert(!doc.getElementById("anyTelephoneNumber-2").hasAttr("checked"))
            assert(!doc.getElementById("anyTelephoneNumber").hasAttr("checked"))
          }


          "include the form's value in the value input" in {
            val doc = asDocument(createView(form.fill(TelephoneOption.Yes(testPhoneNumber))))
            doc.getElementById("telephoneNumber").attr("value") mustBe testPhoneNumber
          }

          "not render an error summary" in {
            val doc = asDocument(createView(form))
            assertNotRenderedByCssSelector(doc, ".govuk-error-summary__title")
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
            assertRenderedByCssSelector(doc, ".govuk-error-summary__title")
          }

          "show an error in the value field's label" in {
            val doc = asDocument(createView(form.withError(FormError("anyTelephoneNumber", "Please enter a valid number"))))
            val errorSpan = doc.getElementsByClass("govuk-error-message").first
            errorSpan.text mustBe s"Error: ${messages(errorMessage)}"
          }
        }
      }
    }


    def answeredYesNoPage(createView: (Form[TelephoneOption]) => HtmlFormat.Appendable, answer: Boolean) = {

      "have only the correct value checked when yes selected" in {
        val doc = asDocument(createView(form.fill(TelephoneOption.Yes(testPhoneNumber))))
        assert(doc.getElementById("anyTelephoneNumber").hasAttr("checked"))
        assert(!doc.getElementById("anyTelephoneNumber-2").hasAttr("checked"))
      }

      "have only the correct value checked when no selected" in {
        val doc = asDocument(createView(form.fill(TelephoneOption.No)))
        assert(!doc.getElementById("anyTelephoneNumber").hasAttr("checked"))
        assert(doc.getElementById("anyTelephoneNumber-2").hasAttr("checked"))
      }

      "not render an error summary" in {
        val doc = asDocument(createView(form.fill(TelephoneOption.Yes(testPhoneNumber))))
        assertNotRenderedByCssSelector(doc, ".govuk-error-summary__title")
      }
    }
  }
}
