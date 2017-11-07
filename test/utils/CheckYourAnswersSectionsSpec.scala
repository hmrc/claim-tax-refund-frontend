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

package utils

import base.SpecBase
import models._
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach

class CheckYourAnswersSectionsSpec extends SpecBase with MockitoSugar with BeforeAndAfterEach {

  private var answers = mock[UserAnswers]

  override def beforeEach = {
    super.beforeEach()
    answers = MockUserAnswers.nothingAnswered
  }

  "sections to show" must {

  }

  "capacity section" must {

  }

  "employer section" when {

    "capacity is Personal Budget Holder Direct" must {

    }

    "capacity is Personal Budget Holder Agent" must {

    }

    "capacity is Company Employee or Director" must {

    }

    "capacity is Company Agent" must {

    }

    "all questions are answered with a UK address" must {
      "have the correct rows in the right order" in {

      }
    }

    "all questions are answered with an international address" must {
      "have the correct rows in the right order" in {

      }
    }

    "employer has contact email is answered as No" must {
      "show the row correctly" in {

      }
    }
  }

  "agent section" must {

    "have the correct title" in {

    }
  }

  "agent section" when {

    "all questions are answered with a UK address" must {
      "have the correct rows in the right order" in {

      }
    }

    "all questions are answered with an international address" must {
      "have the correct rows in the right order" in {

      }
    }

    "agent has contact email is answered as No" must {
      "show the row correctly" in {

      }
    }
  }
}
