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

import connectors.{FakeDataCacheConnector, TaiConnector}
import controllers.actions._
import forms.BooleanForm
import models.SelectTaxYear.CustomTaxYear
import models.{Employment, NormalMode}
import org.mockito.Matchers
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import utils.{FakeNavigator, MockUserAnswers}
import views.html.employmentDetails

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EmploymentDetailsControllerSpec extends ControllerSpecBase with MockitoSugar with GuiceOneAppPerSuite {

  def onwardRoute: Call = routes.IndexController.onPageLoad()
  def noTaiRoute: Call = routes.EnterPayeReferenceController.onPageLoad(NormalMode)

  val formProvider = new BooleanForm()
  val form: Form[Boolean] = formProvider()
  val mockTaiConnector: TaiConnector = mock[TaiConnector]
  val employmentDetails: employmentDetails = fakeApplication.injector.instanceOf[employmentDetails]
  private val taxYear = CustomTaxYear(2017)
  private val mockUserAnswers = MockUserAnswers.claimDetailsUserAnswers()


  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new EmploymentDetailsController(messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction(authConnector, frontendAppConfig),
      dataRetrievalAction, new DataRequiredActionImpl(messagesControllerComponents), employmentDetails, messagesControllerComponents, formProvider, mockTaiConnector)

  def viewAsString(form: Form[_] = form): String = employmentDetails(form, NormalMode,
    Seq(Employment("AVIVA PENSIONS", "754", "AZ00070")), taxYear)(fakeRequest, messages).toString

  "EmploymentDetails Controller" must {

    "return OK and the correct view for a GET" in {
      when(mockUserAnswers.employmentDetails).thenReturn(None)
      when(mockTaiConnector.taiEmployments(Matchers.eq("AB123456A"), Matchers.eq(2017))(Matchers.any(), Matchers.any()))
        .thenReturn(Future.successful(Seq(Employment("AVIVA PENSIONS", "754", "AZ00070"))))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      when(mockUserAnswers.employmentDetails).thenReturn(Some(true))
      when(mockTaiConnector.taiEmployments(Matchers.eq("AB123456A"), Matchers.eq(2017))(Matchers.any(), Matchers.any()))
        .thenReturn(Future.successful(Seq(Employment("AVIVA PENSIONS", "754", "AZ00070"))))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "redirect to the next page when valid data is submitted" in {
      when(mockUserAnswers.employmentDetails).thenReturn(None)
      when(mockTaiConnector.taiEmployments(Matchers.eq("AB123456A"), Matchers.eq(2017))(Matchers.any(), Matchers.any()))
        .thenReturn(Future.successful(Seq(Employment("AVIVA PENSIONS", "754", "AZ00070"))))

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      when(mockUserAnswers.employmentDetails).thenReturn(None)
      when(mockTaiConnector.taiEmployments(Matchers.eq("AB123456A"), Matchers.eq(2017))(Matchers.any(), Matchers.any()))
        .thenReturn(Future.successful(Seq(Employment("AVIVA PENSIONS", "754", "AZ00070"))))

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode)(postRequest)

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

    "redirect to paye reference page when call to tai returns nothing" in {
      when(mockUserAnswers.employmentDetails).thenReturn(None)

      when(mockTaiConnector.taiEmployments(Matchers.eq("AB123456A"), Matchers.eq(2017))(Matchers.any(), Matchers.any()))
        .thenReturn(Future.failed(new Exception("Couldnt find tai details")))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(noTaiRoute.url)
    }

    "redirect to Session Expired if no taxYears have been selected" in {
      when(mockUserAnswers.employmentDetails).thenReturn(None)
      when(mockUserAnswers.selectTaxYear).thenReturn(None)

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired if no taxYears have been selected on submit" in {
      when(mockUserAnswers.employmentDetails).thenReturn(None)
      when(mockUserAnswers.selectTaxYear).thenReturn(None)

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }
  }
}
