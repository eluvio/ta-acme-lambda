enablePlugins(JavaAppPackaging)

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "ta.amce.lambda.acme",
      scalaVersion := "2.12.7",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "AWS ACME Lambda",
    assemblyJarName in assembly := "acme.jar",
    libraryDependencies ++= Seq(
	    "com.amazonaws" % "aws-lambda-java-core" % "1.2.0",
      "org.shredzone.acme4j" % "acme4j-utils" % "2.5",
      "org.shredzone.acme4j" % "acme4j-client" % "2.5",
      "com.frugalmechanic" %% "fm-serializer" % "0.14.0",
      "com.frugalmechanic" %% "fm-common" % "0.29.0"
	  ),
    assemblyMergeStrategy in assembly := {
      case PathList("META-INF", xs @ _*) => MergeStrategy.discard
      case x => MergeStrategy.first
    }
  )