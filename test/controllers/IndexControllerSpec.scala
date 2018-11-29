/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import forms.SelectTaxYearForm
import models.NormalMode
import play.api.data.Form
import play.api.test.Helpers._
import views.html.selectTaxYear
import scala.concurrent.ExecutionContext.Implicits.global

class IndexControllerSpec extends ControllerSpecBase {

  def viewAsString(form: Form[_] = SelectTaxYearForm()) = selectTaxYear(frontendAppConfig, form, NormalMode)(fakeRequest, messages, formPartialRetriever, scalate).toString

  "Index Controller" must {
    "return redirect for a GET and redirect to select tax year" in {
      val result = new IndexController(frontendAppConfig, messagesApi).onPageLoad()(fakeRequest)
      status(result) mustBe TEMPORARY_REDIRECT
      redirectLocation(result) mustBe Some(routes.SelectTaxYearController.onPageLoad(NormalMode).url)
    }
  }
}
