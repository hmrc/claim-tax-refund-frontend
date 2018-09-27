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

import play.api.data.Form
import utils.{CheckYourAnswersHelper, CheckYourAnswersSections, FakeNavigator, MockUserAnswers}
import connectors.FakeDataCacheConnector
import controllers.actions._
import play.api.test.Helpers._
import forms.BooleanForm
import models.{NormalMode, OtherCompanyBenefit}
import models.SelectTaxYear.CYMinus2
import org.mockito.Mockito.when
import play.api.mvc.Call
import viewmodels.AnswerSection
import views.html.anyOtherCompanyBenefits

class AnyOtherCompanyBenefitsControllerSpec extends ControllerSpecBase {

  def onwardRoute: Call = routes.IndexController.onPageLoad()

  val formProvider = new BooleanForm()
  val form = formProvider()
  private val taxYear = CYMinus2
  private val mockUserAnswers = MockUserAnswers.claimDetailsUserAnswers
  private val cya: CheckYourAnswersHelper = new CheckYourAnswersHelper(mockUserAnswers)(messages)
  private val otherCompanyBenefits: AnswerSection = new CheckYourAnswersSections(cya, mockUserAnswers).otherCompanyBenefitSection

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new AnyOtherCompanyBenefitsController(frontendAppConfig, messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction,
      dataRetrievalAction, new DataRequiredActionImpl, formProvider, formPartialRetriever, templateRenderer)

  def viewAsString(form: Form[_] = form): String = anyOtherCompanyBenefits(frontendAppConfig, form, NormalMode, taxYear, otherCompanyBenefits)(fakeRequest, messages, formPartialRetriever, templateRenderer).toString

  "AnyOtherCompanyBenefits Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "not populate the view on a GET when the question has previously been answered as the user always needs to answer this question" in {
      when(mockUserAnswers.anyOtherCompanyBenefits).thenReturn(Some(true))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form)
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

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




