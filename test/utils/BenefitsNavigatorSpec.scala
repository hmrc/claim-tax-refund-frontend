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
import models.{Benefits, CheckMode, NormalMode, OtherBenefit}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar

class BenefitsNavigatorSpec extends SpecBase with MockitoSugar {

  val navigator = new Navigator

  "BenefitsNavigator" when {
    "in normal mode" when {
      "go to SelectBenefits page from AnyBenefits when answer is yes" in {
        val answers = MockUserAnswers.nothingAnswered
        when(answers.anyBenefits) thenReturn Some(true)
        navigator.nextPage(AnyBenefitsId, NormalMode)(answers) mustBe routes.SelectBenefitsController.onPageLoad(NormalMode)
      }

      "Navigating from SelectBenefits" must {
        "go to HowMuchBereavementAllowance when bereavement-allowance checkbox is the first answer selected" in {
          val answers = MockUserAnswers.nothingAnswered

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
          val answers = MockUserAnswers.nothingAnswered

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
          val answers = MockUserAnswers.nothingAnswered

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
          val answers = MockUserAnswers.nothingAnswered

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
          val answers = MockUserAnswers.nothingAnswered

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
          val answers = MockUserAnswers.nothingAnswered

          when(answers.selectBenefits) thenReturn Some(
            Seq(
              Benefits.STATE_PENSION,
              Benefits.OTHER_TAXABLE_BENEFIT
            )
          )

          navigator.nextPage(SelectBenefitsId, NormalMode)(answers) mustBe routes.HowMuchStatePensionController.onPageLoad(NormalMode)
        }

        "go to OtherBenefitsName when other-taxable-benefit checkbox is the first answer selected" in {
          val answers = MockUserAnswers.nothingAnswered

          when(answers.selectBenefits) thenReturn Some(
            Seq(
              Benefits.OTHER_TAXABLE_BENEFIT
            )
          )

          navigator.nextPage(SelectBenefitsId, NormalMode)(answers) mustBe routes.OtherBenefitController.onPageLoad(NormalMode, index = 0)
        }

        "go to AnyCompanyBenefits when all benefits checked and amounts provided" in {
          val answers = MockUserAnswers.benefitsUserAnswers
          navigator.nextPage(SelectBenefitsId, NormalMode)(answers) mustBe routes.AnyCompanyBenefitsController.onPageLoad(NormalMode)
        }
      }

      "Navigating from HowMuchBereavementAllowance" must {
        "go to HowMuchCarersAllowance if this option was selected on SelectBenefits" in {
          val answers = MockUserAnswers.nothingAnswered
          when(answers.selectBenefits) thenReturn Some(Seq(Benefits.CARERS_ALLOWANCE))
          navigator.nextPage(HowMuchBereavementAllowanceId, NormalMode)(answers) mustBe routes.HowMuchCarersAllowanceController.onPageLoad(NormalMode)
        }

        "go to HowMuchJobseekersAllowance if this option was selected on SelectBenefits" in {
          val answers = MockUserAnswers.nothingAnswered
          when(answers.selectBenefits) thenReturn Some(Seq(Benefits.JOBSEEKERS_ALLOWANCE))
          navigator.nextPage(HowMuchBereavementAllowanceId, NormalMode)(answers) mustBe routes.HowMuchJobseekersAllowanceController.onPageLoad(NormalMode)
        }

        "go to HowMuchIncapacityBenefit if this option was selected on SelectBenefits" in {
          val answers = MockUserAnswers.nothingAnswered
          when(answers.selectBenefits) thenReturn Some(Seq(Benefits.INCAPACITY_BENEFIT))
          navigator.nextPage(HowMuchBereavementAllowanceId, NormalMode)(answers) mustBe routes.HowMuchIncapacityBenefitController.onPageLoad(NormalMode)
        }

        "go to HowMuchEmploymentAndSupportAllowance if this option was selected on SelectBenefits" in {
          val answers = MockUserAnswers.nothingAnswered
          when(answers.selectBenefits) thenReturn Some(Seq(Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE))
          navigator.nextPage(HowMuchBereavementAllowanceId, NormalMode)(answers) mustBe routes.HowMuchEmploymentAndSupportAllowanceController.onPageLoad(NormalMode)
        }

        "go to HowMuchStatePension if this option was selected on SelectBenefits" in {
          val answers = MockUserAnswers.nothingAnswered
          when(answers.selectBenefits) thenReturn Some(Seq(Benefits.STATE_PENSION))
          navigator.nextPage(HowMuchBereavementAllowanceId, NormalMode)(answers) mustBe routes.HowMuchStatePensionController.onPageLoad(NormalMode)
        }

        "go to OtherBenefitsName if this option was selected on SelectBenefits" in {
          val answers = MockUserAnswers.nothingAnswered
          when(answers.selectBenefits) thenReturn Some(Seq(Benefits.OTHER_TAXABLE_BENEFIT))
          navigator.nextPage(HowMuchBereavementAllowanceId, NormalMode)(answers) mustBe routes.OtherBenefitController.onPageLoad(NormalMode, index = 0)
        }

      }

      "go to AnyCompanyBenefits once all benefits have been completed" in {
        val answers = MockUserAnswers.nothingAnswered
        when(answers.selectBenefits) thenReturn Some(Seq(
          Benefits.BEREAVEMENT_ALLOWANCE,
          Benefits.CARERS_ALLOWANCE,
          Benefits.JOBSEEKERS_ALLOWANCE,
          Benefits.INCAPACITY_BENEFIT,
          Benefits.EMPLOYMENT_AND_SUPPORT_ALLOWANCE,
          Benefits.STATE_PENSION
        ))
        when(answers.howMuchBereavementAllowance) thenReturn Some ("122.44")
        when(answers.howMuchCarersAllowance) thenReturn Some ("122.44")
        when(answers.howMuchJobseekersAllowance) thenReturn Some ("122.44")
        when(answers.howMuchIncapacityBenefit) thenReturn Some ("122.44")
        when(answers.howMuchEmploymentAndSupportAllowance) thenReturn Some ("122.44")
        when(answers.howMuchStatePension) thenReturn Some ("122.44")
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

      // onwards route from OtherBenefitsName always follows the same pattern

      "go to AnyOtherBenefits from OtherBenefitsName" in {
        val answers = mock[UserAnswers]
        navigator.nextPage(OtherBenefitId, NormalMode)(answers) mustBe routes.AnyOtherBenefitsController.onPageLoad(NormalMode)
      }

      "go to AnyCompanyBenefits from AnyOtherBenefits when answer is no" in {
        val answers = mock[UserAnswers]
        when(answers.anyOtherBenefits) thenReturn Some(false)
        navigator.nextPage(AnyOtherBenefitsId, NormalMode)(answers) mustBe routes.AnyCompanyBenefitsController.onPageLoad(NormalMode)
      }

      "go to OtherBenefitsName from AnyOtherBenefits when answer is yes" in {
        val answers = mock[UserAnswers]
        when(answers.anyOtherBenefits) thenReturn Some(true)
        when(answers.otherBenefit) thenReturn Some(Seq(OtherBenefit("qwerty", "123")))
        navigator.nextPage(AnyOtherBenefitsId, NormalMode)(answers) mustBe routes.OtherBenefitController.onPageLoad(NormalMode, index = 1)
      }
    }

    "when in checkMode" must {
      "when moving from 'AnyBenefitsController'" must {
        "take you to 'SelectBenefits' then selecting 'Yes'" in {
          val answers = MockUserAnswers.nothingAnswered
          when (answers.anyBenefits) thenReturn Some(true)
          navigator.nextPage(AnyBenefitsId, CheckMode)(answers) mustBe routes.SelectBenefitsController.onPageLoad(CheckMode)
        }

        "take you to 'CheckYourAnswers' then selecting 'No'" in {
          val answers = MockUserAnswers.nothingAnswered
          when (answers.anyBenefits) thenReturn Some(false)
          navigator.nextPage(AnyBenefitsId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }

        "take you to 'CheckYourAnswers' when not changing your answer" in {
          val answers = MockUserAnswers.nothingAnswered
          when (answers.anyBenefits) thenReturn Some (true)
          when (answers.selectBenefits) thenReturn Some(Seq(
            Benefits.CARERS_ALLOWANCE
          ))
          when(answers.howMuchCarersAllowance) thenReturn Some ("112.44")
          navigator.nextPage(HowMuchCarersAllowanceId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }

        "take you to SelectBenefits from AnyBenefits when Yes is selected and no company benefits selected" in {
          val answers = MockUserAnswers.benefitsUserAnswers
          when(answers.anyBenefits) thenReturn Some(true)
          when(answers.selectBenefits) thenReturn None
          navigator.nextPage(AnyBenefitsId, CheckMode)(answers) mustBe routes.SelectBenefitsController.onPageLoad(CheckMode)
        }
      }

      "when moving from 'SelectBenefits'" must {
        "go to 'HowMuchBereavement' when 'BEREAVEMENT_ALLOWANCE' selected and HowMuch is empty" in {
          val answers = MockUserAnswers.nothingAnswered
          when(answers.selectBenefits) thenReturn Some(Seq(
            Benefits.BEREAVEMENT_ALLOWANCE
          ))
          navigator.nextPage(SelectBenefitsId, CheckMode)(answers) mustBe routes.HowMuchBereavementAllowanceController.onPageLoad(CheckMode)
          when (answers.howMuchBereavementAllowance) thenReturn Some("112.44")
          navigator.nextPage(SelectBenefitsId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }

        "go to 'CheckYourAnswers' when 'BEREAVEMENT_ALLOWANCE' selected and HowMuch is not empty" in {
          val answers = MockUserAnswers.nothingAnswered
          when (answers.selectBenefits) thenReturn Some(Seq(
            Benefits.BEREAVEMENT_ALLOWANCE
          ))
          when (answers.howMuchBereavementAllowance) thenReturn Some("112.44")
          navigator.nextPage(SelectBenefitsId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }

        "go to 'HowMuchCareers when 'BEREAVEMENT_ALLOWANCE' and 'CARERS_ALLOWANCE' but CAREERS is empty" in {
          val answers = MockUserAnswers.nothingAnswered
          when (answers.selectBenefits) thenReturn Some(Seq(
            Benefits.BEREAVEMENT_ALLOWANCE,
            Benefits.CARERS_ALLOWANCE
          ))
          when (answers.howMuchBereavementAllowance) thenReturn Some("112.44")
          navigator.nextPage(SelectBenefitsId, CheckMode)(answers) mustBe routes.HowMuchCarersAllowanceController.onPageLoad(CheckMode)
        }

        "go to 'HowMuchIncapacity' when all selected but HowMuchIncapcity is Empty " in {
          val answers = MockUserAnswers.nothingAnswered
          when(answers.selectBenefits) thenReturn Some (Benefits.sortedBenefits)
          when(answers.howMuchBereavementAllowance) thenReturn Some ("112.44")
          when(answers.howMuchCarersAllowance) thenReturn Some ("112.44")
          when(answers.howMuchJobseekersAllowance) thenReturn Some ("112.44")
          when(answers.howMuchEmploymentAndSupportAllowance) thenReturn Some ("112.44")
          when(answers.howMuchStatePension) thenReturn Some ("112.44")
          navigator.nextPage(SelectBenefitsId, CheckMode)(answers) mustBe routes.HowMuchIncapacityBenefitController.onPageLoad(CheckMode)
        }
      }

      "Navigating from OtherBenefitsName" must {
        "go to CheckYourAnswersController when name and amount stored" in {
          val answers = MockUserAnswers.benefitsUserAnswers
          navigator.nextPage(OtherBenefitId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }

        "go to CheckYourAnswersController when no amount stored" in {
          val answers = MockUserAnswers.benefitsUserAnswers
          when(answers.anyOtherBenefits) thenReturn None

          navigator.nextPage(OtherBenefitId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }
      }

      "Navigating from AnyOtherBenefit" must {
        "go to CheckYourAnswersController when amount stored" in {
          val answers = MockUserAnswers.benefitsUserAnswers
          navigator.nextPage(AnyOtherBenefitsId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }
      }
    }
  }
}
