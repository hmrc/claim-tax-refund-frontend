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

package controllers

import com.github.tototoshi.play2.scalate.Scalate
import config.FrontendAppConfig
import javax.inject.{Inject, Singleton}
import play.api.i18n.{I18nSupport}
import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.play.partials.FormPartialRetriever
import views.html.accessibility
import java.net.URL

@Singleton
class AccessibilityController @Inject()(val appConfig: FrontendAppConfig,
                                        accessibility: accessibility,
                                       cc: MessagesControllerComponents,
                                       implicit val formPartialRetriever: FormPartialRetriever,
                                       implicit val scalate: Scalate
                                      ) extends FrontendController(cc) with I18nSupport {

  def onPageLoad: Action[AnyContent] = Action { implicit request =>
    val referer_url = new URL(request.headers("referer")).getPath
    Ok(accessibility(appConfig, referer_url))
  }
}
