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

class InternationalAddressForm @Inject() (appConfig: FrontendAppConfig) extends FormErrorHelper with FormatterMaxLength{

  private val maxLength = appConfig.addressLineMaxLength
  private val countryLength = appConfig.countryMaxLength

  def apply(): Form[InternationalAddress] = {
    Form(
      mapping(
        "addressLine1" -> of(formatterMaxLength("global.addressLine1", maxLength)),
        "addressLine2" -> of(formatterMaxLength("global.addressLine2", maxLength)),
        "addressLine3" -> optional(of(formatterMaxLength("global.addressLine3", maxLength))),
        "addressLine4" -> optional(of(formatterMaxLength("global.addressLine4", maxLength))),
        "addressLine5" -> optional(of(formatterMaxLength("global.addressLine5", maxLength))),
        "country" -> of(formatterMaxLength("global.country", countryLength))
      )(InternationalAddress.apply)(InternationalAddress.unapply))
  }
}
