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
import identifiers._
import models.{Benefits, NormalMode}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

class BenefitsNavigatorSpec extends SpecBase with MockitoSugar {

  val navigator = new Navigator

  "BenefitsNavigator" when {
    "in normal mode" when {
      "Navigating from SelectBenefits" must {
        "go to HowMuchBereavementAllowance when bereavement-allowance checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectBenefits) thenReturn Some(
            Seq(
              Benefits.BEREAVEMENT_ALLOWANCE,
              Benefits.CARERS_ALLOWANCE,
              Benefits.JOBSEEKERS_ALLOWANCE,
              Benefits.INCAPACITY_BENEFIT,
              Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
              Benefits.STATE_PENSION,
              Benefits.OTHER_TAXABLE_BENEFIT
            )
          )

          navigator.nextPage(SelectBenefitsId, NormalMode)(answers) mustBe routes.HowMuchBereavementAllowanceController.onPageLoad(NormalMode)
        }

        "go to HowMuchCarersAllowance when carers-allowance checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectBenefits) thenReturn Some(
            Seq(
              Benefits.CARERS_ALLOWANCE,
              Benefits.JOBSEEKERS_ALLOWANCE,
              Benefits.INCAPACITY_BENEFIT,
              Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
              Benefits.STATE_PENSION,
              Benefits.OTHER_TAXABLE_BENEFIT
            )
          )

