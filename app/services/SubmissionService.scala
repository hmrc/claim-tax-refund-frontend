/*
 * Copyright 2022 HM Revenue & Customs
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

import config.FrontendAppConfig
import connectors.CtrConnector
import javax.inject.Inject
import models._
import play.api.Logging
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.audit.DefaultAuditConnector

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SubmissionService @Inject()(appConfig: FrontendAppConfig,
                                  ctrConnector: CtrConnector,
                                  auditConnector: DefaultAuditConnector) extends Logging {

  def ctrSubmission(submission: Submission)(implicit hc: HeaderCarrier): Future[SubmissionResult] = {

    ctrConnector.ctrSubmission(Json.toJson(submission)).map {
      case Some(submissionResponse) =>

        val detailToAudit =
          Submission.asMap(submission) ++
            Map(
              "filename" -> submissionResponse.filename,
              "envelopeId" -> submissionResponse.id
            )

        val event = new SubmissionEvent(detailToAudit)

        auditConnector.sendEvent(event)

        logger.info(s"[DmsSubmissionService][submitSubmission] - submission successful")

        SubmissionSuccessful

      case _ =>
        SubmissionFailed
    }
  }
}
