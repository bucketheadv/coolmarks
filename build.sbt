name := "coolmarks"


lazy val commonSettings = Seq(
  organization := "com.github.chengpohi",
  version := "1.0",
  scalaVersion := "2.11.7"
)

unmanagedBase := baseDirectory.value / "lib"

ivyScala := ivyScala.value map {
  _.copy(overrideScalaVersion = true)
}

val commonDependencies = Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test",
  "org.slf4j" % "slf4j-simple" % "1.7.7",
  "org.json4s" %% "json4s-native" % "3.2.10",
  "org.json4s" %% "json4s-jackson" % "3.2.10",
  "com.typesafe" % "config" % "1.2.1",
  "com.typesafe.akka" %% "akka-actor" % "2.3.8",
  "com.typesafe.akka" %% "akka-testkit" % "2.3.8"
)

val elasticServiceDependencies = Seq(
  "com.github.chengpohi" % "elasticshell_2.11" % "0.1"
)

lazy val elasticservice = project.in(file("elasticservice"))
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= elasticServiceDependencies ++ commonDependencies)
  .settings(
    assemblyMergeStrategy in assembly := {
      case "application.conf" => MergeStrategy.discard
      case PathList("org", "joda", "time", "base", "BaseDateTime.class") => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )

lazy val siny = project.in(file("siny"))
  .settings(commonSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .settings(
    assemblyMergeStrategy in assembly := {
      case "application.conf" => MergeStrategy.discard
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )
  .aggregate(elasticservice)
  .dependsOn(elasticservice)

lazy val coolmarks = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    assemblyJarName in assembly := "coolmarks.jar",
    assemblyMergeStrategy in assembly := {
      case "application.conf" => MergeStrategy.discard
      case PathList("org", "joda", "time", "base", "BaseDateTime.class") => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }
  )
  .aggregate(siny)
  .dependsOn(siny)
