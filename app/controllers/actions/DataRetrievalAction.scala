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

package controllers.actions


import com.google.inject.{ImplementedBy, Inject}
import connectors.DataCacheConnector
import models.requests.{AuthenticatedRequest, OptionalDataRequest}
import play.api.mvc.{ActionTransformer, AnyContent, BodyParser, MessagesControllerComponents}
import utils.UserAnswers

import scala.concurrent.{ExecutionContext, Future}

class DataRetrievalActionImpl @Inject()(val dataCacheConnector: DataCacheConnector,
                                        val cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends DataRetrievalAction {

  override protected def executionContext: ExecutionContext = cc.executionContext
  def parser: BodyParser[AnyContent] = cc.parsers.defaultBodyParser

  override protected def transform[A](request: AuthenticatedRequest[A]): Future[OptionalDataRequest[A]] = {
    dataCacheConnector.fetch(request.externalId).map {
      case None =>
        OptionalDataRequest(
          request.request,
          request.externalId,
          request.nino,
          request.name,
          request.address,
          None
        )
      case Some(data) =>
        OptionalDataRequest(
          request.request,
          request.externalId,
          request.nino,
          request.name,
          request.address,
          Some(new UserAnswers(data))
        )
    }
  }
}

@ImplementedBy(classOf[DataRetrievalActionImpl])
trait DataRetrievalAction extends ActionTransformer[AuthenticatedRequest, OptionalDataRequest]
