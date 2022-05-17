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

package controllers.actions

import com.google.inject.{ImplementedBy, Inject}
import config.FrontendAppConfig
import connectors.DataCacheConnector
import controllers.routes
import models.requests.AuthenticatedRequest
import play.api.mvc.Results._
import play.api.mvc.{ActionBuilder, ActionFunction, AnyContent, BodyParser, MessagesControllerComponents, Request, Result}
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.http.{HeaderCarrier, UnauthorizedException}
import uk.gov.hmrc.play.http.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}

class AuthActionImpl @Inject()(override val authConnector: AuthConnector,
                               val config: FrontendAppConfig,
                               cc: MessagesControllerComponents)(dataCacheConnector: DataCacheConnector)
                              (implicit val executionContext: ExecutionContext) extends AuthAction with AuthorisedFunctions {

  def parser: BodyParser[AnyContent] = cc.parsers.defaultBodyParser
}

@ImplementedBy(classOf[AuthActionImpl])
trait AuthAction extends ActionBuilder[AuthenticatedRequest, AnyContent] with ActionFunction[Request, AuthenticatedRequest] with AuthorisedFunctions {

  val config: FrontendAppConfig
  implicit val executionContext: ExecutionContext

  override def invokeBlock[A](request: Request[A], block: AuthenticatedRequest[A] => Future[Result]): Future[Result] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(request, request.session)

    authorised(ConfidenceLevel.L200 and AffinityGroup.Individual)
      .retrieve(Retrievals.externalId and Retrievals.nino and Retrievals.itmpName and Retrievals.itmpAddress) {
        x =>
          val externalId = x.a.a.a.getOrElse(throw new UnauthorizedException("unable to retrieve external Id"))
          val nino = x.a.a.b.getOrElse(throw new UnauthorizedException("unable to retrieve NINO"))
          val name = x.a.b
          val address = x.b

          if (!uk.gov.hmrc.domain.Nino.isValid(nino)) throw new UnauthorizedException(s"$nino is not a valid nino.")

          block(AuthenticatedRequest(request, externalId, nino, name, address))

      } recover {
      case _: NoActiveSession =>
        Redirect(config.loginUrl, Map("continue" -> Seq(config.loginContinueUrl)))
      case _: InsufficientEnrolments =>
        Redirect(routes.UnauthorisedController.onPageLoad())
      case _: InsufficientConfidenceLevel =>
        Redirect(s"${config.ivUpliftUrl}?origin=CTR&" +
          s"confidenceLevel=200&" +
          s"completionURL=${config.authorisedCallback}&" +
          s"failureURL=${config.unauthorisedCallback}"
        )
      case _: UnsupportedAuthProvider =>
        Redirect(routes.UnauthorisedController.onPageLoad())
      case _: UnsupportedAffinityGroup =>
        Redirect(routes.UnauthorisedController.onPageLoad())
      case _: UnsupportedCredentialRole =>
        Redirect(routes.UnauthorisedController.onPageLoad())
    }
  }
}
