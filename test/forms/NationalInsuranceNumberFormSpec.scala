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

class NationalInsuranceNumberFormSpec extends FormSpec {

  val errorKeyBlank = "blank"
  val errorKeyInvalid = "nationalInsuranceNumber.invalid"

  "NationalInsuranceNumber Form" must {

    "bind a string when the standard national insurance number is valid" in {
      val form = NationalInsuranceNumberForm(errorKeyBlank).bind(Map("value" -> "AB123456A"))
      form.get shouldBe "AB123456A"
    }

    "bind a string when the temporary national insurance number is valid" in {
      val form = NationalInsuranceNumberForm(errorKeyBlank).bind(Map("value" -> "89A12345A"))
      form.get shouldBe "89A12345A"
    }

    "fail to bind an invalid national insurance number" in {
      val expectedError = error("value", errorKeyInvalid)
      checkForError(NationalInsuranceNumberForm(errorKeyInvalid), Map("value" -> "invalid"), expectedError)
    }

    "fail to bind a blank value" in {
      val expectedError = error("value", errorKeyBlank)
      checkForError(NationalInsuranceNumberForm(errorKeyBlank), Map("value" -> ""), expectedError)
    }

    "fail to bind when value is omitted" in {
      val expectedError = error("value", errorKeyBlank)
      checkForError(NationalInsuranceNumberForm(errorKeyBlank), emptyForm, expectedError)
    }
  }
}
