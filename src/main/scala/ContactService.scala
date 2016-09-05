import scala.concurrent.{Future, ExecutionContext}

/**
 * Created by kops513 on 8/30/16.
 */
class ContactService(implicit val executionContext: ExecutionContext) {
  var contacts = Vector.empty[Contact]
  var id = 0

  /**
   * Contact is created here. If contact to be created has id then id is checked if its already exist or not.
   * if it exist error message is thrown otherwise contact will be created.
   * @param contact
   * @return
   */
  def createContacts(contact: AddContact): Future[Option[String]] = {
    contacts.find(_.id == contact.id.getOrElse(-1)) match {
      case Some(q) => Future(None) // Conflict! id is already taken
      case None =>
        val tempContact = Contact(id, contact.name, contact.age.getOrElse(0))
        id = id + 1
        contacts = contacts :+ tempContact
        Future.successful(Some(id.toString))
    }
  }

  /**
   *
   * @return
   */
  def getContacts: Future[List[Contact]] = {
    Future.successful(contacts.toList)
  }

  /**
   *
   * @param id
   * @return
   */
  def getContact(id: String): Future[Option[Contact]] = {
    try {
      val contactId = Integer.parseInt(id)
     Future.successful(Some(contacts(contactId)))
    }catch{
      case e: NumberFormatException => Future.failed(new NumberFormatException("id must be integer"))
      case  _ => Future.failed(new Exception("exception"))
    }
  }

  /**
   *
   * @param id
   * @param contact
   * @return
   */
  def updateContact(id: String, contact: UpdateContact): Future[String] = {
    try{
      val contactId = Integer.parseInt(id)

      val prevContact = contacts(contactId)
       val updateContact = Contact(contactId,contact.name.getOrElse(prevContact.name), contact.age.getOrElse(prevContact.age))
      contacts = contacts.updated(contactId, updateContact)
      Future(s"contact with id = $id is updated")
    }catch{
      case e: NumberFormatException => Future.successful("contactId must be integer")
      case _ => Future.successful(s"contact with id = $id does not exist")
    }

  }

  /**
   *
   * @param id
   * @return
   */
  def deleteContact(id: String): Future[String] ={
    try{
      val contactId = Integer.parseInt(id)
      contacts = contacts.filterNot(_.id == contactId)
      Future(s"contact with id = $id is deleted")
    }catch{
      case e: NumberFormatException => Future.successful("id must be integer")
      case _ => Future.successful("contact does not exist")
    }

  }

}
