package domain.repositories.user
import cats.effect.IO
import domain.models.User
import doobie.util.transactor.Transactor
import doobie.implicits._

import java.util.UUID

class UserRepositoryImpl(transactor: Transactor[IO]) extends UserRepository {

  override def create(username: String, password: String): IO[Either[Throwable, UUID]] = {
    sql"""
         |INSERT INTO based.user (username, password, created_at)
         |VALUES ($username, $password, CURRENT_TIMESTAMP)
       """
      .stripMargin
      .update
      .withUniqueGeneratedKeys[UUID]("id")
      .attemptSql
      .transact(transactor)
  }

  def getPasswordHashByUsername(username: String): IO[Either[Throwable, Option[String]]] = {
    sql"""
         |SELECT id, username, password, created_at FROM based.user
         |WHERE username = $username
       """
      .stripMargin
      .query[String]
      .option
      .attemptSql
      .transact(transactor)
  }

  override def updatePassword(userId: UUID, newPassword: String): IO[Either[Throwable, Unit]] = {
    sql"""
         |UPDATE based.user
         |SET password = $newPassword
         |WHERE id = $userId
     """
      .stripMargin
      .update
      .run
      .attemptSql
      .transact(transactor)
      .map(_.map(_ => ())) // Преобразуем результат к типу Either[Throwable, Unit]
  }
  override def delete(id: UUID): IO[Either[Throwable, Unit]] = {
    sql"DELETE FROM based.user WHERE id = $id".update
      .run
      .attemptSql
      .transact(transactor)
      .map {
        case Right(_) => Right(())
        case Left(ex) => Left(ex)
      }
  }

}
