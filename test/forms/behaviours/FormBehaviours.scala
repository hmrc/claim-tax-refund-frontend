/*
 * Copyright 2022 HM Revenue & Customs
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

package forms.behaviours

import config.FrontendAppConfig
import forms.FormSpec
import models.{MandatoryField, MaxLengthField, RegexField}
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.Injector
import play.api.test.FakeRequest
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

trait FormBehaviours extends FormSpec with GuiceOneAppPerSuite {

  val validData: Map[String, String]

  val form: Form[_]

  def injector: Injector = app.injector

  def frontendAppConfig: FrontendAppConfig = injector.instanceOf[FrontendAppConfig]

  def messagesApi: MessagesApi = injector.instanceOf[MessagesApi]

  def fakeRequest = FakeRequest("", "")

  def messages: Messages = messagesApi.preferred(fakeRequest)

  def questionForm[A](expectedResult: A) = {
    "bind valid values correctly" in {
      val boundForm = form.bind(validData)
      boundForm.get shouldBe expectedResult
    }
  }

  def formWithOptionalTextFields(fields: String*) = {
    for (field <- fields) {
      s"bind when $field is omitted" in {
        val data = validData - field
        val boundForm = form.bind(data)
        boundForm.errors.isEmpty shouldBe true
      }
    }
  }

  def formWithMaxLengthTextFields(fields: MaxLengthField*) = {
    for (field <- fields) {
      s"fail to bind when ${field.fieldName} has more characters than ${field.maxLength}" in {
        val invalid = "A" * (field.maxLength + 1)
        val data = validData + (field.fieldName -> invalid)
        val expectedError = error(field.fieldName, field.errorMessageKey, field.maxLength)
        checkForError(form, data, expectedError)
      }
    }
  }

  def formWithRegex(fields: RegexField*) = {
    for (field <- fields) {
      s"fail regex validation ${field.regexString}" in {
        val invalid = "."
        val data = validData + (field.fieldName -> invalid)
        val expectedError = error(field.fieldName, field.errorMessageKey)
        checkForError(form, data, expectedError)
      }
    }
  }

  def formWithMandatoryTextFields(fields: MandatoryField*) = {
    for (field <- fields) {
      s"fail to bind when ${field.fieldName} is omitted" in {
        val data = validData + (field.fieldName -> "")
        val expectedError = error(field.fieldName, field.errorMessageKey)
        checkForError(form, data, expectedError)
      }
    }
  }

  def formWithConditionallyMandatoryField(booleanField: String, field: String) = {
    s"bind when $booleanField is false and $field is omitted" in {
      val data = validData + (booleanField -> "false") - field
      val boundForm = form.bind(data)
      boundForm.errors.isEmpty shouldBe true
    }

    s"fail to bind when $booleanField is true and $field is omitted" in {
      val data = validData + (booleanField -> "true") - field
      val expectedError = error(field, "error.required")
      checkForError(form, data, expectedError)
    }
  }

  def formWithBooleans(fields: String*) = {
    for (field <- fields) {
      s"fail to bind when $field is omitted" in {
        val data = validData - field
        val expectedError = error(field, "error.boolean")
        checkForError(form, data, expectedError)
      }

      s"fail to bind when $field is invalid" in {
        val data = validData + (field -> "invalid value")
        val expectedError = error(field, "error.boolean")
        checkForError(form, data, expectedError)
      }
    }
  }

  def formWithOptionField(field: String, errorKeyBlank: String, validValues: String*) = {
    for (validValue <- validValues) {
      s"bind when $field is set to $validValue" in {
        val data = validData + (field -> validValue)
        val boundForm = form.bind(data)
        boundForm.errors.isEmpty shouldBe true
      }
    }

    s"fail to bind when $field is omitted" in {
      val data = validData - field
      val expectedError = error(field, errorKeyBlank)
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field is invalid" in {
      val data = validData + (field -> "invalid value")
      val expectedError = error(field, "error.unknown")
      checkForError(form, data, expectedError)
    }
  }

  def formWithDateField(field: String) = {
    s"fail to bind when $field day is omitted" in {
      val data = validData - s"$field.day"
      val expectedError = error(s"$field.day", "error.date.day_blank")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field day is 0" in {
      val data = validData + (s"$field.day" -> "0")
      val expectedError = error(s"$field.day", "error.date.day_invalid")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field day is greater than 31" in {
      val data = validData + (s"$field.day" -> "32")
      val expectedError = error(s"$field.day", "error.date.day_invalid")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field day is negative" in {
      val data = validData + (s"$field.day" -> "-1")
      val expectedError = error(s"$field.day", "error.date.day_invalid")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field day is non-numeric" in {
      val data = validData + (s"$field.day" -> "invalid")
      val expectedError = error(s"$field.day", "error.date.day_invalid")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field month is omitted" in {
      val data = validData - s"$field.month"
      val expectedError = error(s"$field.month", "error.date.month_blank")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field month is 0" in {
      val data = validData + (s"$field.month" -> "0")
      val expectedError = error(s"$field.month", "error.date.month_invalid")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field month is greater than 12" in {
      val data = validData + (s"$field.month" -> "13")
      val expectedError = error(s"$field.month", "error.date.month_invalid")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field month is negative" in {
      val data = validData + (s"$field.month" -> "-1")
      val expectedError = error(s"$field.month", "error.date.month_invalid")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field month is non-numeric" in {
      val data = validData + (s"$field.month" -> "invalid")
      val expectedError = error(s"$field.month", "error.date.month_invalid")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field year is omitted" in {
      val data = validData - s"$field.year"
      val expectedError = error(s"$field.year", "error.date.year_blank")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field year is 0" in {
      val data = validData + (s"$field.year" -> "0")
      val expectedError = error(s"$field.year", "error.date.year_invalid")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field year is greater than 2050" in {
      val data = validData + (s"$field.year" -> "2051")
      val expectedError = error(s"$field.year", "error.date.year_invalid")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field year is negative" in {
      val data = validData + (s"$field.year" -> "-1")
      val expectedError = error(s"$field.year", "error.date.year_invalid")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when $field year is non-numeric" in {
      val data = validData + (s"$field.year" -> "invalid")
      val expectedError = error(s"$field.year", "error.date.year_invalid")
      checkForError(form, data, expectedError)
    }

    s"fail to bind when the $field is invalid" in {
      val data = validData + (s"$field.day" -> "30") + (s"$field.month" -> "2")
      val expectedError = error("dateOfBirth", "error.invalid_date")
      checkForError(form, data, expectedError)
    }

  }
}
