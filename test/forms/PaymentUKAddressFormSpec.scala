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

import config.FrontendAppConfig
import forms.behaviours.FormBehaviours
import models.{MandatoryField, MaxLengthField, RegexField, UkAddress}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form

class PaymentUKAddressFormSpec extends FormBehaviours with MockitoSugar {

  private val addressLineMaxLength = 35
  private val postcodeRegex = """([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9]?[A-Za-z]))))\s?[0-9][A-Za-z]{2})"""

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    when(instance.addressLineMaxLength) thenReturn addressLineMaxLength
    when(instance.postcodeRegex) thenReturn postcodeRegex
    instance
  }

  private val addressLine1Blank = "global.addressLine1.blank"
  private val addressLine1TooLong = "global.addressLine1.tooLong"
  private val addressLine2Blank = "global.addressLine2.blank"
  private val addressLine2TooLong = "global.addressLine2.tooLong"
  private val addressLine3TooLong = "global.addressLine3.tooLong"
  private val addressLine4TooLong = "global.addressLine4.tooLong"
  private val addressLine5TooLong = "global.addressLine5.tooLong"
  private val postcodeInvalid = "global.postcode.invalid"
  private val postcodeBlank = "global.postcode.blank"

  val validData: Map[String, String] = Map(
    "addressLine1" -> "line 1",
    "addressLine2" -> "line 2",
    "addressLine3" -> "line 3",
    "addressLine4" -> "line 4",
    "addressLine5" -> "line 5",
    "postcode" -> "NE1 7RF"
  )

  override val form: Form[UkAddress] = new PaymentUKAddressForm(appConfig)()

  "PaymentUKAddress form" must {
    behave like questionForm(UkAddress("line 1", "line 2", Some("line 3"), Some("line 4"), Some("line 5"), "NE1 7RF"))

    behave like formWithMandatoryTextFields(
      MandatoryField("addressLine1", addressLine1Blank),
      MandatoryField("addressLine2", addressLine2Blank),
      MandatoryField("postcode", postcodeBlank))

    behave like formWithOptionalTextFields("addressLine3", "addressLine4", "addressLine5")

    behave like formWithMaxLengthTextFields(
      MaxLengthField("addressLine1", addressLine1TooLong, addressLineMaxLength),
      MaxLengthField("addressLine2", addressLine2TooLong, addressLineMaxLength),
      MaxLengthField("addressLine3", addressLine3TooLong, addressLineMaxLength),
      MaxLengthField("addressLine4", addressLine4TooLong, addressLineMaxLength),
      MaxLengthField("addressLine5", addressLine5TooLong, addressLineMaxLength))

    behave like formWithRegex(RegexField("postcode", postcodeInvalid, postcodeRegex))
  }
}
