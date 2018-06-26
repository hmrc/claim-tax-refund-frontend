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

package forms.mappings

import models.AnyTaxPaid
import play.api.data.Forms.tuple
import play.api.data.Mapping
import uk.gov.voa.play.form.ConditionalMappings.mandatoryIfTrue

trait AnyTaxPaidMapping extends Mappings {

  def anyTaxPaidMapping(requiredKey: String,
                             requiredTaxAmountKey: String,
                             taxAmountInvalidKey: String):
  Mapping[AnyTaxPaid] = {
    val currencyRegex = """(?=.)^\$?(([1-9][0-9]{0,2}(,[0-9]{3})*)|[0-9]+)?(\.[0-9]{1,2})?$"""

    def fromTaxPaid(anyTaxPaid: AnyTaxPaid): (Boolean, Option[String]) = {
      anyTaxPaid match {
        case AnyTaxPaid.Yes(amount) => (true, Some(amount))
        case AnyTaxPaid.No =>  (false, None)
      }
    }

    def toTaxPaid(taxPaidTuple: (Boolean, Option[String])) = {

      taxPaidTuple match {
        case (true, Some(amount))  => AnyTaxPaid.Yes(amount)
        case (false, None)  => AnyTaxPaid.No
        case _ => throw new RuntimeException("Invalid selection")
      }
    }

    tuple("anyTaxPaid" -> boolean(requiredKey),
      "taxPaidAmount" -> mandatoryIfTrue(
        "anyTaxPaid",
        text(requiredTaxAmountKey).verifying(regexValidation(currencyRegex,taxAmountInvalidKey))
      )
    )
      .transform(toTaxPaid, fromTaxPaid)
  }

}
