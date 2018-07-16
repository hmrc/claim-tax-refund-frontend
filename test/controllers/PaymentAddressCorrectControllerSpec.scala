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

import connectors.FakeDataCacheConnector
import controllers.actions._
import forms.BooleanForm
import identifiers.PaymentAddressCorrectId
import models.NormalMode
import models.requests.{AuthenticatedRequest, OptionalDataRequest}
import play.api.data.Form
import play.api.libs.json.JsBoolean
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core.retrieve.{ItmpAddress, ItmpName}
import uk.gov.hmrc.http.cache.client.CacheMap
import utils.{FakeNavigator, MockUserAnswers, UserAnswers}
import views.html.paymentAddressCorrect

import scala.concurrent.Future

class PaymentAddressCorrectControllerSpec extends ControllerSpecBase {

  def onwardRoute = routes.IndexController.onPageLoad()

  val formProvider = new BooleanForm()
  val form = formProvider()

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new PaymentAddressCorrectController(frontendAppConfig, messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction,
      dataRetrievalAction, new DataRequiredActionImpl, formProvider)

  def fakeDataRetrievalActionNoAddress(mockUserAnswers: UserAnswers = MockUserAnswers.claimDetailsUserAnswers) = new DataRetrievalAction {
    override protected def transform[A](request: AuthenticatedRequest[A]): Future[OptionalDataRequest[A]] = {
      Future.successful(
        OptionalDataRequest(
          request,
          "123123",
          "AB123456A",
          Some(ItmpName(Some("sdadsad"), None, None)),
          Some(ItmpAddress(None, None, None, None, None, None, None, None)),
          Some(mockUserAnswers))
      )
    }
  }

  def viewAsString(form: Form[_] = form) = paymentAddressCorrect(frontendAppConfig, form, NormalMode, itmpAddress)(fakeRequest, messages).toString

  "PaymentAddressCorrect Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val validData = Map(PaymentAddressCorrectId.toString -> JsBoolean(true))
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(dontGetAnyData).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to is address in UK if no itmp address information is present" in {
      val result = controller(fakeDataRetrievalActionNoAddress()).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.IsPaymentAddressInTheUKController.onPageLoad(NormalMode).url)
    }

    "redirect to is address in UK if no itmp address information is present on submit" in {
      val result = controller(fakeDataRetrievalActionNoAddress()).onSubmit(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.IsPaymentAddressInTheUKController.onPageLoad(NormalMode).url)
    }
  }
}
