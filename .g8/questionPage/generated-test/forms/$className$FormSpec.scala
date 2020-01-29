package forms

import config.FrontendAppConfig
import forms.behaviours.FormBehaviours
import models.{$className$, MandatoryField}
import org.scalatestplus.mockito.MockitoSugar
import play.api.data.Form
import org.mockito.Mockito._

class $className$FormSpec extends FormBehaviours with MockitoSugar {

  def appConfig: FrontendAppConfig = {
    val instance = mock[FrontendAppConfig]
    instance
  }

  private val field1ErrorKeyBlank = "error.required"
  private val field2ErrorKeyBlank = "error.required"

  val validData: Map[String, String] = Map(
    "field1" -> "value 1",
    "field2" -> "value 2"
  )

  override val form: Form[_] = new $className$Form(appConfig)()

  "$className$ form" must {
    behave like questionForm($className$("value 1", "value 2"))

    behave like formWithMandatoryTextFields(
      MandatoryField("field1", field1ErrorKeyBlank),
      MandatoryField("field2", field2ErrorKeyBlank)
    )
  }
}
