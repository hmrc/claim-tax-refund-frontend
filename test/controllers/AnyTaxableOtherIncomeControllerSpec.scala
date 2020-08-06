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
import forms.{AnyTaxPaidForm, OtherTaxableIncomeForm}
import models.SelectTaxYear.CustomTaxYear
import models.{AnyTaxPaid, NormalMode, OtherTaxableIncome}
import org.mockito.Mockito.when
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import utils.{FakeNavigator, MockUserAnswers}
import views.html.anyTaxableOtherIncome

import scala.concurrent.ExecutionContext.Implicits.global

class AnyTaxableOtherIncomeControllerSpec extends ControllerSpecBase with GuiceOneAppPerSuite {

  def onwardRoute: Call = routes.IndexController.onPageLoad()

  private val notSelectedKey = "anyTaxableOtherIncome.notSelected"
  private val blankKey = "anyTaxableOtherIncome.blank"
  private val invalidKey = "anyTaxableOtherIncome.invalid"

  private val testAnswer = "9,999.00"
  private val mockUserAnswers = MockUserAnswers.claimDetailsUserAnswers()
  private val taxYear = CustomTaxYear(2017)
  private val incomeName = "test income"
  private val anyTaxableOtherIncome: anyTaxableOtherIncome = fakeApplication.injector.instanceOf[anyTaxableOtherIncome]

  private val formProvider = new OtherTaxableIncomeForm(frontendAppConfig)
  private val taxPaidFormProvider = new AnyTaxPaidForm
  private val taxPaidForm = taxPaidFormProvider(
    messages(notSelectedKey, incomeName),
    messages(blankKey, incomeName),
    messages(invalidKey, incomeName)
  )

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new AnyTaxableOtherIncomeController(frontendAppConfig, messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction(authConnector, frontendAppConfig),
      dataRetrievalAction, new DataRequiredActionImpl(messagesControllerComponents), anyTaxableOtherIncome, messagesControllerComponents, sequenceUtil, formProvider, taxPaidFormProvider, formPartialRetriever, scalate)

  def viewAsString(form: Form[_] = taxPaidForm): String =
    anyTaxableOtherIncome(frontendAppConfig, form, NormalMode, 0, taxYear, incomeName)(fakeRequest, messages, formPartialRetriever, scalate).toString


  "AnyTaxableOtherIncome Controller" must {

    "return OK and the correct view for a GET" in {
      when(mockUserAnswers.otherTaxableIncome).thenReturn(Some(Seq(OtherTaxableIncome(incomeName, "123", None))))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, 0)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(taxPaidForm)
    }

    "populate the view correctly on a GET when YES has previously been answered" in {
      when(mockUserAnswers.otherTaxableIncome).thenReturn(Some(Seq(OtherTaxableIncome(incomeName, "123", Some(AnyTaxPaid.Yes(testAnswer))))))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, 0)(fakeRequest)

      contentAsString(result) mustBe viewAsString(taxPaidForm.fill(AnyTaxPaid.Yes(testAnswer)))
    }

    "populate the view correctly on a GET when NO has previously been answered" in {
      when(mockUserAnswers.otherTaxableIncome).thenReturn(Some(Seq(OtherTaxableIncome(incomeName, "123", Some(AnyTaxPaid.No)))))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, 0)(fakeRequest)

      contentAsString(result) mustBe viewAsString(taxPaidForm.fill(AnyTaxPaid.No))
    }

    "redirect to the next page when valid YES data is submitted" in {
      when(mockUserAnswers.otherTaxableIncome).thenReturn(Some(Seq(OtherTaxableIncome(incomeName, testAnswer, Some(AnyTaxPaid.Yes(testAnswer))))))
      val postRequest = fakeRequest.withFormUrlEncodedBody(("anyTaxPaid", "true"),("taxPaidAmount", testAnswer))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode, 0)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "redirect to the next page when valid NO data is submitted" in {
      when(mockUserAnswers.otherTaxableIncome).thenReturn(Some(Seq(OtherTaxableIncome(incomeName, "123", Some(AnyTaxPaid.No)))))
      val postRequest = fakeRequest.withFormUrlEncodedBody(("anyTaxPaid", "false"))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode, 0)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = taxPaidForm.bind(Map("value" -> "invalid value"))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode, 0)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(NormalMode, 0)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
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

    "redirect to Session Expired if no other income name has been provided " in {
      when(mockUserAnswers.otherTaxableIncome).thenReturn(None)
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode, 0)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired if no other income name has been provided on submit" in {
      when(mockUserAnswers.otherTaxableIncome).thenReturn(None)
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode, 0)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }
  }
}
