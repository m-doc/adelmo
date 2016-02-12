enablePlugins(MdocPlugin)

name := "adelmo"

validateCommands -= "coverage"

lazy val jsProjects = Seq(client)

lazy val client = project
  .settings(
    persistLauncher := true,
    persistLauncher in Test := false,
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.8.2"
  )
  .enablePlugins(MdocPlugin)
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSPlay)

lazy val server = project
  .settings(
    scalaJSProjects := jsProjects,
    libraryDependencies += "com.vmunier" %% "play-scalajs-scripts" % "0.4.0"
  )
  .enablePlugins(MdocPlugin)
  .enablePlugins(PlayScala)
  .aggregate(jsProjects.map(sbt.Project.projectToRef): _*)
