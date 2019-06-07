/*
 * Copyright 2019 HM Revenue & Customs
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
import models.SelectTaxYear.CYMinus2
import models._
import org.mockito.Mockito._
import play.api.data.Form
import play.api.mvc.Call
import play.api.test.Helpers._
import utils.{FakeNavigator, MockUserAnswers}
import views.html.deleteOther
import scala.concurrent.ExecutionContext.Implicits.global

class DeleteOtherControllerSpec extends ControllerSpecBase {

  def onwardRoute: Call = routes.IndexController.onPageLoad()

  val formProvider = new BooleanForm()
  val form = formProvider()
  private val mockUserAnswers = MockUserAnswers
  private val taxYear = CYMinus2

  val itemName = "qwerty"

  val index = Index(0)
  val invalidIndex = Index(6)

  val benefitCollectionId = "otherBenefits"
  val companyBenefitCollectionId = "otherCompanyBenefits"
  val taxableIncomeCollectionId = "otherTaxableIncome"
  val invalidCollectionId = "qwerty"


  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new DeleteOtherController(frontendAppConfig, messagesApi, FakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute), FakeAuthAction(authConnector, frontendAppConfig),
      dataRetrievalAction, new DataRequiredActionImpl(messagesControllerComponents), messagesControllerComponents, formProvider, formPartialRetriever, scalate)

  def viewAsString(form: Form[_] = form, index: Index, itemName: String, collectionId: String): String =
    deleteOther(frontendAppConfig, form, NormalMode, index, itemName, collectionId, taxYear)(fakeRequest, messages, formPartialRetriever, scalate).toString

  "DeleteOther Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller(fakeDataRetrievalAction(mockUserAnswers.minimalValidUserAnswers))
        .onPageLoad(NormalMode, index, itemName, benefitCollectionId)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString(form, index, itemName, benefitCollectionId)
    }

    "redirect to AnyOtherBenefit when value is true and valid benefit submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers.benefitsUserAnswers))
        .onSubmit(CheckMode, index, itemName, benefitCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.AnyOtherBenefitsController.onPageLoad(CheckMode).url)
    }

    "redirect to RemoveOtherSelectedOption when value is true and no other benefits are available" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val answers = mockUserAnswers.benefitsUserAnswers

      when(answers.otherBenefit) thenReturn Some(Seq.empty)

      val result = controller(fakeDataRetrievalAction(answers))
        .onSubmit(NormalMode, index, itemName, benefitCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.RemoveOtherSelectedOptionController.onPageLoad(NormalMode, OtherBenefit.collectionId).url)
    }


    "redirect to AnyOtherCompanyBenefits when value is true and valid companyBenefit submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers.companyBenefitsUserAnswers))
        .onSubmit(NormalMode, index, itemName, companyBenefitCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.AnyOtherCompanyBenefitsController.onPageLoad(NormalMode).url)
    }

    "redirect to RemoveOtherSelectedOption when value is true and no other companyBenefits are available" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val answers = mockUserAnswers.companyBenefitsUserAnswers

      when(answers.otherCompanyBenefit) thenReturn Some(Seq.empty)

      val result = controller(fakeDataRetrievalAction(answers))
        .onSubmit(NormalMode, index, itemName, companyBenefitCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.RemoveOtherSelectedOptionController.onPageLoad(NormalMode, OtherCompanyBenefit.collectionId).url)
    }


    "redirect to CheckYourAnswers when value is true and valid otherTaxableIncome submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers.taxableIncomeUserAnswers))
        .onSubmit(NormalMode, index, itemName, taxableIncomeCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "redirect to RemoveOtherSelectedOption when value is true and no other otherTaxableIncome are available" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val answers = mockUserAnswers.taxableIncomeUserAnswers

      when(answers.otherTaxableIncome) thenReturn Some(Seq.empty)

      val result = controller(fakeDataRetrievalAction(answers))
        .onSubmit(NormalMode, index, itemName, taxableIncomeCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.RemoveOtherSelectedOptionController.onPageLoad(NormalMode, OtherTaxableIncome.collectionId).url)
    }


    "redirect to SessionExpired when value is true and no otherBenefit userAnswer is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller(fakeDataRetrievalAction())
        .onSubmit(NormalMode, index, itemName, benefitCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to SessionExpired when value is true and no otherCompanyBenefit userAnswer is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller(fakeDataRetrievalAction())
        .onSubmit(NormalMode, index, itemName, companyBenefitCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }


    "redirect to SessionExpired when value is true and no otherTaxableIncome userAnswer is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller(fakeDataRetrievalAction())
        .onSubmit(NormalMode, index, itemName, taxableIncomeCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to SessionExpired when id is not valid" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller(fakeDataRetrievalAction())
        .onSubmit(NormalMode, index, itemName, invalidCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to AnyOtherBenefit when valid data is submitted and value is false" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "false"))

      val result = controller(fakeDataRetrievalAction())
        .onSubmit(NormalMode, index, itemName, benefitCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.AnyOtherBenefitsController.onPageLoad(NormalMode).url)
    }

    "redirect to AnyOtherCompanyBenefits when valid data is submitted and value is false" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "false"))

      val result = controller(fakeDataRetrievalAction())
        .onSubmit(NormalMode, index, itemName, companyBenefitCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.AnyOtherCompanyBenefitsController.onPageLoad(NormalMode).url)
    }

    "redirect to AnyOtherTaxableIncome when valid data is submitted and value is false" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "false"))

      val result = controller(fakeDataRetrievalAction())
        .onSubmit(NormalMode, index, itemName, taxableIncomeCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.AnyOtherTaxableIncomeController.onPageLoad(NormalMode).url)
    }

    "redirect to SessionExpired when invalid id provided and value is true" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers.taxableIncomeUserAnswers))
        .onSubmit(NormalMode, index, itemName, invalidCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to SessionExpired when invalid id provided and value is false" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "false"))

      val result = controller(fakeDataRetrievalAction(mockUserAnswers.taxableIncomeUserAnswers))
        .onSubmit(NormalMode, index, itemName, invalidCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = form.bind(Map("value" -> "invalid value"))

      val result = controller(fakeDataRetrievalAction())
        .onSubmit(NormalMode, index, itemName, benefitCollectionId)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm, index, itemName, benefitCollectionId)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData)
        .onPageLoad(NormalMode, index, itemName, benefitCollectionId)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a GET if no taxYear is available" in {
      val minimalValidUserAnswers = mockUserAnswers.minimalValidUserAnswers

      when(minimalValidUserAnswers.selectTaxYear) thenReturn None

      val result = controller(fakeDataRetrievalAction(minimalValidUserAnswers))
        .onPageLoad(NormalMode, index, itemName, benefitCollectionId)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "true"))
      val result = controller(dontGetAnyData)
        .onSubmit(NormalMode, index, itemName, benefitCollectionId)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }
  }
}




