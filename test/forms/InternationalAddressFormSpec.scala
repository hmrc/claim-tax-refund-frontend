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

import config.FrontendAppConfig
import forms.behaviours.FormBehaviours
import models.{InternationalAddress, MaxLengthField}
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form
import org.mockito.Mockito._

class InternationalAddressFormSpec extends FormBehaviours with MockitoSugar {

  val addressLineMaxLength = 35
  val countryMaxLength = 10

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    when(instance.addressLineMaxLength) thenReturn addressLineMaxLength
    when(instance.countryMaxLength) thenReturn countryMaxLength
    instance
  }

  val addressLine1Blank = "global.addressLine1.blank"
  val addressLine1TooLong = "global.addressLine1.tooLong"
  val addressLine2Blank = "global.addressLine2.blank"
  val addressLine2TooLong = "global.addressLine2.tooLong"
  val addressLine3TooLong = "global.addressLine3.tooLong"
  val addressLine4TooLong = "global.addressLine4.tooLong"
  val addressLine5TooLong = "global.addressLine5.tooLong"
  val countryBlank = "global.country.blank"
  val countryTooLong = "global.country.tooLong"

  val validData: Map[String, String] = Map(
    "addressLine1" -> "value 1",
    "addressLine2" -> "value 2",
    "addressLine3" -> "value 3",
    "addressLine4" -> "value 4",
    "addressLine5" -> "value 5",
    "country" -> "country"
  )

  override val form: Form[_] = new InternationalAddressForm(appConfig)()

  "International Address form" must {
    behave like questionForm(InternationalAddress("value 1", "value 2", Some("value 3"), Some("value 4"), Some("value 5"), "country"))

    behave like formWithMandatoryTextFieldsAndCustomKey(
      ("addressLine1", addressLine1Blank),
      ("addressLine2", addressLine2Blank),
      ("country", countryBlank))

    behave like formWithOptionalTextFields("addressLine3", "addressLine4", "addressLine5")

    behave like formWithMaxLengthTextFields(
      MaxLengthField("addressLine1", addressLine1TooLong, addressLineMaxLength),
      MaxLengthField("addressLine2", addressLine2TooLong, addressLineMaxLength),
      MaxLengthField("addressLine3", addressLine3TooLong, addressLineMaxLength),
      MaxLengthField("addressLine4", addressLine4TooLong, addressLineMaxLength),
      MaxLengthField("addressLine5", addressLine5TooLong, addressLineMaxLength),
      MaxLengthField("country", countryTooLong, countryMaxLength))
  }
}
