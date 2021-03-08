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

package forms.mappings

import models.TelephoneOption
import play.api.data.Forms.tuple
import play.api.data.Mapping
import uk.gov.voa.play.form.ConditionalMappings.mandatoryIfTrue

trait TelephoneOptionMapping extends Mappings {

  def telephoneOptionMapping(requiredKey: String,
                      requiredTelephoneKey: String,
                      telephoneInvalidKey: String):
  Mapping[TelephoneOption] = {
    val telephoneRegex = """^\+?[0-9\s\(\)]{1,20}$"""

    def fromTelephone(telephone: TelephoneOption): (Boolean, Option[String]) = {
      telephone match {
        case TelephoneOption.Yes(number) => (true, Some(number))
        case TelephoneOption.No =>  (false, None)
      }
    }

    def toTelephone(telephoneTuple: (Boolean, Option[String])) = {

      telephoneTuple match {
        case (true, Some(number))  => TelephoneOption.Yes(number)
        case (false, None)  => TelephoneOption.No
        case _ => throw new RuntimeException("Invalid selection")
      }
    }

    tuple("anyTelephoneNumber" -> boolean(requiredKey),
      "telephoneNumber" -> mandatoryIfTrue("anyTelephoneNumber",
        text(requiredTelephoneKey).verifying(regexValidation(telephoneRegex,telephoneInvalidKey))))
      .transform(toTelephone, fromTelephone)
  }

}
