/*
 * Copyright 2020 HM Revenue & Customs
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

package utils

import play.api.libs.json._

trait Enumerable[A] {

  def values: Set[A]

  def harnessReads: PartialFunction[String, A]

  def withName(str: String): Option[A] =
    mappings.get(str)

  private lazy val mappings: Map[String, A] =
    values.map {
      value =>
        (value.toString, value)
    }.toMap

  implicit lazy val reads: Reads[A] =
    Reads {
      case JsString(str) if mappings.contains(str) =>
        JsSuccess(mappings(str))
      case JsString(str) if harnessReads.isDefinedAt(str) =>
        JsSuccess(harnessReads(str))
      case _ =>
        JsError("error.invalid")
    }

  implicit lazy val writes: Writes[A] =
    Writes(value => JsString(value.toString))

  implicit lazy val formats: Format[A] = Format(reads, writes)
}
