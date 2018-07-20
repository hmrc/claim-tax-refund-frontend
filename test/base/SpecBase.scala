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

package base

import config.FrontendAppConfig
import models.OtherBenefit
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice._
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.Injector
import play.api.test.FakeRequest
import uk.gov.hmrc.auth.core.retrieve.ItmpAddress
import utils.SequenceUtil

trait SpecBase extends PlaySpec with GuiceOneAppPerSuite {

  def injector: Injector = app.injector

  def frontendAppConfig: FrontendAppConfig = injector.instanceOf[FrontendAppConfig]

  def messagesApi: MessagesApi = injector.instanceOf[MessagesApi]

  def fakeRequest = FakeRequest("", "")

  def messages: Messages = messagesApi.preferred(fakeRequest)

  def sequenceUtil: SequenceUtil[OtherBenefit] = injector.instanceOf[SequenceUtil[OtherBenefit]]

  val itmpAddress = ItmpAddress(
    Some("Line1"),
    Some("Line2"),
    Some("Line3"),
    Some("Line4"),
    Some("Line5"),
    Some("AB1 2CD"),
    Some("United Kingdom"),
    Some("UK")
  )
}
