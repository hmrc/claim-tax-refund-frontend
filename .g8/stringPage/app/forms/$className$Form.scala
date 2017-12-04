package forms

import com.google.inject.Inject
import config.FrontendAppConfig
import play.api.data.Form
import play.api.data.Forms._

class $className$Form @Inject() (appConfig: FrontendAppConfig) extends FormErrorHelper with Constraints {

  private val $className;format="decap"$BlankKey = "error.required"

  def apply(): Form[String] = Form("value" -> text.verifying(nonEmpty($className;format="decap"$BlankKey)))
}
