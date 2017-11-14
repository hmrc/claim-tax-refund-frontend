package forms

import forms.behaviours.FormBehaviours

class $className$FormSpec extends FormBehaviours {

  val validData: Map[String, String] = Map(
    "value" -> $className$Form.options.head.value
  )

  val form = $className$Form()

  "$className$ form" must {
    behave like questionForm[String]($className$Form.options.head.value)

    behave like formWithOptionField("value", $className$Form.options.map{x => x.value}:_*)
  }
}
