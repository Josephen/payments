package domain.repositories.user

import cats.effect.IO

import java.util.UUID

trait UserRepository {

  def create(username: String, password: String): IO[Either[Throwable, UUID]]

  def getPasswordHashByUsername(username: String): IO[Either[Throwable, Option[String]]]

  def updatePassword(userId: UUID, newPassword: String): IO[Either[Throwable, Unit]]

  def delete(id: UUID): IO[Either[Throwable, Unit]]

}

