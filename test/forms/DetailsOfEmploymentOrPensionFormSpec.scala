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

package forms

import config.FrontendAppConfig
import forms.behaviours.FormBehaviours
import models.{MandatoryField, MaxLengthField}
import org.scalatestplus.mockito.MockitoSugar
import play.api.data.Form

class DetailsOfEmploymentOrPensionFormSpec extends FormBehaviours with MockitoSugar {

  val errorKeyBlank = "detailsOfEmploymentOrPension.blank"
  val errorTooLong = "detailsOfEmploymentOrPension.tooLong"
  val maxLength = 500

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    instance
  }

  val validData: Map[String, String] = Map("value" -> "This is some sample text, I 'allow' symbols and text including 1234567!£$%^&*()")

  override val form: Form[_] = new DetailsOfEmploymentOrPensionForm(appConfig)()

  "DetailsOfEmploymentOrPension Form" must {

    behave like formWithMandatoryTextFields(MandatoryField("value", errorKeyBlank))

    behave like formWithMaxLengthTextFields(MaxLengthField("value", errorTooLong, maxLength))

    behave like questionForm("This is some sample text, I 'allow' symbols and text including 1234567!£$%^&*()")

  }
}
