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
import models.MandatoryField
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form

class OtherBenefitFormSpec extends FormBehaviours with MockitoSugar {

  val nameKeyBlank = "otherBenefit.name.blank"
  val amountKeyBlank = "otherBenefit.amount.blank"


  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    instance
  }

  val validData: Map[String, String] = Map(
    "name" -> "qwerty",
    "amount" -> "123"
  )

  val newForm = new OtherBenefitForm(appConfig)
  override val form: Form[_] = new OtherBenefitForm(appConfig)(Seq.empty, 0)

  "OtherBenefitsName Form" must {

    behave like formWithMandatoryTextFields(
      MandatoryField("name", nameKeyBlank),
      MandatoryField("amount", amountKeyBlank)
    )

    /*
    behave like formThatDoesNotAllowDuplicateValues(
      "2qwerty", Seq("1qwerty", "2qwerty", "3qwerty")
    )

    "fail to bind" when {
      "amount is invalid" in {

      }
    }*/
  }
}
