lazy val client = project
  .enablePlugins(MdocPlugin)
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSPlay)
  .settings(
    name := "adelmo-client",
    libraryDependencies ++= Seq(
      "com.github.japgolly.scalajs-react" %%% "core" % "0.10.4",
      "org.scala-js" %%% "scalajs-dom" % "0.8.2"
    ),
    persistLauncher := true,
    persistLauncher in Test := false
  )

lazy val server = project
  .enablePlugins(MdocPlugin)
  .enablePlugins(PlayScala)
  .aggregate(client)
  .settings(
    name := "adelmo",
    libraryDependencies ++= Seq(
      Library.bootstrap,
      Library.jquery,
      Library.react,
      Library.reactDom,
      Library.webjarsPlay,
      "com.vmunier" %% "play-scalajs-scripts" % "0.4.0"
    ),
    scalaJSProjects += client,
    serverLoading in Debian :=
      com.typesafe.sbt.packager.archetypes.ServerLoader.SystemV,
    mdocValidateCommands --= Seq("coverage", "coverageReport"),
    mdocValidateCommands += "debian:packageBin"
  )

onLoad.in(Global) := onLoad.in(Global).value
  .andThen(state => Command.process("project server", state))
