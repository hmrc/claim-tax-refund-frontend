/*
 * Copyright 2020 HM Revenue & Customs
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

import config.FrontendAppConfig
import models.requests.AuthenticatedRequest
import play.api.mvc.{AnyContent, BodyParser, MessagesControllerComponents, Request, Result}
import uk.gov.hmrc.auth.core.AuthConnector
import uk.gov.hmrc.auth.core.retrieve.{ItmpAddress, ItmpName}

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

case class FakeAuthAction(authConnector: AuthConnector,
                          config: FrontendAppConfig)(implicit mcc: MessagesControllerComponents) extends AuthAction {
  override def invokeBlock[A](request: Request[A], block: AuthenticatedRequest[A] => Future[Result]): Future[Result] =
    block(AuthenticatedRequest(
      request, "",
      "AB123456A",
      Some(ItmpName(Some("firstName"),Some("middleName"), Some("familyName"))),
      Some(ItmpAddress(
        Some("Line1"),
        Some("Line2"),
        Some("Line3"),
        Some("Line4"),
        Some("Line5"),
        Some("AB1 2CD"),
        Some("United Kingdom"),
        Some("UK")
      ))
    ))

  override def parser: BodyParser[AnyContent] = mcc.parsers.anyContent
  override val executionContext: ExecutionContext = implicitly[ExecutionContext]
}
