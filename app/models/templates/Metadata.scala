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

package models.templates

import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Metadata(customerId: String = "", hmrcReceivedAt: LocalDateTime = LocalDateTime.now, xmlCreatedAt: LocalDateTime = LocalDateTime.now) {

  val timeStamp: String = xmlCreatedAt.toString("ssMMyyddmmHH")
  val fileFormat: String = "pdf"
  val mimeType: String = "application/pdf"

  val casKey: String = ""
  val submissionMark: String = ""
  val attachmentCount: Int = 0
  val numberOfPages: Int = 2

  val formId: String = ""

  val businessArea: String = ""
  val classificationType: String = ""
  val source: String = ""
  val target: String = ""
  val store: Boolean = true
  val robotXml: Boolean = false
}


object Metadata {

  implicit val writes: Writes[Metadata] =
    Writes {
      metadata =>
        Json.obj(
          "customerId" -> metadata.customerId,
          "hmrcReceivedAt" -> metadata.hmrcReceivedAt.toString,
          "xmlCreatedAt" -> metadata.xmlCreatedAt.toString,
          "submissionReference" -> metadata.timeStamp,
          "reconciliationId" -> metadata.timeStamp,
          "fileFormat" -> metadata.fileFormat,
          "mimeType" -> metadata.mimeType,
          "casKey" -> metadata.casKey,
          "submissionMark" -> metadata.submissionMark,
          "attachmentCount" -> metadata.attachmentCount,
          "numberOfPages" -> metadata.numberOfPages,
          "formId" -> metadata.formId,
          "businessArea" -> metadata.businessArea,
          "classificationType" -> metadata.classificationType,
          "source" -> metadata.source,
          "target" -> metadata.target,
          "store" -> metadata.store,
          "robotXml" -> metadata.robotXml
        )
    }

  implicit def reads: Reads[Metadata] = (
      (__ \ "customerId").read[String] and
      (__ \ "hmrcReceivedAt").read[LocalDateTime](jodaDateReads) and
      (__ \ "xmlCreatedAt").read[LocalDateTime](jodaDateReads)
    )(apply _)

  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ"

  val jodaDateReads: Reads[LocalDateTime] = Reads[LocalDateTime](js =>
    js.validate[String].map[LocalDateTime](dtString =>
      LocalDateTime.parse(dtString)
    )
  )
 }
