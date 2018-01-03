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

package forms

import config.FrontendAppConfig
import forms.behaviours.FormBehaviours
import models.{MandatoryField, MaxLengthField, RegexField}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import play.api.data.Form

class UniqueTaxpayerReferenceFormSpec extends FormBehaviours with MockitoSugar {

  private val testRegex = """^[0-9kK]{10}$"""
  private val errorKeyInvalid = "uniqueTaxpayerReference.invalid"
  private val errorKeyBlank = "uniqueTaxpayerReference.blank"

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    when(instance.utrRegex) thenReturn testRegex
    instance
  }

  val validData: Map[String, String] = Map("value" -> "1234567890")

  override val form: Form[_] = new UniqueTaxpayerReferenceForm(appConfig)()

  "UniqueTaxpayerReference Form" must {

    behave like questionForm("1234567890")

    behave like formWithMandatoryTextFields(MandatoryField("value", errorKeyBlank))

    behave like formWithRegex(RegexField("value", errorKeyInvalid, testRegex))
  }
}
