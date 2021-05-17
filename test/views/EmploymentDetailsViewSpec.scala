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
import forms.BooleanForm
import models.SelectTaxYear.CustomTaxYear
import models.{Employment, NormalMode}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import views.behaviours.YesNoViewBehaviours
import views.html.employmentDetails

class EmploymentDetailsViewSpec extends YesNoViewBehaviours with GuiceOneAppPerSuite {

  private val messageKeyPrefix = "employmentDetails"
  private val hintTextKey = "employmentDetails.hintText"
  private val fakeEmployments = Seq(Employment("AVIVA PENSIONS", "754", "AZ00070"))
  private val taxYear = CustomTaxYear(2017)

  override val form = new BooleanForm()()
  val employmentDetails: employmentDetails = fakeApplication.injector.instanceOf[employmentDetails]

  def createViewUsingForm = (form: Form[_]) => employmentDetails(frontendAppConfig, form, NormalMode, fakeEmployments, taxYear)(fakeRequest, messages, templateRenderer, ec)

  def createView = () => employmentDetails(frontendAppConfig, form, NormalMode, fakeEmployments, taxYear)(fakeRequest, messages, templateRenderer, ec)

  "EmploymentDetails view" must {

    behave like normalPage(createView, messageKeyPrefix, None, taxYear.asString(messages))

    behave like pageWithBackLink(createView)

    behave like pageWithSecondaryHeader(createView, messages("site.service_name.with_tax_year", taxYear.asString(messages)))

    behave like yesNoPage(
      createView = createViewUsingForm,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = routes.EmploymentDetailsController.onSubmit(NormalMode).url,
      expectedHintTextKey = Some(hintTextKey)
    )

    "contain a table" in {
      val doc = asDocument(createViewUsingForm(form))
      val table = doc.getElementsByTag("table")
      table.size mustBe 1
    }

    "contains correct employer name inside of table" in {
      val doc = asDocument(createViewUsingForm(form))
      val employerName = doc.getElementsByTag("th").eachText.contains("AVIVA PENSIONS")
      employerName mustBe true
    }

    "contains correct staff number inside of table" in {
      val doc = asDocument(createViewUsingForm(form))
      val staffNumber = doc.getElementsByTag("td").eachText.contains("AZ00070")
      staffNumber mustBe true
    }
  }
}
