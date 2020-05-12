name := """cashforchores"""
organization := "teamsix"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.1"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.+"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "teamsix.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "teamsix.binders._"
