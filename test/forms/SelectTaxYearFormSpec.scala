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

import forms.behaviours.FormBehaviours
import models.SelectTaxYear
import models.SelectTaxYear.CYMinus1
import utils.RadioOption

class SelectTaxYearFormSpec extends FormBehaviours {

  private val errorKeyBlank = "selectTaxYear.blank"

  def radioButtonOptions: Seq[RadioOption] = SelectTaxYear.options

  val validData: Map[String, String] = Map(
    "value" -> radioButtonOptions.head.value
  )

  val form = SelectTaxYearForm()

  "SelectTaxYear form" must {

    behave like questionForm[SelectTaxYear](CYMinus1)

    behave like formWithOptionField("value", errorKeyBlank, radioButtonOptions.map(_.value): _*)
  }
}
