name:="faproj"

version:="1.0"

scalaVersion:="2.10.2"

resolvers += "Twitter Maven repo" at "http://maven.twttr.com/"

com.twitter.scrooge.ScroogeSBT.newSettings

libraryDependencies ++= Seq(
	"com.twitter" %% "finagle-core" % "6.6.2",
	"com.twitter" %% "finagle-http" % "6.6.2",
	"com.twitter" %% "finagle-thrift" % "6.6.2",
	"com.twitter" %% "scrooge-core" % "3.9.0"
)

scalacOptions ++= Seq("-deprecation", "-unchecked")
