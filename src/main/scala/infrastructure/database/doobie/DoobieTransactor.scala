package infrastructure.database.doobie

import cats.effect.IO
import doobie.util.transactor.Transactor

object DoobieTransactor {

  def create(
    dbConfig: DatabaseConfig
  ): Transactor[IO] =
    Transactor.fromDriverManager[IO](
      driver = dbConfig.driver,
      url = dbConfig.url,
      user = dbConfig.username,
      pass = dbConfig.password
  )

}