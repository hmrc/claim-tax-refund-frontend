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

package repositories

import models.CacheMap
import org.apache.pekko.actor.ActorSystem
import org.bson.BsonType

import java.time.Instant
import javax.inject.{Inject, Singleton}
import org.mongodb.scala.model.{IndexModel, IndexOptions, ReplaceOptions, UpdateOptions}
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Indexes._
import org.mongodb.scala.model.Updates._
import play.api.{Configuration, Logging}
import play.api.libs.json.{Format, JsValue, Json, OFormat}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.formats.MongoJavatimeFormats
import uk.gov.hmrc.mongo.play.json.{Codecs, PlayMongoRepository}

import java.util.concurrent.TimeUnit
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.{FiniteDuration, SECONDS}


case class DatedCacheMap(id: String,
                         data: Map[String, JsValue],
                         lastUpdated: Instant = Instant.now())

object DatedCacheMap {
  implicit val dateFormat: Format[Instant] = MongoJavatimeFormats.Implicits.jatInstantFormat
  implicit val formats: OFormat[DatedCacheMap] = Json.format[DatedCacheMap]

  def apply(cacheMap: CacheMap): DatedCacheMap = DatedCacheMap(cacheMap.id, cacheMap.data)
}

class ReactiveMongoRepository(config: Configuration, mongo: MongoComponent)(implicit ec: ExecutionContext)
  extends PlayMongoRepository[DatedCacheMap](
    collectionName = config.get[String]("appName"),
    mongoComponent = mongo,
    domainFormat = DatedCacheMap.formats,
    indexes = Seq(
      IndexModel(ascending("lastUpdated"), IndexOptions()
        .name("userAnswersExpiry")
        .expireAfter(config.get[Long]("mongodb.timeToLiveInSeconds"), SECONDS))
    )
    , extraCodecs = Seq(Codecs.playFormatCodec(CacheMap.formats))
  ) with Logging {

  def upsert(cm: CacheMap): Future[Boolean] = {
    val dcm = DatedCacheMap(cm)
    collection.replaceOne(
      filter = equal("id", dcm.id),
      replacement = dcm,
      ReplaceOptions().upsert(true)
    ).toFuture().map(_.wasAcknowledged())
  }

  def get(id: String): Future[Option[CacheMap]] =
    collection.find[CacheMap](and(equal("id", id))).headOption()
}

@Singleton
class SessionRepository @Inject()(config: Configuration, mongo: MongoComponent)(implicit executionContext: ExecutionContext) {

  private lazy val sessionRepository = new ReactiveMongoRepository(config, mongo)

  def apply(): ReactiveMongoRepository = sessionRepository
}
