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
import forms.AnyAgentReferenceForm
import models.{AgentRef, NormalMode}
import play.api.data.{Form, FormError}
import play.twirl.api.HtmlFormat
import views.behaviours.QuestionViewBehaviours
import views.html.anyAgentRef

class AnyAgentRefViewSpec extends QuestionViewBehaviours[AgentRef]{

  val messageKeyPrefix = "anyAgentRef"

  val formProvider = new AnyAgentReferenceForm()
  val form = formProvider()

  def createView = () => anyAgentRef(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => anyAgentRef(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "AnyAgentRef view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    yesNoPage(createViewUsingForm, messageKeyPrefix, routes.AnyAgentRefController.onSubmit(NormalMode).url)

    def yesNoPage(createView: (Form[AgentRef]) => HtmlFormat.Appendable,
                  messageKeyPrefix: String,
                  expectedFormAction: String,
                  expectedHintText: Option[String] = None) = {

      "behave like a page with a Yes/No question" when {
        "rendered" must {
          "contain a legend for the question" in {
            val doc = asDocument(createView(form))
            val legends = doc.getElementsByTag("legend")
            legends.size mustBe 1
          }

          "contain a heading" in {
            val doc = asDocument(createView(form))
            assertContainsText(doc, messages(s"$messageKeyPrefix.heading"))
          }

          "contain an input for the value" in {
            val doc = asDocument(createView(form))
            assertRenderedById(doc, "agentRef.anyAgentRef-yes")
            assertRenderedById(doc, "agentRef.anyAgentRef-no")
          }

          "have no values checked when rendered with no form" in {
            val doc = asDocument(createView(form))
            assert(!doc.getElementById("agentRef.anyAgentRef-yes").hasAttr("checked"))
            assert(!doc.getElementById("agentRef.anyAgentRef-no").hasAttr("checked"))
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
            val doc = asDocument(createView(form.withError(FormError("agentRef.anyAgentRef", "Please enter a valid number"))))
            val errorSpan = doc.getElementsByClass("error-notification").first
            errorSpan.text mustBe messages(errorMessage)
          }
        }
      }
    }


    def answeredYesNoPage(createView: (Form[AgentRef]) => HtmlFormat.Appendable, answer: Boolean) = {

      "have only the correct value checked when yes selected" in {
        val doc = asDocument(createView(form.fill(AgentRef.Yes("AB1234"))))
        assert(doc.getElementById("agentRef.anyAgentRef-yes").hasAttr("checked"))
        assert(!doc.getElementById("agentRef.anyAgentRef-no").hasAttr("checked"))
      }

      "have only the correct value checked when no selected" in {
        val doc = asDocument(createView(form.fill(AgentRef.No)))
        assert(!doc.getElementById("agentRef.anyAgentRef-yes").hasAttr("checked"))
        assert(doc.getElementById("agentRef.anyAgentRef-no").hasAttr("checked"))
      }


      "not render an error summary" in {
        val doc = asDocument(createView(form.fill(AgentRef.Yes("AB1234"))))
        assertNotRenderedById(doc, "error-summary_header")
      }
    }
  }
}
