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

import controllers.routes
import forms.AnyAgentReferenceForm
import models.SelectTaxYear.CYMinus1
import models.{AnyAgentRef, NormalMode}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.{Form, FormError}
import play.twirl.api.HtmlFormat
import views.behaviours.QuestionViewBehaviours
import views.html.anyAgentRef

class AnyAgentRefViewSpec extends QuestionViewBehaviours[AnyAgentRef] with GuiceOneAppPerSuite {

  private val requiredKey = "anyAgentRef.blank"
  private val requiredAgentRefKey = "anyAgentRef.blankAgentRef"
  private val messageKeyPrefix = "anyAgentRef"
  private val nomineeName = "Test Nominee"
  private val testAgentRef = "123456"
  private val taxYear = CYMinus1

  val formProvider = new AnyAgentReferenceForm()
  val form = formProvider(messages(requiredKey, nomineeName), messages(requiredAgentRefKey, nomineeName))
  val anyAgentRef: anyAgentRef = fakeApplication.injector.instanceOf[anyAgentRef]

  def createView = () => anyAgentRef(frontendAppConfig, form, NormalMode, nomineeName, taxYear)(fakeRequest, messages, templateRenderer, ec)

  def createViewUsingForm = (form: Form[_]) =>
    anyAgentRef(frontendAppConfig, form, NormalMode, nomineeName, taxYear)(fakeRequest, messages, templateRenderer, ec)

  "AnyAgentRef view" must {

    behave like normalPage(createView, messageKeyPrefix, None, nomineeName)

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    yesNoPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.AnyAgentRefController.onSubmit(NormalMode).url,
      expectedHintTextKey = None,
      args = nomineeName
    )

    def yesNoPage(createView: Form[AnyAgentRef] => HtmlFormat.Appendable,
                  messageKeyPrefix: String,
                  expectedFormAction: String,
                  expectedHintTextKey: Option[String],
                  args: Any*) = {

      "behave like a page with a Yes/No question" when {
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

          "contain an input for the value" in {
            val doc = asDocument(createView(form))
            assertRenderedById(doc, "anyAgentRef-yes")
            assertRenderedById(doc, "anyAgentRef-no")
            assertRenderedById(doc, "yesAgentRef")
          }

          "have no values checked when rendered with no form" in {
            val doc = asDocument(createView(form))
            assert(!doc.getElementById("anyAgentRef-yes").hasAttr("checked"))
            assert(!doc.getElementById("anyAgentRef-no").hasAttr("checked"))
          }


          "include the form's value in the value input" in {
            val doc = asDocument(createView(form.fill(AnyAgentRef.Yes(testAgentRef))))
            doc.getElementById("agentRef").attr("value") mustBe testAgentRef
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
            val doc = asDocument(createView(form.withError(FormError("anyAgentRef", "Please enter a valid number"))))
            val errorSpan = doc.getElementsByClass("error-notification").first
            errorSpan.text mustBe messages(errorMessage)
          }
        }
      }
    }


    def answeredYesNoPage(createView: (Form[AnyAgentRef]) => HtmlFormat.Appendable, answer: Boolean) = {

      "have only the correct value checked when yes selected" in {
        val doc = asDocument(createView(form.fill(AnyAgentRef.Yes(testAgentRef))))
        assert(doc.getElementById("anyAgentRef-yes").hasAttr("checked"))
        assert(!doc.getElementById("anyAgentRef-no").hasAttr("checked"))
      }

      "have only the correct value checked when no selected" in {
        val doc = asDocument(createView(form.fill(AnyAgentRef.No)))
        assert(!doc.getElementById("anyAgentRef-yes").hasAttr("checked"))
        assert(doc.getElementById("anyAgentRef-no").hasAttr("checked"))
      }


      "not render an error summary" in {
        val doc = asDocument(createView(form.fill(AnyAgentRef.Yes(testAgentRef))))
        assertNotRenderedById(doc, "error-summary_header")
      }
    }
  }
}
