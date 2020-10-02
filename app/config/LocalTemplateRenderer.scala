/*
 * Copyright 2020 HM Revenue & Customs
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

package config

import java.io.StringWriter

import com.github.mustachejava.Mustache
import javax.inject.Inject
import play.api.i18n.Messages
import play.twirl.api.Html
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.DefaultHttpClient
import uk.gov.hmrc.renderer.TemplateRenderer
import uk.gov.hmrc.http.HttpReads.Implicits._

import scala.language.postfixOps
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

class LocalTemplateRenderer @Inject()(appConfig: FrontendAppConfig,
                                      WSHttp: DefaultHttpClient)
                                      extends TemplateRenderer {
  override lazy val templateServiceBaseUrl = appConfig.servicesConfig.baseUrl("frontend-template-provider")
  override val refreshAfter: Duration = 10 minutes

  private implicit val hc = HeaderCarrier()

  override def fetchTemplate(path: String): Future[String] =  {
    WSHttp.GET[HttpResponse](url = path).map(_.body)
  }

  private def renderTemplate(path: String)(content: Html, extraArgs: Map[String, Any])(implicit messages: Messages): Html = {
    import collection.JavaConverters._
    val isWelsh = messages.lang.code.take(2)=="cy"
    val attributes: java.util.Map[String, Any] = (Map("article" -> content.body, "isWelsh" -> isWelsh) ++ extraArgs).asJava
    val m: Mustache = cache.get(path)
    val sw = new StringWriter()
    m.execute(sw, attributes)
    sw.flush()
    Html(sw.toString)
  }

  override def renderDefaultTemplate(path: String, content: Html, extraArgs: Map[String, Any])(implicit messages: Messages): Html =
    renderTemplate(path)(content, extraArgs)(messages)
}