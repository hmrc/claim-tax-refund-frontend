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

package base

import config.{AddressLookupConfig, FrontendAppConfig, LocalTemplateRenderer}
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice._
import play.api.Application
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.Injector
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.MessagesControllerComponents
import play.api.test.FakeRequest
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.auth.core.retrieve.ItmpAddress
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.{MockTemplateRenderer, SequenceUtil}

import scala.concurrent.ExecutionContext

trait SpecBase extends PlaySpec with GuiceOneAppPerSuite with MockitoSugar {

  override lazy val app: Application = {

    import play.api.inject._

    new GuiceApplicationBuilder()
      .overrides(
        bind[TemplateRenderer].to[LocalTemplateRenderer],
      ).build()
  }

  def injector: Injector = app.injector

  def frontendAppConfig: FrontendAppConfig = injector.instanceOf[FrontendAppConfig]

  def templateRenderer: LocalTemplateRenderer = MockTemplateRenderer.renderer
  val ec: ExecutionContext = mock[ExecutionContext]
  def addressLookupConfig: AddressLookupConfig = injector.instanceOf[AddressLookupConfig]
  def messagesControllerComponents: MessagesControllerComponents = injector.instanceOf[MessagesControllerComponents]
  def authConnector: AuthConnector = injector.instanceOf[AuthConnector]

  def messagesApi: MessagesApi = messagesControllerComponents.messagesApi

  def fakeRequest = FakeRequest(method = "", path = "")

  def messages: Messages = messagesApi.preferred(fakeRequest)

  def sequenceUtil[A]: SequenceUtil[A] = injector.instanceOf[SequenceUtil[A]]

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

  val testResponseAddress: JsValue = {
    Json.parse(input = "{\n\"auditRef\":\"e9e2fb3f-268f-4c4c-b928-3dc0b17259f2\",\n\"address\":{\n\"lines\":[\n\"Line1\",\n\"Line2\",\n\"Line3\",\n\"Line4\"\n],\n \"postcode\":\"NE1 1LX\",\n\"country\":{\n\"code\":\"GB\",\n\"name\":\"United Kingdom\"\n}\n}\n}")
  }

  implicit val hc: HeaderCarrier = HeaderCarrier()

}
