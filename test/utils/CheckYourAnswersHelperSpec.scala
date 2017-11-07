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
import org.mockito.Mockito.when
import org.scalatest.BeforeAndAfterEach
import org.scalatest.mockito.MockitoSugar

class CheckYourAnswersHelperSpec extends SpecBase with MockitoSugar with BeforeAndAfterEach {

  private var answers = mock[UserAnswers]

  override def beforeEach = {
    super.beforeEach()
    answers = MockUserAnswers.minimalValidUserAnswers
  }


  "employer name" must {

  }

  "is employer address in the UK" must {

  }

  "employer UK address" must {

  }

  "employer international address" must {

  }

  "employer telephone number" must {

  }

  "employer has contact email" must {

  }

  "employer contact email address" must {

  }

  "agent name" must {

  }

  "is agent address in the UK" must {

  }

  "agent UK address" must {

  }

  "agent international address" must {

  }

  "agent telephone number" must {

  }

  "agent has contact email" must {

  }

  "agent contact email address" must {

  }
}
