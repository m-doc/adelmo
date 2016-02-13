lazy val root = project.in(file("."))
  .enablePlugins(MdocPlugin)
  .aggregate(client, server)
  .settings(
    name := "adelmo",
    validateCommands --= Seq("coverage", "coverageReport"),
    validateCommands += "debian:packageBin"
  )

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
    name := "adelmo-server",
    scalaJSProjects += client,
    libraryDependencies ++= Seq(
      ws,
      MdocLibrary.commonModel,
      Library.circeCore,
      Library.circeGeneric,
      "com.vmunier" %% "play-scalajs-scripts" % "0.4.0"
    ),
    maintainer := "m-doc <info@m-doc.org>"
  )
