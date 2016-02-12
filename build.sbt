enablePlugins(MdocPlugin)

name := "adelmo"

libraryDependencies ++= Seq(
)

// sbt-native-packager
enablePlugins(JavaServerAppPackaging)
maintainer := "m-doc <info@m-doc.org>"
packageSummary := description.value
packageDescription := s"See <${homepage.value.getOrElse("")}> for more information."

// deb settings
enablePlugins(DebianPlugin)
serverLoading in Debian := com.typesafe.sbt.packager.archetypes.ServerLoader.SystemV

validateCommands += "debian:packageBin"
