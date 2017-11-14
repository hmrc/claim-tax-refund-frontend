package controllers.$pluralName;format="lower"$

import javax.inject.Inject

import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import connectors.DataCacheConnector
import controllers.actions._
import forms.$className$Form
import identifiers.{Edit$className$Id, $pluralName$Id}
import models.{Mode, $className$}
import utils.UserAnswers
import views.html.$pluralName;format="lower"$.edit$className$

import scala.concurrent.Future

class Edit$className;format="cap"$Controller @Inject()(appConfig: FrontendAppConfig,
                                                  override val messagesApi: MessagesApi,
                                                  dataCacheConnector: DataCacheConnector,
                                                  navigator: Navigator,
                                                  getData: DataRetrievalAction,
                                                  requireData: DataRequiredAction) extends FrontendController with I18nSupport {

  import config.FrontendAppConfig

  def onPageLoad(index: Int, mode: Mode) = (getData andThen requireData) {
    implicit request =>
      request.userAnswers.$pluralName;format="decap"$.flatMap(x => x.lift(index)) match {
        case None => NotFound
        case Some($className;format="decap"$) => Ok(edit$className$(index, appConfig, $className$Form().fill($className;format="decap"$), mode))
      }
  }

  def onSubmit(index: Int, mode: Mode) = (getData andThen requireData).async {
    implicit request =>
      request.userAnswers.$pluralName;format="decap"$.flatMap(x => x.lift(index)) match {
        case None => Future.successful(NotFound)
        case Some($className;format="decap"$) =>
          $className$Form().bindFromRequest().fold(
            (formWithErrors: Form[$className$]) =>
              Future.successful(BadRequest(edit$className$(index, appConfig, formWithErrors, mode))),
            (value) =>
              dataCacheConnector.replaceInCollection[$className$](request.sessionId, $pluralName$Id.toString, index, value).map(cacheMap =>
                Redirect(navigator.nextPage(Edit$className$Id, mode)(new UserAnswers(cacheMap)))
              )
          )
      }
  }
}
