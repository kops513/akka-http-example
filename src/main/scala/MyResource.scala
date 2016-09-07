
import akka.http.scaladsl.marshalling.{ToResponseMarshaller, ToResponseMarshallable}

import scala.concurrent.{ExecutionContext, Future}
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.{Directives, Route}

import scala.util.{Failure, Success}


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
    onComplete(resource) {
      case Success(t) => t match {
        case Some(j) => complete(ToResponseMarshallable(t))
        case None => complete(404, "Object Not-Found")
                          }
      case Failure(ex) => {
        ex match {
          case e1: NumberFormatException => complete(400,"id must be integer")
          case e2: NoSuchElementException  => complete(404, "id not found")
          case e3: IndexOutOfBoundsException  => complete(404, "id not found")
          case e4: Throwable => complete(500,e4.toString)
        }
      }
    }


  def complete(resource: Future[String]): Route =
    onComplete(resource) {
      case Success(t) =>  complete(200, t)
      case Failure(ex) => {
        ex match {
          case e1: NumberFormatException => complete(400,"id must be integer")
          case e2: NoSuchElementException => complete(404, "id not found")
          case e3: IndexOutOfBoundsException  => complete(404, "id not found")
          case e4: Throwable => complete(500,e4.toString)
        }
      }
    }




}
