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

package views

import config.FrontendAppConfig
import models.{NormalMode, UkAddress}
import org.scalatest.mockito.MockitoSugar
import views.behaviours.ViewBehaviours
import views.html.userDetails

class UserDetailsViewSpec extends ViewBehaviours with MockitoSugar {

  val messageKeyPrefix = "userDetails"

  val appConfig: FrontendAppConfig = mock[FrontendAppConfig]

  val testName = "testFName testLastName"
  val testNino = "AB123123A"
  val testAddress = UkAddress("testLine1", "testLine2", Some("testLine3"), Some("testLine4"), Some("testLine5"), "AB1 0CD")

  def createView = () => userDetails(frontendAppConfig, NormalMode, testName, testNino, testAddress)(fakeRequest, messages)

  "UserDetails view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)
  }
}
