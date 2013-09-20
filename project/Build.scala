import sbt._
import Keys._ 
import android.ArbitraryProject
 
object SVolleyBuild extends Build {
  import android.Keys._
  import android.Dependencies.LibraryProject
 
  val androidLib = "com.google.android" % "android" % "2.2.1" % "provided"

  val volleyGit = uri("https://android.googlesource.com/platform/frameworks/volley#4c2fe1379f5d18bac2d27e89d4abb553e6f8d2e8")

  val volleyBase = ArbitraryProject.git(volleyGit)

  lazy val volleySettings = android.Plugin.androidBuild ++ Seq(
    platformTarget in Android := "android-17",
    libraryProject in Android := true
  )
 
  override def buildLoaders = ArbitraryProject.settingsLoader(
    Map(volleyBase -> volleySettings))
 
  lazy val volley = RootProject(volleyBase)

  lazy val basicSettings = Seq(
    organization := "com.smint-corp",
    description := "Wrapper around Google Volley library for Android",
    startYear := Some(2013),
    scalaVersion := "2.10.2",
    scalacOptions := Seq(
      "-target:jvm-1.6", "-deprecation", "-feature"
    ),
    javacOptions ++= Seq(
      "-source", "1.6",
      "-target", "1.6"))

 
  lazy val main = Project(id = "main", base = file("."))
    .settings(name := "svolley")
    .settings(version := "0.0.2")
    .settings(basicSettings: _*)
    .settings(libraryDependencies += androidLib) dependsOn(volley % "provided")

}
