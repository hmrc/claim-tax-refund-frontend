import play.core.PlayVersion
import play.sbt.PlayImport.ws
import sbt._

object AppDependencies {
  val mongoVersion = "1.7.0"

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc.mongo"            %% "hmrc-mongo-play-28"            % mongoVersion,
    "uk.gov.hmrc"                  %% "play-conditional-form-mapping" % "1.13.0-play-28",
    "uk.gov.hmrc"                  %% "tax-year"                      % "4.0.0",
    "com.googlecode.libphonenumber" % "libphonenumber"                % "8.13.21",
    "uk.gov.hmrc"                  %% "sca-wrapper-play-28"           % "1.3.0"
  )

  def test(scope: String = "test"): Seq[ModuleID] = Seq(
    "uk.gov.hmrc"            %% "bootstrap-test-play-28"   % "8.1.0" % scope,
    "uk.gov.hmrc.mongo"      %% "hmrc-mongo-test-play-28"  % mongoVersion % scope,
    "com.github.tomakehurst"  % "wiremock-standalone"      % "2.27.2" % scope,
    "org.scalatestplus"      %% "scalatestplus-mockito"    % "1.0.0-M2" % scope,
    "org.scalatestplus.play" %% "scalatestplus-play"       % "5.1.0" % scope,
    "org.scalatestplus"      %% "scalatestplus-scalacheck" % "3.1.0.0-RC2" % scope,
    "org.pegdown"             % "pegdown"                  % "1.6.0" % scope,
    "org.jsoup"               % "jsoup"                    % "1.16.1" % scope,
    "com.typesafe.play"      %% "play-test"                % PlayVersion.current % scope,
    "org.mockito"             % "mockito-all"              % "1.10.19" % scope
  )

  def apply(): Seq[ModuleID] = compile ++ test()

}



