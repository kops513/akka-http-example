
import akka.http.scaladsl.marshalling.{ToResponseMarshaller, ToResponseMarshallable}

import scala.concurrent.{ExecutionContext, Future}
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.{Directives, Route}



trait MyResource extends Directives with ContactJsonProtocols{

  implicit def executionContext: ExecutionContext

  def completeWithLocationHeader[T](resourceId: Future[Option[T]], ifDefinedStatus: Int, ifEmptyStatus: Int): Route =
    onSuccess(resourceId) {
      case Some(t) => completeWithLocationHeader(ifDefinedStatus, t)
      case None => complete(ifEmptyStatus, "")
    }

  def completeWithLocationHeader[T](status: Int, resourceId: T): Route =
    extractRequestContext { requestContext =>
      val request = requestContext.request
      val location = request.uri.copy(path = request.uri.path / resourceId.toString)
      respondWithHeader(Location(location)) {
        complete(status, s"created object with id $resourceId")
      }
    }

  def complete[T: ToResponseMarshaller](resource: Future[Option[T]]): Route =
    onSuccess(resource) {
      case Some(t) => complete(ToResponseMarshallable(t))
    }

  //  def complete[T: ToResponseMarshaller](resource: Future[List[T]]): Route =
  //    onSuccess(resource) { t =>  complete(t.)}

  def complete(resource: Future[String]): Route =
    onSuccess(resource) { t => complete(200, t)
    }



}
