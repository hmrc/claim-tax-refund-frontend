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

import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.mockito.MockitoSugar

class BenefitsSpec extends AnyWordSpecLike with Matchers with MockitoSugar {

    "Benefits model" must {
      "Return map in correct order" in {

        Benefits.sortedBenefits.head mustBe Benefits.BEREAVEMENT_ALLOWANCE
        Benefits.sortedBenefits(1) mustBe Benefits.CARERS_ALLOWANCE
        Benefits.sortedBenefits(2) mustBe Benefits.JOBSEEKERS_ALLOWANCE
        Benefits.sortedBenefits(3) mustBe Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE
        Benefits.sortedBenefits(4) mustBe Benefits.INCAPACITY_BENEFIT
        Benefits.sortedBenefits(5) mustBe Benefits.STATE_PENSION
        Benefits.sortedBenefits(6) mustBe Benefits.OTHER_TAXABLE_BENEFIT
      }
    }
}
