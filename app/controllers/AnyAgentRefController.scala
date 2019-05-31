/*
 * Copyright 2019 HM Revenue & Customs
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

import com.github.tototoshi.play2.scalate._
import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.actions._
import forms.AnyAgentReferenceForm
import identifiers.AnyAgentRefId
import javax.inject.Inject
import models.{AnyAgentRef, Mode, SelectTaxYear}
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Result
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.play.partials.FormPartialRetriever
import utils.{Navigator, UserAnswers}
import views.html.anyAgentRef

import scala.concurrent.{ExecutionContext, Future}

class AnyAgentRefController @Inject()(appConfig: FrontendAppConfig,
                                      override val messagesApi: MessagesApi,
                                      dataCacheConnector: DataCacheConnector,
                                      navigator: Navigator,
                                      authenticate: AuthAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      cc: MessagesControllerComponents,
                                      formProvider: AnyAgentReferenceForm,
                                      implicit val formPartialRetriever: FormPartialRetriever,
                                      implicit val scalate: Scalate
                                     )(implicit ec: ExecutionContext) extends FrontendController(cc) with I18nSupport {

  val requiredKey = "anyAgentRef.blank"
  val requiredAgentRefKey = "anyAgentRef.blankAgentRef"

  def onPageLoad(mode: Mode) = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val result: Option[Result] = for {
        taxYear: SelectTaxYear <- request.userAnswers.selectTaxYear
        nomineeName: String <- request.userAnswers.nomineeFullName
      } yield {
        val form: Form[AnyAgentRef] = formProvider(
          cc.messagesApi.preferred(request).messages(requiredKey, nomineeName),
          cc.messagesApi.preferred(request).messages(requiredAgentRefKey, nomineeName)
        )
        val preparedForm = request.userAnswers.anyAgentRef match {
          case None => form
          case Some(value) => form.fill(value)
        }
        Ok(anyAgentRef(appConfig, preparedForm, mode, nomineeName, taxYear))
      }

      result.getOrElse {
        Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }

  def onSubmit(mode: Mode) = (authenticate andThen getData andThen requireData).async {
    implicit request =>
      val result: Option[Future[Result]] = for {
        taxYear: SelectTaxYear <- request.userAnswers.selectTaxYear
        nomineeName: String <- request.userAnswers.nomineeFullName
      } yield {
        val form: Form[AnyAgentRef] = formProvider(
          cc.messagesApi.preferred(request).messages(requiredKey, nomineeName),
          cc.messagesApi.preferred(request).messages(requiredAgentRefKey, nomineeName)
        )
        form.bindFromRequest().fold(
          (formWithErrors: Form[_]) =>
            Future.successful(BadRequest(anyAgentRef(appConfig, formWithErrors, mode, nomineeName, taxYear))),
          value =>
            dataCacheConnector.save[AnyAgentRef](request.externalId, AnyAgentRefId.toString, value).map(cacheMap =>
              Redirect(navigator.nextPage(AnyAgentRefId, mode)(new UserAnswers(cacheMap))))
        )
      }

      result.getOrElse {
        Future.successful(Redirect(routes.SessionExpiredController.onPageLoad()))
      }
  }
}
