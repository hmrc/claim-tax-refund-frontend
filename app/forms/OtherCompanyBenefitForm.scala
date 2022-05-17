/*
 * Copyright 2022 HM Revenue & Customs
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
import models.{Index, OtherCompanyBenefit}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}
import play.api.i18n.MessagesApi

class OtherCompanyBenefitForm @Inject()(messages: MessagesApi, appConfig: FrontendAppConfig) extends FormErrorHelper with Mappings with Constraints {

  private val currencyRegex = appConfig.currencyRegex
  private val nameBlankKey = "otherCompanyBenefit.name.blank"
  private val amountBlankKey = "otherCompanyBenefit.amount.blank"
  private val amountInvalidKey = "otherCompanyBenefit.amount.invalid"
  private val duplicateBenefitKey = "otherCompanyBenefit.duplicate"

  def apply(otherCompanyBenefit: Seq[OtherCompanyBenefit], index: Index): Form[OtherCompanyBenefit] = {

    val duplicateNameConstraint = Constraint[String] {
      companyBenefitName: String =>
        if(filter(otherCompanyBenefit, index, companyBenefitName).forall(companyBenefit => companyBenefit.name != companyBenefitName)) {
          Valid
        } else {
          Invalid(Seq(ValidationError(duplicateBenefitKey, companyBenefitName)))
        }
    }

    Form(
      mapping(
        "name" -> text(nameBlankKey).verifying(duplicateNameConstraint),
        "amount" -> text(amountBlankKey).verifying(amountInvalidKey, a => a.matches(currencyRegex))
      )(OtherCompanyBenefit.apply)(OtherCompanyBenefit.unapply)
    )
  }

  def filter(seq: Seq[OtherCompanyBenefit], index: Index, name: String): Seq[OtherCompanyBenefit] = {
    if (Index(index) < seq.length && seq(index).name.nonEmpty && seq(index).name == name) {
      seq.filterNot(_ == seq(index))
    } else {
      seq
    }
  }

}
