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

package connectors

import base.SpecBase
import models.Employment
import org.mockito.Matchers
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class TaiConnectorSpec extends SpecBase with MockitoSugar with ScalaFutures {


  "TaiConnector" must {

    "return a sequence of employments" in {

      implicit val hc: HeaderCarrier = HeaderCarrier()

      val httpMock = mock[HttpClient]
      val appConfig = frontendAppConfig
      val submissionURL = s"${appConfig.taiUrl}/tai/AB123123A/employments/years/2016"

      when(httpMock.GET[Seq[Employment]](Matchers.eq(submissionURL))(Matchers.any(), Matchers.any(), Matchers.any()))
        .thenReturn(Future.successful(Seq(Employment("AVIVA PENSIONS", "754", "AZ00070"))))

      val connector = new TaiConnectorImpl(frontendAppConfig, httpMock)
      val futureResult = connector.taiEmployments("AB123123A", 2016)

      whenReady(futureResult) { result =>
        result mustBe Seq(Employment("AVIVA PENSIONS", "754", "AZ00070"))
      }
    }
  }

}
