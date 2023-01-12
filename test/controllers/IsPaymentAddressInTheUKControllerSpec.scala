/*
 * Copyright 2023 HM Revenue & Customs
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

import connectors.{AddressLookupConnector, FakeDataCacheConnector}
import controllers.actions._
import forms.BooleanForm
import models.NormalMode
import models.SelectTaxYear.CustomTaxYear
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import utils.{FakeNavigator, MockUserAnswers}
import views.html.isPaymentAddressInTheUK

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class IsPaymentAddressInTheUKControllerSpec extends ControllerSpecBase with MockitoSugar with GuiceOneAppPerSuite {

  def onwardRoute: Call = routes.IndexController.onPageLoad

  val formProvider = new BooleanForm()
  val form = formProvider()
  val isPaymentAddressInTheUK: isPaymentAddressInTheUK = fakeApplication.injector.instanceOf[isPaymentAddressInTheUK]
  private val mockAddressLookup = mock[AddressLookupConnector]
  private val taxYear = CustomTaxYear(2017)
  private val mockUserAnswers = MockUserAnswers.minimalValidUserAnswers()

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new IsPaymentAddressInTheUKController(frontendAppConfig, messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction(authConnector, frontendAppConfig),
      dataRetrievalAction, new DataRequiredActionImpl(messagesControllerComponents), isPaymentAddressInTheUK, messagesControllerComponents, formProvider, mockAddressLookup)

  def viewAsString(form: Form[_] = form) : String = isPaymentAddressInTheUK(
    frontendAppConfig,
    form,
    NormalMode,
    taxYear
  )(fakeRequest, messages).toString

  "IsPaymentAddressInTheUK Controller" must {

    "return OK and the correct view for a GET" in {
      when (mockAddressLookup.initialise(any(), any())(any(), any())) thenReturn Future.successful(None)
      val result = controller(fakeDataRetrievalAction()).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      when (mockAddressLookup.initialise(any(), any())(any(), any())).thenReturn(Future.successful(None))
      when (mockUserAnswers.isPaymentAddressInTheUK) thenReturn Some(true)
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(value = true))
    }

    "redirect to address lookup address when able to connect to the api" in {
      val url: String = "http://localhost:9028/lookup-address/ca36139b-cee5-4a99-902c-ce7b9963d7ce/lookup"
      when (mockAddressLookup.initialise(any(), any())(any(), any())).thenReturn(Future.successful(Some(url)))
      val result = controller(fakeDataRetrievalAction()).onPageLoad(NormalMode)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("http://localhost:9028/lookup-address/ca36139b-cee5-4a99-902c-ce7b9963d7ce/lookup")
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true")).withMethod("POST")
      val result = controller(fakeDataRetrievalAction()).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value")).withMethod("POST")
      val boundForm = form.bind(Map("value" -> "invalid value"))

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
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true")).withMethod("POST")
      val result = controller(dontGetAnyData).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }
  }
}