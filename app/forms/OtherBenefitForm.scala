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

package forms

import com.google.inject.Inject
import config.FrontendAppConfig
import forms.mappings.{Constraints, Mappings}
import models.{Index, OtherBenefit}
import play.api.data.Form
import play.api.data.Forms._
class OtherBenefitForm @Inject()(appConfig: FrontendAppConfig) extends FormErrorHelper with Mappings with Constraints {

  private val currencyRegex = appConfig.currencyRegex
  private val nameBlankKey = "otherBenefit.name.blank"
  private val amountBlankKey = "otherBenefit.amount.blank"
  private val amountInvalidKey = "otherBenefit.amount.invalid"
  private val duplicateBenefitKey = "otherBenefit.duplicate"

  def apply(otherBenefit: Seq[OtherBenefit], index: Index): Form[OtherBenefit] = {
    Form(
      mapping(
        "name" -> text(nameBlankKey),//.verifying(duplicateBenefitKey,  a => otherBenefitsName.forall(p => !p.name.contains(a))),
        "amount" -> text(amountBlankKey)//.verifying(regexValidation(currencyRegex, amountInvalidKey))
      )(OtherBenefit.apply)(OtherBenefit.unapply)
    )
  }
}

