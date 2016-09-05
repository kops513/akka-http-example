


import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext

/**
 * Created by kops513 on 8/31/16.
 */
trait RestInterface extends Resources{

  implicit val executionContext: ExecutionContext

  lazy val contactService = new ContactService

  val routes: Route = contactRoutes

}

trait Resources extends ContactResource
