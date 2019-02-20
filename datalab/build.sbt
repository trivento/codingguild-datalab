name := "datalab"

version := "latest"

scalaVersion := "2.12.8"

val sparkVersion = "2.4.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-mllib" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  )

javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),

/*    sparkComponents := Seq(),

    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxPermSize=2048M", "-XX:+CMSClassUnloadingEnabled"),
    scalacOptions ++= Seq("-deprecation", "-unchecked"),
    parallelExecution in Test := false,
    fork := true,

    coverageHighlighting := true,
*/
/*
    libraryDependencies ++= Seq(
      "org.apache.spark" %% "spark-streaming" % sparkVersion % "provided",
      "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",

      "org.scalatest" %% "scalatest" % "3.0.5" % "test",
      "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
    ),
*/
    // uses compile classpath for the run task, including "provided" jar (cf http://stackoverflow.com/a/21803413/3827)
    //run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)).evaluated,

  //  pomIncludeRepository := { x => false },
    
 //  resolvers ++= Seq(
 //     "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/",
 //     "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
 //     "Second Typesafe repo" at "http://repo.typesafe.com/typesafe/maven-releases/",
  //    Resolver.sonatypeRepo("public")
 //   ),

  //  pomIncludeRepository := { x => false },

    // publish settings
 //   publishTo := {
  //    val nexus = "https://oss.sonatype.org/"
  //    if (isSnapshot.value)
   //     Some("snapshots" at nexus + "content/repositories/snapshots")
    //  else
   //     Some("releases"  at nexus + "service/local/staging/deploy/maven2")
   // }
  
