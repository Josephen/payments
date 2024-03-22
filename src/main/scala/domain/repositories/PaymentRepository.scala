package domain.repositories

import cats.effect.IO
import domain.models.Payment

import java.util.UUID

trait PaymentRepository {

  def create(payment: Payment): IO[Either[Throwable, UUID]]

  def getAll: IO[Either[Throwable, List[Payment]]]

  def findById(id: UUID): IO[Either[Throwable, Option[Payment]]]

  def update(payment: Payment): IO[Either[Throwable, Unit]]

  def delete(id: UUID): IO[Either[Throwable, Unit]]

}