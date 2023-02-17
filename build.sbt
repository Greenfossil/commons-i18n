name := "commons-i18n"

organization := "com.greenfossil"

version := "1.0.1"

scalaVersion := "3.2.2"

scalacOptions ++= Seq("-unchecked", "-feature", "-deprecation")

Compile / javacOptions ++= Seq("-source", "17")

lazy val configExtVersion = "1.0.0"

lazy val commonsI18n = project in file(".")

val javaLibraries = Seq(
  "org.slf4j" % "slf4j-api" % "2.0.3",
  "ch.qos.logback" % "logback-classic" % "1.4.4" % Test,
)

/*
 * https://github.com/scala/scala-module-dependency-sample
 */
libraryDependencies ++= javaLibraries ++ Seq(
  "com.greenfossil" %% "typesafe-config-ext" % configExtVersion,
  "org.scalameta" %% "munit" % "0.7.29" % Test,
)
