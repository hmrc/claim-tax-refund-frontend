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

class TelephoneNumberFormSpec extends FormBehaviours with MockitoSugar {

  private val errorKeyInvalid = "telephoneNumber.invalid"
  private val testRegex = """^\+?[0-9\s\(\)]{1,20}$"""

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    when(instance.telephoneRegex) thenReturn testRegex
    instance
  }

  val validData: Map[String, String] = Map("value" -> "01963 123456")

  override val form: Form[_] = new TelephoneNumberForm(appConfig)()

  "TelephoneNumber Form" must {

    behave like questionForm("01963 123456")

    behave like formWithMandatoryTextFieldsAndCustomKey(("value", errorKeyInvalid))
  }
}
