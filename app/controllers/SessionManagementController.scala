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

package controllers

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions.{AuthAction, DataRequiredAction, DataRetrievalAction}
import javax.inject.Inject
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.play.partials.FormPartialRetriever
import views.html.sessionTimedout

import scala.concurrent.{ExecutionContext, Future}

class SessionManagementController @Inject()(val appConfig: FrontendAppConfig,
                                            sessionTimedout: sessionTimedout,
                                            cc: MessagesControllerComponents,
                                            authenticate: AuthAction,
                                            dataCacheConnector: DataCacheConnector,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            implicit val executionContext: ExecutionContext) extends FrontendController(cc) with I18nSupport{

  def extendSession: Action[AnyContent] = Action.async {
    Future.successful(Ok("OK"))
  }

  def signedOut: Action[AnyContent] = Action {
    implicit request =>
    Ok(sessionTimedout(appConfig)).withNewSession
  }

  def clearSessionData: Action[AnyContent] = authenticate.async {
    implicit request =>
      dataCacheConnector.removeAll(request.externalId).map { cacheMap =>
        Redirect(routes.SessionManagementController.signedOut)
      }
  }
}
