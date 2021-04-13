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
  "uk.gov.hmrc"           %% "bootstrap-frontend-play-27"     % "3.0.0",
  "uk.gov.hmrc"           %% "simple-reactivemongo"           % "7.31.0-play-27",
  "uk.gov.hmrc"           %% "local-template-renderer"        % "2.10.0-play-26",
  "uk.gov.hmrc"           %% "govuk-template"                 % "5.63.0-play-27",
  "uk.gov.hmrc"           %% "play-health"                    % "3.16.0-play-27",
  "uk.gov.hmrc"           %% "play-ui"                        % "8.21.0-play-27",
  "uk.gov.hmrc"           %% "http-caching-client"            % "9.2.0-play-27",
  "uk.gov.hmrc"           %% "play-conditional-form-mapping"  % "1.5.0-play-27",
  "uk.gov.hmrc"           %% "play-partials"                  % "7.1.0-play-27",
  "uk.gov.hmrc"           %% "play-language"                  % "4.10.0-play-27",
  "uk.gov.hmrc"           %% "tax-year"                       % "1.2.0",
  "org.scalatra.scalate"  %% "play-scalate"                   % "0.6.0",
  "org.scalatra.scalate"  %% "scalate-core"                   % "1.9.6",
  "uk.gov.hmrc"           %% "domain"                         % "5.10.0-play-27",
)

def test(scope: String = "test"): Seq[ModuleID] = Seq(
  "com.github.tomakehurst"  %   "wiremock"                % "2.26.3" % scope,
  "com.github.tomakehurst"  %   "wiremock-jre8"           % "2.26.3" % scope,
  "org.scalatest"           %%  "scalatest"               % "3.0.9" % scope,
  "org.scalatestplus.play"  %%  "scalatestplus-play"      % "4.0.3" % scope,
  "org.scalacheck"          %%  "scalacheck"              % "1.15.1" % scope,
  "org.pegdown"             %   "pegdown"                 % "1.6.0" % scope,
  "org.jsoup"               %   "jsoup"                   % "1.13.1" % scope,
  "com.typesafe.play"       %%  "play-test"               % PlayVersion.current % scope,
  "org.mockito"             %   "mockito-all"             % "1.10.19" % scope,
  "uk.gov.hmrc"             %%  "play-allowlist-filter"   % "1.0.0-play-27"
)

def oneForkedJvmPerTest(tests: Seq[TestDefinition]): Seq[Group] =
  tests.map { test =>
    Group(test.name, Seq(test), SubProcess(ForkOptions().withRunJVMOptions(Vector(s"-Dtest.name=${test.name}"))))
  }

lazy val microservice = Project(appName, file("."))
  .enablePlugins(Seq(play.sbt.PlayScala, PlayNettyServer, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory) ++ plugins: _*)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
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
  .settings(scalaVersion := "2.12.12")
// ***************
// Use the silencer plugin to suppress warnings from unused imports in compiled twirl templates
scalacOptions += "-P:silencer:pathFilters=routes"
scalacOptions += "-P:silencer:lineContentFilters=^\\w"
libraryDependencies ++= Seq(
  compilerPlugin("com.github.ghik" % "silencer-plugin" % "1.7.1" cross CrossVersion.full),
  "com.github.ghik" % "silencer-lib" % "1.7.1" % Provided cross CrossVersion.full
)
// ***************
