import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "iAttendance"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "net.sourceforge.jtds" % "jtds" % "1.2",
            "mysql" % "mysql-connector-java" % "5.1.21",
            "commons-io" % "commons-io" % "2.4",
            "org.apache.httpcomponents" % "httpclient" % "4.0-beta1",
            "org.apache.httpcomponents" % "httpclient" % "4.5.5"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
