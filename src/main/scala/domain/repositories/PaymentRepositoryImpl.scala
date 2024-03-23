package domain.repositories

import cats.effect.IO
import domain.models.Payment
import domain.models.Payment._
import doobie.implicits._
import doobie.util.transactor.Transactor

import java.util.UUID

class PaymentRepositoryImpl(transactor: Transactor[IO]) extends PaymentRepository {

  override def create(payment: Payment): IO[Either[Throwable, UUID]] = {
    sql"""
         |INSERT INTO based.payments (amount, description)
         |VALUES (${payment.amount}, ${payment.description})
       """
      .stripMargin
      .update
      .withUniqueGeneratedKeys[UUID]("id")
      .attemptSql
      .transact(transactor)
  }

  override def findById(id: UUID): IO[Either[Throwable, Option[Payment]]] = {
    sql"SELECT id, amount, description FROM based.payments WHERE id = ${id.toString}::uuid"
      .query[Payment]
      .option
      .attemptSql
      .transact(transactor)
  }

  def getAll: IO[Either[Throwable, List[Payment]]] =
    sql"SELECT id, amount, description FROM based.payments"
      .query[Payment]
      .to[List]
      .attemptSql
      .transact(transactor)

  override def update(payment: Payment): IO[Either[Throwable, Unit]] =
    sql"""
         |UPDATE based.payments
         |SET amount = ${payment.amount}, description = ${payment.description}
         |WHERE id = ${payment.id}
       """
      .stripMargin
      .update
      .run
      .attemptSql
      .transact(transactor)
      .map {
        case Right(_) => Right(())
        case Left(ex) => Left(ex)
      }

  override def delete(id: UUID): IO[Either[Throwable, Unit]] =
    sql"DELETE FROM payments WHERE id = $id".update
      .run
      .attemptSql
      .transact(transactor)
      .map {
        case Right(_) => Right(())
        case Left(ex) => Left(ex)
      }

}