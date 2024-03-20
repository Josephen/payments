package domain.repositories

import cats.effect.IO
import domain.models.Payment

import java.util.UUID

trait PaymentRepository {

  def create(payment: Payment): IO[Long]

  def getAll(): IO[List[Payment]]

  def findById(id: UUID): IO[Option[Payment]]

  def update(payment: Payment): IO[Unit]

  def delete(id: UUID): IO[Unit]

}

