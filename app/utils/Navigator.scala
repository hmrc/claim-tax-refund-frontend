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
import models.{CheckMode, Mode, NormalMode}

@Singleton
class Navigator @Inject()() {

  private val routeMap: Map[Identifier, UserAnswers => Call] = Map(
    FullNameId -> (_ => routes.NationalInsuranceNumberController.onPageLoad(NormalMode)),
    NationalInsuranceNumberId -> (_ => routes.IsTheAddressInTheUKController.onPageLoad(NormalMode)),
    IsTheAddressInTheUKId -> isAddressInUkRoute,
    UkAddressId -> (_ =>  routes.TelephoneNumberController.onPageLoad(NormalMode)),
    InternationalAddressId -> (_ => routes.TelephoneNumberController.onPageLoad(NormalMode)),
    TelephoneNumberId -> (_ => routes.AreYouSelfAssessedController.onPageLoad(NormalMode))
  )

  private val editRouteMap: Map[Identifier, UserAnswers => Call] = Map(

  )

  //TODO: Add international address route to false
  private def isAddressInUkRoute(userAnswers: UserAnswers) = userAnswers.isTheAddressInTheUK match {
    case Some(true) => routes.UkAddressController.onPageLoad(NormalMode)
    case Some(false) => routes.InternationalAddressController.onPageLoad(NormalMode)
    case None => routes.SessionExpiredController.onPageLoad()
  }

  def nextPage(id: Identifier, mode: Mode): UserAnswers => Call = mode match {
    case NormalMode =>
      routeMap.getOrElse(id, _ => routes.IndexController.onPageLoad())
    case CheckMode =>
      editRouteMap.getOrElse(id, _ => routes.CheckYourAnswersController.onPageLoad())
  }
}
