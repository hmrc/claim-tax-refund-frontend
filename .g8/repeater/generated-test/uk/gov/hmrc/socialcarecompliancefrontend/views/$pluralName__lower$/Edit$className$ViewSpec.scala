package views.$pluralName;format="lower"$

import play.api.data.Form
import controllers.$pluralName;format="lower"$.routes
import forms.$className$Form
import models.{NormalMode, $className$}
import views.behaviours.QuestionViewBehaviours
import views.html.$pluralName;format="lower"$.edit$className$

class Edit$className$ViewSpec extends QuestionViewBehaviours[$className$] {

  val messageKeyPrefix = "$pluralName;format="decap"$.edit"

  val index = 0

  def createView = () => edit$className$(index, frontendAppConfig, $className$Form(), NormalMode)(fakeRequest, messages)

  def createViewUsingForm = (form: Form[$className$]) => edit$className$(index, frontendAppConfig, form, NormalMode)(fakeRequest, messages)

  override val form = $className$Form()

  "Edit $className$ view" must {

    behave like normalPage(createView, messageKeyPrefix)

    behave like pageWithBackLink(createView)

    behave like pageWithTextFields(createViewUsingForm, messageKeyPrefix, routes.Edit$className$Controller.onSubmit(index, NormalMode).url, "field1", "field2")
  }
}
