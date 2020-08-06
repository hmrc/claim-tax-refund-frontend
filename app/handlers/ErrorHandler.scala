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

package handlers

import com.github.tototoshi.play2.scalate._
import config.FrontendAppConfig
import javax.inject.{Inject, Singleton}
import play.api.http.Status.{BAD_REQUEST, FORBIDDEN}
import play.api.i18n.MessagesApi
import play.api.mvc.Results._
import play.api.mvc.{Request, RequestHeader, Result}
import play.twirl.api.Html
import uk.gov.hmrc.play.bootstrap.frontend.http.FrontendErrorHandler
import uk.gov.hmrc.play.partials.FormPartialRetriever
import views.html.{error_template,unauthorised_error_template}

import scala.concurrent.Future

@Singleton
class ErrorHandler @Inject()(
                              appConfig: FrontendAppConfig,
                              error_template: error_template,
                              unauthorised_error_template: unauthorised_error_template,
                              val messagesApi: MessagesApi,
                              implicit val formPartialRetriever: FormPartialRetriever,
                              implicit val scalate: Scalate
                            ) extends FrontendErrorHandler {

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    statusCode match {
      case BAD_REQUEST =>
        implicit val req: Request[Result.type] = Request(request, Result)
        val body: Result = BadRequest(error_template(
          pageTitle = "error.generic.title",
          heading = "error.generic.title",
          errorMessages = Seq("error.generic.message1", "error.generic.message2"),
          appConfig = appConfig
        ))

        Future.successful(body)
      case FORBIDDEN =>
        Future.successful(Forbidden(views.html.defaultpages.unauthorized()))
      case _ =>
        Future.successful(BadRequest(views.html.defaultpages.badRequest(request.method, request.uri, message)))
    }
  }

  override def standardErrorTemplate(pageTitle: String, heading: String, message: String)(implicit request: Request[_]): Html =
    unauthorised_error_template(pageTitle, heading, message, appConfig)
}

