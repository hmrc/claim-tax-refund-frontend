package forms

import config.FrontendAppConfig
import forms.behaviours.FormBehaviours
import models.MaxLengthField
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import play.api.data.Form

class $className$FormSpec extends FormSpec with MockitoSugar {

  val errorKeyBlank = "blank"

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    instance
  }

  val validData: Map[String, String] = Map("value" -> "test answer")

  override val form: Form[_] = new FullNameForm(appConfig)()

  "$className$ Form" must {

    behave like questionForm(String("value"))

    behave like formWithMandatoryTextFieldsAndCustomKey(("value", errorKeyBlank))
  }
}
