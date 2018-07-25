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
import forms.mappings.{Constraints, Mappings}
import models.AnyTaxPaid
import play.api.data.Form
import play.api.data.Forms._
import uk.gov.voa.play.form.ConditionalMappings.mandatoryIfTrue

class AnyTaxPaidForm @Inject() extends FormErrorHelper with Mappings with Constraints {

  private val currencyRegex = """(?=.)^\$?(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+)?(\.[0-9]{1,2})?$"""
  private val notSelectedKey = "anyTaxableOtherIncome.notSelected"
  private val blankKey = "anyTaxableOtherIncome.blank"
  private val invalidKey = "anyTaxableOtherIncome.invalid"

  def apply(): Form[AnyTaxPaid] = {
    Form(
      mapping(
        tuple("anyTaxPaid" -> boolean(notSelectedKey),
          "taxPaidAmount" -> mandatoryIfTrue(
            "anyTaxPaid",
            text(blankKey).verifying(regexValidation(currencyRegex, invalidKey))
          )
        )
          .transform(toTaxPaid, fromTaxPaid)
      )(AnyTaxPaid.apply)(AnyTaxPaid.unapply)
    )
  }

  def fromTaxPaid(anyTaxPaid: AnyTaxPaid): (Boolean, Option[String]) = {
    anyTaxPaid match {
      case AnyTaxPaid.Yes(amount) => (true, Some(amount))
      case AnyTaxPaid.No =>  (false, None)
    }
  }

  def toTaxPaid(taxPaidTuple: (Boolean, Option[String])): AnyTaxPaid = {
    taxPaidTuple match {
      case (true, Some(amount))  => AnyTaxPaid.Yes(amount)
      case (false, None)  => AnyTaxPaid.No
      case _ => throw new RuntimeException("Invalid selection")
    }
  }
}
