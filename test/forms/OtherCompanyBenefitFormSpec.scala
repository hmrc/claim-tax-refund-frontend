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
import models.{Index, OtherCompanyBenefit}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.data.{Form, FormError}
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class OtherCompanyBenefitFormSpec extends FormBehaviours with MockitoSugar {

  private val currencyRegex = """(?=.)^\$?(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+)?(\.[0-9]{1,2})?$"""
  val nameKeyBlank = "otherCompanyBenefit.name.blank"
  val amountKeyBlank = "otherCompanyBenefit.amount.blank"
  val amountKeyInvalid = "otherCompanyBenefit.amount.invalid"
  val duplicateBenefitKey = "otherCompanyBenefit.duplicate"

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    when(instance.currencyRegex) thenReturn currencyRegex
    instance
  }

  val validData: Map[String, String] = Map(
    "name" -> "qwerty",
    "amount" -> "123"
  )

  override val form: Form[OtherCompanyBenefit] = new OtherCompanyBenefitForm(messagesApi, appConfig)(Seq.empty, 0)

  def otherCompanyBenefitForm(otherBenefits: Seq[OtherCompanyBenefit], index: Index): Form[OtherCompanyBenefit] =
    new OtherCompanyBenefitForm(messagesApi, appConfig)(otherBenefits, index)

  "OtherCompanyBenefitsName Form" must {

    "bind successfully with valid name and amount" in {
      val result: Form[OtherCompanyBenefit] = otherCompanyBenefitForm(Seq(OtherCompanyBenefit("qwerty", "123")), 0).bind(validData)
      result.errors.size shouldBe 0
      result.get shouldBe OtherCompanyBenefit("qwerty", "123")
    }

    "fail to bind with missing name" in {
      val result: Form[OtherCompanyBenefit] = form.bind(Map("amount" -> "123"))
      result.errors.size shouldBe 1
      result.errors shouldBe Seq(FormError("name", nameKeyBlank))
    }

    "fail to bind if name is duplicate" in {
      val result: Form[OtherCompanyBenefit] =
        otherCompanyBenefitForm(Seq(OtherCompanyBenefit("qwerty", "123")), 1).bind(validData)

      result.errors.size shouldBe 1
      result.errors shouldBe Seq(FormError("name", duplicateBenefitKey, Seq("qwerty")))
    }

    "fail to bind with missing amount" in {
      val result: Form[OtherCompanyBenefit] = form.bind(Map("name" -> "qwerty"))
      result.errors.size shouldBe 1
      result.errors shouldBe Seq(FormError("amount", amountKeyBlank))
    }

    "fail to bind with invalid amount" in {
      val result: Form[OtherCompanyBenefit] = form.bind(Map("name" -> "qwerty", "amount" -> "qwerty"))
      result.errors.size shouldBe 1
      result.errors shouldBe Seq(FormError("amount", amountKeyInvalid))
    }
  }
}