package views

import config.FrontendAppConfig
import play.api.data.Form
import controllers.routes
import forms.$className$Form
import models.{NormalMode, $className$}
import org.scalatest.mockito.MockitoSugar
import views.behaviours.QuestionViewBehaviours
import views.html.$className;format="decap"$

class $className$ViewSpec extends QuestionViewBehaviours[$className$] with MockitoSugar {

  val messageKeyPrefix = "$className;format="decap"$"

  val appConfig: FrontendAppConfig = mock[FrontendAppConfig]

  override val form: Form[$className$] = new $className$Form(appConfig)()

  def createView = () => $className;format="decap"$(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[$className$]) => $className;format="decap"$(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  override val form = $className$Form()

  "$className$ view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithTextFields(createViewUsingForm, messageKeyPrefix, routes.$className$Controller.onSubmit(NormalMode).url, "field1", "field2")
  }
}
