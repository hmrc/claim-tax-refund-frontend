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

package models

import base.SpecBase
import play.api.libs.json.Json

class SubmissionSpec extends SpecBase {

  private val testMetadata = "<xml><metadata></metadata></xml>"
  private val testPdf = "<html>Test</html>"
  private val testXml = "<xml><robot></robot></xml>"
  private val submission = Submission(testPdf, testMetadata, testXml)

  ".asMap" must {

    "return a map" in {
      Submission.asMap(submission) mustBe Map(
        "pdf" -> testPdf,
        "metaData" -> testMetadata,
        "xml" -> testXml
      )
    }
  }

  "Submission data must " must {
    "contain pdf, metadata and robotXml" in {
      val result = Json.toJson(submission)
      assert(result.toString.contains("pdf"))
      assert(result.toString.contains("metadata"))
      assert(result.toString.contains("xml"))
    }
  }
}
