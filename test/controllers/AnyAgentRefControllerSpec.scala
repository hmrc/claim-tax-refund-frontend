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
import forms.AnyAgentReferenceForm
import identifiers.{AgentRefId, AnyAgentRefId}
import models.SelectTaxYear.CustomTaxYear
import models.{AnyAgentRef, NormalMode}
import org.mockito.Mockito.when
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.data.Form
import play.api.libs.json._
import play.api.test.Helpers._
import utils.{FakeNavigator, MockUserAnswers}
import views.html.anyAgentRef

import scala.concurrent.ExecutionContext.Implicits.global

class AnyAgentRefControllerSpec extends ControllerSpecBase with GuiceOneAppPerSuite {

  def onwardRoute = routes.IndexController.onPageLoad

  val requiredKey = "anyAgentRef.blank"
  val requiredAgentRefKey = "anyAgentRef.blankAgentRef"
  val nomineeName = "Test Nominee"
  val formProvider = new AnyAgentReferenceForm()
  val form: Form[AnyAgentRef] = formProvider(messages(requiredKey, nomineeName), messages(requiredAgentRefKey, nomineeName))
  val validYesData = Map(AnyAgentRefId.toString -> Json.obj(AnyAgentRefId.toString -> JsBoolean(true), AgentRefId.toString -> JsString("AB1234")))
  val validNoData = Map(AnyAgentRefId.toString -> Json.obj(AnyAgentRefId.toString -> JsBoolean(false)))
  val anyAgentRef: anyAgentRef = fakeApplication().injector.instanceOf[anyAgentRef]
  private val taxYear = CustomTaxYear(2017)
  private val mockUserAnswers = MockUserAnswers.claimDetailsUserAnswers()

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new AnyAgentRefController(messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction(authConnector, frontendAppConfig),
      dataRetrievalAction, new DataRequiredActionImpl(messagesControllerComponents), anyAgentRef, messagesControllerComponents, formProvider)

  def viewAsString(form: Form[_] = form) = anyAgentRef(form, NormalMode, nomineeName, taxYear)(fakeRequest, messages).toString

  "AnyAgentRef Controller" must {

    "return OK and the correct view for a GET" in {
      when(mockUserAnswers.nomineeFullName).thenReturn(Some(nomineeName))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when YES has previously been answered" in {
      when(mockUserAnswers.nomineeFullName).thenReturn(Some(nomineeName))
      when(mockUserAnswers.anyAgentRef).thenReturn(Some(AnyAgentRef.Yes("AB1234")))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(AnyAgentRef.Yes("AB1234")))
    }

    "populate the view correctly on a GET when NO has previously been answered" in {
      when(mockUserAnswers.nomineeFullName).thenReturn(Some(nomineeName))
      when(mockUserAnswers.anyAgentRef).thenReturn(Some(AnyAgentRef.No))
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(form.fill(AnyAgentRef.No))
    }

    "redirect to the next page when valid YES data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("anyAgentRef", "true"),("agentRef", "AB1234")).withMethod("POST")
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "redirect to the next page when valid NO data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("anyAgentRef", "false")).withMethod("POST")
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value")).withMethod("POST")
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true")).withMethod("POST")
      val result = controller(dontGetAnyData).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }

    "redirect to Session Expired if no taxYears have been selected" in {
      when(mockUserAnswers.nomineeFullName).thenReturn(None)

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }

    "redirect to Session Expired if no taxYears have been selected on submit" in {
      when(mockUserAnswers.nomineeFullName).thenReturn(None)

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad.url)
    }
  }
}
