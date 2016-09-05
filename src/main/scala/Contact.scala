import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

/**
 * Created by kops513 on 8/30/16.
 */
case class Contact(id: Int, name: String, age: Int){
  require(!name.isEmpty, "name must not be empty")
}

case class AddContact(id: Option[Int] = None, name: String, age: Option[Int] = None)

case class UpdateContact(id: Option[Int] = None, name: Option[String] = None, age: Option[Int] = None)

case class Message(message: String)

trait ContactJsonProtocols  extends DefaultJsonProtocol with SprayJsonSupport{
  implicit val contactFormat = jsonFormat3(Contact)
  implicit val addContactFormat = jsonFormat3(AddContact)
  implicit val updateContactFormat = jsonFormat3(UpdateContact)

}