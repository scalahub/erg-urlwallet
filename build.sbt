name := "erg-urlwallet"

version := "0.1"

lazy val appkit = RootProject(uri("git://github.com/scalahub/appkit-mod.git"))

libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-collection-compat" % "2.1.3",
      "org.apache.httpcomponents" % "httpclient" % "4.3.3",
      "javax.servlet" % "servlet-api" % "2.5" % "provided",
      "net.glxn" % "qrgen" % "1.4",
      "io.circe" %% "circe-parser" % "0.13.0",
      "net.snaq" % "dbpool" % "7.0.1",
      "com.h2database" % "h2" % "1.4.199"
)

resolvers ++= Seq("Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
      "SonaType" at "https://oss.sonatype.org/content/groups/public",
      "Typesafe maven releases" at "http://repo.typesafe.com/typesafe/maven-releases/",
      "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/")


lazy val root = (project in file("."))
    .dependsOn(appkit)
    .settings(
          updateOptions := updateOptions.value.withLatestSnapshots(false)
    )

enablePlugins(JettyPlugin)

