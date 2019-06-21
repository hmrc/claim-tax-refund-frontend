import play.core.PlayVersion
import play.sbt.PlayImport._
import play.sbt.routes.RoutesKeys
import sbt.Tests.{Group, SubProcess}
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.{defaultSettings, scalaSettings}
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings
import uk.gov.hmrc.versioning.SbtGitVersioning


val appName = "claim-tax-refund-frontend"

lazy val appDependencies: Seq[ModuleID] = compile ++ test()
lazy val plugins: Seq[Plugins] = Seq.empty
lazy val playSettings: Seq[Setting[_]] = Seq.empty

val compile = Seq(
  ws,
  "uk.gov.hmrc"           %% "simple-reactivemongo"           % "7.20.0-play-26",
  "uk.gov.hmrc"           %% "logback-json-logger"            % "4.6.0",
  "uk.gov.hmrc"           %% "govuk-template"                 % "5.36.0-play-26",
  "uk.gov.hmrc"           %% "play-health"                    % "3.14.0-play-26",
  "uk.gov.hmrc"           %% "play-ui"                        % "7.40.0-play-26",
  "uk.gov.hmrc"           %% "http-caching-client"            % "8.3.0",
  "uk.gov.hmrc"           %% "play-conditional-form-mapping"  % "0.2.0",
  "uk.gov.hmrc"           %% "bootstrap-play-26"              % "0.41.0",
  "uk.gov.hmrc"           %% "local-template-renderer"        % "2.5.0",
  "uk.gov.hmrc"           %% "play-partials"                  % "6.9.0-play-26",
  "uk.gov.hmrc"           %% "play-language"                  % "3.4.0",
  "uk.gov.hmrc"           %% "tax-year"                       % "0.4.0",
  "org.scalatra.scalate"  %% "play-scalate"                   % "0.5.0",
  "org.scalatra.scalate"  %% "scalate-core"                   % "1.9.4",
  "uk.gov.hmrc"           %% "domain"                         % "5.6.0-play-26"
)

def test(scope: String = "test"): Seq[ModuleID] = Seq(
  "com.github.tomakehurst"  % "wiremock"                % "2.23.2" % scope,
  "com.github.tomakehurst"  % "wiremock-jre8"           % "2.23.2" % scope,
  "uk.gov.hmrc"             %% "hmrctest"               % "3.9.0-play-26" % scope,
  "org.scalatest"           %% "scalatest"              % "3.0.8" % scope,
  "org.scalatestplus.play"  %% "scalatestplus-play"     % "3.1.2" % scope,
  "org.scalacheck"          %% "scalacheck"             % "1.14.0" % scope,
  "org.pegdown"             % "pegdown"                 % "1.6.0" % scope,
  "org.jsoup"               % "jsoup"                   % "1.12.1" % scope,
  "com.typesafe.play"       %% "play-test"              % PlayVersion.current % scope,
  "org.mockito"             % "mockito-all"             % "1.10.19" % scope,
  "uk.gov.hmrc"             %% "play-whitelist-filter"  % "2.0.0"
)

def oneForkedJvmPerTest(tests: Seq[TestDefinition]): Seq[Group] =
  tests map {
    test => new Group(test.name, Seq(test), SubProcess(ForkOptions(runJVMOptions = Seq("-Dtest.name=" + test.name))))
  }

lazy val microservice = Project(appName, file("."))
  .enablePlugins(Seq(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory) ++ plugins: _*)
  .settings(playSettings: _*)
  .settings(RoutesKeys.routesImport ++= Seq("models._"))
  .settings(
    ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*filters.*;.*handlers.*;.*components.*;.*repositories.*;" +
      ".*BuildInfo.*;.*javascript.*;.*FrontendAuditConnector.*;.*Routes.*;.*GuiceInjector;.*DataCacheConnector;" +
      ".*ControllerConfiguration;.*LanguageSwitchController",
    ScoverageKeys.coverageMinimum := 80,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true,
    parallelExecution in Test := false
  )
  .settings(scalaSettings: _*)
  .settings(publishingSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(
    scalacOptions ++= Seq("-feature"),
    libraryDependencies ++= appDependencies,
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    PlayKeys.devSettings += "play.server.http.port" -> "9969"
  )
  .settings(
    Keys.fork in Test := true,
    javaOptions in Test += "-Dconfig.file=conf/test.application.conf")
  .settings(resolvers ++= Seq(
    Resolver.bintrayRepo("hmrc", "releases"),
    Resolver.jcenterRepo,
    Resolver.bintrayRepo("emueller", "maven")
  ))
  .settings(
    // concatenate js
    Concat.groups := Seq(
      "javascripts/claimtaxrefundfrontend-app.js" -> group(Seq("javascripts/show-hide-content.js", "javascripts/claimtaxrefundfrontend.js"))
    ),
    // prevent removal of unused code which generates warning errors due to use of third-party libs
    uglifyCompressOptions := Seq("unused=false", "dead_code=false"),
    pipelineStages := Seq(digest),
    // below line required to force asset pipeline to operate in dev rather than only prod
    pipelineStages in Assets := Seq(concat, uglify),
    // only compress files generated by concat
    includeFilter in uglify := GlobFilter("claimtaxrefundfrontend-*.js")
  )
  .settings(majorVersion := 0)


