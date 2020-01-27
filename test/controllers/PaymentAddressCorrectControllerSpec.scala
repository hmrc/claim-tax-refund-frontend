/*
 * Copyright 2020 HM Revenue & Customs
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
import models.NormalMode
import models.SelectTaxYear.CustomTaxYear
import models.requests.{AuthenticatedRequest, OptionalDataRequest}
import org.mockito.Mockito.when
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core.retrieve.{ItmpAddress, ItmpName}
import utils.{FakeNavigator, MockUserAnswers}
import views.html.paymentAddressCorrect

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class PaymentAddressCorrectControllerSpec extends ControllerSpecBase {

  def onwardRoute: Call = routes.IndexController.onPageLoad()

  val formProvider = new BooleanForm()
  val form = formProvider()
  private val taxYear = CustomTaxYear(2017)
  private val mockUserAnswers = MockUserAnswers.minimalValidUserAnswers()

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new PaymentAddressCorrectController(frontendAppConfig, messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction(authConnector, frontendAppConfig),
      dataRetrievalAction, new DataRequiredActionImpl(messagesControllerComponents), messagesControllerComponents, formProvider, formPartialRetriever, scalate)

  def fakeDataRetrievalActionItmpAddress(itmpAddress: Option[ItmpAddress]): DataRetrievalAction = new DataRetrievalAction {
    override protected def executionContext: ExecutionContext = implicitly[ExecutionContext]

    override protected def transform[A](request: AuthenticatedRequest[A]): Future[OptionalDataRequest[A]] = {
      Future.successful(
        OptionalDataRequest(
          request = request,
          externalId = "123123",
          nino = "AB123456A",
          name = Some(ItmpName(Some("sdadsad"), None, None)),
          address = itmpAddress,
          userAnswers = Some(MockUserAnswers.claimDetailsUserAnswers()))
      )
    }
  }

  val emptyItmpAddress: ItmpAddress = ItmpAddress(
    line1 = None,
    line2 = None,
    line3 = None,
    line4 = None,
    line5 = None,
    postCode = None,
    countryName = None,
    countryCode = None
  )

  val ukItmpAddress: ItmpAddress = ItmpAddress(
    line1 = Some("1 some street"),
    line2 = None,
    line3 = None,
    line4 = None,
    line5 = None,
    postCode = Some("NE31YH"),
    countryName = Some("United Kingdom"),
    countryCode = Some("UK")
  )

  val internationalItmpAddress: ItmpAddress = ItmpAddress(
    line1 = Some("La Rue De Bastille"),
    line2 = None,
    line3 = None,
    line4 = None,
    line5 = None,
    postCode = None,
    countryName = Some("France"),
    countryCode = Some("FR")
  )

  def viewAsString(form: Form[_] = form): String =
    paymentAddressCorrect(
      frontendAppConfig,
      form,
      NormalMode,
      itmpAddress,
      taxYear)(fakeRequest, messages, formPartialRetriever, scalate).toString

  "PaymentAddressCorrect Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller(fakeDataRetrievalActionItmpAddress(Some(itmpAddress))).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      when(mockUserAnswers.paymentAddressCorrect).thenReturn(Some(true))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(true))
    }

    "redirect to the next page when valid data TRUE is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller(fakeDataRetrievalAction()).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "redirect to the next page when valid data FALSE is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "false"))

      val result = controller(fakeDataRetrievalAction()).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

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
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(dontGetAnyData).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to is address in UK if no itmp address information is present on load" in {
      val result = controller(fakeDataRetrievalActionItmpAddress(Some(emptyItmpAddress))).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.IsPaymentAddressInTheUKController.onPageLoad(NormalMode).url)
    }

    "redirect to is address in UK if international ITMP address on page load" in {
      val result = controller(fakeDataRetrievalActionItmpAddress(Some(internationalItmpAddress))).onPageLoad(NormalMode)(fakeRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.IsPaymentAddressInTheUKController.onPageLoad(NormalMode).url)
    }

    "load paymentAddressCorrect if valid uk address on page load" in {
      val result = controller(fakeDataRetrievalActionItmpAddress(Some(ukItmpAddress))).onPageLoad(NormalMode)(fakeRequest)
      status(result) mustBe OK
    }

    "redirect to paymentAddressCorrect if valid uk address on submit" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(fakeDataRetrievalActionItmpAddress(Some(ukItmpAddress))).onSubmit(NormalMode)(postRequest)
      status(result) mustBe SEE_OTHER
    }
  }
}
