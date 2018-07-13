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
import models.{CheckMode, CompanyBenefits, NormalMode}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._

class CompanyBenefitsNavigatorSpec extends SpecBase with MockitoSugar {

  private val navigator = new Navigator
  private val answers = MockUserAnswers.nothingAnswered

  "CompanyBenefitsNavigator" when {
    "in normal mode" when {
      "go to SelectAnyCompanyBenefits from AnyCompanyBenefits when Yes is selected" in {
        when(answers.anyCompanyBenefits) thenReturn Some(true)
        navigator.nextPage(AnyCompanyBenefitsId, NormalMode)(answers) mustBe routes.SelectCompanyBenefitsController.onPageLoad(NormalMode)
      }

      "Navigating from SelectCompanyBenefits" must {
        "go to COMPANY_CAR_BENEFIT when company-car-benefit checkbox is the first answer selected" in {
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
          when(answers.selectCompanyBenefits) thenReturn Some(
            Seq(
              CompanyBenefits.MEDICAL_BENEFIT,
              CompanyBenefits.OTHER_COMPANY_BENEFIT
            )
          )

          navigator.nextPage(SelectCompanyBenefitsId, NormalMode)(answers) mustBe routes.HowMuchMedicalBenefitsController.onPageLoad(NormalMode)
        }

        "go to OTHER_COMPANY_BENEFIT when other-company-benefit checkbox is the first answer selected" in {
          when(answers.selectCompanyBenefits) thenReturn Some(
            Seq(
              CompanyBenefits.OTHER_COMPANY_BENEFIT
            )
          )

          navigator.nextPage(SelectCompanyBenefitsId, NormalMode)(answers) mustBe routes.OtherCompanyBenefitsNameController.onPageLoad(NormalMode)
        }

        "go to AnyTaxableIncome when all benefits checked and amounts provided" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          navigator.nextPage(SelectCompanyBenefitsId, NormalMode)(answers) mustBe routes.AnyTaxableIncomeController.onPageLoad(NormalMode)
        }
      }

      "Navigating from HowMuchHowMuchCarBenefits" must {
        "go to HowMuchFuelBenefit if this option was selected on SelectCompanyBenefit" in {
          when(answers.selectCompanyBenefits) thenReturn Some(Seq(CompanyBenefits.FUEL_BENEFIT))
          navigator.nextPage(HowMuchCarBenefitsId, NormalMode)(answers) mustBe routes.HowMuchFuelBenefitController.onPageLoad(NormalMode)
        }

        "go to HowMuchMedicalBenefit if this option was selected on SelectCompanyBenefit" in {
          when(answers.selectCompanyBenefits) thenReturn Some(Seq(CompanyBenefits.MEDICAL_BENEFIT))
          navigator.nextPage(HowMuchCarBenefitsId, NormalMode)(answers) mustBe routes.HowMuchMedicalBenefitsController.onPageLoad(NormalMode)
        }

        "go to OtherCompanyBenefitsName if this option was selected on SelectCompanyBenefit" in {
          when(answers.selectCompanyBenefits) thenReturn Some(Seq(CompanyBenefits.OTHER_COMPANY_BENEFIT))
          navigator.nextPage(HowMuchCarBenefitsId, NormalMode)(answers) mustBe routes.OtherCompanyBenefitsNameController.onPageLoad(NormalMode)
        }
      }

      // onwards route from OtherCompanyBenefitsName always follows the same pattern

      "go to HowMuchOtherCompanyBenefit from OtherCompanyBenefitsName" in {
        navigator.nextPage(OtherCompanyBenefitsNameId, NormalMode)(answers) mustBe routes.HowMuchOtherCompanyBenefitController.onPageLoad(NormalMode)
      }

      "go to AnyOtherCompanyBenefits from HowMuchOtherCompanyBenefit" in {
        navigator.nextPage(HowMuchOtherCompanyBenefitId, NormalMode)(answers) mustBe routes.AnyOtherCompanyBenefitsController.onPageLoad(NormalMode)
      }

      "go to AnyTaxableIncome from AnyOtherCompanyBenefits when answer is no" in {
        when(answers.anyOtherCompanyBenefits) thenReturn Some(false)
        navigator.nextPage(AnyOtherCompanyBenefitsId, NormalMode)(answers) mustBe routes.AnyTaxableIncomeController.onPageLoad(NormalMode)
      }

      "go to OtherCompanyBenefitsName from AnyOtherCompanyBenefits when answer is yes" in {
        when(answers.anyOtherCompanyBenefits) thenReturn Some(true)
        navigator.nextPage(AnyOtherCompanyBenefitsId, NormalMode)(answers) mustBe routes.OtherCompanyBenefitsNameController.onPageLoad(NormalMode)
      }
    }

