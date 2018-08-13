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

package handlers

import config.FrontendAppConfig
import controllers.actions.AuthAction
import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.http.HttpErrorHandler
import play.api.http.Status.{BAD_REQUEST, FORBIDDEN}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Results._
import play.api.mvc.{Request, RequestHeader, Result, Results}
import uk.gov.hmrc.play.partials.FormPartialRetriever
import uk.gov.hmrc.renderer.TemplateRenderer
import views.html.error_template

import scala.concurrent.Future

@Singleton
class ErrorHandler @Inject()(
                              appConfig: FrontendAppConfig,
                              val messagesApi: MessagesApi,
                              implicit val formPartialRetriever: FormPartialRetriever,
                              implicit val templateRenderer: TemplateRenderer,
                              authAction: AuthAction
                            ) extends HttpErrorHandler with I18nSupport {

  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    statusCode match {
      case BAD_REQUEST =>
        authAction.invokeBlock[None.type](Request(request, None), { implicit request =>
          Future.successful(BadRequest(error_template(
            pageTitle = "error.generic.title",
            heading = "error.generic.title",
            errorMessages = Seq("error.generic.message1", "error.generic.message2"),
            appConfig = appConfig
          )))
        })
      case FORBIDDEN =>
        Future.successful(Forbidden(views.html.defaultpages.unauthorized()))
      case clientError if statusCode >= 400 && statusCode < 500 =>
        Future.successful(Results.Status(clientError)(views.html.defaultpages.badRequest(request.method, request.uri, message)))
      case nonClientError =>
        throw new IllegalArgumentException(s"onClientError invoked with non client error status code $statusCode: $message")
    }
  }

  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    Future.successful {
      Logger.warn("Exception in request", exception)
      InternalServerError("Error: " + exception.getMessage)
    }
  }
}

