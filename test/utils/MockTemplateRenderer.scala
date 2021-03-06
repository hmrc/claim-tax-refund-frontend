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

package utils

import config.{FrontendAppConfig, LocalTemplateRenderer}
import org.scalatestplus.mockito.MockitoSugar
import play.api.i18n.Messages
import play.twirl.api.Html
import uk.gov.hmrc.play.bootstrap.http.DefaultHttpClient

import scala.concurrent.Future
import scala.concurrent.duration._

object MockTemplateRenderer extends MockitoSugar {
  val renderer = new LocalTemplateRenderer(mock[FrontendAppConfig],mock[DefaultHttpClient]) {
    override lazy val templateServiceBaseUrl = "http://example.com/template/mustache"
    override val refreshAfter: FiniteDuration = 10.minutes

    override def fetchTemplate(path: String): Future[String] = ???

    override def renderDefaultTemplate(path: String, content: Html, extraArgs: Map[String, Any])(implicit messages: Messages) = {
      Html("<title>" + extraArgs("pageTitle") + "</title>" + content)
    }
  }
}


