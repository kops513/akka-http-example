package core

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
/**
 * Created by kops513 on 9/7/16.
 */
trait ApiErrorHandling {
  implicit def myExceptionHandler: ExceptionHandler = ExceptionHandler {
    case e: NoSuchElementException =>  complete(NotFound, s"Invalid id: ${e.getMessage}")
    case e1: NumberFormatException => complete(400,"id must be integer")
    case e3: IndexOutOfBoundsException  => complete(404, "id not found")
  }
}
