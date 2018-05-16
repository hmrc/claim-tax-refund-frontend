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

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.actions.{AuthAction, DataRequiredAction, DataRetrievalAction}
import models.SubmissionSuccessful
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import utils.{CheckYourAnswersHelper, CheckYourAnswersSections}
import views.html.check_your_answers
import services.SubmissionService

class CheckYourAnswersController @Inject()(appConfig: FrontendAppConfig,
                                           override val messagesApi: MessagesApi,
                                           authenticate: AuthAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           submissionService: SubmissionService) extends FrontendController with I18nSupport {

  def onPageLoad() = (authenticate andThen getData andThen requireData) {
    implicit request =>
      val cyaHelper = new CheckYourAnswersHelper(request.userAnswers)
      val sections = new CheckYourAnswersSections(cyaHelper, request.userAnswers)
      Ok(check_your_answers(appConfig, sections.sectionsToShow))
  }

  def onSubmit() = (authenticate andThen getData andThen requireData).async {
    implicit request =>

      implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))

      submissionService.ctrSubmission(request.userAnswers) map {
        case SubmissionSuccessful => Redirect(routes.SessionExpiredController.onPageLoad())
        case _ =>                    Redirect(routes.SessionExpiredController.onPageLoad())
      }
  }
}
