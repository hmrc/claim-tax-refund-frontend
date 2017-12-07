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
import models.{MandatoryField, RegexField}
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form

class NationalInsuranceNumberFormSpec extends FormBehaviours with MockitoSugar {

  private val errorKeyInvalid = "nationalInsuranceNumber.invalid"
  private val errorKeyBlank = "nationalInsuranceNumber.blank"
  private val testRegex = """^((?!BG)(?!GB)(?!NK)(?!KN)(?!TN)(?!NT)(?!ZZ)(?:[A-CEGHJ-PR-TW-Z][A-CEGHJ-NPR-TW-Z])(?:\d){6}([A-D]|\s)?)|(\d{2})([a-zA-Z])(\d{5})([a-zA-Z])$"""

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    when(instance.ninoRegex) thenReturn testRegex
    instance
  }

  val validData: Map[String, String] = Map("value" -> "AB123456A")

  override val form: Form[_] = new NationalInsuranceNumberForm(appConfig)()

  "NationalInsuranceNumber Form" must {

    behave like questionForm("AB123456A")

    behave like formWithMandatoryTextFields(MandatoryField("value", errorKeyBlank))

    behave like formWithRegex(RegexField("value", errorKeyInvalid, testRegex))
  }
}
