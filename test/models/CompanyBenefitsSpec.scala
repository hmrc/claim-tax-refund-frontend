/*
 * Copyright 2021 HM Revenue & Customs
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

import org.scalatest.{MustMatchers, WordSpec}
import org.scalatestplus.mockito.MockitoSugar

class CompanyBenefitsSpec extends WordSpec with MustMatchers with MockitoSugar {

  "CompanyBenefits model" must {
    "Return map in correct order" in {

      CompanyBenefits.sortedCompanyBenefits.head mustBe CompanyBenefits.COMPANY_CAR_BENEFIT
      CompanyBenefits.sortedCompanyBenefits(1) mustBe CompanyBenefits.FUEL_BENEFIT
      CompanyBenefits.sortedCompanyBenefits(2) mustBe CompanyBenefits.MEDICAL_BENEFIT
      CompanyBenefits.sortedCompanyBenefits(3) mustBe CompanyBenefits.OTHER_COMPANY_BENEFIT
    }
  }
}
