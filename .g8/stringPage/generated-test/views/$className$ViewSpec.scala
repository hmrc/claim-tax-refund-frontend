package views

import play.api.data.Form
import controllers.routes
import forms.$className$Form
import models.NormalMode
import views.behaviours.StringViewBehaviours
import views.html.$className;format="decap"$

class $className$ViewSpec extends StringViewBehaviours {

  val messageKeyPrefix = "$className;format="decap"$"

  def createView = () => $className;format="decap"$(frontendAppConfig, $className$Form(), NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[String]) => $className;format="decap"$(frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  val form = $className$Form()

  "$className$ view" must {
    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like stringPage(createViewUsingForm, messageKeyPrefix, routes.$className$Controller.onSubmit(NormalMode).url)
  }
}