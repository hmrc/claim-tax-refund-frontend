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

import controllers.routes
import models.CheckMode
import viewmodels.{AnswerRow, RepeaterAnswerRow, RepeaterAnswerSection}

class CheckYourAnswersHelper(userAnswers: UserAnswers) {

  def payAsYouEarn: Option[AnswerRow] = userAnswers.payAsYouEarn map {
    x => AnswerRow("payAsYouEarn.checkYourAnswersLabel", s"$x", false, routes.PayAsYouEarnController.onPageLoad(CheckMode).url)
  }

  def uniqueTaxpayerReference: Option[AnswerRow] = userAnswers.uniqueTaxpayerReference map {
    x => AnswerRow("uniqueTaxpayerReference.checkYourAnswersLabel", s"$x", false, routes.UniqueTaxpayerReferenceController.onPageLoad(CheckMode).url)
  }

  def areYouSelfAssessed: Option[AnswerRow] = userAnswers.areYouSelfAssessed map {
    x => AnswerRow("areYouSelfAssessed.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.AreYouSelfAssessedController.onPageLoad(CheckMode).url)
  }

  def telephoneNumber: Option[AnswerRow] = userAnswers.telephoneNumber map {
    x => AnswerRow("telephoneNumber.checkYourAnswersLabel", s"$x", false, routes.TelephoneNumberController.onPageLoad(CheckMode).url)
  }

  def ukAddress: Option[AnswerRow] = userAnswers.ukAddress map {
    x => AnswerRow("ukAddress.checkYourAnswersLabel", s"${x.addressLine1} ${x.addressLine2}", false, routes.UkAddressController.onPageLoad(CheckMode).url)
  }

  def internationalAddress: Option[AnswerRow] = userAnswers.internationalAddress map {
    x => AnswerRow("internationalAddress.checkYourAnswersLabel", s"${x.addressLine1} ${x.addressLine2} ${x.addressLine3} ${x.country}", false, routes.InternationalAddressController.onPageLoad(CheckMode).url)
  }

  def isTheAddressInTheUK: Option[AnswerRow] = userAnswers.isTheAddressInTheUK map {
    x => AnswerRow("isTheAddressInTheUK.checkYourAnswersLabel", if(x) "site.yes" else "site.no", true, routes.IsTheAddressInTheUKController.onPageLoad(CheckMode).url)
  }

  def nationalInsuranceNumber: Option[AnswerRow] = userAnswers.nationalInsuranceNumber map {
    x => AnswerRow("nationalInsuranceNumber.checkYourAnswersLabel", s"$x", false, routes.NationalInsuranceNumberController.onPageLoad(CheckMode).url)
  }

  def fullName: Option[AnswerRow] = userAnswers.fullName map {
    x => AnswerRow("fullName.checkYourAnswersLabel", s"$x", false, routes.FullNameController.onPageLoad(CheckMode).url)
  }
}
