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

package connectors

import config.FrontendAppConfig
import models.AddressLookupResponse
import models.templates.AddressLookup
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

class AddressLookupConnector (appConfig: FrontendAppConfig, http: HttpClient) {

  def addressLookSubmission(json: JsValue)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Option[AddressLookupResponse]] =

  val addressLookupUrl = "http://localhost:9028/api/init"

  val js = new AddressLookup("http://localhost:9969/")

  http.POST(addressLookupUrl, addressLookSubmission(Json.toJson(js)))



}