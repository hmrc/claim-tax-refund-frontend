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

import connectors.FakeDataCacheConnector
import controllers.actions._
import forms.HowMuchIncapacityBenefitForm
import models.NormalMode
import models.SelectTaxYear.CustomTaxYear
import org.mockito.Mockito.when
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.api.test.Helpers._
import utils.{FakeNavigator, MockUserAnswers}
import views.html.howMuchIncapacityBenefit

import scala.concurrent.ExecutionContext.Implicits.global

class HowMuchIncapacityBenefitControllerSpec extends ControllerSpecBase with GuiceOneAppPerSuite {

  private val testAnswer = "9,999.99"
  private val form = new HowMuchIncapacityBenefitForm(frontendAppConfig)()
  private val taxYear = CustomTaxYear(2017)
  private val mockUserAnswers = MockUserAnswers.claimDetailsUserAnswers()
  private val howMuchIncapacityBenefit: howMuchIncapacityBenefit = fakeApplication().injector.instanceOf[howMuchIncapacityBenefit]

  def onwardRoute = routes.IndexController.onPageLoad

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new HowMuchIncapacityBenefitController(messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction(authConnector, frontendAppConfig),
      dataRetrievalAction, new DataRequiredActionImpl(messagesControllerComponents), howMuchIncapacityBenefit, messagesControllerComponents, new HowMuchIncapacityBenefitForm(frontendAppConfig))

  def viewAsString(form: Form[_] = form) = howMuchIncapacityBenefit(form, NormalMode, taxYear)(fakeRequest, messages).toString

  "HowMuchIncapacityBenefit Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller(fakeDataRetrievalAction()).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      when(mockUserAnswers.howMuchIncapacityBenefit).thenReturn(Some(testAnswer))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(testAnswer))
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", testAnswer)).withMethod("POST")
      val result = controller(fakeDataRetrievalAction()).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "")).withMethod("POST")
      val boundForm = form.bind(Map("value" -> ""))
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
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", testAnswer)).withMethod("POST")
      val result = controller(dontGetAnyData).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }

    "redirect to Session Expired if no taxYears have been selected" in {
      when(mockUserAnswers.selectTaxYear).thenReturn(None)

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }

    "redirect to Session Expired if no taxYears have been selected on submit" in {
      when(mockUserAnswers.selectTaxYear).thenReturn(None)

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }
  }
}
