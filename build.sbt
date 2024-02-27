name := "commons-i18n"

organization := "com.greenfossil"

version := "1.0.9"

scalaVersion := "3.3.1"

scalacOptions ++= Seq("-unchecked", "-feature", "-deprecation")

Compile / javacOptions ++= Seq("-source", "17")


lazy val commonsI18n = project in file(".")

val javaLibraries = Seq(
  "org.slf4j" % "slf4j-api" % "2.0.10",
  "ch.qos.logback" % "logback-classic" % "1.4.14" % Test,
)

/*
 * https://github.com/scala/scala-module-dependency-sample
 */
libraryDependencies ++= javaLibraries ++ Seq(
  "com.greenfossil" %% "typesafe-config-ext" % "1.0.2",
  "org.scalameta" %% "munit" % "0.7.29" % Test,
)
