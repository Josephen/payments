package app

import com.typesafe.config.ConfigFactory
import infrastructure.database.doobie.DatabaseConfig

case class ApplicationConfig(
  dbConfig: DatabaseConfig
)

object ApplicationConfig {
  def load(): ApplicationConfig = {
    val config = ConfigFactory.load("application.conf")
    val dbConfig = DatabaseConfig.fromConfig(config)
    ApplicationConfig(dbConfig)
  }
}