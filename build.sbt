name := "univer_signal_resolver"

version := "1.0"

scalaVersion := "2.12.4"
lazy val akkaVersion = "2.5.8"

libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-math3" % "3.0",
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "org.apache.logging.log4j" % "log4j-core" % "2.10.0",
  "org.apache.logging.log4j" % "log4j-api-scala_2.12" % "11.0",
  "com.storm-enroute" %% "scalameter-core" % "0.8.2"
)

mainClass := Some("actor.Application")

        