/*
 * Copyright 2017 HM Revenue & Customs
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
import models.MaxLengthField
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import play.api.data.Form

class FullNameFormSpec extends FormBehaviours with MockitoSugar {

  val errorKeyBlank = "fullName.blank"
  val maxLength = 35

  def appConfig: FrontendAppConfig = {
        val instance = mock[FrontendAppConfig]
        when(instance.fullNameLength) thenReturn maxLength
        instance
     }

  val validData: Map[String, String] = Map("value" -> "test answer")

  override val form: Form[_] = new FullNameForm(appConfig)()

  "FullName Form" must {

    behave like formWithMaxLengthTextFields(MaxLengthField("value", errorKeyBlank, maxLength))

    behave like formWithMandatoryTextFieldsAndCustomKey(("value", errorKeyBlank))
  }
}
