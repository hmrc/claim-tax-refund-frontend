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

class TelephoneNumberFormSpec extends FormSpec {

  val errorKeyBlank = "telephoneNumber.blank"
  val errorKeyInvalid = "telephoneNumber.invalid"
  val testRegex = """^\+?[0-9\s\(\)]{1,20}$"""

  "TelephoneNumber Form" must {

    "bind a string when the standard telephone number is valid" in {
      val validNum = "0191 111 1111"
      val form = TelephoneNumberForm(testRegex).bind(Map("value" -> validNum))
      form.get shouldBe "0191 111 1111"
    }

    "bind a string when the standard telephone number is valid with brackets" in {
      val validNum = "(0191) 111 1111"
      val form = TelephoneNumberForm(testRegex).bind(Map("value" -> validNum))
      form.get shouldBe "(0191) 111 1111"
    }

    "bind a string when the standard telephone number is valid with Int code" in {
      val validNum = "+44191 111 1111"
      val form = TelephoneNumberForm(testRegex).bind(Map("value" -> validNum))
      form.get shouldBe "+44191 111 1111"
    }

    "bind a string when the international telephone number is valid" in {
      val validNum = "+14155552671"
      val form = TelephoneNumberForm(testRegex).bind(Map("value" -> validNum))
      form.get shouldBe "+14155552671"
    }

    "fail to bind an invalid telephone number" in {
      val expectedError = error("value", errorKeyInvalid)
      checkForError(TelephoneNumberForm(testRegex), Map("value" -> "invalid"), expectedError)
    }

    "bind a string" in {
      val form = TelephoneNumberForm(testRegex).bind(Map("value" -> "0191 111 1111"))
      form.get shouldBe "0191 111 1111"
    }

    "fail to bind a blank value" in {
      val expectedError = error("value", errorKeyBlank)
      checkForError(TelephoneNumberForm(testRegex), Map("value" -> ""), expectedError)
    }

    "fail to bind when value is omitted" in {
      val expectedError = error("value", errorKeyBlank)
      checkForError(TelephoneNumberForm(testRegex), emptyForm, expectedError)
    }
  }
}
