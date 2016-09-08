package service


import model.{UpdateContact, AddContact, Contact}

import scala.concurrent.{ExecutionContext, Future}

/**
 * Created by kops513 on 8/30/16.
 */
class ContactService(implicit val executionContext: ExecutionContext) {
  var contacts = Vector.empty[Contact]
  var id = 0

  /**
   *
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
        Future.successful(Some((id-1).toString))
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
     Future.successful(contacts.find(p => p.id == contactId))
    }catch{
      case e : Exception => Future.failed(e)
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

      val prevContact =contacts.find(p => p.id == contactId).get
       val updateContact = Contact(contactId,contact.name.getOrElse(prevContact.name), contact.age.getOrElse(prevContact.age))
      contacts = contacts.updated(contactId, updateContact)
      Future(s"contact with id = $id is updated")
    }catch{
      case e : Exception => Future.failed(e)
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
      case e : Exception => Future.failed(e)
    }

  }

}
