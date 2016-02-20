lazy val client = project
  .enablePlugins(MdocPlugin)
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSPlay)
  .settings(
    name := "adelmo-client",
    persistLauncher := true,
    persistLauncher in Test := false,
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.8.2"
  )

lazy val server = project
  .enablePlugins(MdocPlugin)
  .enablePlugins(PlayScala)
  .aggregate(client)
  .settings(
    name := "adelmo",
    scalaJSProjects += client,
    libraryDependencies ++= Seq(
      ws,
      MdocLibrary.commonModel,
      Library.bootstrap,
      Library.circeCore,
      Library.circeGeneric,
      Library.webjarsPlay,
      "com.vmunier" %% "play-scalajs-scripts" % "0.4.0"
    ),
    serverLoading in Debian := com.typesafe.sbt.packager.archetypes.ServerLoader.SystemV,
    mdocValidateCommands --= Seq("coverage", "coverageReport"),
    mdocValidateCommands += "debian:packageBin"
  )

// loads the Play project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
