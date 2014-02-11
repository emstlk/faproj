name := "faproj"

version := "1.0"

scalaVersion := "2.10.2"

resolvers += "Twitter Maven repo" at "http://maven.twttr.com/"

com.github.retronym.SbtOneJar.oneJarSettings

com.twitter.scrooge.ScroogeSBT.newSettings

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-core" % "6.6.2",
  "com.twitter" %% "finagle-http" % "6.6.2",
  "com.twitter" %% "finagle-thrift" % "6.6.2",
  "com.twitter" %% "scrooge-core" % "3.9.0",
  "com.restfb" % "restfb" % "1.6.12",
  "org.postgresql" % "postgresql" % "9.2-1002-jdbc4",
  "com.mchange" % "c3p0" % "0.9.2.1",
  "org.squeryl" %% "squeryl" % "0.9.6-RC2"
)

scalacOptions ++= Seq("-deprecation", "-unchecked")
