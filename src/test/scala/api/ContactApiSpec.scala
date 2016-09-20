package api

import akka.http.scaladsl.model._
import model.Contact
import spray.json._

/**
 * Created by kops513 on 9/8/16.
 */
class ContactApiSpec extends BaseSpec{

  "Contact Api" should {

    "create new contact" in {
      val requestEntity = HttpEntity(MediaTypes.`application/json`, JsObject("name" -> JsString("kapila"),
        "age" -> JsNumber(27)).toString)

      Post("/contacts", requestEntity) ~> contactRoutes ~> check {
        responseAs[String] shouldEqual "created object with id 0"
        response.status should be(StatusCode.int2StatusCode(201))
        Get("/contacts") ~> contactRoutes ~> check {
          responseAs[Seq[Contact]] should have length 1
        }
      }
    }

    "get contact By Id" in {
      Get("/contacts/0") ~> contactRoutes ~> check{
        response.status should be(StatusCode.int2StatusCode(200))
        val t = responseAs[Contact]
        t.name shouldEqual "kapila"
      }
    }

    val updateEntity = HttpEntity(MediaTypes.`application/json`, JsObject("name" -> JsString("kops")).toString)

    "update contact" in {
      Put("/contacts/0",updateEntity) ~> contactRoutes ~> check{
        response.status should be(StatusCode.int2StatusCode(200))
        Get("/contacts/0") ~> contactRoutes ~> check {
          val updatedContact = responseAs[Contact]
          updatedContact.name shouldEqual "kops"
        }
      }
    }

    "delete contact" in {
      Delete("/contacts/0") ~> contactRoutes ~> check{
        response.status should be(StatusCode.int2StatusCode(200))
        Get("/contacts/0") ~> contactRoutes ~> check {
          response.status should be(StatusCode.int2StatusCode(404))
        }
      }
    }

  }
}
