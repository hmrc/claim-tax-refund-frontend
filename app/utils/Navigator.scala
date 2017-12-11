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

import javax.inject.{Inject, Singleton}

import play.api.mvc.Call
import controllers.routes
import identifiers._
import models.FullOrPartialClaim.{OptionAll, OptionSome}
import models.TypeOfClaim.{OptionPAYE, OptionSA}
import models.{CheckMode, Mode, NormalMode}

@Singleton
class Navigator @Inject()() {

  private val routeMap: Map[Identifier, UserAnswers => Call] = Map(
    FullNameId -> (_ => routes.NationalInsuranceNumberController.onPageLoad(NormalMode)),
    NationalInsuranceNumberId -> (_ => routes.IsTheAddressInTheUKController.onPageLoad(NormalMode)),
    IsTheAddressInTheUKId -> isAddressInUkRoute,
    UkAddressId -> (_ =>  routes.TelephoneNumberController.onPageLoad(NormalMode)),
    InternationalAddressId -> (_ => routes.TelephoneNumberController.onPageLoad(NormalMode)),
    TelephoneNumberId -> (_ => routes.TypeOfClaimController.onPageLoad(NormalMode)),
    TypeOfClaimId -> typeOfClaim,
    UniqueTaxpayerReferenceId -> (_ => routes.FullOrPartialClaimController.onPageLoad(NormalMode)),
    PayAsYouEarnId -> (_ => routes.SelectTaxYearController.onPageLoad(NormalMode)),
    SelectTaxYearId -> (_ => routes.AnyBenefitsController.onPageLoad(NormalMode)),
    FullOrPartialClaimId -> fullOrPartialClaim,
    AnyBenefitsId -> anyBenefits
  )

  private val editRouteMap: Map[Identifier, UserAnswers => Call] = Map(

  )

  private def isAddressInUkRoute(userAnswers: UserAnswers) = userAnswers.isTheAddressInTheUK match {
    case Some(true) => routes.UkAddressController.onPageLoad(NormalMode)
    case Some(false) => routes.InternationalAddressController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def typeOfClaim(userAnswers: UserAnswers) = userAnswers.typeOfClaim match {
    case Some(OptionSA) => routes.UniqueTaxpayerReferenceController.onPageLoad(NormalMode)
    case Some(OptionPAYE) => routes.PayAsYouEarnController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def fullOrPartialClaim(userAnswers: UserAnswers) = userAnswers.fullOrPartialClaim match {
    case Some(OptionSome) => routes.PartialClaimAmountController.onPageLoad(NormalMode)
    case Some(OptionAll) => ???
    case None => routes.SessionExpiredController.onPageLoad()
  }

  private def anyBenefits(userAnswers: UserAnswers) = userAnswers.anyBenefits match {
    case Some(true) => ???
    case Some(false) => routes.OtherIncomeController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  def nextPage(id: Identifier, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode =>
      routeMap.getOrElse(id, _ => routes.IndexController.onPageLoad())
    case CheckMode =>
      editRouteMap.getOrElse(id, _ => routes.CheckYourAnswersController.onPageLoad())
  }
}
