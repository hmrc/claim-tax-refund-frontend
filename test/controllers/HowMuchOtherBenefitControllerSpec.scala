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
import forms.HowMuchOtherBenefitForm
import models.{Index, NormalMode}
import models.SelectTaxYear.CYMinus2
import org.mockito.Mockito.when
import play.api.mvc.Call
import views.html.howMuchOtherBenefit

class HowMuchOtherBenefitControllerSpec extends ControllerSpecBase {

  def onwardRoute: Call = routes.IndexController.onPageLoad()

  val testAnswer = Seq("9,999.99", "1,000.00")
  val form = new HowMuchOtherBenefitForm(frontendAppConfig)()
  private val taxYear = CYMinus2
  private val otherBenefitName = Seq("test benefit 1", "test benefit 2")
  private val mockUserAnswers = MockUserAnswers.claimDetailsUserAnswers

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new HowMuchOtherBenefitController(frontendAppConfig, messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction,
      dataRetrievalAction, new DataRequiredActionImpl, sequenceUtil, new HowMuchOtherBenefitForm(frontendAppConfig))

  def viewAsString(form: Form[_] = form, index: Index): String = howMuchOtherBenefit(frontendAppConfig, form, NormalMode, taxYear, otherBenefitName(index), index)(fakeRequest, messages).toString

  "HowMuchOtherBenefit Controller" must {

    "return OK and the correct view for a GET" in {
      when(mockUserAnswers.otherBenefitsName).thenReturn(Some(otherBenefitName))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, 0)(fakeRequest)
      val result1 = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, 1)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(form, 0)
      contentAsString(result1) mustBe viewAsString(form, 1)
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      when(mockUserAnswers.otherBenefitsName).thenReturn(Some(otherBenefitName))
      when(mockUserAnswers.howMuchOtherBenefit).thenReturn(Some(testAnswer))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, 0)(fakeRequest)
      val result1 = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, 1)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(testAnswer.head), 0)
      contentAsString(result1) mustBe viewAsString(form.fill(testAnswer(1)), 1)
    }

    "redirect to the next page when valid data is submitted" in {
      when(mockUserAnswers.otherBenefitsName).thenReturn(Some(otherBenefitName))
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", testAnswer.head))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode, 0)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      when(mockUserAnswers.otherBenefitsName).thenReturn(Some(otherBenefitName))
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", ""))
      val boundForm = form.bind(Map("value" -> ""))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode, 0)(postRequest)
      val result1 = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode, 1)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm, 0)
      contentAsString(result1) mustBe viewAsString(boundForm, 1)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(NormalMode, 0)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", testAnswer.head))
      val result = controller(dontGetAnyData).onSubmit(NormalMode, 0)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired if no taxYears have been selected" in {
      when(mockUserAnswers.selectTaxYear).thenReturn(None)

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, 0)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired if no taxYears have been selected on submit" in {
      when(mockUserAnswers.selectTaxYear).thenReturn(None)

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode, 0)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired if no other benefit name have been selected" in {
      when(mockUserAnswers.otherBenefitsName).thenReturn(None)

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, 0)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired if no other benefit name have been selected on submit" in {
      when(mockUserAnswers.otherBenefitsName).thenReturn(None)

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode, 0)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }
  }
}
