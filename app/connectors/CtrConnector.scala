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

package connectors

import config.FrontendAppConfig
import javax.inject.Inject
import models.SubmissionResponse
import play.api.Logging
import play.api.http.Status._
import play.api.libs.json.JsValue
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

class CtrConnector @Inject()(appConfig: FrontendAppConfig, http: HttpClient) extends Logging {

  def ctrSubmission(submissionJson: JsValue)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[SubmissionResponse]] = {

    val submissionUrl = s"${appConfig.ctrUrl}/claim-tax-refund/submit"
    val postRequest: Future[Option[SubmissionResponse]] = http.POST[JsValue, HttpResponse](submissionUrl, submissionJson).map {
      response =>
        response.status match {
          case OK =>
            response.json.asOpt[SubmissionResponse]

          case other =>
            logger.warn(s"[CtrConnector][ctrSubmission] - received HTTP status $other from $submissionUrl")
            None
        }
    }

    postRequest.recover {
      case e =>
        logger.error(s"[CtrConnector][ctrSubmission] - submission to $submissionUrl failed", e)
        None
    }
  }
}
