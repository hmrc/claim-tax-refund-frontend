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
import org.mockito.Mockito._
import utils.MockUserAnswers

class SubmissionSpec extends SpecBase {

  val NA = "N/A"
  val answers = MockUserAnswers.nothingAnswered

  ".apply" must {

    "build " in {
      when(answers.selectTaxYear) thenReturn Some(SelectTaxYear.CYMinus2)
      val submission = Submission(answers)

      submission mustBe Submission(
        SelectTaxYear.CYMinus2)
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
      val submission = Submission(answers)

      Submission.asMap(submission) mustBe Map(
        SelectTaxYearId.toString -> "current-year-minus-2"
      )
    }
  }
}
