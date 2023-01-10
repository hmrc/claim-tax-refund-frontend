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

package connectors

import javax.inject.Singleton
import com.google.inject.{ImplementedBy, Inject}
import config.FrontendAppConfig
import models.Employment
import play.api.libs.json._
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class TaiConnectorImpl @Inject()(appConfig: FrontendAppConfig, http: HttpClient) extends TaiConnector {

  override def taiEmployments(nino: String, year: Int)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Seq[Employment]] = {

    val submissionUrl = s"${appConfig.taiUrl}/tai/$nino/employments/years/$year"

    implicit val employmentsReads: Reads[Seq[Employment]] = Employment.employmentsReads

    http.GET[Seq[Employment]](submissionUrl)
  }
}

@ImplementedBy(classOf[TaiConnectorImpl])
trait TaiConnector {
  def taiEmployments(nino: String, year: Int)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Seq[Employment]]
}
