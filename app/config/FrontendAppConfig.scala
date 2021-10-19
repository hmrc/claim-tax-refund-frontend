/*
 * Copyright 2021 HM Revenue & Customs
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

import java.net.URLEncoder

import com.google.inject.{Inject, Singleton}
import play.api.Configuration
import controllers.routes
import play.api.i18n.Lang
import play.api.mvc.{Call, Request}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import scala.util.Try

@Singleton
class FrontendAppConfig @Inject()(val servicesConfig: ServicesConfig, val configuration: Configuration) {

  private def loadConfig(key: String): String = Try(servicesConfig.getString(key)).getOrElse(throw new Exception(s"Missing configuration key: $key"))
  private def loadConfigInt(key: String): Int = Try(servicesConfig.getInt(key)).getOrElse(throw new Exception(s"Missing configuration key: $key"))

  private lazy val contactHost = configuration.getOptional[String]("contact-frontend.host").getOrElse("")
  lazy val contactFormServiceIdentifier = loadConfig("contact-frontend.serviceId")

  lazy val assetsPrefix: String = loadConfig(s"assets.url") + loadConfig(s"assets.version") + '/'
  lazy val frontendTemplatePath: String = loadConfig("microservice.services.frontend-template-provider.path")

  lazy val reportAProblemPartialUrl = s"$contactHost/contact/problem_reports_ajax?service=$contactFormServiceIdentifier"
  lazy val reportAProblemNonJSUrl = s"$contactHost/contact/problem_reports_nonjs?service=$contactFormServiceIdentifier"
  lazy val betaFeedbackUrl = s"$contactHost/contact/beta-feedback"
  lazy val betaFeedbackUnauthenticatedUrl = s"$contactHost/contact/beta-feedback-unauthenticated"
  lazy val feedbackSurveyUrl: String = loadConfig("urls.feedback-survey")

  lazy val authUrl: String = servicesConfig.baseUrl("auth")
  lazy val loginUrl: String = loadConfig("urls.login")
  lazy val loginContinueUrl: String = loadConfig("urls.loginContinue")
  lazy val ctrUrl: String = servicesConfig.baseUrl("claim-tax-refund")
  lazy val addressLookupUrl: String = servicesConfig.baseUrl(serviceName = "address-lookup-frontend")
  lazy val addressLookupContinueUrlNormalMode: String = loadConfig(key = "microservice.services.address-lookup-frontend.continueUrlNormalMode")
  lazy val addressLookupContinueUrlCheckMode: String = loadConfig(key = "microservice.services.address-lookup-frontend.continueUrlCheckMode")

  lazy val ivUpliftUrl: String = loadConfig("identity-verification-uplift.host")
  lazy val authorisedCallback: String = loadConfig("identity-verification-uplift.authorised-callback.url")
  lazy val unauthorisedCallback: String = loadConfig("identity-verification-uplift.unauthorised-callback.url")

  lazy val taiUrl: String = servicesConfig.baseUrl("tai")

  lazy val ptaBaseUrl = servicesConfig.baseUrl("pertax-frontend")
  lazy val ptaHomeUrl = s"$ptaBaseUrl${servicesConfig.getConfString("pertax-frontend.urls.home","/personal-account")}"
  lazy val messagesUrl = s"$ptaBaseUrl${servicesConfig.getConfString("pertax-frontend.urls.messages","/messages")}"
  lazy val paperlessSettingsUrl = s"$ptaBaseUrl${servicesConfig.getConfString("pertax-frontend.urls.paperlessSettings","/preferences")}"
  lazy val personalDetailsUrl = s"$ptaBaseUrl${servicesConfig.getConfString("pertax-frontend.urls.personalDetails","/personal-details")}"
  lazy val signOutUrl = s"$ptaBaseUrl${servicesConfig.getConfString("pertax-frontend.urls.signOut","/signout?continueUrl=%2Ffeedback%2FCTR")}"

  lazy val trackingBaseUrl = servicesConfig.baseUrl("tracking-frontend")
  lazy val trackingHomeUrl = s"$trackingBaseUrl${servicesConfig.getConfString("tracking-frontend.urls.home","/track")}"

  lazy val languageTranslationEnabled: Boolean = Try(servicesConfig.getBoolean("microservice.services.features.welsh-translation")).getOrElse(true)

  lazy val fullNameLength: Int = loadConfigInt("microservice.services.validation.full-name-length")
  lazy val ninoRegex: String = loadConfig("microservice.services.validation.nino-regex")
  lazy val addressLineMaxLength: Int = loadConfigInt("microservice.services.validation.address-line-length")
  lazy val postcodeRegex: String = loadConfig("microservice.services.validation.postcode-regex")
  lazy val countryMaxLength: Int = loadConfigInt("microservice.services.validation.country-length")
  lazy val agentReferenceMaxLength: Int = loadConfigInt("microservice.services.validation.agent-reference-length")
  lazy val nomineeFullNameMaxLength: Int = loadConfigInt("microservice.services.validation.payee-full-name-length")
  lazy val detailsOfEmploymentOrPensionMaxLength: Int = loadConfigInt("microservice.services.validation.details-of-employment-or-pension-max-length")

  lazy val telephoneRegex: String = loadConfig("microservice.services.validation.telephone-regex")
  lazy val utrRegex: String = loadConfig("microservice.services.validation.utr-regex")
  lazy val payeRegex: String = loadConfig("microservice.services.validation.paye-regex")
  lazy val currencyRegex: String = loadConfig("microservice.services.validation.currency-regex")

  lazy val accessibilityStatementPath: String = loadConfig("accessibility-statement.service-path")

  lazy val accessibilityStatementFrontendBaseUrl: String = configuration.getOptional[String]("accessibility-statement.host").getOrElse("")
  def accessibilityFooterUrl(referrerUrl: Option[String] = None): String = {
    val referrerString = referrerUrl.map (url => s"""?referrerUrl=${URLEncoder.encode(url, "UTF-8")}""").getOrElse("")
    s"$accessibilityStatementFrontendBaseUrl/accessibility-statement$accessibilityStatementPath$referrerString"
  }

  def languageMap: Map[String, Lang] = Map("english" -> Lang("en"), "cymraeg" -> Lang("cy"))

  def routeToSwitchLanguage: String => Call = (lang: String) => routes.LanguageSwitchController.switchToLanguage(lang)
}
