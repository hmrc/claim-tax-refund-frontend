package controllers.$pluralName;format="lower"$

import javax.inject.Inject

import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import config.FrontendAppConfig
import uk.gov.hmrc.play.bootstrap.controller.FrontendController
import connectors.DataCacheConnector
import controllers.actions._
import forms.BooleanForm
import identifiers.Any$pluralName$Id
import models.Mode
import utils.{Navigator, UserAnswers}
import views.html.$pluralName;format="lower"$.any$pluralName$

import scala.concurrent.Future

class Any$pluralName$Controller @Inject()(appConfig: FrontendAppConfig,
                                         override val messagesApi: MessagesApi,
                                         dataCacheConnector: DataCacheConnector,
                                         navigator: Navigator,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction) extends FrontendController with I18nSupport {

  def onPageLoad(mode: Mode) = (getData andThen requireData) {
    implicit request =>
      val preparedForm = request.userAnswers.any$pluralName$ match {
        case None => BooleanForm()
        case Some(value) => BooleanForm().fill(value)
      }
      Ok(any$pluralName$(appConfig, preparedForm, mode))
  }

  def onSubmit(mode: Mode) = (getData andThen requireData).async {
    implicit request =>
      BooleanForm().bindFromRequest().fold(
        (formWithErrors: Form[Boolean]) =>
          Future.successful(BadRequest(any$pluralName$(appConfig, formWithErrors, mode))),
        (value) =>
          dataCacheConnector.save[Boolean](request.sessionId, Any$pluralName$Id.toString, value).map(cacheMap =>
            Redirect(navigator.nextPage(Any$pluralName$Id, mode)(new UserAnswers(cacheMap))))
      )
  }
}
