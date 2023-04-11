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
import forms.AnyTaxPaidForm
import models.SelectTaxYear.CustomTaxYear
import models.{AnyTaxPaid, NormalMode}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.{Form, FormError}
import play.twirl.api.HtmlFormat
import views.behaviours.NewQuestionViewBehaviours
import views.html.anyTaxableForeignIncome

class AnyTaxableForeignIncomeViewSpec extends NewQuestionViewBehaviours[AnyTaxPaid] with GuiceOneAppPerSuite {

  private val messageKeyPrefix = "anyTaxableForeignIncome"
  private val testAmount = "9,999.00"
  private val notSelectedKey = "anyTaxableForeignIncome.notSelected"
  private val blankKey = "anyTaxableForeignIncome.blank"
  private val invalidKey = "anyTaxableForeignIncome.invalid"
  private val taxYear = CustomTaxYear(2017)

  val formProvider = new AnyTaxPaidForm()
  val form = formProvider(notSelectedKey, blankKey, invalidKey)
  val anyTaxableForeignIncome: anyTaxableForeignIncome = fakeApplication().injector.instanceOf[anyTaxableForeignIncome]

  def createView = () => anyTaxableForeignIncome(form, NormalMode, taxYear)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[_]) => anyTaxableForeignIncome(form, NormalMode, taxYear)(fakeRequest, messages)

  "AnyTaxableForeignIncome view" must {

    behave like normalPage(createView, messageKeyPrefix, None, taxYear.asString(messages))

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    yesNoPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.AnyTaxableForeignIncomeController.onSubmit(NormalMode).url,
      expectedHintTextKey = None
    )

    def yesNoPage(createView: (Form[AnyTaxPaid]) => HtmlFormat.Appendable,
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

          "contain an input for the value" in {
            val doc = asDocument(createView(form))
            assertRenderedById(doc, "anyTaxPaid-2")
            assertRenderedById(doc, "anyTaxPaid")
            assertRenderedById(doc, "taxPaidAmount")
          }

          "have no values checked when rendered with no form" in {
            val doc = asDocument(createView(form))
            assert(!doc.getElementById("anyTaxPaid").hasAttr("checked"))
            assert(!doc.getElementById("anyTaxPaid-2").hasAttr("checked"))
          }

          "include the form's value in the value input" in {
            val doc = asDocument(createView(form.fill(AnyTaxPaid.Yes(testAmount))))
            doc.getElementById("taxPaidAmount").attr("value") mustBe testAmount
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
            assertRenderedByCssSelector(doc, ".govuk-error-summary__title")
          }

          "show an error in the value field's label" in {
            val doc = asDocument(createView(form.withError(FormError("anyTaxPaid", "Please enter a valid number"))))
            val errorSpan = doc.getElementsByClass("govuk-error-message").first
            errorSpan.text mustBe s"Error: ${messages(errorMessage)}"
          }
        }
      }
    }


    def answeredYesNoPage(createView: (Form[AnyTaxPaid]) => HtmlFormat.Appendable, answer: Boolean) = {

      "have only the correct value checked when yes selected" in {
        val doc = asDocument(createView(form.fill(AnyTaxPaid.Yes(testAmount))))
        assert(doc.getElementById("anyTaxPaid").hasAttr("checked"))
        assert(!doc.getElementById("anyTaxPaid-2").hasAttr("checked"))
      }

      "have only the correct value checked when no selected" in {
        val doc = asDocument(createView(form.fill(AnyTaxPaid.No)))
        assert(!doc.getElementById("anyTaxPaid").hasAttr("checked"))
        assert(doc.getElementById("anyTaxPaid-2").hasAttr("checked"))
      }

      "not render an error summary" in {
        val doc = asDocument(createView(form.fill(AnyTaxPaid.Yes(testAmount))))
        assertNotRenderedById(doc, "error-summary_header")
      }
    }
  }
}
