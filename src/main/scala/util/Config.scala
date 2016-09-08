package util

import com.typesafe.config.ConfigFactory

/**
 * Created by kops513 on 8/30/16.
 */
trait Config {
  protected val config = ConfigFactory.load()
    protected val logLevel = config.getString("akka.loglevel")
  protected val interface = config.getString("http.interface")
  protected val port = config.getInt("http.port")
}
