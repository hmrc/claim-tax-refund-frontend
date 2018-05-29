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
import org.joda.time.{DateTime, DateTimeUtils, LocalDateTime}
import org.scalacheck.Gen
import org.scalatest.prop.PropertyChecks
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import play.api.libs.json.{JsValue, Json}

class MetadataSpec extends WordSpec with MustMatchers with OptionValues with PropertyChecks {

  ".writes" must {

    "contain the correct fields" in {

      forAll(Gen.alphaNumStr) {
        customerId =>
          val metadata = Metadata(customerId)
          val json = Json.toJson(metadata)

          json mustEqual Json.obj(
            "customerId" -> metadata.customerId,
            "hmrcReceivedAt" -> metadata.hmrcReceivedAt.toString,
            "xmlCreatedAt" -> metadata.xmlCreatedAt.toString,
            "submissionReference" -> metadata.submissionReference,
            "reconciliationId" -> metadata.reconciliationId,
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
    }
  }

  ".reads" must {
    "be successfully parsed" in {
      val metadata = new Metadata("Test meta")
      val fakeJson = FakeJson
      val json = Json.toJson(metadata)
      fakeJson mustBe(json)
    }
  }

  def FakeJson: JsValue =
    Json.obj(
      "customerId" -> "Test meta",
      "hmrcReceivedAt" -> LocalDateTime.now.toString,
      "xmlCreatedAt" -> LocalDateTime.now.toString,
      "submissionReference" -> LocalDateTime.now.toString("ssMMyyddmmHH"),
      "reconciliationId" -> LocalDateTime.now.toString("ssMMyyddmmHH"),
      "fileFormat" -> "pdf",
      "mimeType" -> "application/pdf",
      "casKey" -> "",
      "submissionMark" -> "",
      "attachmentCount" -> 0,
      "numberOfPages" -> 2,
      "formId" -> "",
      "businessArea" -> "",
      "classificationType" -> "",
      "source" -> "",
      "target" -> "",
      "store" -> true,
      "robotXml" -> false
    )
}

