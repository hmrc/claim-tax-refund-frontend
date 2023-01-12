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

package models

import org.joda.time.LocalDateTime
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import play.api.libs.json.{JsValue, Json}

import scala.xml._
import scala.xml.Utility._

class
MetadataSpec extends WordSpec with MustMatchers with OptionValues with ScalaCheckPropertyChecks {

  private val localDT = LocalDateTime.now()
  private val testMetadata: Metadata = new Metadata("AB123456", "123", "123", localDT, "", "en")
  private val testWelshMetadata: Metadata = new Metadata("AB123456", "123", "123", localDT, "", "cy")
  private val testXml: NodeSeq = Metadata.toXml(testMetadata)
  private val testWelshXml: NodeSeq = Metadata.toXml(testWelshMetadata)

  "metadata xml" must {
    "contain correct header" in {
      testXml \ "document" \ "header" \ "title" must contain(<title>{testMetadata.submissionRef}</title>)
      testXml \ "document" \ "header" \ "format" must contain(<format>pdf</format>)
      testXml \ "document" \ "header" \ "mime_type" must contain(<mime_type>application/pdf</mime_type>)
      testXml \ "document" \ "header" \ "store" must contain(<store>{true}</store>)
      testXml \ "document" \ "header" \ "source" must contain(<source>R39_EN</source>)
      testXml \ "document" \ "header" \ "target" must contain(<target>DMS</target>)
      testXml \ "document" \ "header" \ "reconciliation_id" must contain(<reconciliation_id>{testMetadata.submissionRef + "-" + localDT.toString("ssMMyyddmmHH")}</reconciliation_id>)
    }

    "contain correct metadata xml for hmrc_time_of_receipt" in {
      testXml \ "document" \ "metadata" \ "attribute" must contain(
        trim(
          <attribute>
            <attribute_name>hmrc_time_of_receipt</attribute_name>
            <attribute_type>time</attribute_type>
            <attribute_values>
              <attribute_value>{testMetadata.timeStamp.toString("dd/MM/yyyy HH:mm:ss")}</attribute_value>
            </attribute_values>
          </attribute>
        )
      )
    }

    "contain correct metadata xml for time_xml_created" in {
      testXml \ "document" \ "metadata" \ "attribute" must contain(
        trim(
          <attribute>
            <attribute_name>time_xml_created</attribute_name>
            <attribute_type>time</attribute_type>
            <attribute_values>
              <attribute_value>{testMetadata.timeStamp.toString("dd/MM/yyyy HH:mm:ss")}</attribute_value>
            </attribute_values>
          </attribute>
        )
      )
    }

    "contain correct metadata xml for submission_reference" in {
      testXml \ "document" \ "metadata" \ "attribute" must contain(
        trim(
          <attribute>
            <attribute_name>submission_reference</attribute_name>
            <attribute_type>string</attribute_type>
            <attribute_values>
              <attribute_value>{testMetadata.submissionRef}</attribute_value>
            </attribute_values>
          </attribute>
        )
      )
    }

    "contain correct metadata xml for form_id" in {
      testXml \ "document" \ "metadata" \ "attribute" must contain(
        trim(
          <attribute>
            <attribute_name>form_id</attribute_name>
            <attribute_type>string</attribute_type>
            <attribute_values>
              <attribute_value>{testMetadata.formId}</attribute_value>
            </attribute_values>
          </attribute>
        )
      )
    }

    "contain correct metadata xml for number_pages" in {
      testXml \ "document" \ "metadata" \ "attribute" must contain(
        trim(
          <attribute>
            <attribute_name>number_pages</attribute_name>
            <attribute_type>integer</attribute_type>
            <attribute_values>
              <attribute_value>{testMetadata.numberOfPages.toString}</attribute_value>
            </attribute_values>
          </attribute>
        )
      )
    }

    "contain correct metadata xml for source" in {
      testXml \ "document" \ "metadata" \ "attribute" must contain(
        trim(
          <attribute>
            <attribute_name>source</attribute_name>
            <attribute_type>string</attribute_type>
            <attribute_values>
              <attribute_value>{testMetadata.source}</attribute_value>
            </attribute_values>
          </attribute>
        )
      )
    }

    "contain correct metadata xml for customer_id" in {
      testXml \ "document" \ "metadata" \ "attribute" must contain(
        trim(
          <attribute>
            <attribute_name>customer_id</attribute_name>
            <attribute_type>string</attribute_type>
            <attribute_values>
              <attribute_value>{testMetadata.customerId}</attribute_value>
            </attribute_values>
          </attribute>
        )
      )
    }

    "contain correct metadata xml for submission_mark" in {
      testXml \ "document" \ "metadata" \ "attribute" must contain(
        trim(
          <attribute>
            <attribute_name>submission_mark</attribute_name>
            <attribute_type>string</attribute_type>
            <attribute_values>
              <attribute_value>{testMetadata.submissionMark}</attribute_value>
            </attribute_values>
          </attribute>
        )
      )
    }

    "contain correct metadata xml for cas_key" in {
      testXml \ "document" \ "metadata" \ "attribute" must contain(
        trim(
          <attribute>
            <attribute_name>cas_key</attribute_name>
            <attribute_type>string</attribute_type>
            <attribute_values>
              <attribute_value>{testMetadata.casKey}</attribute_value>
            </attribute_values>
          </attribute>
        )
      )
    }

    "contain correct metadata xml for classification_type" in {
      testXml \ "document" \ "metadata" \ "attribute" must contain(
        trim(
          <attribute>
            <attribute_name>classification_type</attribute_name>
            <attribute_type>string</attribute_type>
            <attribute_values>
              <attribute_value>{testMetadata.classificationType}</attribute_value>
            </attribute_values>
          </attribute>
        )
      )
    }

    "contain correct metadata xml for business_area" in {
      testXml \ "document" \ "metadata" \ "attribute" must contain(
        trim(
          <attribute>
            <attribute_name>business_area</attribute_name>
            <attribute_type>string</attribute_type>
            <attribute_values>
              <attribute_value>{testMetadata.businessArea}</attribute_value>
            </attribute_values>
          </attribute>
        )
      )
    }

    "contain correct metadata xml for attachment_count" in {
      testXml \ "document" \ "metadata" \ "attribute" must contain(
        trim(
          <attribute>
            <attribute_name>attachment_count</attribute_name>
            <attribute_type>int</attribute_type>
            <attribute_values>
              <attribute_value>{testMetadata.attachmentCount.toString}</attribute_value>
            </attribute_values>
          </attribute>
        )
      )
    }

    "contain correct metadata xml for classification_type when set to welsh" in {
      testWelshXml \ "document" \ "metadata" \ "attribute" must contain(
        trim(
          <attribute>
            <attribute_name>classification_type</attribute_name>
            <attribute_type>string</attribute_type>
            <attribute_values>
              <attribute_value>{testWelshMetadata.classificationType}</attribute_value>
            </attribute_values>
          </attribute>
        )
      )
    }

  }

  ".writes" must {
    "contain the correct fields" in {
      val json = Json.toJson(testMetadata)

      json mustEqual Json.obj(
        "customerId" -> testMetadata.customerId,
        "hmrcReceivedAt" -> testMetadata.timeStamp.toString,
        "xmlCreatedAt" -> testMetadata.timeStamp.toString,
        "submissionReference" -> testMetadata.submissionRef,
        "reconciliationId" -> testMetadata.formattedTimeStamp,
        "fileFormat" -> testMetadata.fileFormat,
        "mimeType" -> testMetadata.mimeType,
        "casKey" -> testMetadata.casKey,
        "submissionMark" -> testMetadata.submissionMark,
        "attachmentCount" -> testMetadata.attachmentCount,
        "numberOfPages" -> testMetadata.numberOfPages,
        "formId" -> testMetadata.formId,
        "businessArea" -> testMetadata.businessArea,
        "classificationType" -> testMetadata.classificationType,
        "source" -> testMetadata.source,
        "target" -> testMetadata.target,
        "store" -> testMetadata.store,
        "robotXml" -> testMetadata.robotXml
      )
    }
  }

  ".reads" must {
    "be successfully parsed" in {
      val fakeJson = jsonOutput
      val json = Json.toJson(testMetadata)
      fakeJson mustBe json
    }
  }

  def jsonOutput: JsValue =
    Json.obj(
      "customerId" -> "AB123456",
      "hmrcReceivedAt" -> localDT.toString,
      "xmlCreatedAt" -> localDT.toString,
      "submissionReference" -> testMetadata.submissionRef,
      "reconciliationId" -> localDT.toString("ssMMyyddmmHH"),
      "fileFormat" -> "pdf",
      "mimeType" -> "application/pdf",
      "casKey" -> "",
      "submissionMark" -> "123",
      "attachmentCount" -> 0,
      "numberOfPages" -> 2,
      "formId" -> "R39_EN",
      "businessArea" -> "PSA",
      "classificationType" -> "PSA-DFS Repayments",
      "source" -> "R39_EN",
      "target" -> "DMS",
      "store" -> true,
      "robotXml" -> true
    )
}

