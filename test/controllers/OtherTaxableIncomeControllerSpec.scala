/*
 * Copyright 2021 HM Revenue & Customs
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
import forms.OtherTaxableIncomeForm
import models.{AnyTaxPaid, Index, NormalMode, OtherTaxableIncome}
import models.SelectTaxYear.CustomTaxYear
import org.mockito.Mockito.when
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.mvc.Call
import views.html.otherTaxableIncome

import scala.concurrent.ExecutionContext.Implicits.global

class OtherTaxableIncomeControllerSpec extends ControllerSpecBase with GuiceOneAppPerSuite {

  def onwardRoute: Call = routes.AnyTaxableOtherIncomeController.onPageLoad(NormalMode, 0)

  val testAnswer = OtherTaxableIncome("answer", "123", Some(AnyTaxPaid.Yes("123")))
  val form = new OtherTaxableIncomeForm(frontendAppConfig)(Seq.empty, 0)
  val otherTaxableIncome: otherTaxableIncome = fakeApplication.injector.instanceOf[otherTaxableIncome]
  private val taxYear = CustomTaxYear(2017)
  private val mockUserAnswers = MockUserAnswers.claimDetailsUserAnswers()

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new OtherTaxableIncomeController(frontendAppConfig, messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction(authConnector, frontendAppConfig),
      dataRetrievalAction, new DataRequiredActionImpl(messagesControllerComponents), otherTaxableIncome, messagesControllerComponents, sequenceUtil, new OtherTaxableIncomeForm(frontendAppConfig), formPartialRetriever, templateRenderer)


  def viewAsString(form: Form[OtherTaxableIncome], index: Index): String = otherTaxableIncome(frontendAppConfig, form, NormalMode, index, taxYear)(fakeRequest, messages, formPartialRetriever, templateRenderer).toString

  "OtherTaxableIncomeName Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller(fakeDataRetrievalAction()).onPageLoad(NormalMode, 0)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(form, 0)
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      when(mockUserAnswers.otherTaxableIncome).thenReturn(Some(Seq(testAnswer)))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, 0)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(testAnswer), 0)
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("name", "qwerty"), ("amount", "123"))
      val result = controller(fakeDataRetrievalAction()).onSubmit(NormalMode, 0)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", ""))
      val boundForm = form.bind(Map("value" -> ""))

      val result = controller(fakeDataRetrievalAction()).onSubmit(NormalMode, 0)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm, 0)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(NormalMode, 0)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("name", "qwerty"), ("amount", "123"))
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
  }
}
