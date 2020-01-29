package views

import config.FrontendAppConfig
import play.api.data.Form
import controllers.routes
import forms.$className$Form
import models.NormalMode
import org.scalatestplus.mockito.MockitoSugar
import views.behaviours.StringViewBehaviours
import views.html.$className;format="decap"$

class $className$ViewSpec extends StringViewBehaviours with MockitoSugar {

  val messageKeyPrefix = "$className;format="decap"$"

  val appConfig: FrontendAppConfig = mock[FrontendAppConfig]

  override val form: Form[String] = new $className$Form(appConfig)()

  def createView = () => $className;format="decap"$(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) => $className;format="decap"$(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "$className$ view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like stringPage(createViewUsingForm, messageKeyPrefix, routes.$className$Controller.onSubmit(NormalMode).url)
  }
}
