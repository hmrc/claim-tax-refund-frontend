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

package config

import com.google.inject.AbstractModule
import play.api.{Configuration, Environment}
import views.{LayoutProvider, NewLayoutProvider, OldLayoutProvider}

class Module(environment: Environment, configuration: Configuration) extends AbstractModule {

  val scaWrapperEnabled: Boolean = configuration.getOptional[Boolean]("microservice.services.features.sca-wrapper").getOrElse(true)

  override def configure(): Unit = {

    if (scaWrapperEnabled) {
      bind(classOf[LayoutProvider]).to(classOf[NewLayoutProvider]).asEagerSingleton()
    } else {
      bind(classOf[LayoutProvider]).to(classOf[OldLayoutProvider]).asEagerSingleton()
    }
  }
}
