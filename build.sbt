name := "svolley"

description := "A Scala wrapper for Android Volley"

homepage := Some(url("http://github.com/knshiro/svolley"))

organization := "me.ugo"

version := "0.0.3-SNAPSHOT"

scalaVersion := "2.10.4"

scalacOptions ++= Seq("-feature", "-deprecation")

crossScalaVersions := Seq("2.10.4", "2.11.1")

scalacOptions in (Compile, doc) ++= Seq(
  "-sourcepath", baseDirectory.value.getAbsolutePath,
  "-doc-source-url", "https://github.com/knshiro/svolley/tree/master€{FILE_PATH}.scala"
)

libraryDependencies ++= Seq(
  "com.google.android" % "android" % "4.1.1.4" % "provided",
  "com.mcxiaoke.volley" % "library" % "1.0.5"
)

licenses += ("WTFPL", url("http://www.wtfpl.net/txt/copying/"))
