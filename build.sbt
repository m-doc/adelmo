enablePlugins(MdocPlugin)
enablePlugins(PlayScala)

name := "adelmo"

libraryDependencies ++= Seq(
  ws,
  MdocLibrary.commonModel,
  Library.bootstrap,
  Library.circeCore,
  Library.circeGeneric,
  Library.webjarsPlay
)

serverLoading in Debian := com.typesafe.sbt.packager.archetypes.ServerLoader.SystemV

mdocValidateCommands += "debian:packageBin"
