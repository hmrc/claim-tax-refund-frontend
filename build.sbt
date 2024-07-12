import play.core.PlayVersion
import play.sbt.PlayImport._
import play.sbt.routes.RoutesKeys
import sbt.Tests.{Group, SubProcess}
import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.{defaultSettings, scalaSettings}
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin


val appName = "claim-tax-refund-frontend"

lazy val appDependencies: Seq[ModuleID] = AppDependencies()
lazy val plugins: Seq[Plugins] = Seq.empty
lazy val playSettings: Seq[Setting[_]] = Seq.empty

lazy val microservice = Project(appName, file("."))
  .enablePlugins(Seq(play.sbt.PlayScala, PlayNettyServer, SbtDistributablesPlugin) ++ plugins: _*)
  .disablePlugins(JUnitXmlReportPlugin) // Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(playSettings: _*)
  .settings(RoutesKeys.routesImport ++= Seq("models._"))
  .settings(
    ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*filters.*;.*handlers.*;.*components.*;.*repositories.*;" +
      ".*BuildInfo.*;.*javascript.*;.*FrontendAuditConnector.*;.*Routes.*;.*GuiceInjector;.*DataCacheConnector;" +
      ".*ControllerConfiguration;.*LanguageSwitchController",
    ScoverageKeys.coverageMinimumStmtTotal := 80,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true,
    Test / parallelExecution  := false
  )
  .settings(scalaSettings: _*)
  .settings(isPublicArtefact := true)
  .settings(defaultSettings(): _*)
  .settings(
    scalacOptions ++= Seq("-feature"),
    libraryDependencies ++= appDependencies,
    retrieveManaged := true,
    update / evictionWarningOptions := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    PlayKeys.devSettings += "play.server.http.port" -> "9969"
  )
  .settings(
    Test / Keys.fork := true
  )
  .settings(resolvers ++= Seq(
    Resolver.jcenterRepo,
  ))
  .settings(majorVersion := 0)
  .settings(scalaVersion := "2.13.12")
  .settings(
    TwirlKeys.templateImports ++= Seq(
      "uk.gov.hmrc.govukfrontend.views.html.components._",
      "uk.gov.hmrc.hmrcfrontend.views.html.components._",
      "uk.gov.hmrc.hmrcfrontend.views.html.helpers._",
      "uk.gov.hmrc.govukfrontend.views.html.components.implicits._"
    )
  )
// ***************
// Use the silencer plugin to suppress warnings from unused imports in compiled twirl templates
scalacOptions += "-Wconf:cat=lint-multiarg-infix:silent"
scalacOptions += "-Wconf:cat=unused-imports&src=html/.*:s"
scalacOptions += "-Wconf:src=routes/.*:s"
// ***************


