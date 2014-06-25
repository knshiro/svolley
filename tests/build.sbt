import android.Keys._

android.Plugin.androidBuild

name := "svolley-tests"

scalacOptions in Compile += "-feature"

platformTarget in Android := "android-18"

scalaVersion := "2.10.4"

//lazy val volley = ProjectRef(uri(".."),"svolley")

//lazy val root = (project in file(".")).dependsOn(volley)

libraryDependencies ++= Seq(
  "me.ugo" %% "svolley" % "0.0.3-SNAPSHOT"
)

proguardScala in Android := true

proguardOptions in Android ++= Seq("-dontobfuscate", "-dontoptimize")

proguardOptions in Android ++= Seq(
  "-keep public class * extends junit.framework.TestCase",
  "-keepclassmembers class * extends junit.framework.TestCase { *; }",
  "-keep public class * extends junit.framework.Assert",
  "-keepclassmembers class * extends junit.framework.Assert { *; }"
)

proguardOptions in Android ++= Seq(
  "-keep public class me.ugo.svolley.** { *; }",
  "-keep public class scala.collection.mutable.StringBuilder",
  "-keepclassmembers class scala.collection.mutable.StringBuilder { *; }"
)
