name := "erg-urlwallet"

version := "0.1"

updateOptions := updateOptions.value.withLatestSnapshots(false)

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "org.scala-lang.modules"   %% "scala-collection-compat" % "2.1.3",
  "org.apache.httpcomponents" % "httpclient"              % "4.3.3",
  "javax.servlet"             % "servlet-api"             % "2.5" % "provided",
  "net.glxn"                  % "qrgen"                   % "1.4",
  "io.circe"                 %% "circe-parser"            % "0.13.0",
  "org.ergoplatform"         %% "ergo-appkit"             % "4.0.3",
  "io.github.scalahub"       %% "scaladb"                 % "1.0"
)

resolvers ++= Seq(
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
  "SonaType" at "https://oss.sonatype.org/content/groups/public",
  ("Typesafe maven releases" at "http://repo.typesafe.com/typesafe/maven-releases/").withAllowInsecureProtocol(true),
  "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

lazy val root = (project in file("."))
  .settings(
    updateOptions := updateOptions.value.withLatestSnapshots(false)
  )

enablePlugins(JettyPlugin)
