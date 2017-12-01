package forms

import play.api.data.{Form, FormError}
import play.api.data.Forms._
import play.api.data.format.Formatter

class $className$Form @Inject() (appConfig: FrontendAppConfig) extends FormErrorHelper with Constraints {

  private val $className;format="decap"$BlankKey = "error.required"

  def apply(): Form[String] = Form("value" -> text.verifying(nonEmpty($className;format="decap"$BlankKey)))
}
