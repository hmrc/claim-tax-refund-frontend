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

package services

import base.SpecBase
import connectors.CtrConnector
import models._
import org.mockito.ArgumentCaptor
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.BeforeAndAfterEach
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.play.bootstrap.audit.DefaultAuditConnector

import scala.concurrent.Future

class SubmissionServiceSpec extends SpecBase with MockitoSugar with ScalaFutures with BeforeAndAfterEach {

  private val submission = Submission("pdf", "metadata", "xml")
  private val mockAuditConnector = mock[DefaultAuditConnector]
  private val mockCtrConnector = mock[CtrConnector]

  val service = new SubmissionService(frontendAppConfig, mockCtrConnector, mockAuditConnector)

  override def beforeEach(): Unit = {
    super.beforeEach()
    reset(mockAuditConnector, mockCtrConnector)
  }

  "ctrSubmission" when {
    "the submission is successful" must {
      "return success" in {
        when(mockCtrConnector.ctrSubmission(any())(any(), any())) thenReturn Future.successful(Some(SubmissionResponse("id", "filename")))

        val futureResult = service.ctrSubmission(submission)

        whenReady(futureResult) {
          result =>
            result mustBe SubmissionSuccessful
        }
      }

      "audit the event" in {
        when(mockCtrConnector.ctrSubmission(any())(any(), any())) thenReturn Future.successful(Some(SubmissionResponse("id", "filename")))

        val eventCaptor = ArgumentCaptor.forClass(classOf[SubmissionEvent])
        val futureResult = service.ctrSubmission(submission)

        whenReady(futureResult) {
          _ =>
            verify(mockAuditConnector).sendEvent(eventCaptor.capture)(any(), any())

            eventCaptor.getValue.detail.get("envelopeId") mustBe Some("id")
            eventCaptor.getValue.detail.get("filename") mustBe Some("filename")

            eventCaptor.getValue.tags must contain("transactionName" -> "Submission from Claim Tax Refund Frontend")
        }
      }
    }

    "the submission fails" must {
      "return failure" in {
        when(mockCtrConnector.ctrSubmission(any())(any(), any())) thenReturn Future.successful(None)

        val futureResult = service.ctrSubmission(submission)

        whenReady(futureResult) {
          result =>
            result mustBe SubmissionFailed
        }
      }
    }
  }
}
