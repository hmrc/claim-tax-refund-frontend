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

import config.{AddressLookupConfig, FrontendAppConfig}
import javax.inject.Inject
import models.AddressLookup
import play.api.Logging
import play.api.i18n.{Lang, MessagesApi}
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.HttpClient
import utils.UserAnswers

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AddressLookupConnector @Inject()(
                                        appConfig: FrontendAppConfig,
                                        addressLookupConfig: AddressLookupConfig,
                                        http: HttpClient,
                                        messagesApi: MessagesApi,
                                        dataCacheConnector: DataCacheConnector
                                      ) extends Logging {

  def initialise(continueUrl: String, accessibilityFooterUrl: String)(implicit hc: HeaderCarrier, language: Lang): Future[Option[String]] = {
    val addressLookupUrl = s"${appConfig.addressLookupUrl}/api/v2/init"
    val addressConfig = Json.toJson(addressLookupConfig.config(continueUrl = s"$continueUrl", accessibilityFooterUrl))
    http.POST[JsValue, HttpResponse](addressLookupUrl, body = addressConfig).map {
      response =>
        response.status match {
          case 202 =>
            Some(response.header(key = "Location")
              .getOrElse(s"[AddressLookupConnector][initialise] - Failed to obtain location from $addressLookupUrl"))
          case other =>
            logger.warn(s"[AddressLookupConnector][initialise] - received HTTP status $other from $addressLookupUrl")
            None
        }
    }.recover {
      case e: Exception =>
        logger.warn(s"[AddressLookupConnector][initialise] - connection to $addressLookupUrl failed", e)
        None
    }
  }

  def getAddress(cacheId: String, saveKey: String, id: String)(implicit hc: HeaderCarrier): Future[UserAnswers] = {
    val getAddressUrl = s"${appConfig.addressLookupUrl}/api/confirmed?id=$id"

    for {
      address: AddressLookup <- http.GET[AddressLookup](getAddressUrl)
      cacheMap: CacheMap <- dataCacheConnector.save(cacheId, saveKey, address)
    } yield new UserAnswers(cacheMap)
  }
}
