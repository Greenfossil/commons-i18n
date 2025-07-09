name := "commons-i18n"

organization := "com.greenfossil"

version := "1.3.0"

scalaVersion := "3.7.1"

scalacOptions ++= Seq("-unchecked", "-feature", "-deprecation")

lazy val commonsI18n = project in file(".")

val javaLibraries = Seq(
  "org.slf4j" % "slf4j-api" % "2.0.16",
  "ch.qos.logback" % "logback-classic" % "1.5.16" % Provided,
)

/*
 * https://github.com/scala/scala-module-dependency-sample
 */
libraryDependencies ++= javaLibraries ++ Seq(
  "com.greenfossil" %% "typesafe-config-ext" % "1.3.0",
  "org.scalameta" %% "munit" % "1.1.1" % Test,
)

//https://www.scala-sbt.org/1.x/docs/Publishing.html
ThisBuild / versionScheme := Some("early-semver")
