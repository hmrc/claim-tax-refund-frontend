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

import connectors.{FakeDataCacheConnector, TaiConnector}
import controllers.actions._
import forms.BooleanForm
import models.requests.{AuthenticatedRequest, OptionalDataRequest}
import models.{Employment, NormalMode}
import org.mockito.Matchers
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core.retrieve.{ItmpAddress, ItmpName}
import utils.{FakeNavigator, MockUserAnswers}
import views.html.taiEmploymentDetails

import scala.concurrent.Future

class TaiEmploymentDetailsControllerSpec extends ControllerSpecBase with MockitoSugar {

  def onwardRoute = routes.IndexController.onPageLoad()

  val formProvider = new BooleanForm()
  val form = formProvider()
  val mockTaiConnector = mock[TaiConnector]
  val mockUserAnswers = MockUserAnswers.yourDetailsUserAnswers

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new TaiEmploymentDetailsController(frontendAppConfig, messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction,
      dataRetrievalAction, new DataRequiredActionImpl, formProvider, mockTaiConnector)

  def viewAsString(form: Form[_] = form) = taiEmploymentDetails(frontendAppConfig, form, NormalMode,
    Seq(Employment("AVIVA PENSIONS", "754", "AZ00070")))(fakeRequest, messages).toString

  val fakeDataRetrievalAction = new DataRetrievalAction {
    override protected def transform[A](request: AuthenticatedRequest[A]): Future[OptionalDataRequest[A]] = {
      Future.successful(OptionalDataRequest(request, "123123", ItmpName(Some("sdadsad"), Some("sdfasfad"), Some("adfsdfa")), "AB123456A",
        ItmpAddress(None, None, None, None, None, None, None, None),
        Some(mockUserAnswers)))
    }
  }

  "TaiEmploymentDetails Controller" must {

    "return OK and the correct view for a GET" in {
      when(mockUserAnswers.taiEmploymentDetails).thenReturn(None)
      when(mockTaiConnector.taiEmployments(Matchers.eq("AB123456A"), Matchers.eq(2016))(Matchers.any(), Matchers.any()))
        .thenReturn(Future.successful(Seq(Employment("AVIVA PENSIONS", "754", "AZ00070"))))

      val result = controller(fakeDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      when(mockUserAnswers.taiEmploymentDetails).thenReturn(Some(true))
      when(mockTaiConnector.taiEmployments(Matchers.eq("AB123456A"), Matchers.eq(2016))(Matchers.any(), Matchers.any()))
        .thenReturn(Future.successful(Seq(Employment("AVIVA PENSIONS", "754", "AZ00070"))))

      val result = controller(fakeDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "redirect to the next page when valid data is submitted" in {
      when(mockUserAnswers.taiEmploymentDetails).thenReturn(None)
      when(mockTaiConnector.taiEmployments(Matchers.eq("AB123456A"), Matchers.eq(2016))(Matchers.any(), Matchers.any()))
        .thenReturn(Future.successful(Seq(Employment("AVIVA PENSIONS", "754", "AZ00070"))))

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(fakeDataRetrievalAction).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      when(mockUserAnswers.taiEmploymentDetails).thenReturn(None)
      when(mockTaiConnector.taiEmployments(Matchers.eq("AB123456A"), Matchers.eq(2016))(Matchers.any(), Matchers.any()))
        .thenReturn(Future.successful(Seq(Employment("AVIVA PENSIONS", "754", "AZ00070"))))

      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))
      val result = controller(fakeDataRetrievalAction).onSubmit(NormalMode)(postRequest)

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

    "redirect to Session Expired if call to tai has failed" in {
      when(mockUserAnswers.taiEmploymentDetails).thenReturn(None)

      when(mockTaiConnector.taiEmployments(Matchers.eq("AB123456A"), Matchers.eq(2016))(Matchers.any(), Matchers.any()))
        .thenReturn(Future.failed(new Exception("Couldnt find tai details")))

      val result = controller(fakeDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired if no taxYears have been selected" in {
      when(mockUserAnswers.taiEmploymentDetails).thenReturn(None)
      when(mockUserAnswers.selectTaxYear).thenReturn(None)

      val result = controller(fakeDataRetrievalAction).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired if no taxYears have been selected on submit" in {
      when(mockUserAnswers.taiEmploymentDetails).thenReturn(None)
      when(mockUserAnswers.selectTaxYear).thenReturn(None)

      val result = controller(fakeDataRetrievalAction).onSubmit(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }
  }
}
