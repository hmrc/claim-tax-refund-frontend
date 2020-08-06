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

import connectors.AddressLookupConnector
import controllers.actions.{DataRequiredActionImpl, DataRetrievalAction, FakeAuthAction}
import models.{CheckMode, NormalMode}
import org.mockito.Matchers._
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.test.Helpers.{status, _}
import utils.{FakeNavigator, MockUserAnswers}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class AddressLookupRoutingControllerSpec extends ControllerSpecBase with MockitoSugar {

  def onwardRoute = routes.IndexController.onPageLoad()

  private val mockAddressLookup: AddressLookupConnector = mock[AddressLookupConnector]
  private val mockUserAnswers = MockUserAnswers.minimalValidUserAnswers()

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) =
    new AddressLookupRoutingController(frontendAppConfig, new FakeNavigator(desiredRoute = onwardRoute), mockAddressLookup, FakeAuthAction(authConnector, frontendAppConfig),
      messagesControllerComponents, dataRetrievalAction, new DataRequiredActionImpl(messagesControllerComponents))

  "AddressLookupRoutingController" must {
    when(mockAddressLookup.getAddress(any(),any(),any())(any())).thenReturn(Future.successful(mockUserAnswers))

    "When addressId present and Normal mode, call getAddress and match mode appropriately" in {
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).addressLookupCallback(Some("AddID1234"), NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
    }

    "When addressId present and Check mode, call getAddress and match mode appropriately" in {
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).addressLookupCallback(Some("AddID1234"), CheckMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
    }

    "When no addressId present and Normal mode, call getAddress and match mode appropriately" in {
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).addressLookupCallback(None, NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
    }

    "When no addressId present and Check mode, call getAddress and match mode appropriately" in {
      val result = controller(fakeDataRetrievalAction(mockUserAnswers)).addressLookupCallback(None, CheckMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
    }

  }

}
