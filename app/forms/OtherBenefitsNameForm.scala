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

import com.google.inject.Inject
import config.FrontendAppConfig
import forms.mappings.{Constraints, Mappings}
import play.api.data.Form

class OtherBenefitsNameForm @Inject() (appConfig: FrontendAppConfig) extends FormErrorHelper with Mappings with Constraints {

  private val errorBlankKey = "otherBenefitsName.blank"
  private val duplicateBenefitKey = "otherBenefitsName.duplicate"

  def apply(otherBenefitsName: Seq[String]): Form[String] = Form(
    "value" -> text(errorBlankKey).verifying(duplicateBenefitKey,  a => !otherBenefitsName.contains(a))
  )
}
