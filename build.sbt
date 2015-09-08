name := "coolmarks"

version := "1.0"

scalaVersion := "2.11.3"

unmanagedBase := baseDirectory.value / "lib"

ivyScala := ivyScala.value map {
  _.copy(overrideScalaVersion = true)
}

libraryDependencies ++= {
  Seq(
    "com.secer.elastic" % "elasticservice_2.11" % "1.0",
    "com.github.chengpohi" % "siny_2.11" % "1.1"
    //"org.siny.web" % "siny_2.11" % "1.0"
  )
}