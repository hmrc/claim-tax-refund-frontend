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

import play.api.data.Form
import utils.{FakeNavigator, MockUserAnswers}
import connectors.FakeDataCacheConnector
import controllers.actions._
import play.api.test.Helpers._
import forms.HowMuchOtherTaxableIncomeForm
import models.NormalMode
import models.SelectTaxYear.CYMinus2
import org.mockito.Mockito.when
import views.html.howMuchOtherTaxableIncome


class HowMuchOtherTaxableIncomeControllerSpec extends ControllerSpecBase {

  def onwardRoute = routes.IndexController.onPageLoad()

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new HowMuchOtherTaxableIncomeController(frontendAppConfig, messagesApi, FakeDataCacheConnector,
      new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction,
      dataRetrievalAction, new DataRequiredActionImpl, new HowMuchOtherTaxableIncomeForm(frontendAppConfig))

  val mockUserAnswers = MockUserAnswers.yourDetailsUserAnswers

  val testAnswer = "9,999.99"
  def taxYear = CYMinus2.asString

  val form = new HowMuchOtherTaxableIncomeForm(frontendAppConfig)()

  def viewAsString(form: Form[_] = form) = howMuchOtherTaxableIncome(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages).toString

  "howMuchOtherTaxableIncome Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller(someData).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      when (mockUserAnswers.howMuchOtherTaxableIncome).thenReturn(Some(testAnswer))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(testAnswer))
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", testAnswer))

      val result = controller(fakeDataRetrievalAction()).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", ""))
      val boundForm = form.bind(Map("value" -> ""))

      val result = controller(fakeDataRetrievalAction()).onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", testAnswer))
      val result = controller(dontGetAnyData).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }
  }

  "redirect to Session Expired if no taxYears have been selected" in {
    when(mockUserAnswers.selectTaxYear).thenReturn(None)

    val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

    status(result) mustBe SEE_OTHER
    redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
  }

  "redirect to Session Expired if no taxYears have been selected on submit" in {
    when(mockUserAnswers.selectTaxYear).thenReturn(None)

    val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode)(fakeRequest)

    status(result) mustBe SEE_OTHER
    redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
  }
}
