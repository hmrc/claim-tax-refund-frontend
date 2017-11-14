package controllers.$pluralName;format="lower"$

import javax.inject.Inject

import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import connectors.DataCacheConnector
import controllers.actions._
import forms.$className$Form
import identifiers.{Add$className$Id, $pluralName$Id}
import models.{Mode, $className$}
import utils.UserAnswers
import views.html.$pluralName;format="lower"$.add$className$

import scala.concurrent.Future

class Add$className;format="cap"$Controller @Inject()(appConfig: FrontendAppConfig,
                                                  override val messagesApi: MessagesApi,
                                                  dataCacheConnector: DataCacheConnector,
                                                  navigator: Navigator,
                                                  getData: DataRetrievalAction,
                                                  requireData: DataRequiredAction) extends FrontendController with I18nSupport {

  import config.FrontendAppConfig

  def onPageLoad(mode: Mode) = (getData andThen requireData) {
    implicit request =>
      Ok(add$className$(appConfig, $className$Form(), mode))
  }

  def onSubmit(mode: Mode) = (getData andThen requireData).async {
    implicit request =>
      $className$Form().bindFromRequest().fold(
        (formWithErrors: Form[$className$]) =>
          Future.successful(BadRequest(add$className$(appConfig, formWithErrors, mode))),
        (value) =>
          dataCacheConnector.addToCollection[$className$](request.sessionId, $pluralName$Id.toString, value).map(cacheMap =>
            Redirect(navigator.nextPage(Add$className$Id, mode)(new UserAnswers(cacheMap))))
      )
  }
}
