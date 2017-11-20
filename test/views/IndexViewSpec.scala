/*
 * Copyright 2017 HM Revenue & Customs
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

package views

import controllers.routes
import models.NormalMode
import views.behaviours.ViewBehaviours
import views.html.index

class IndexViewSpec extends ViewBehaviours {

  def view = () => index(frontendAppConfig, call)(fakeRequest, messages)

  val call = routes.FullNameController.onPageLoad(NormalMode)

  "Index view" must {

    behave like normalPage(view, "index", "guidance")
  }

  "link should direct the user to capacity registered page" in {
    val doc = asDocument(view())
    //assertContainsText(doc, "apply for the National Minimum Wage social care compliance scheme.")
    doc.getElementById("start-now").attr("href") must include("/fullName")
  }
}
