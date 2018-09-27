import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning
import play.sbt.PlayImport._
import play.core.PlayVersion
import play.sbt.routes.RoutesKeys
import sbt.Tests.{Group, SubProcess}
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.{addTestReportOption, defaultSettings, scalaSettings}
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings


val appName = "claim-tax-refund-frontend"

lazy val appDependencies: Seq[ModuleID] = compile ++ test()
lazy val plugins: Seq[Plugins] = Seq.empty
lazy val playSettings: Seq[Setting[_]] = Seq.empty


val playHealthVersion = "2.1.0"
val logbackJsonLoggerVersion = "3.1.0"
val govukTemplateVersion = "5.3.0"
val playUiVersion = "7.4.0"
val hmrcTestVersion = "2.3.0"
val scalaTestVersion = "3.0.1"
val scalaTestPlusPlayVersion = "2.0.1"
val pegdownVersion = "1.6.0"
val mockitoAllVersion = "1.10.19"
val httpCachingClientVersion = "7.0.0"
val playReactivemongoVersion = "5.2.0"
val playConditionalFormMappingVersion = "0.2.0"
val playLanguageVersion = "3.4.0"
val wireMockVersion = "2.15.0"
val bootstrapVersion = "1.7.0"
val localTemplateRendererVersion = "2.0.0"
val playPartialsVersion = "6.1.0"

val compile = Seq(
  ws,
  "uk.gov.hmrc" %% "play-reactivemongo" % playReactivemongoVersion,
  "uk.gov.hmrc" %% "logback-json-logger" % logbackJsonLoggerVersion,
  "uk.gov.hmrc" %% "govuk-template" % govukTemplateVersion,
  "uk.gov.hmrc" %% "play-health" % playHealthVersion,
  "uk.gov.hmrc" %% "play-ui" % playUiVersion,
  "uk.gov.hmrc" %% "http-caching-client" % httpCachingClientVersion,
  "uk.gov.hmrc" %% "play-conditional-form-mapping" % playConditionalFormMappingVersion,
  "uk.gov.hmrc" %% "bootstrap-play-25" % bootstrapVersion,
  "uk.gov.hmrc" %% "local-template-renderer" % localTemplateRendererVersion,
  "uk.gov.hmrc" %% "play-partials" % playPartialsVersion,
  "uk.gov.hmrc" %% "play-language" % playLanguageVersion
)

def test(scope: String = "test"): Seq[ModuleID] = Seq(
  "com.github.tomakehurst" % "wiremock" % wireMockVersion % "test,it",
  "uk.gov.hmrc" %% "hmrctest" % hmrcTestVersion % scope,
  "org.scalatest" %% "scalatest" % scalaTestVersion % scope,
  "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusPlayVersion % scope,
  "org.scalacheck" %% "scalacheck" % "1.14.0" % scope,
  "org.pegdown" % "pegdown" % pegdownVersion % scope,
  "org.jsoup" % "jsoup" % "1.10.3" % scope,
  "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
  "org.mockito" % "mockito-all" % mockitoAllVersion % scope,
  "uk.gov.hmrc" %% "play-whitelist-filter"  % "2.0.0"
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
    scalacOptions ++= Seq("-Xfatal-warnings", "-feature"),
    libraryDependencies ++= appDependencies,
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    PlayKeys.devSettings += "play.server.http.port" -> "9969"
  )
  .configs(IntegrationTest)
  .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
  .settings(
    Keys.fork in Test := true,
    Keys.fork in IntegrationTest := false,
    javaOptions in Test += "-Dconfig.file=conf/test.application.conf",
    unmanagedSourceDirectories in IntegrationTest <<= (baseDirectory in IntegrationTest) (base => Seq(base / "it")),
    addTestReportOption(IntegrationTest, "int-test-reports"),
    testGrouping in IntegrationTest := oneForkedJvmPerTest((definedTests in IntegrationTest).value),
    parallelExecution in IntegrationTest := false)
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
    UglifyKeys.compressOptions := Seq("unused=false", "dead_code=false"),
    pipelineStages := Seq(digest),
    // below line required to force asset pipeline to operate in dev rather than only prod
    pipelineStages in Assets := Seq(concat, uglify),
    // only compress files generated by concat
    includeFilter in uglify := GlobFilter("claimtaxrefundfrontend-*.js")
  )
  .settings(majorVersion := 0)


