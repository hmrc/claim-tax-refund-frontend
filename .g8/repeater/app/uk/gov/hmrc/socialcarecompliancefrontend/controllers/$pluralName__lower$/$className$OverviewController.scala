package controllers.$pluralName;format="lower"$

import javax.inject.Inject

import play.api.i18n.{I18nSupport, MessagesApi}
import config.FrontendAppConfig
import controllers.actions.{AuthAction, DataRequiredAction, DataRetrievalAction}
import identifiers.{$className$OverviewId, $pluralName$Id}
import models.Mode
import utils.Navigator
import viewmodels.$className$OverviewViewModel
import views.html.$pluralName;format="lower"$.$className;format="decap"$Overview
import uk.gov.hmrc.play.bootstrap.controller.FrontendController

class $className$OverviewController @Inject()(val appConfig: FrontendAppConfig,
                                          val messagesApi: MessagesApi,
                                          navigator: Navigator,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction) extends FrontendController with I18nSupport {

  def onPageLoad(mode: Mode) = (getData andThen requireData) {
    implicit request =>
      val viewModel = $className$OverviewViewModel(
        request.userAnswers.$pluralName;format="decap"$.getOrElse(Seq()),
        routes.Add$className$Controller.onPageLoad(mode),
        navigator.nextPage($className$OverviewId, mode)(request.userAnswers))
      Ok($className;format="decap"$Overview(appConfig, viewModel, mode))
  }
}
