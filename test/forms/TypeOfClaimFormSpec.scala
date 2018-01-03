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

import forms.behaviours.FormBehaviours
import models.TypeOfClaim

class TypeOfClaimFormSpec extends FormBehaviours {

  private val errorKeyBlank = "typeOfClaim.blank"

  val validData: Map[String, String] = Map(
    "value" -> TypeOfClaimForm.options.head.value
  )

  val form = TypeOfClaimForm()

  "TypeOfClaim form" must {

    behave like questionForm[TypeOfClaim](TypeOfClaim.values.head)

    behave like formWithOptionField("value", errorKeyBlank, TypeOfClaimForm.options.toSeq.map(_.value): _*)
  }
}
