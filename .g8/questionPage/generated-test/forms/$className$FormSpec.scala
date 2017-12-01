package forms

import forms.behaviours.FormBehaviours
import models.$className$
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form
import org.mockito.Mockito._

class $className$FormSpec extends FormBehaviours with MockitoSugar {

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    instance
  }

  val validData: Map[String, String] = Map(
    "field1" -> "value 1",
    "field2" -> "value 2"
  )

  override val form: Form[_] = $className$Form(appConfig)()

  "$className$ form" must {
    behave like questionForm($className$("value 1", "value 2"))

    behave like formWithMandatoryTextFields("field1", "field2")
  }
}
