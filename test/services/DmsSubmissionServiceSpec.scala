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

package services

import base.SpecBase
import connectors.CtrConnector
import models._
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.mockito.{ArgumentCaptor, Matchers}
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditResult
import uk.gov.hmrc.play.bootstrap.audit.DefaultAuditConnector
import utils.MockUserAnswers

import scala.concurrent.{ExecutionContext, Future}

class DmsSubmissionServiceSpec extends SpecBase with MockitoSugar with ScalaFutures {

  implicit val hcCaptor = ArgumentCaptor.forClass(classOf[HeaderCarrier])
  implicit val ecCaptor = ArgumentCaptor.forClass(classOf[ExecutionContext])
  val taxYear = SelectTaxYear.CYMinus2

  ".ctrSubmission" when {

    "the submission is successful" must {

      "return success" in {
        val mockAuditConnector = mock[DefaultAuditConnector]

        val answers = MockUserAnswers.minimalValidUserAnswers

        val mockCtrConnector = mock[CtrConnector]
        when(mockCtrConnector.ctrSubmission(any())(any(), any())) thenReturn Future.successful(Some(SubmissionResponse("id", "filename")))

        val service = new DmsSubmissionService(frontendAppConfig, mockCtrConnector, mockAuditConnector)
        implicit val hc = new HeaderCarrier

        val eventCaptor = ArgumentCaptor.forClass(classOf[SubmissionEvent])
        val futureResult = service.ctrSubmission(answers)

        whenReady(futureResult) { result =>
          result mustBe SubmissionSuccessful
        }
      }

      "audit the event" in {
        val mockAuditConnector = mock[DefaultAuditConnector]
        when(mockAuditConnector.sendEvent(Matchers.any())(Matchers.any(), Matchers.any())).thenReturn(Future.successful(AuditResult.Success))

        val answers = MockUserAnswers.minimalValidUserAnswers

        val mockCtrConnector = mock[CtrConnector]
        when(mockCtrConnector.ctrSubmission(any())(any(), any())) thenReturn Future.successful(Some(SubmissionResponse("id", "filename")))

        val service = new DmsSubmissionService(frontendAppConfig, mockCtrConnector, mockAuditConnector)
        implicit val hc = new HeaderCarrier

        val eventCaptor = ArgumentCaptor.forClass(classOf[SubmissionEvent])
        val futureResult = service.ctrSubmission(answers)

        whenReady(futureResult) { result =>
          verify(mockAuditConnector).sendEvent(eventCaptor.capture)(hcCaptor.capture, ecCaptor.capture)

          eventCaptor.getValue.detail.get("selectTaxYear") mustBe Some(taxYear.asString)
          eventCaptor.getValue.detail.get("envelopeId") mustBe Some("id")
          eventCaptor.getValue.detail.get("filename") mustBe Some("filename")

          eventCaptor.getValue.tags must contain(
            "transactionName" -> "Submission from Claim Tax Refund Frontend"
          )
        }
      }
    }

    "the submission fails" must {

      "return failure" in {
        val mockAuditConnector = mock[DefaultAuditConnector]

        val answers = MockUserAnswers.minimalValidUserAnswers

        val mockCtrConnector = mock[CtrConnector]
        when(mockCtrConnector.ctrSubmission(any())(any(), any())) thenReturn Future.successful(None)

        val service = new DmsSubmissionService(frontendAppConfig, mockCtrConnector, mockAuditConnector)
        implicit val hc = new HeaderCarrier

        val eventCaptor = ArgumentCaptor.forClass(classOf[SubmissionEvent])
        val futureResult = service.ctrSubmission(answers)

        whenReady(futureResult) { result =>
          result mustBe SubmissionFailed
        }
      }

      "not audit the event" in {
        val mockAuditConnector = mock[DefaultAuditConnector]
        when(mockAuditConnector.sendEvent(Matchers.any())(Matchers.any(), Matchers.any())).thenReturn(Future.successful(AuditResult.Success))

        val answers = MockUserAnswers.minimalValidUserAnswers

        val mockCtrConnector = mock[CtrConnector]
        when(mockCtrConnector.ctrSubmission(any())(any(), any())) thenReturn Future.successful(None)

        val service = new DmsSubmissionService(frontendAppConfig, mockCtrConnector, mockAuditConnector)
        implicit val hc = new HeaderCarrier

        val eventCaptor = ArgumentCaptor.forClass(classOf[SubmissionEvent])
        val futureResult = service.ctrSubmission(answers)

        whenReady(futureResult) { result =>
          verify(mockAuditConnector, never).sendEvent(eventCaptor.capture)(hcCaptor.capture, ecCaptor.capture)
        }
      }
    }
  }
}
