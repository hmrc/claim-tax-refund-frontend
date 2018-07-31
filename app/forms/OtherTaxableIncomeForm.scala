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
import models.{Index, OtherTaxableIncome}
import play.api.data.Form
import play.api.data.Forms._

class OtherTaxableIncomeForm @Inject()(appConfig: FrontendAppConfig) extends FormErrorHelper with Mappings with Constraints {

  private val currencyRegex = appConfig.currencyRegex
  private val nameBlankKey = "otherTaxableIncome.name.blank"
  private val amountBlankKey = "otherTaxableIncome.amount.blank"
  private val amountInvalidKey = "otherTaxableIncome.amount.invalid"
  private val duplicateBenefitKey = "otherTaxableIncome.duplicate"

  def apply(otherBenefit: Seq[OtherTaxableIncome], index: Index): Form[OtherTaxableIncome] = {
    Form(
      mapping(
        "name" -> text(nameBlankKey).verifying(duplicateBenefitKey, a => filter(otherBenefit, index, a).forall(p => p.name != a)),
        "amount" -> text(amountBlankKey).verifying(amountInvalidKey, a => a.matches(currencyRegex))
      )(OtherTaxableIncome.apply)(OtherTaxableIncome.unapply)
    )
  }

  def filter(seq: Seq[OtherTaxableIncome], index: Index, name: String): Seq[OtherTaxableIncome] = {
    if (Index(index) < seq.length && seq(index).name.nonEmpty && seq(index).name == name) {
      seq.filterNot(_ == seq(index))
    } else {
      seq
    }
  }
}
