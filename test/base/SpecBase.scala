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

import config.{CtrFormPartialRetriever, FrontendAppConfig}
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice._
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.Injector
import play.api.mvc.RequestHeader
import play.api.test.FakeRequest
import play.twirl.api.Html
import uk.gov.hmrc.auth.core.retrieve.ItmpAddress
import uk.gov.hmrc.play.bootstrap.filters.frontend.crypto.SessionCookieCrypto
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import utils.{MockTemplateRenderer, SequenceUtil}

trait SpecBase extends PlaySpec with GuiceOneAppPerSuite with MockitoSugar {

  def injector: Injector = app.injector

  def frontendAppConfig: FrontendAppConfig = injector.instanceOf[FrontendAppConfig]

  def messagesApi: MessagesApi = injector.instanceOf[MessagesApi]

  def fakeRequest = FakeRequest("", "")

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

  implicit val formPartialRetriever: CtrFormPartialRetriever =
    new MockCtrFormPartialRetriever(httpGet = mock[HttpClient], sessionCookieCrypto = mock[SessionCookieCrypto])

  implicit val templateRenderer: MockTemplateRenderer.type =
    MockTemplateRenderer
}

class MockCtrFormPartialRetriever(httpGet:HttpClient, sessionCookieCrypto: SessionCookieCrypto)
  extends CtrFormPartialRetriever(httpGet, sessionCookieCrypto) with MockitoSugar {

  override def getPartialContent(url: String, templateParameters: Map[String, String], errorMessage: Html)(implicit request:RequestHeader): Html = {
    Html("")
  }
}
