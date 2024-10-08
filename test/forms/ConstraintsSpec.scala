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

import forms.mappings.Constraints
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatest.{EitherValues, OptionValues}
import play.api.data.validation.{Invalid, Valid}

class ConstraintsSpec extends AnyWordSpecLike with Matchers with EitherValues with OptionValues with Constraints {

  ".firstError" must {

    "return Valid when all constraints pass" in {

      val result = firstError(maxLength(10), nonEmpty(), regexValidation("""^\w+$"""))("foo")
      result mustEqual Valid
    }

    "return Invalid when the first constraint fails" in {

      val result = firstError(maxLength(10), nonEmpty())("a" * 11)
      result mustEqual Invalid("error.maxLength", 10)
    }

    "return Invalid when the second constraint fails" in {

      val result = firstError(maxLength(10), nonEmpty())("")
      result mustEqual Invalid("error.required")
    }

    "return Invalid for the first error when both constraints fail" in {

      val result = firstError(maxLength(-1), nonEmpty())("")
      result mustEqual Invalid("error.maxLength", -1)
    }
  }

  "regex" must {

    "return Valid for matching regex" in {
      val result = regexValidation("""^\w+$""")("foo")
      result mustEqual Valid
    }

    "return Invalid for none matching regex" in {
      val result = regexValidation("""^[abc]$""")("foo")
      result mustEqual Invalid("error.invalid")
    }
  }

  ".opt" must {

    import play.api.data.format.Formats._

    ".bind" must {

      "return None for an empty string" in {
        val result = opt[String].bind("foo", Map("foo" -> ""))
        result.value mustBe None
      }

      "return None for a missing key" in {
        val result = opt[String].bind("foo", Map.empty)
        result.value mustBe None
      }

      "return Some for a valid string" in {
        val result = opt[String].bind("foo", Map("foo" -> "bar"))
        result.value.value mustEqual "bar"
      }
    }

    ".unbind" must {

      "return an empty map when the value is None" in {
        opt[String].unbind("foo", None) must be(empty)
      }

      "return a map with the relevant data" in {
        opt[String].unbind("foo", Some("bar")) must contain("foo" -> "bar")
      }
    }
  }
}
