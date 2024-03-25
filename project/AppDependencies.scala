import play.core.PlayVersion
import play.sbt.PlayImport.ws
import sbt._

object AppDependencies {
  val mongoVersion = "1.7.0"
  val boostrapVersion = "8.4.0"

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-30" % mongoVersion,
    "uk.gov.hmrc" %% "play-conditional-form-mapping-play-30" % "2.0.0",
    "uk.gov.hmrc" %% "tax-year" % "4.0.0",
    "com.googlecode.libphonenumber" % "libphonenumber" % "8.13.32",
    "uk.gov.hmrc" %% "sca-wrapper-play-30" % "1.6.0",
    "commons-codec" % "commons-codec" % "1.16.1"
  )

  def test(scope: String = "test"): Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-30" % boostrapVersion % scope,
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-test-play-30" % mongoVersion % scope,
    "org.scalatestplus" %% "scalacheck-1-17" % "3.2.16.0"
  )

  def apply(): Seq[ModuleID] = compile ++ test()

}



