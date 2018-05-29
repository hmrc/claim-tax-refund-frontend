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

import base.SpecBase
import identifiers.SelectTaxYearId
import models.templates.Metadata
import org.joda.time.{DateTime, DateTimeUtils, LocalDateTime}
import org.mockito.Mockito._
import play.api.libs.json.Json
import utils.MockUserAnswers

class SubmissionSpec extends SpecBase {

  val NA = "N/A"
  val answers = MockUserAnswers.minimalValidUserAnswers
  val submission = Submission(answers)
  val taxYear = SelectTaxYear.CYMinus2
  val timeNow = new LocalDateTime
  val testMetadata = new Metadata("test_case", timeNow, timeNow)


  ".apply" must {

    "build " in {
      when(answers.selectTaxYear) thenReturn Some(SelectTaxYear.CYMinus2)


      val result = Submission(taxYear.asString, "<html>Test result</html>", Json.toJson(testMetadata).toString)

      val fakeSubmission = Submission(answers.selectTaxYear.get.asString, "<html>Test result</html>", Json.toJson(testMetadata).toString)

      fakeSubmission mustBe result
    }


    "fail to build" in {
      val exception = intercept[IllegalArgumentException] {
        val submission = Submission(MockUserAnswers.nothingAnswered)
      }

      exception.getMessage mustBe "requirement failed: Tax year was not answered"
    }
  }

  ".asMap" must {

    "return a map" in {
      when(answers.selectTaxYear) thenReturn Some(SelectTaxYear.CYMinus2)
      val submission = Submission (answers.selectTaxYear.get.asString, "<html>Test result</html>", Json.toJson(testMetadata).toString)

      Submission.asMap(submission) mustBe Map(
        SelectTaxYearId.toString -> taxYear.asString,
        "pdfHtml" -> "<html>Test result</html>",
        "metaData" -> Json.toJson(testMetadata).toString
      )
    }
  }

  "Submission data must " must {

    "contain correct tax year" in {
      assert(submission.toString.contains(taxYear.asString))
    }

    "contain expected keys for backend" in {
      val result = Json.toJson(submission)
      assert(result.toString.contains("pdfHtml"))
    }

    "contain metaData" in {
      val result = Json.toJson(submission)
      assert(result.toString.contains("metadata"))
    }

    "contain keys in metadata" in {
      val result = Json.toJson(submission).toString

      assert(result.contains("metadata"))
      assert(result.contains("hmrcReceivedAt"))
      assert(result.contains("xmlCreatedAt"))
      assert(result.contains("submissionReference"))
      assert(result.contains("reconciliationId"))
      assert(result.contains("fileFormat"))
      assert(result.contains("mimeType"))
      assert(result.contains("casKey"))
      assert(result.contains("submissionMark"))
      assert(result.contains("attachmentCount"))
      assert(result.contains("numberOfPages"))
      assert(result.contains("formId"))
      assert(result.contains("businessArea"))
      assert(result.contains("classificationType"))
      assert(result.contains("source"))
      assert(result.contains("target"))
      assert(result.contains("store"))
    }
  }
}
