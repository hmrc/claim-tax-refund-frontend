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
import forms.BooleanForm
import models.SelectTaxYear.CustomTaxYear
import models.{NormalMode, OtherBenefit}
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import utils.{FakeNavigator, MockUserAnswers}
import views.html.removeOtherSelectedOption

import scala.concurrent.ExecutionContext.Implicits.global

class RemoveOtherSelectedOptionControllerSpec extends ControllerSpecBase with MockitoSugar with GuiceOneAppPerSuite {

  def onwardRoute: Call = routes.IndexController.onPageLoad

  val formProvider = new BooleanForm()
  val form = formProvider()
  val removeOtherSelectedOption: removeOtherSelectedOption = fakeApplication().injector.instanceOf[removeOtherSelectedOption]

  private val taxYear = CustomTaxYear(2017)
  private val mockUserAnswers = MockUserAnswers.benefitsUserAnswers()
  private val collectionId = OtherBenefit.collectionId

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new RemoveOtherSelectedOptionController(messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction(authConnector, frontendAppConfig),
      dataRetrievalAction, new DataRequiredActionImpl(messagesControllerComponents), removeOtherSelectedOption, messagesControllerComponents, formProvider)

  def viewAsString(form: Form[_] = form): String =
    removeOtherSelectedOption(form, NormalMode, taxYear, collectionId)(fakeRequest, messages).toString

  "RemoveOtherSelectedOption Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, collectionId)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      when(mockUserAnswers.removeOtherSelectedOption) thenReturn Some(true)
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, collectionId)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form)
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true")).withMethod("POST")
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode, collectionId)(postRequest)

      status(result) mustBe SEE_OTHER
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value")).withMethod("POST")
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller(fakeDataRetrievalAction()).onSubmit(NormalMode, collectionId)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(NormalMode, collectionId)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true")).withMethod("POST")
      val result = controller(dontGetAnyData).onSubmit(NormalMode, collectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }
  }
}