    "in check mode" when {
      "go to CheckYourAnswersController from AnyCompanyBenefits when Yes is selected and company benefits section complete" in {
        val answers = MockUserAnswers.companyBenefitsUserAnswers
        when(answers.anyCompanyBenefits) thenReturn Some(true)
        navigator.nextPage(AnyCompanyBenefitsId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      "go to SelectCompanyBenefits from AnyCompanyBenefits when Yes is selected and no company benefits selected" in {
        val answers = MockUserAnswers.companyBenefitsUserAnswers
        when(answers.anyCompanyBenefits) thenReturn Some(true)
        when(answers.selectCompanyBenefits) thenReturn None
        navigator.nextPage(AnyCompanyBenefitsId, CheckMode)(answers) mustBe routes.SelectCompanyBenefitsController.onPageLoad(CheckMode)
      }

      "go to CheckYourAnswersController from AnyCompanyBenefits when No is selected" in {
        val answers = MockUserAnswers.companyBenefitsUserAnswers
        when(answers.anyCompanyBenefits) thenReturn Some(false)
        navigator.nextPage(AnyCompanyBenefitsId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      "Navigating from SelectCompanyBenefits" must {
        "go to CheckYourAnswers when all answer selected and all have an associated amount" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          navigator.nextPage(SelectCompanyBenefitsId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }

        "go to HowMuchCarBenefits when company car benefit selected but no associated amount" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          when(answers.howMuchCarBenefits) thenReturn None

          navigator.nextPage(SelectCompanyBenefitsId, CheckMode)(answers) mustBe routes.HowMuchCarBenefitsController.onPageLoad(CheckMode)
        }

        "go to HowMuchMedicalBenefits when medical benefit selected but no associated amount" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          when(answers.howMuchMedicalBenefits) thenReturn None

          navigator.nextPage(SelectCompanyBenefitsId, CheckMode)(answers) mustBe routes.HowMuchMedicalBenefitsController.onPageLoad(CheckMode)
        }

        "go to HowMuchFuelBenefit when fuel benefit selected but no associated amount" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          when(answers.howMuchFuelBenefit) thenReturn None

          navigator.nextPage(SelectCompanyBenefitsId, CheckMode)(answers) mustBe routes.HowMuchFuelBenefitController.onPageLoad(CheckMode)
        }

        "go to OtherCompanyBenefitsName when other company benefit selected but no associated name and amount" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          when(answers.otherCompanyBenefitsName) thenReturn None

          navigator.nextPage(SelectCompanyBenefitsId, CheckMode)(answers) mustBe routes.OtherCompanyBenefitsNameController.onPageLoad(CheckMode)
        }
      }

      "Navigating from HowMuchCarBenefits" must {
        "go to CheckYourAnswersController when all selected benefits have associated amounts" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          navigator.nextPage(HowMuchCarBenefitsId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }

        "go to HowMuchFuelBenefit when fuel benefit selected without an associated amount" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          when(answers.howMuchFuelBenefit) thenReturn None

          navigator.nextPage(HowMuchCarBenefitsId, CheckMode)(answers) mustBe routes.HowMuchFuelBenefitController.onPageLoad(CheckMode)
        }

        "go to HowMuchMedicalBenefits when medical benefit selected without an associated amount" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          when(answers.howMuchMedicalBenefits) thenReturn None

          navigator.nextPage(HowMuchCarBenefitsId, CheckMode)(answers) mustBe routes.HowMuchMedicalBenefitsController.onPageLoad(CheckMode)
        }

        "go to OtherCompanyBenefitsName when other company benefit selected without an associated name and amount" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          when(answers.otherCompanyBenefitsName) thenReturn None

          navigator.nextPage(HowMuchCarBenefitsId, CheckMode)(answers) mustBe routes.OtherCompanyBenefitsNameController.onPageLoad(CheckMode)
        }
      }

      "Navigating from HowMuchFuelBenefit" must {
        "go to CheckYourAnswersController when all selected benefits have associated amounts" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          navigator.nextPage(HowMuchFuelBenefitId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }

        "go to HowMuchMedicalBenefits when medical benefit selected without an associated amount" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          when(answers.howMuchMedicalBenefits) thenReturn None

          navigator.nextPage(HowMuchFuelBenefitId, CheckMode)(answers) mustBe routes.HowMuchMedicalBenefitsController.onPageLoad(CheckMode)
        }

        "go to OtherCompanyBenefitsName when other company benefit selected without an associated name and amount" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          when(answers.otherCompanyBenefitsName) thenReturn None

          navigator.nextPage(HowMuchFuelBenefitId, CheckMode)(answers) mustBe routes.OtherCompanyBenefitsNameController.onPageLoad(CheckMode)
        }
      }

      "Navigating from HowMuchMedicalBenefits" must {
        "go to CheckYourAnswersController when all selected benefits have associated amounts" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          navigator.nextPage(HowMuchMedicalBenefitsId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }

        "go to OtherCompanyBenefitsName when other company benefit selected without an associated name and amount" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          when(answers.otherCompanyBenefitsName) thenReturn None

          navigator.nextPage(HowMuchMedicalBenefitsId, CheckMode)(answers) mustBe routes.OtherCompanyBenefitsNameController.onPageLoad(CheckMode)
        }
      }

      "Navigating from OtherCompanyBenefitsName" must {
        "go to CheckYourAnswersController when name and amount stored" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          navigator.nextPage(OtherCompanyBenefitsNameId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }

        "go to HowMuchOtherCompanyBenefit when no amount stored" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          when(answers.howMuchOtherCompanyBenefit) thenReturn None

          navigator.nextPage(OtherCompanyBenefitsNameId, CheckMode)(answers) mustBe routes.HowMuchOtherCompanyBenefitController.onPageLoad(CheckMode)
        }
      }

      "Navigating from HowMuchOtherCompanyBenefit" must {
        "go to CheckYourAnswersController when amount stored" in {
          val answers = MockUserAnswers.companyBenefitsUserAnswers
          navigator.nextPage(HowMuchOtherCompanyBenefitId, CheckMode)(answers) mustBe routes.CheckYourAnswersController.onPageLoad()
        }
      }
    }
  }
}
