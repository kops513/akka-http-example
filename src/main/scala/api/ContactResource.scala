package api

import akka.http.scaladsl.server.Route
import core.MyResource
import model.{UpdateContact, AddContact, ContactJsonProtocols}
import service.ContactService


/**
 * Created by kops513 on 8/30/16.
 */
trait ContactResource extends MyResource with ContactJsonProtocols {
  val contactService: ContactService

  def contactRoutes: Route =
    rejectEmptyResponse {
      pathPrefix("contacts") {
        pathEnd {
          post {
            entity(as[AddContact]) { contact =>
              completeWithLocationHeader(
                resourceId = contactService.createContacts(contact),
                ifDefinedStatus = 201, ifEmptyStatus = 409)
            }
          } ~
          get {
            onSuccess(contactService.getContacts) { t =>  complete(200,t.toSeq)}

          }
        } ~
        path(Segment) { id =>
          get {
            complete(contactService.getContact(id))
          } ~
            put {
              entity(as[UpdateContact]) { contact =>
                complete(contactService.updateContact(id, contact))
              }
            } ~
            delete {
              complete(contactService.deleteContact(id))
            }
        }
      }
    }
   }



