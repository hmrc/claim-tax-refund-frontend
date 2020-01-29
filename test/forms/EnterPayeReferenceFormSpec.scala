/*
 * Copyright 2020 HM Revenue & Customs
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
import models.{MandatoryField, RegexField}
import org.scalatestplus.mockito.MockitoSugar
import play.api.data.Form

class EnterPayeReferenceFormSpec extends FormBehaviours with MockitoSugar {

  private val enterPayeReferenceBlankKey = "enterPayeReference.blank"
  private val enterPayeReferenceInvalid = "enterPayeReference.invalid"
  private val testRegex = """^[0-9]{3}\/[0-9A-Za-z]{1,13}$"""

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    instance
  }

  val validData: Map[String, String] = Map("value" -> "123/AB1234")

  override val form: Form[_] = new EnterPayeReferenceForm(appConfig)()

  "EnterPayeReference Form" must {

    behave like formWithMandatoryTextFields(MandatoryField("value", enterPayeReferenceBlankKey))

    behave like formWithRegex(RegexField("value", enterPayeReferenceInvalid, testRegex))

    behave like questionForm("123/AB1234")
  }
}
