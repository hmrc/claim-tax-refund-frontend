/*
 * Copyright 2022 HM Revenue & Customs
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
import forms.mappings.Constraints
import play.api.data.Form
import play.api.data.Forms._

class HowMuchCarersAllowanceForm @Inject() (appConfig: FrontendAppConfig) extends FormErrorHelper with Constraints {

  private val howMuchCarersAllowanceBlankKey = "howMuchCarersAllowance.blank"
  private val howMuchCarersAllowanceInvalidKey = "howMuchCarersAllowance.invalid"
  private val currencyRegex = appConfig.currencyRegex

  def apply(): Form[String] = Form(
    "value" -> text.verifying(
      firstError(
        nonEmpty(howMuchCarersAllowanceBlankKey),
        regexValidation(currencyRegex, howMuchCarersAllowanceInvalidKey))
    )
  )

}
