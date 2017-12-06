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

import play.api.data.Form
import play.api.data.Forms._
import play.api.data.format.Formatter
import utils.RadioOption
import models.FullOrPartialClaim

object FullOrPartialClaimForm extends FormErrorHelper {

  def apply(): Form[FullOrPartialClaim] =
    Form(single("value" -> of(fullOrPartialClaimFormatter)))

  def options = FullOrPartialClaim.values.map {
    value =>
      RadioOption("fullOrPartialClaim", value.toString)
  }

  private def fullOrPartialClaimFormatter = new Formatter[FullOrPartialClaim] {

    def bind(key: String, data: Map[String, String]) = data.get(key) match {
      case Some(s) => FullOrPartialClaim.withName(s)
        .map(Right.apply)
        .getOrElse(produceError(key, "error.unknown"))
      case None => produceError(key, "fullOrPartialClaim.blank")
    }

    def unbind(key: String, value: FullOrPartialClaim) = Map(key -> value.toString)
  }

  private def optionIsValid(value: String) = options.exists(o => o.value == value)
}
