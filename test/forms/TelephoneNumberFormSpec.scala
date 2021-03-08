/*
 * Copyright 2021 HM Revenue & Customs
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

import forms.mappings.TelephoneOptionMapping
import models.TelephoneOption
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.{Form, FormError}

class TelephoneNumberFormSpec extends FormSpec with TelephoneOptionMapping with ScalaCheckPropertyChecks {

  "TelephoneNumber form" must {
    val form: Form[TelephoneOption] = new TelephoneNumberForm()()
    val anyTelephoneNumber = "anyTelephoneNumber"
    val telephoneNumber = "telephoneNumber"
    val testTelephoneNumber = "0191 1111 111"

    "bind successfully when yes and telephone is valid" in {
      val result = form.bind(Map(anyTelephoneNumber -> "true", telephoneNumber -> testTelephoneNumber))
      result.errors.size shouldBe 0
      result.get shouldBe TelephoneOption.Yes(testTelephoneNumber)
    }

    "bind successfully when no" in {
      val result = form.bind(Map(anyTelephoneNumber -> "false"))
      result.errors.size shouldBe 0
      result.get shouldBe TelephoneOption.No
    }

    "fail to bind" when {
      "yes is selected but telephone number is not provided" in {
        val result = form.bind(Map(anyTelephoneNumber -> "true"))
        result.errors shouldBe Seq(FormError(telephoneNumber, "telephoneNumber.blank"))
      }
      "Telephone is invalid" in {
        val result = form.bind(Map(anyTelephoneNumber -> "true", telephoneNumber -> "test"))
        result.errors shouldBe Seq(FormError(telephoneNumber, "telephoneNumber.invalid"))
      }
    }

    "Successfully unbind 'telephone.anyTelephoneNumber.Yes'" in {
      val result = form.fill(TelephoneOption.Yes(testTelephoneNumber)).data
      result should contain(anyTelephoneNumber -> "true")
      result should contain(telephoneNumber -> testTelephoneNumber)
    }
    "Successfully unbind 'telephone.anyTelephoneNumber.No'" in {
      val result = form.fill(TelephoneOption.No).data
      result should contain(anyTelephoneNumber -> "false")
    }
  }
}
