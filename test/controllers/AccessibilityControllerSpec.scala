/*
 * Copyright 2020 HM Revenue & Customs
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

import play.api.test.Helpers._
import views.html.accessibility
import java.net.URL

import org.scalatestplus.play.guice.GuiceOneAppPerSuite

class AccessibilityControllerSpec extends ControllerSpecBase with GuiceOneAppPerSuite {

  val url = "http://www.example.com/url"
  val accessibility = fakeApplication.injector.instanceOf[accessibility]

  "Accessibility Controller" must {
    "return 200 for a GET" in {
      val result = new AccessibilityController(frontendAppConfig, accessibility, messagesControllerComponents, formPartialRetriever, scalate).onPageLoad()(fakeRequest.withHeaders(("referer", url)))
      status(result) mustBe OK
    }

    "return the correct view for a GET" in {
      val result = new AccessibilityController(frontendAppConfig, accessibility, messagesControllerComponents, formPartialRetriever, scalate).onPageLoad()(fakeRequest.withHeaders(("referer", url)))
      contentAsString(result) mustBe accessibility(frontendAppConfig, new URL(url).getPath)(fakeRequest, messages, formPartialRetriever, scalate).toString
    }
  }
}
