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

import com.google.inject.Inject
import config.FrontendAppConfig
import play.api.data.Form
import play.api.data.Forms._
import models.InternationalAddress

class InternationalAddressForm @Inject() (appConfig: FrontendAppConfig) extends FormErrorHelper with Constraints {

  private val maxLengthInt = appConfig.addressLineMaxLength
  private val countryLength = appConfig.countryMaxLength

  private val addressLine1KeyBlank = "global.addressLine1.blank"
  private val addressLine1KeyTooLong = "global.addressLine1.tooLong"
  private val addressLine2KeyBlank = "global.addressLine2.blank"
  private val addressLine2KeyTooLong = "global.addressLine2.tooLong"
  private val addressLine3KeyTooLong = "global.addressLine3.tooLong"
  private val addressLine4KeyTooLong = "global.addressLine4.tooLong"
  private val addressLine5KeyTooLong = "global.addressLine5.tooLong"
  private val countryKeyBlank = "global.country.blank"
  private val countryKeyTooLong = "global.country.tooLong"

  def apply(): Form[InternationalAddress] = {
    Form(
      mapping(
        "addressLine1" -> text.verifying(nonEmpty(addressLine1KeyBlank), maxLength(maxLengthInt, addressLine1KeyTooLong)),
        "addressLine2" -> text.verifying(nonEmpty(addressLine2KeyBlank), maxLength(maxLengthInt, addressLine2KeyTooLong)),
        "addressLine3" -> optional(text.verifying(maxLength(maxLengthInt, addressLine3KeyTooLong))),
        "addressLine4" -> optional(text.verifying(maxLength(maxLengthInt, addressLine4KeyTooLong))),
        "addressLine5" -> optional(text.verifying(maxLength(maxLengthInt, addressLine5KeyTooLong))),
        "country"      -> text.verifying(nonEmpty(countryKeyBlank), maxLength(countryLength, countryKeyTooLong))
      )(InternationalAddress.apply)(InternationalAddress.unapply)
    )
  }
}
