/*
 * Copyright 2023 HM Revenue & Customs
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
import models.WhereToSendPayment

class WhereToSendPaymentFormSpec extends FormBehaviours {

  private val errorKeyBlank = "whereToSendPayment.blank"

  val validData: Map[String, String] = Map(
    "value" -> WhereToSendPaymentForm.options.head.value
  )

  val form = WhereToSendPaymentForm()

  "WhereToSendPayment form" must {

    behave like questionForm[WhereToSendPayment](WhereToSendPayment.values.head)

    behave like formWithOptionField("value", errorKeyBlank, WhereToSendPaymentForm.options.toSeq.map(_.value): _*)
  }
}
