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

import forms.mappings.AnyTaxPaidMapping
import models.AnyTaxPaid
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.FormError
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class HowMuchTaxPaidSpec extends FormSpec with AnyTaxPaidMapping with ScalaCheckPropertyChecks {

  val notSelectedKey = "Value not selected"
  val blankKey = "Value not entered"
  val invalidKey = "Invalid value"

  "AnyTaxPaid form" must {

    val form = new AnyTaxPaidForm()(notSelectedKey, blankKey, invalidKey)
    val anyTaxPaid = "anyTaxPaid"
    val taxPaidAmount = "taxPaidAmount"
    val testTaxAmount = "9,999.99"

    "bind successfully when yes and taxPaidAmount is valid" in {
      val result = form.bind(Map(anyTaxPaid -> "true", taxPaidAmount -> testTaxAmount))
      result.errors.size shouldBe 0
      result.get shouldBe AnyTaxPaid.Yes(testTaxAmount)
    }

    "bind successfully when no" in {
      val result = form.bind(Map(anyTaxPaid -> "false"))
      result.errors.size shouldBe 0
      result.get shouldBe AnyTaxPaid.No
    }

    "fail to bind" when {
      "yes is selected but taxPaidAmount is not provided" in {
        val result = form.bind(Map(anyTaxPaid -> "true"))
        result.errors shouldBe Seq(FormError(taxPaidAmount, blankKey))
      }
      "taxPaidAmount is invalid" in {
        val result = form.bind(Map(anyTaxPaid -> "true", taxPaidAmount -> "test"))
        result.errors shouldBe Seq(FormError(taxPaidAmount, invalidKey))
      }
    }

    "Successfully unbind 'anyTaxPaid.Yes'" in {
      val result = form.fill(AnyTaxPaid.Yes(testTaxAmount)).data
      result should contain(anyTaxPaid -> "true")
      result should contain(taxPaidAmount -> testTaxAmount)
    }
    "Successfully unbind 'anyTaxPaid.No'" in {
      val result = form.fill(AnyTaxPaid.No).data
      result should contain(anyTaxPaid -> "false")
    }
  }

}
