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

package config

import com.google.inject.{Inject, Singleton}
import play.api.{Configuration, Environment}
import play.api.i18n.Lang
import uk.gov.hmrc.play.bootstrap.config.AppName
import controllers.routes
import play.api.Mode.Mode
import uk.gov.hmrc.play.config.ServicesConfig

@Singleton
class FrontendAppConfig @Inject() (override val runModeConfiguration: Configuration, environment: Environment) extends ServicesConfig {

  override protected def mode: Mode = environment.mode

  private def loadConfig(key: String) = runModeConfiguration.getString(key).getOrElse(throw new Exception(s"Missing configuration key: $key"))
  private def loadConfigInt(key: String) = runModeConfiguration.getInt(key).getOrElse(throw new Exception(s"Missing configuration key: $key"))

  private lazy val contactHost = runModeConfiguration.getString("contact-frontend.host").getOrElse("")
  private val contactFormServiceIdentifier = "claimtaxrefundfrontend"

  lazy val analyticsToken = loadConfig(s"google-analytics.token")
  lazy val analyticsHost = loadConfig(s"google-analytics.host")
  lazy val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
  lazy val betaFeedbackUrl = s"$contactHost/contact/beta-feedback"
  lazy val betaFeedbackUnauthenticatedUrl = s"$contactHost/contact/beta-feedback-unauthenticated"

  lazy val authUrl = baseUrl("auth")
  lazy val loginUrl = loadConfig("urls.login")
  lazy val loginContinueUrl = loadConfig("urls.loginContinue")

  lazy val languageTranslationEnabled = runModeConfiguration.getBoolean("microservice.services.features.welsh-translation").getOrElse(true)

  lazy val fullNameLength = loadConfigInt("microservice.services.validation.full-name-length")
  lazy val ninoRegex = loadConfig("microservice.services.validation.nino-regex")
  lazy val addressLineMaxLength = loadConfigInt("microservice.services.validation.address-line-length")
  lazy val postcodeRegex = loadConfig("microservice.services.validation.postcode-regex")
  lazy val countryMaxLength = loadConfigInt("microservice.services.validation.country-length")
  lazy val agentReferenceMaxLength = loadConfigInt("microservice.services.validation.agent-reference-length")
  lazy val payeeFullNameMaxLength = loadConfigInt("microservice.services.validation.payee-full-name-length")

  lazy val telephoneRegex = loadConfig("microservice.services.validation.telephone-regex")
  lazy val utrRegex = loadConfig("microservice.services.validation.utr-regex")
  lazy val payeRegex = loadConfig("microservice.services.validation.paye-regex")
  lazy val currencyRegex = loadConfig("microservice.services.validation.currency-regex")

  def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy"))
  def routeToSwitchLanguage = (lang: String) => routes.LanguageSwitchController.switchToLanguage(lang)
}
