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

import play.api.data.Form
import play.api.libs.json.{JsBoolean, JsString, Json}
import utils.{FakeNavigator, MockUserAnswers}
import connectors.FakeDataCacheConnector
import controllers.actions._
import play.api.test.Helpers._
import forms.AnyTaxPaidForm
import identifiers.{AnyTaxPaidId, TaxPaidAmountId}
import models.SelectTaxYear.CustomTaxYear
import models.{AnyTaxPaid, NormalMode}
import org.mockito.Mockito.when
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import views.html.anyTaxableRentalIncome

import scala.concurrent.ExecutionContext.Implicits.global

class AnyTaxableRentalIncomeControllerSpec extends ControllerSpecBase with GuiceOneAppPerSuite {

  def onwardRoute = routes.IndexController.onPageLoad()

  private val notSelectedKey = "anyTaxableRentalIncome.notSelected"
  private val blankKey = "anyTaxableRentalIncome.blank"
  private val invalidKey = "anyTaxableRentalIncome.invalid"

  val testAnswer = "9,999.00"
  val validYesData = Map(AnyTaxPaidId.toString -> Json.obj(AnyTaxPaidId.toString -> JsBoolean(true), TaxPaidAmountId.toString -> JsString(testAnswer)))
  val validNoData = Map(AnyTaxPaidId.toString -> Json.obj(AnyTaxPaidId.toString -> JsBoolean(false)))
  val anyTaxableRentalIncome: anyTaxableRentalIncome = fakeApplication.injector.instanceOf[anyTaxableRentalIncome]
  private val mockUserAnswers = MockUserAnswers.claimDetailsUserAnswers()
  private val taxYear = CustomTaxYear(2017)

  val formProvider = new AnyTaxPaidForm
  private val form = formProvider(notSelectedKey, blankKey, invalidKey)


  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new AnyTaxableRentalIncomeController(frontendAppConfig, messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction(authConnector, frontendAppConfig),
      dataRetrievalAction, new DataRequiredActionImpl(messagesControllerComponents), anyTaxableRentalIncome, messagesControllerComponents, formProvider, formPartialRetriever, templateRenderer)

  def viewAsString(form: Form[_] = form) = anyTaxableRentalIncome(frontendAppConfig, form, NormalMode, taxYear)(fakeRequest, messages, formPartialRetriever, templateRenderer).toString

  "AnyTaxableRentalIncome Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller(fakeDataRetrievalAction()).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when YES has previously been answered" in {
      when(mockUserAnswers.anyTaxableRentalIncome).thenReturn(Some(AnyTaxPaid.Yes(testAnswer)))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(AnyTaxPaid.Yes(testAnswer)))
    }

    "populate the view correctly on a GET when NO has previously been answered" in {
      when(mockUserAnswers.anyTaxableRentalIncome).thenReturn(Some(AnyTaxPaid.No))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(AnyTaxPaid.No))
    }

    "redirect to the next page when valid YES data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("anyTaxPaid", "true"),("taxPaidAmount", testAnswer))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "redirect to the next page when valid NO data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("anyTaxPaid", "false"))
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
}
