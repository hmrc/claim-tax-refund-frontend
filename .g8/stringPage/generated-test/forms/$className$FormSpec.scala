package forms

import config.FrontendAppConfig
import forms.behaviours.FormBehaviours
import models.MaxLengthField
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._
import play.api.data.Form

class $className$FormSpec extends FormBehaviours with MockitoSugar {

  val errorKeyBlank = "error.required"

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    instance
  }

  val validData: Map[String, String] = Map("value" -> "test answer")

  override val form: Form[_] = new $className$Form(appConfig)()

  "$className$ Form" must {

    behave like formWithMandatoryTextFieldsAndCustomKey(("value", errorKeyBlank))
  }
}
