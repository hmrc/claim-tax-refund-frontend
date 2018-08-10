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

package controllers

import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.NomineeFullNameForm
import identifiers.NomineeFullNameId
import javax.inject.Inject
import models.Mode
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import uk.gov.hmrc.play.partials.FormPartialRetriever
import uk.gov.hmrc.renderer.TemplateRenderer
import utils.{Navigator, UserAnswers}
import views.html.nomineeFullName

import scala.concurrent.Future

class NomineeFullNameController @Inject()(
                                         appConfig: FrontendAppConfig,
                                         override val messagesApi: MessagesApi,
                                         dataCacheConnector: DataCacheConnector,
                                         navigator: Navigator,
                                         authenticate: AuthAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formBuilder: NomineeFullNameForm,
                                         implicit val formPartialRetriever: FormPartialRetriever,
                                         implicit val templateRenderer: TemplateRenderer) extends FrontendController with I18nSupport {

  private val form: Form[String] = formBuilder()

  def onPageLoad(mode: Mode) = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.nomineeFullName match {
        case None => form
        case Some(value) => form.fill(value)
      }
      Ok(nomineeFullName(appConfig, preparedForm, mode))
  }

  def onSubmit(mode: Mode) = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      form.bindFromRequest().fold(
        (formWithErrors: Form[_]) =>
          Future.successful(BadRequest(nomineeFullName(appConfig, formWithErrors, mode))),
        (value) =>
          dataCacheConnector.save[String](request.externalId, NomineeFullNameId.toString, value).map(cacheMap =>
            Redirect(navigator.nextPage(NomineeFullNameId, mode)(new UserAnswers(cacheMap))))
      )
  }
}
