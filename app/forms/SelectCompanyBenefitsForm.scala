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

import models.CompanyBenefits
import models.CompanyBenefits.{companyCarBenefit, fuelBenefit, medicalBenefit, otherCompanyBenefit}
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formatter
import play.api.i18n.Messages
import utils.RadioOption

object SelectCompanyBenefitsForm extends FormErrorHelper {

  def apply(): Form[CompanyBenefits] =
    Form("value" -> of(companyBenefitsFormatter))

  def options(implicit messages: Messages): Seq[RadioOption] = Seq(
    RadioOption(
      companyCarBenefit.toString, companyCarBenefit.toString,
      messages("selectCompanyBenefits.companyCar")),
    RadioOption(
      fuelBenefit.toString, fuelBenefit.toString,
      messages("selectCompanyBenefits.fuel")),
    RadioOption(
      medicalBenefit.toString, medicalBenefit.toString,
      messages("selectCompanyBenefits.medical")),
    RadioOption(
      otherCompanyBenefit.toString, otherCompanyBenefit.toString,
      messages("selectCompanyBenefits.other"))
  )

  private def companyBenefitsFormatter = new Formatter[CompanyBenefits] {

    private val errorKeyBlank = "selectCompanyBenefits.blank"

    def bind(key: String, data: Map[String, String]) = data.get(key) match {
      case Some(s) => CompanyBenefits.withName(s)
        .map(Right.apply)
        .getOrElse(produceError(key, "error.unknown"))
      case None => produceError(key, errorKeyBlank)
    }

    def unbind(key: String, value: CompanyBenefits) = Map(key -> value.toString)
  }
}
