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
import play.api.data.{Form, FormError}

trait Constraints {

  def firstError[A](constraints: Constraint[A]*): Constraint[A] =
    Constraint {
      instance =>
        constraints
          .map(_.apply(instance))
          .find(_ != Valid)
          .getOrElse(Valid)
    }

  def nonEmpty(error: String = "error.required"): Constraint[String] =
    Constraint {
      case "" =>
        Invalid(error)
      case _ =>
        Valid
    }

  def maxLength(maxLength: Int, error: String = "error.maxLength"): Constraint[String] =
    Constraint {
      case str if str.length > maxLength =>
        Invalid(error, maxLength)
      case _ =>
        Valid
    }

  def opt[A](implicit f: Formatter[A]): Formatter[Option[A]] =
    new Formatter[Option[A]] {

      override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], Option[A]] = {
        data.get(key) match {
          case Some("") | None => Right(None)
          case _ => f.bind(key, data).right.map(Some.apply)
        }
      }

      override def unbind(key: String, value: Option[A]): Map[String, String] = {
        value.map {
          a => f.unbind(key, a)
        }
      }.getOrElse(Map.empty)
    }

  val form: Form[String] = {

    import play.api.data.Forms._
    import play.api.data._

    Form("foo" -> text.verifying(firstError(nonEmpty(), maxLength(20))))
  }
}