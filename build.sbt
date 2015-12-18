import sbt._
import Keys._


// Create a main class (for development purposes only!)
mainClass in (Compile, run) := Some("org.leisurelyscript.gdl.Main")

lazy val root = (project in file(".")).
  settings(
    name := "leisurely_script",
    version := "1.0",
    scalaVersion := "2.11.7",
    libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
    libraryDependencies += "io.spray" %%  "spray-json" % "1.3.2",
    libraryDependencies += "commons-codec" %  "commons-codec" % "1.9",
    libraryDependencies += "net.databinder.dispatch" %% "dispatch-core" % "0.11.2"
  )
