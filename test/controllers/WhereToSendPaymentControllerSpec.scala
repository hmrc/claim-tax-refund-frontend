/*
 * Copyright 2022 HM Revenue & Customs
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

import connectors.FakeDataCacheConnector
import controllers.actions._
import forms.WhereToSendPaymentForm
import models.SelectTaxYear.CustomTaxYear
import models.{NormalMode, WhereToSendPayment}
import org.mockito.Mockito.when
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.api.test.Helpers._
import utils.{FakeNavigator, MockUserAnswers}
import views.html.whereToSendPayment

import scala.concurrent.ExecutionContext.Implicits.global

class WhereToSendPaymentControllerSpec extends ControllerSpecBase with GuiceOneAppPerSuite {

  private val taxYear = CustomTaxYear(2017)
  private val mockUserAnswers = MockUserAnswers.minimalValidUserAnswers()
  private val whereToSendPayment: whereToSendPayment = fakeApplication.injector.instanceOf[whereToSendPayment]

  def onwardRoute = routes.IndexController.onPageLoad

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new WhereToSendPaymentController(messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction(authConnector, frontendAppConfig),
      dataRetrievalAction, new DataRequiredActionImpl(messagesControllerComponents), whereToSendPayment, messagesControllerComponents)

  def viewAsString(form: Form[_] = WhereToSendPaymentForm()) =
    whereToSendPayment(form, NormalMode, taxYear)(fakeRequest, messages).toString

  "WhereToSendPayment Controller" must {

    "return OK and the correct view for a GET" in {
      when(mockUserAnswers.whereToSendPayment).thenReturn(None)
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      when(mockUserAnswers.whereToSendPayment).thenReturn(Some(WhereToSendPayment.values.head))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(WhereToSendPaymentForm().fill(WhereToSendPayment.values.head))
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", WhereToSendPaymentForm.options.head.value)).withMethod("POST")

      val result = controller(fakeDataRetrievalAction()).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value")).withMethod("POST")
      val boundForm = WhereToSendPaymentForm().bind(Map("value" -> "invalid value"))

      val result = controller(fakeDataRetrievalAction()).onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", WhereToSendPaymentForm.options.head.value)).withMethod("POST")
      val result = controller(dontGetAnyData).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }
  }
}
