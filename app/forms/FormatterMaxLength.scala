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

import play.api.data.format.Formatter
import play.api.data.validation.{Constraint, Invalid, Valid}

trait FormatterMaxLength extends FormErrorHelper {

  protected def maxLengthError: String = "error.maxLength"

  def maxLengthConstraint(maxLength: Int): Constraint[String] =
    Constraint {
      case str if str.length > maxLength =>
        Invalid(maxLengthError)
      case _ =>
        Valid
    }

  def formatterMaxLength(key: String, maxLength: Int) = new Formatter[String] {

    val errorKeyBlank = s"$key.blank"
    val errorKeyTooLong = s"$key.tooLong"

    def bind(key: String, data: Map[String, String]) = {
      data.get(key) match {
        case None => produceError(key, errorKeyBlank)
        case Some("") => produceError(key, errorKeyBlank)
        case Some(s) if s.length > maxLength => produceError(key, errorKeyTooLong)
        case Some(s) => Right(s)
      }
    }

    def unbind(key: String, value: String) = Map(key -> value)
  }
}
