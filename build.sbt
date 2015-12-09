import sbt._
import Keys._


mainClass in (Compile, run) := Some("org.leisurelyscript.gdl.Main")
//fork := true
//javaOptions in test += "-Xms4G -Xmx4G -Xss10M -XX:+CMSClassUnloadingEnabled -XX:MaxPermSize=1024M"
lazy val root = (project in file(".")).
    settings(
        name := "leisurely_script",
        version := "1.0",
        scalaVersion := "2.11.7",
        libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test",
        libraryDependencies += "io.spray" %%  "spray-json" % "1.3.2"
    )