          navigator.nextPage(SelectBenefitsId, NormalMode)(answers) mustBe routes.HowMuchCarersAllowanceController.onPageLoad(NormalMode)
        }

        "go to HowMuchJobseekersAllowance when jobseekers-allowance checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectBenefits) thenReturn Some(
            Seq(
              Benefits.JOBSEEKERS_ALLOWANCE,
              Benefits.INCAPACITY_BENEFIT,
              Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
              Benefits.STATE_PENSION,
              Benefits.OTHER_TAXABLE_BENEFIT
            )
          )

          navigator.nextPage(SelectBenefitsId, NormalMode)(answers) mustBe routes.HowMuchJobseekersAllowanceController.onPageLoad(NormalMode)
        }

        "go to HowMuchIncapacityBenefit when incapacity-benefit checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectBenefits) thenReturn Some(
            Seq(
              Benefits.INCAPACITY_BENEFIT,
              Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
              Benefits.STATE_PENSION,
              Benefits.OTHER_TAXABLE_BENEFIT
            )
          )

          navigator.nextPage(SelectBenefitsId, NormalMode)(answers) mustBe routes.HowMuchIncapacityBenefitController.onPageLoad(NormalMode)
        }

        "go to HowMuchEmploymentAndSupport when employment-and-support-allowance checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectBenefits) thenReturn Some(
            Seq(
              Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
              Benefits.STATE_PENSION,
              Benefits.OTHER_TAXABLE_BENEFIT
            )
          )

          navigator.nextPage(SelectBenefitsId, NormalMode)(answers) mustBe routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(NormalMode)
        }

        "go to HowMuchStatePension when state-pension checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectBenefits) thenReturn Some(
            Seq(
              Benefits.STATE_PENSION,
              Benefits.OTHER_TAXABLE_BENEFIT
            )
          )

          navigator.nextPage(SelectBenefitsId, NormalMode)(answers) mustBe routes.HowMuchStatePensionController.onPageLoad(NormalMode)
        }

        "go to OtherTaxableBenefitsName when other-taxable-benefit checkbox is the first answer selected" in {
          val answers = mock[UserAnswers]

          when(answers.selectBenefits) thenReturn Some(
            Seq(
              Benefits.OTHER_TAXABLE_BENEFIT
            )
          )

          navigator.nextPage(SelectBenefitsId, NormalMode)(answers) mustBe routes.OtherTaxableBenefitsNameController.onPageLoad(NormalMode)
        }
      }

      "Navigating from HowMuchBereavementAllowance" must {
        "go to HowMuchCarersAllowance if this option was selected on SelectBenefits" in {
          val answers = mock[UserAnswers]
          when(answers.selectBenefits) thenReturn Some(Seq(Benefits.CARERS_ALLOWANCE))
          navigator.nextPage(HowMuchBereavementAllowanceId, NormalMode)(answers) mustBe routes.HowMuchCarersAllowanceController.onPageLoad(NormalMode)
        }

        "go to HowMuchJobseekersAllowance if this option was selected on SelectBenefits" in {
          val answers = mock[UserAnswers]
          when(answers.selectBenefits) thenReturn Some(Seq(Benefits.JOBSEEKERS_ALLOWANCE))
          navigator.nextPage(HowMuchBereavementAllowanceId, NormalMode)(answers) mustBe routes.HowMuchJobseekersAllowanceController.onPageLoad(NormalMode)
        }

        "go to HowMuchIncapacityBenefit if this option was selected on SelectBenefits" in {
          val answers = mock[UserAnswers]
          when(answers.selectBenefits) thenReturn Some(Seq(Benefits.INCAPACITY_BENEFIT))
          navigator.nextPage(HowMuchBereavementAllowanceId, NormalMode)(answers) mustBe routes.HowMuchIncapacityBenefitController.onPageLoad(NormalMode)
        }

        "go to HowMuchEmploymentAndSupportAllowance if this option was selected on SelectBenefits" in {
          val answers = mock[UserAnswers]
          when(answers.selectBenefits) thenReturn Some(Seq(Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE))
          navigator.nextPage(HowMuchBereavementAllowanceId, NormalMode)(answers) mustBe routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(NormalMode)
        }

        "go to HowMuchStatePension if this option was selected on SelectBenefits" in {
          val answers = mock[UserAnswers]
          when(answers.selectBenefits) thenReturn Some(Seq(Benefits.STATE_PENSION))
          navigator.nextPage(HowMuchBereavementAllowanceId, NormalMode)(answers) mustBe routes.HowMuchStatePensionController.onPageLoad(NormalMode)
        }

        "go to OtherTaxableBenefitsName if this option was selected on SelectBenefits" in {
          val answers = mock[UserAnswers]
          when(answers.selectBenefits) thenReturn Some(Seq(Benefits.OTHER_TAXABLE_BENEFIT))
          navigator.nextPage(HowMuchBereavementAllowanceId, NormalMode)(answers) mustBe routes.OtherTaxableBenefitsNameController.onPageLoad(NormalMode)
        }

      }

      "go to AnyCompanyBenefits once all benefits have been completed" in {
        val answers = mock[UserAnswers]
        when(answers.selectBenefits) thenReturn Some(Seq(
          Benefits.BEREAVEMENT_ALLOWANCE,
          Benefits.CARERS_ALLOWANCE,
          Benefits.JOBSEEKERS_ALLOWANCE,
          Benefits.INCAPACITY_BENEFIT,
          Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
          Benefits.STATE_PENSION
        ))
        navigator.nextPage(HowMuchStatePensionId, NormalMode)(answers) mustBe routes.AnyCompanyBenefitsController.onPageLoad(NormalMode)
      }

      "go to SessionExpired if answers.selectBenefits is None on SelectBenefits page" in {
        val answers = mock[UserAnswers]
        when(answers.selectBenefits) thenReturn None
        navigator.nextPage(SelectBenefitsId, NormalMode)(answers) mustBe routes.SessionExpiredController.onPageLoad()
      }

      "go to SessionExpired if answers.selectBenefits is None on other benefit pages" in {
        val answers = mock[UserAnswers]
        when(answers.selectBenefits) thenReturn None
        navigator.nextPage(HowMuchBereavementAllowanceId, NormalMode)(answers) mustBe routes.SessionExpiredController.onPageLoad()
      }

      // onwards route from OtherTaxableBenefit always follows the same pattern

      "go to HowMuchOtherTaxableBenefit from OtherTaxableBenefitsName" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(OtherTaxableBenefitsNameId, NormalMode)(answers) mustBe routes.HowMuchOtherTaxableBenefitController.onPageLoad(NormalMode)
      }

      "go to AnyOtherTaxableBenefits from HowMuchOtherTaxableBenefit" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(HowMuchOtherTaxableBenefitId, NormalMode)(answers) mustBe routes.AnyOtherTaxableBenefitsController.onPageLoad(NormalMode)
      }

      "go to AnyCompanyBenefits from AnyOtherTaxableBenefits when answer is no" in {
        val answers = mock[UserAnswers]
        when(answers.anyOtherTaxableBenefits) thenReturn Some(false)
        navigator.nextPage(AnyOtherTaxableBenefitsId, NormalMode)(answers) mustBe routes.AnyCompanyBenefitsController.onPageLoad(NormalMode)
      }

      "go to OtherTaxableBenefitsName from AnyOtherTaxableBenefits when answer is yes" in {
        val answers = mock[UserAnswers]
        when(answers.anyOtherTaxableBenefits) thenReturn Some(true)
        navigator.nextPage(AnyOtherTaxableBenefitsId, NormalMode)(answers) mustBe routes.OtherTaxableBenefitsNameController.onPageLoad(NormalMode)
      }
    }
  }
}
