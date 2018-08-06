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
import utils.{FakeNavigator, MockUserAnswers}
import connectors.FakeDataCacheConnector
import controllers.actions._
import play.api.test.Helpers._
import forms.BooleanForm
import models.{CheckMode, Index, NormalMode, OtherBenefit}
import views.html.deleteOther
import org.mockito.Mockito.when
import play.api.mvc.Call

class DeleteOtherControllerSpec extends ControllerSpecBase {

  def onwardRoute: Call = routes.IndexController.onPageLoad()

  val formProvider = new BooleanForm()
  val form = formProvider()
  private val mockUserAnswers = MockUserAnswers.benefitsUserAnswers


  val itemName = "qwerty"
  val index = Index(0)
  val benefitCollectionId = "otherBenefits"


  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new DeleteOtherController(frontendAppConfig, messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction,
      dataRetrievalAction, new DataRequiredActionImpl, formProvider)

  def viewAsString(form: Form[_] = form, index: Index, itemName: String, collectionId: String): String =
    deleteOther(frontendAppConfig, form, NormalMode, index, itemName, collectionId)(fakeRequest, messages).toString

  "DeleteOther Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(NormalMode, index, itemName, benefitCollectionId)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(form, index, itemName, benefitCollectionId)
    }

    "redirect to CheckYourAnswers when value is true" in {
      mockUserAnswers.otherBenefit.get.size mustBe 3
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).onSubmit(NormalMode, index, itemName, benefitCollectionId)(postRequest)
      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.CheckYourAnswersController.onPageLoad().url)
    }

    "redirect to CheckYourAnswers when valid data is submitted and value is false" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "false"))

      val result = controller().onSubmit(NormalMode, index, itemName, benefitCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.CheckYourAnswersController.onPageLoad().url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller().onSubmit(NormalMode, index, itemName, benefitCollectionId)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm, index, itemName, benefitCollectionId)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(NormalMode, index, itemName, benefitCollectionId)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(dontGetAnyData).onSubmit(NormalMode, index, itemName, benefitCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }
  }
}




