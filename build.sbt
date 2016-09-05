//import com.typesafe.sbt.packager.archetypes.JavaServerAppPackaging
//enablePlugins(JavaServerAppPackaging)

// factor out common settings into a sequence
lazy val commonSettings = Seq(

  organization := "com.kapila",
  version := "0.1",
  // set the Scala version used for the project
  scalaVersion := "2.11.7"

)

lazy val root = (project in file(".")).
     settings(commonSettings: _*).
     settings(
       name :=  "akka-http-example",  
       scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8"),

libraryDependencies ++= {
  val akkaV = "2.4.9"
  Seq( 
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV
    )
})

mainClass in Global := Some("Main")



