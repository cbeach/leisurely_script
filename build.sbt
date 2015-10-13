import sbt._
import Keys._


// mainClass in (Compile, run) := Some("")
lazy val root = (project in file(".")).
    settings(
        name := "leisurely_script",
        version := "1.0",
        scalaVersion := "2.11.7",
        libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
        libraryDependencies += "io.spray" %%  "spray-json" % "1.3.2",
        libraryDependencies += "com.assembla.scala-incubator" %% "graph-core" % "1.9.4"
    )
