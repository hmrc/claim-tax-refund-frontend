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
import models.templates.Metadata
import org.joda.time.LocalDateTime
import play.api.libs.json.Json
import utils.MockUserAnswers

class SubmissionSpec extends SpecBase {

  private val answers = MockUserAnswers.minimalValidUserAnswers
  private val submission = Submission(answers)
  private val timeNow = new LocalDateTime
  private val testMetadata = new Metadata("test_case", timeNow, timeNow)

  ".apply" must {

    "build " in {
      val result = Submission("<html>Test result</html>", Json.toJson(testMetadata).toString)

      val fakeSubmission = Submission("<html>Test result</html>", Json.toJson(testMetadata).toString)

      fakeSubmission mustBe result
    }
  }

  ".asMap" must {

    "return a map" in {
      val submission = Submission ("<html>Test result</html>", Json.toJson(testMetadata).toString)

      Submission.asMap(submission) mustBe Map(
        "pdfHtml" -> "<html>Test result</html>",
        "metaData" -> Json.toJson(testMetadata).toString
      )
    }
  }

  "Submission data must " must {

    "contain expected keys for backend" in {
      val result = Json.toJson(submission)
      assert(result.toString.contains("pdfHtml"))
    }

    "contain metadata" in {
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
