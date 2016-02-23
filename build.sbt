enablePlugins(MdocPlugin)
enablePlugins(PlayScala)
enablePlugins(SbtWeb)

name := "adelmo"

libraryDependencies ++= Seq(
  ws,
  MdocLibrary.commonModel,
  Library.bootstrap,
  Library.circeCore,
  Library.circeGeneric,
  Library.jquery,
  Library.react,
  Library.reactDom,
  Library.webjarsPlay
)

serverLoading in Debian := com.typesafe.sbt.packager.archetypes.ServerLoader.SystemV

mdocValidateCommands += "debian:packageBin"
