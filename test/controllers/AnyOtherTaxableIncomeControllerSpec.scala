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

import connectors.FakeDataCacheConnector
import controllers.actions._
import forms.BooleanForm
import models.SelectTaxYear.CustomTaxYear
import models._
import org.mockito.Mockito.when
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import utils.{FakeNavigator, MockUserAnswers}
import views.html.anyOtherTaxableIncome

import scala.concurrent.ExecutionContext.Implicits.global

class AnyOtherTaxableIncomeControllerSpec  extends ControllerSpecBase with GuiceOneAppPerSuite {

  def onwardRoute: Call = routes.IndexController.onPageLoad()

  private val formProvider = new BooleanForm()
  private val form = formProvider()
  private val taxYear = CustomTaxYear(2017)
  private val mockUserAnswers = MockUserAnswers.claimDetailsUserAnswers()

  val anyOtherTaxableIncome: anyOtherTaxableIncome = fakeApplication.injector.instanceOf[anyOtherTaxableIncome]

  val complete: Seq[(OtherTaxableIncome, Int)] = Seq((OtherTaxableIncome("qwerty", "123", Some(AnyTaxPaid.Yes("1234"))),0))
  val incomplete: Seq[(OtherTaxableIncome, Int)] = Seq((OtherTaxableIncome("qwerty", "123", None),1))

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new AnyOtherTaxableIncomeController(frontendAppConfig, messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction(authConnector, frontendAppConfig),
      dataRetrievalAction, new DataRequiredActionImpl(messagesControllerComponents), anyOtherTaxableIncome, messagesControllerComponents, formProvider, formPartialRetriever, templateRenderer)

  def viewAsString(form: Form[_] = form, mode: Mode = NormalMode): String =
    anyOtherTaxableIncome(frontendAppConfig,
                          form,
                          mode,
													taxYear,
                          complete,
                          incomplete)(fakeRequest, messages, formPartialRetriever, templateRenderer).toString

  "AnyOtherTaxableIncome Controller" must {

    "return OK and the correct view for a GET" in {
			when (mockUserAnswers.otherTaxableIncome).thenReturn(Some(Seq(
				OtherTaxableIncome("qwerty", "123", Some(AnyTaxPaid.Yes("1234"))),
				OtherTaxableIncome("qwerty", "123", None)
			)))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "return OK and the correct view for a GET while in CheckMode" in {
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(CheckMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(mode = CheckMode)
    }


    "not populate the view on a GET when the question has previously been answered as the user always needs to answer this question" in {
      when(mockUserAnswers.anyOtherTaxableIncome).thenReturn(Some(true))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form)
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(fakeDataRetrievalAction(MockUserAnswers.taxableIncomeUserAnswers)).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "redirect to the next page when valid data is submitted while in CheckMode" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(fakeDataRetrievalAction(MockUserAnswers.taxableIncomeUserAnswers)).onSubmit(CheckMode)(postRequest)

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




