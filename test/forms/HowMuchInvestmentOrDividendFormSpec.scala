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
import models.{MandatoryField, RegexField}
import org.scalatestplus.mockito.MockitoSugar
import org.mockito.Mockito._
import play.api.data.Form

class HowMuchInvestmentOrDividendFormSpec extends FormBehaviours with MockitoSugar {

  private val testRegex = """(?=.)^\$?(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+)?(\.[0-9]{1,2})?$"""
  private val errorKeyInvalid = "howMuchInvestmentOrDividend.invalid"
  private val errorKeyBlank = "howMuchInvestmentOrDividend.blank"

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    when(instance.currencyRegex) thenReturn testRegex
    instance
  }

  val validData: Map[String, String] = Map("value" -> "9,999.99")

  override val form: Form[_] = new HowMuchInvestmentOrDividendForm(appConfig)()

  "HowMuchInvestmentOrDividend Form" must {

    behave like questionForm("""9,999.99""")

    behave like formWithMandatoryTextFields(MandatoryField("value", errorKeyBlank))

    behave like formWithRegex(RegexField("value", errorKeyInvalid, testRegex))
  }
}
