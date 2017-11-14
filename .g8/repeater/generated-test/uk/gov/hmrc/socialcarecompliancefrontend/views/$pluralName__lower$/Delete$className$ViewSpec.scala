package views.$pluralName;format="lower"$

import play.api.data.Form
import controllers.$pluralName;format="lower"$.routes
import forms.BooleanForm
import models.{NormalMode, $className$}
import views.behaviours.YesNoViewBehaviours
import views.html.$pluralName;format="lower"$.delete$className$

class Delete$className$ViewSpec extends YesNoViewBehaviours {
  val messageKeyPrefix = "$pluralName;format="decap"$.delete"

  val index = 0

  val $className;format="decap"$ = $className$("value 1", "value 2")

  override val form = BooleanForm()

  def createView = () => delete$className$(index, $className;format="decap"$, frontendAppConfig, BooleanForm(), NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[Boolean]) => delete$className$(index, $className;format="decap"$, frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  "Delete $className$ view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like yesNoPage(createViewUsingForm, messageKeyPrefix, routes.Delete$className$Controller.onSubmit(index, NormalMode).url)
  }
}
