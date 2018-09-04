/*
 * Copyright 2018 HM Revenue & Customs
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

package models

import models.templates.Metadata
import play.api.libs.json.{Format, Json}
import utils.UserAnswers

case class Submission(pdf: String, metadata: String, xml: String)

object Submission {

  implicit val format: Format[Submission] = Json.format[Submission]

  def apply(answers: UserAnswers): Submission = {

    require(answers.pdf.isDefined, "PDF has not been created")
    require(answers.xml.isDefined, "XML has not been created")
    require(answers.metadata.isDefined, new Metadata("Metadata has not been created"))

    val meta = Json.toJson(answers.metadata).toString()
    val pdf = answers.pdf.getOrElse("Failed to get PDF")
    val xml = answers.xml.getOrElse("Failed to get XML")

    Submission(pdf, meta, xml)
  }

  def asMap(e: Submission): Map[String, String] = {
    Map(
      "pdf" -> e.pdf,
      "metaData" -> e.metadata.toString,
      "xml" -> e.xml
    )
  }
}
