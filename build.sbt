import sbt._
import Keys._


// Create a main class (for development purposes only!)
mainClass in (Compile, run) := Some("org.leisurelyscript.gdl.Main")

lazy val root = (project in file(".")).
  settings(
    name := "leisurely_script",
    version := "1.0",
    scalaVersion := "2.11.7",
    libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
