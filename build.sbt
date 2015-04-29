name := "finagle-example"

version := "0.1"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-http" % "6.25.0",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  )

