package api

import akka.http.scaladsl.testkit.ScalatestRouteTest
import core.Resources
import org.scalatest._
/**
 * Created by kops513 on 9/19/16.
 */
trait BaseSpec extends WordSpec with Matchers with ScalatestRouteTest with Resources {

}
