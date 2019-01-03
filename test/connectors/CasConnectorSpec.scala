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
import com.github.tomakehurst.wiremock.client.WireMock._
import models.{SubmissionArchiveRequest, SubmissionArchiveResponse}
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import utils.{ReferenceGenerator, WireMockHelper}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CasConnectorSpec extends SpecBase with MockitoSugar with WireMockHelper with GuiceOneAppPerSuite with ScalaFutures with IntegrationPatience {

  override implicit lazy val app: Application =
    new GuiceApplicationBuilder()
      .configure(
        conf = "microservice.services.claim-tax-refund.port" -> server.port
      )
      .build()

  implicit val referenceGenerator: ReferenceGenerator = new ReferenceGenerator()

  private lazy val casConnector: CasConnector = app.injector.instanceOf[CasConnector]

  "CasConnector" must {
    "return a casKey when sending a valid SubmissionArchiveRequest" in {

      val submissionReference: String = referenceGenerator.generateSubmissionNumber

      val data: SubmissionArchiveRequest = SubmissionArchiveRequest("", submissionReference, "", "")

      server.stubFor(
        post(urlEqualTo(s"/claim-tax-refund/archive-submission/"))
          .willReturn(
            aResponse()
              .withStatus(200)
              .withBody("""{"casKey": "cas-1234"}""")
          )
      )

      val result: Future[SubmissionArchiveResponse] = casConnector.archiveSubmission(submissionReference, data)

      whenReady(result) {
        result =>
          result.casKey mustBe "cas-1234"
      }
    }
  }
}
