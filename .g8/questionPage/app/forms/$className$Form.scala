package forms

import com.google.inject.Inject
import config.FrontendAppConfig
import forms.mappings.Constraints
import play.api.data.Form
import play.api.data.Forms._
import models.$className$

class $className$Form @Inject() (appConfig: FrontendAppConfig) extends FormErrorHelper with Constraints {

  private val field1BlankKey = "error.required"
  private val field2BlankKey = "error.required"

  def apply(): Form[$className$] = {
    Form(
      mapping(
        "field1" -> text.verifying(nonEmpty(field1BlankKey)),
        "field2" -> text.verifying(nonEmpty(field2BlankKey))
      )($className$.apply)($className$.unapply)
    )
  }
}
