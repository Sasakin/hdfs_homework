name := "hw1"

version := "0.1"

scalaVersion := "2.13.7"

val common = Seq(version:= "0.1", scalaVersion:= "2.13.7")

libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "3.2.1"

compile / mainClass := Some("Application")
assembly / mainClass := Some("Application")

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}


lazy val root = (project in file("."))
  .settings(common)
  .settings(
    publishArtifact := true
  )


assembly / assemblyJarName := "hw1.jar"

