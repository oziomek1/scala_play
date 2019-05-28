name := "scala_play"
 
version := "1.0" 
      
lazy val `scala_play` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

resolvers += "Atlassian Releases" at "https://maven.atlassian.com/public/"

scalaVersion := "2.12.2"
libraryDependencies ++= Seq( ehcache , ws , specs2 % Test , guice, filters )

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.2.0"
libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.2.3"

libraryDependencies += evolutions

libraryDependencies += "com.typesafe.play" %% "play-slick" % "3.0.0"
libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "3.0.0"
libraryDependencies += "org.xerial"        %  "sqlite-jdbc" % "3.23.1"

val silhouetteVersion = "5.0.3"

libraryDependencies += "com.mohiva" %% "play-silhouette" % silhouetteVersion
libraryDependencies += "com.mohiva" %% "play-silhouette-persistence" % silhouetteVersion
libraryDependencies += "com.mohiva" %% "play-silhouette-password-bcrypt" % silhouetteVersion
libraryDependencies += "com.mohiva" %% "play-silhouette-crypto-jca" % silhouetteVersion
libraryDependencies += "com.mohiva" %% "play-silhouette-testkit" % silhouetteVersion % "test"

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )

