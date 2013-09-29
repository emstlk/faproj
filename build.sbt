name:="faproj"

version:="1.0"

scalaVersion:="2.10.2"

resolvers += "Twitter Maven repo" at "http://maven.twttr.com/"
//resolvers += Resolver.url("scalasbt", new URL("http://scalasbt.artifactoryonline.com/scalasbt/sbt-plugin-releases"))(Resolver.ivyStylePatterns)


libraryDependencies ++= Seq(
	"com.twitter" %% "finagle-core" % "6.6.2",
	"com.twitter" %% "finagle-http" % "6.6.2"
)

scalacOptions += "-unchecked"

