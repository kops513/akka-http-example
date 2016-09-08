package core

import akka.actor._
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

/**
 * Created by kops513 on 8/30/16.
 */
object Main extends App  with RestInterface{

  implicit val system = ActorSystem("main-contact-actor")
  implicit val materializer = ActorMaterializer()

  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val timeout = Timeout(10 seconds)
 // val parsedConfig = ConfigFactory.parseFile(new File("src/main/resources/application.conf"))

  protected val config = ConfigFactory.load()
  protected val logLevel = config.getString("akka.loglevel")

  protected val interface = config.getString("http.interface")
  protected val port = config.getInt("http.port")

  val api = routes

  Http().bindAndHandle(handler = api, interface = interface, port = port) map { binding =>
    println(s"REST interface bound to ${binding.localAddress}") } recover { case ex =>
    println(s"REST interface could not bind to $interface:$port", ex.getMessage)
  }

}
