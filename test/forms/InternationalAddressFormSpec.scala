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

import forms.behaviours.FormBehaviours
import models.InternationalAddress

class InternationalAddressFormSpec extends FormBehaviours {

  val validData: Map[String, String] = Map(
    "addressLine1" -> "line 1",
    "addressLine2" -> "line 2",
    "addressLine3" -> "line 3",
    "country" -> "country"
  )

  val form = InternationalAddressForm()

  "InternationalAddress form" must {
    behave like questionForm(InternationalAddress("line 1", "line 2", Some("line 3"), "country"))

    behave like formWithMandatoryTextFields("addressLine1", "addressLine2", "country")

    behave like formWithOptionalTextFields("addressLine3")  }
}
