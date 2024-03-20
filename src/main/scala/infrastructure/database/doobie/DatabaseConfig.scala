package infrastructure.database.doobie

case class DatabaseConfig(
  driver: String,
  url: String,
  username: String,
  password: String,
  schema: String
)

object DatabaseConfig {
  def fromConfig(config: com.typesafe.config.Config): DatabaseConfig = {
    val dbConfig = config.getConfig("db")
    DatabaseConfig(
      driver = dbConfig.getString("driver"),
      url = dbConfig.getString("url"),
      username = dbConfig.getString("username"),
      password = dbConfig.getString("password"),
      schema = dbConfig.getString("schema")
    )
  }
}