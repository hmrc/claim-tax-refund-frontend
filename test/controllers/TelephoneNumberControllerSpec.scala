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

import connectors.{AddressLookupConnector, FakeDataCacheConnector}
import controllers.actions._
import forms.TelephoneNumberForm
import identifiers.{AnyTelephoneId, TelephoneNumberId}
import models._
import models.requests.DataRequest
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import play.api.data.Form
import play.api.libs.json.{JsBoolean, JsString, Json}
import play.api.mvc.{Call, Request}
import play.api.test.Helpers._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient
import utils.{FakeNavigator, MockUserAnswers}
import views.html.telephoneNumber

import scala.concurrent.{ExecutionContext, Future}

class TelephoneNumberControllerSpec extends ControllerSpecBase with MockitoSugar with ScalaFutures {

  def onwardRoute: Call = routes.IndexController.onPageLoad()

  implicit val hc: HeaderCarrier = HeaderCarrier()
  implicit val ec: ExecutionContext = mock[ExecutionContext]
  implicit val request: Request[_] = mock[Request[_]]
  implicit val dataCacheConnector = FakeDataCacheConnector
  implicit val dataRequest =  mock[DataRequest[_]]


  val testAnswer = "0191 111 1111"
  val formProvider = new TelephoneNumberForm()
  val form = formProvider()
  val httpMock = mock[HttpClient]
  val mockAddressLookup: AddressLookupConnector = new AddressLookupConnector(frontendAppConfig, httpMock, messagesApi, dataCacheConnector)
  val validYesData = Map(AnyTelephoneId.toString -> Json.obj(AnyTelephoneId.toString -> JsBoolean(true), TelephoneNumberId.toString -> JsString(testAnswer)))
  val validNoData = Map(AnyTelephoneId.toString -> Json.obj(AnyTelephoneId.toString -> JsBoolean(false)))
  private val mockUserAnswers = MockUserAnswers.claimDetailsUserAnswers

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new TelephoneNumberController(frontendAppConfig, messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction,
      dataRetrievalAction, new DataRequiredActionImpl, formProvider, formPartialRetriever, templateRenderer)
  def viewAsString(form: Form[_] = form) = telephoneNumber(frontendAppConfig, form, NormalMode)(fakeRequest, messages, formPartialRetriever, templateRenderer).toString

  "TelephoneNumberController" must {

    "return OK and the correct view for a GET" in {
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, None)(fakeRequest)
      status(result) mustBe OK
    }

    "populate the view correctly on a GET when YES has previously been answered" in {
      when(mockUserAnswers.anyTelephoneNumber).thenReturn(Some(TelephoneOption.Yes(testAnswer)))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, None)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(TelephoneOption.Yes(testAnswer)))
    }

    "populate the view correctly on a GET when NO has previously been answered" in {
      when(mockUserAnswers.anyTelephoneNumber).thenReturn(Some(TelephoneOption.No))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(CheckMode, None)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(TelephoneOption.No))
    }

    "redirect to the next page when valid YES data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("anyTelephoneNumber", "true"),("telephoneNumber", testAnswer))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "redirect to the next page when valid NO data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("anyTelephoneNumber", "false"))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(NormalMode, None)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(dontGetAnyData).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "stay on this page in CheckMode when no ID in URL" in {
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(CheckMode, None)(fakeRequest)

      status(result) mustBe OK
    }

    "redirect to CYA in CheckMode when there is an ID in the URL" in {
      when(request.getQueryString(key ="id")).thenReturn(Some("123445"))
      when(mockAddressLookup.getAddress(cacheId ="",
        saveKey = "",
        id = "")
      ).thenReturn(Future.successful(???))
      when(mockUserAnswers.anyTelephoneNumber).thenReturn(Some(TelephoneOption.No))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(CheckMode, None)(fakeRequest)

      status(result) mustBe SEE_OTHER
    }
  }
}
