name := "leisurely_script"

mainClass in (Compile, run) := Some("astTest")
logLevel in Compile := Level.Error;


lazy val commonSettings = Seq(
  scalaVersion := "2.11.8",
  scalacOptions += "-Xplugin-require:macroparadise",
  scalacOptions += "-Xlint:_",
  scalacOptions += "-Ywarn-unused-import",
  scalacOptions += "-Ywarn-unused",
  scalacOptions += "-Ywarn-value-discard",
  scalacOptions += "-Ywarn-infer-any",
  scalacOptions += "-Ywarn-dead-code",
  resolvers += Resolver.typesafeRepo("releases"),
  resolvers += Resolver.sonatypeRepo("releases"),
  resolvers += Resolver.sonatypeRepo("snapshots"),
  addCompilerPlugin("org.scalamacros" % "paradise" % "3.0.0-M3" cross CrossVersion.full),
  libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.0-RC4",
  libraryDependencies += "org.scalameta" %% "scalameta" % "1.0.0"
)

lazy val root = (project in file("."))
    .settings(commonSettings:_*)
    //.settings(coverageEnabled := true)
    .dependsOn(macros)

lazy val macros = (project in file("macros"))
    .settings(commonSettings:_*)
    .settings(
      libraryDependencies += "org.scalameta" %% "scalameta" % "1.0.0"
)
