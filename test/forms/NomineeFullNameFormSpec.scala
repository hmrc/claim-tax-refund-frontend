/*
 * Copyright 2019 HM Revenue & Customs
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
import models.{MandatoryField, MaxLengthField}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form

class NomineeFullNameFormSpec extends FormBehaviours with MockitoSugar {

  private val errorKeyBlank = "nomineeFullName.blank"
  private val errorKeyTooLong = "nomineeFullName.tooLong"
  private val maxLength = frontendAppConfig.nomineeFullNameMaxLength

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    when(instance.nomineeFullNameMaxLength) thenReturn maxLength
    instance
  }

  val validData: Map[String, String] = Map("value" -> "test answer")

  override val form: Form[_] = new NomineeFullNameForm(appConfig)()

  "NomineeFullName Form" must {

    behave like formWithMandatoryTextFields(MandatoryField("value", errorKeyBlank))

    behave like formWithMaxLengthTextFields(MaxLengthField("value", errorKeyTooLong, maxLength))
  }
}
