package views

import config.FrontendAppConfig
import play.api.data.Form
import controllers.routes
import forms.$className$Form
import models.NormalMode
import org.scalatest.mockito.MockitoSugar
import views.behaviours.StringViewBehaviours
import views.html.$className;format="decap"$

class $className$ViewSpec extends StringViewBehaviours with MockitoSugar {

  val messageKeyPrefix = "$className;format="decap"$"

  val appConfig: FrontendAppConfig = mock[FrontendAppConfig]

  override val form: Form[String] = new $className$Form(appConfig)()

  def createView = () => $className;format="decap"$(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) => $className;format="decap"$(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  val form = $className$Form()

  "$className$ view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like stringPage(createViewUsingForm, messageKeyPrefix, routes.$className$Controller.onSubmit(NormalMode).url)
  }
}
