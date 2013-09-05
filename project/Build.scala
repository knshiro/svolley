import sbt._
import Keys._

object Dependencies {
  val resolutionRepos = Seq(
    //    "typesafe releases" at "http://repo.typesafe.com/typesafe/releases",
    //    "typesafe snapshots" at "http://repo.typesafe.com/typesafe/snapshots",
    //    "sonatype releases" at "https://oss.sonatype.org/content/repositories/releases",
    //    "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  )

  val android = "com.google.android" % "android" % "2.2.1" % "provided"
}


object SVolleyBuild extends Build {

  import Dependencies._

  lazy val basicSettings = Seq(
    organization := "com.smint-corp",
    description := "Wrapper around Google Volley library for Android",
    startYear := Some(2013),
    scalaVersion := "2.10.2",
    resolvers ++= Dependencies.resolutionRepos,
    scalacOptions := Seq(
      "-target:jvm-1.6", "-deprecation", "-feature"
    ),
    javacOptions ++= Seq(
      "-source", "1.6",
      "-target", "1.6"))

  lazy val main = Project(id = "main", base = file("."))
    .settings(name := "svolley")
    .settings(version := "0.0.1")
    .settings(basicSettings: _*)
    .settings(libraryDependencies += android)

  // configure prompt to show current project
  override lazy val settings = super.settings :+ {
    shellPrompt := {
      s => Project.extract(s).currentProject.id + "> "
    }
  }


}
