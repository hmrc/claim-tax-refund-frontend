package forms

import forms.behaviours.FormBehaviours
import models.$className$

class $className$FormSpec extends FormBehaviours {

  val validData: Map[String, String] = Map(
    "field1" -> "value 1",
    "field2" -> "value 2"
  )

  val form = $className$Form()

  "$className$ form" must {
    behave like questionForm[$className$]($className$("value 1", "value 2"))

    behave like formWithMandatoryTextFields("field1", "field2")
  }
}
