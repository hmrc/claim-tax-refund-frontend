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

package utils

import base.SpecBase
import controllers.routes
import identifiers.{HowMuchCarBenefitsId, SelectCompanyBenefitsId}
import models.{CompanyBenefits, NormalMode}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._

class CompanyBenefitsNavigatorSpec extends SpecBase with MockitoSugar {

  val navigator = new Navigator

  "CompanyBenefitsNavigator" when {
    "in normal mode" when {
      "Navigating from SelectCompanyBenefits" must {
        "go to COMPANY_CAR_BENEFIT when company-car-benefit checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectCompanyBenefits) thenReturn Some(
            Seq(
              CompanyBenefits.COMPANY_CAR_BENEFIT,
              CompanyBenefits.FUEL_BENEFIT,
              CompanyBenefits.MEDICAL_BENEFIT,
              CompanyBenefits.OTHER_COMPANY_BENEFIT
            )
          )

          navigator.nextPage(SelectCompanyBenefitsId, NormalMode)(answers) mustBe routes.HowMuchCarBenefitsController.onPageLoad(NormalMode)
        }

        "go to FUEL_BENEFIT when fuel-benefit checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectCompanyBenefits) thenReturn Some(
            Seq(
              CompanyBenefits.FUEL_BENEFIT,
              CompanyBenefits.MEDICAL_BENEFIT,
              CompanyBenefits.OTHER_COMPANY_BENEFIT
            )
          )

          navigator.nextPage(SelectCompanyBenefitsId, NormalMode)(answers) mustBe routes.HowMuchFuelBenefitController.onPageLoad(NormalMode)
        }

        "go to MEDICAL_BENEFIT when medical-benefit checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectCompanyBenefits) thenReturn Some(
            Seq(
              CompanyBenefits.MEDICAL_BENEFIT,
              CompanyBenefits.OTHER_COMPANY_BENEFIT
            )
          )

          navigator.nextPage(SelectCompanyBenefitsId, NormalMode)(answers) mustBe routes.HowMuchMedicalBenefitsController.onPageLoad(NormalMode)
        }

        "go to OTHER_COMPANY_BENEFIT when other-company-benefit checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectCompanyBenefits) thenReturn Some(
            Seq(
              CompanyBenefits.OTHER_COMPANY_BENEFIT
            )
          )

          navigator.nextPage(SelectCompanyBenefitsId, NormalMode)(answers) mustBe routes.OtherCompanyBenefitsNameController.onPageLoad(NormalMode)
        }

        "Navigating from HowMuchHowMuchCarBenefits" must {
          "go to HowMuchFuelBenefit if this option was selected on SelectCompanyBenefit" in {
            val answers = mock[UserAnswers]
            when(answers.selectCompanyBenefits) thenReturn Some(Seq(CompanyBenefits.FUEL_BENEFIT))
            navigator.nextPage(HowMuchCarBenefitsId, NormalMode)(answers) mustBe routes.HowMuchFuelBenefitController.onPageLoad(NormalMode)
          }

          "go to HowMuchMedicalBenefit if this option was selected on SelectCompanyBenefit" in {
            val answers = mock[UserAnswers]
            when(answers.selectCompanyBenefits) thenReturn Some(Seq(CompanyBenefits.MEDICAL_BENEFIT))
            navigator.nextPage(HowMuchCarBenefitsId, NormalMode)(answers) mustBe routes.HowMuchMedicalBenefitsController.onPageLoad(NormalMode)
          }

          "go to OtherCompanyBenefitsName if this option was selected on SelectCompanyBenefit" in {
            val answers = mock[UserAnswers]
            when(answers.selectCompanyBenefits) thenReturn Some(Seq(CompanyBenefits.OTHER_COMPANY_BENEFIT))
            navigator.nextPage(HowMuchCarBenefitsId, NormalMode)(answers) mustBe routes.OtherCompanyBenefitsNameController.onPageLoad(NormalMode)
          }
        }
      }
    }
  }
}
