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

package forms

import models.UkAddress
import play.api.data.Form
import play.api.data.Forms.{mapping, of, optional}

object UkAddressForm extends FormErrorHelper with FormatterMaxLength{

  def apply(addressMaxLength: Int, postcodeLength: Int): Form[UkAddress] = {
    Form(
      mapping(
        "addressLine1" -> of(formatterMaxLength("global.addressLine1", addressMaxLength)),
        "addressLine2" -> of(formatterMaxLength("global.addressLine2", addressMaxLength)),
        "addressLine3" -> optional(of(formatterMaxLength("global.addressLine3", addressMaxLength))),
        "addressLine4" -> optional(of(formatterMaxLength("global.addressLine4", addressMaxLength))),
        "addressLine5" -> optional(of(formatterMaxLength("global.addressLine5", addressMaxLength))),
        "postcode" -> of(formatterMaxLength("global.postcode", postcodeLength))
      )(UkAddress.apply)(UkAddress.unapply))
  }
}
