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
import models.{Index, OtherBenefit}
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form

class OtherBenefitFormSpec extends FormBehaviours with MockitoSugar {

  val form: Form[OtherBenefit] = new OtherBenefitForm(appConfig)(Seq(OtherBenefit("blah", "123")), Index(0))

  private val currencyRegex = """(?=.)^\$?(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+)?(\.[0-9]{1,2})?$"""
  val nameKeyBlank = "otherBenefit.name.blank"
  val amountKeyBlank = "otherBenefit.amount.blank"
  val amountKeyInvalid = "otherBenefit.amount.invalid"

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    when(instance.currencyRegex) thenReturn currencyRegex
    instance
  }

  val validData: Map[String, String] = Map(
    "name" -> "qwerty",
    "amount" -> "123"
  )

  "OtherBenefitsName Form" must {

    "bind successfully with valid name and amount" in {
      val result = form.bind(validData)

      result.errors.size shouldBe 0
      /*result.get shouldBe OtherBenefit.apply("qwerty", "123")*/
    }

  }
}
