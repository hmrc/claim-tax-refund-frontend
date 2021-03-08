/*
 * Copyright 2021 HM Revenue & Customs
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

import models.WhereToSendPayment
import play.api.data.{Form, FormError}
import play.api.data.Forms._
import play.api.data.format.Formatter
import utils.{Message, RadioOption}

object WhereToSendPaymentForm extends FormErrorHelper {

  private val errorKeyBlank = "whereToSendPayment.blank"

  def apply(): Form[WhereToSendPayment] =
    Form(single("value" -> of(whereToSendPaymentFormatter)))

  def options: Set[RadioOption] = WhereToSendPayment.values.map {
    value =>
      RadioOption(
        id = s"whereToSendPayment.$value",
        value = value.toString,
        message = Message(s"whereToSendPayment.$value")
      )
  }

  private def whereToSendPaymentFormatter: Formatter[WhereToSendPayment] = new Formatter[WhereToSendPayment] {

    def bind(key: String, data: Map[String, String]): Either[Seq[FormError], WhereToSendPayment] = data.get(key) match {
      case Some(s) => WhereToSendPayment.withName(s)
        .map(Right.apply)
        .getOrElse(produceError(key, "error.unknown"))
      case None => produceError(key, errorKeyBlank)
    }

    def unbind(key: String, value: WhereToSendPayment): Map[String, String] = Map(key -> value.toString)
  }
}
