import sbt._
import Keys._
import sbtunidoc.Plugin.unidocSettings
import de.johoop.jacoco4sbt._
import JacocoPlugin._
import com.typesafe.sbt.license.{DepModuleInfo, LicenseInfo}
import com.typesafe.sbt.SbtLicenseReport.autoImport._
import com.typesafe.sbt.SbtAspectj.{ Aspectj, defaultAspectjSettings }
import com.typesafe.sbt.SbtAspectj.AspectjKeys.{ aspectjVersion, compileOnly, lintProperties, weaverOptions }

object BuildSettings {

  lazy val scalaSettings = Seq(
    scalaVersion := "2.11.8",
    scalacOptions ++= Seq(
      "-target:jvm-1.7",
      "-unchecked",
      "-deprecation",
      "-feature",
      "-language:postfixOps",
      "-language:existentials"
    ),
    javacOptions ++= Seq(
      "-Xlint:unchecked",
      "-source",
      "1.8"

    )
  )

  lazy val rev = SettingKey[String]("rev", "Revision of software")

  lazy val versionSettings = Seq(
    organization := "com.mine",
    rev := {
      "0"
    },
    version := {
      "1.0" + rev.value
    }
  )

}

object Dependencies {
  lazy val testSettings = Seq(
    resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases",
    libraryDependencies ++= Seq(
      "org.specs2" %% "specs2" % "2.4.17" % "test",
      "org.scalatest" %% "scalatest" % "2.2.5" % "test",
      "junit" % "junit" % "4.12" % "test",
      "com.novocode" % "junit-interface" % "0.11" % "test",
      "com.twitter" %% "util-core" % "6.29.0" % "test"
    ),
    parallelExecution in Test := false,
    fork in (Test, run) := true
  )

  lazy val jedisSettings = Seq(
    libraryDependencies ++= Seq(
      "redis.clients" % "jedis" % "2.9.0",
      "com.orange.redis-embedded" % "embedded-redis" % "0.6" % "test"
    )
  )

  lazy val configSettings = Seq(
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.1"
    )
  )

}

object Publishing {
  lazy val publishSettings = Seq(
    publishMavenStyle := true,
    pomIncludeRepository := { x => false }
  )
}

object JediScalaBuild extends Build {

  import BuildSettings._
  import Dependencies._
  import Publishing._


  lazy val commonSettings = versionSettings ++ publishSettings ++ jacoco.settings


  lazy val projectName = "pug"

  lazy val pug = Project(projectName, file(projectName),
    settings = Defaults.coreDefaultSettings ++
      commonSettings ++
      jedisSettings ++
      scalaSettings ++
      configSettings ++
      testSettings)

}
