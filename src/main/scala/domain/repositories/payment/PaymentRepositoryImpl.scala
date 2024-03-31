package domain.repositories.payment

import cats.effect.IO
import domain.models.{ Payment, PaymentRequest }
import domain.models.Payment._
import doobie.implicits._
import doobie.util.transactor.Transactor

import java.util.UUID

class PaymentRepositoryImpl(transactor: Transactor[IO]) extends PaymentRepository {

  override def create(payment: PaymentRequest): IO[Either[Throwable, UUID]] = {
    sql"""
         |INSERT INTO based.payment (user_id, amount, description, created_at)
         |VALUES (${payment.userId},${payment.amount}, ${payment.description}, CURRENT_TIMESTAMP)
       """
      .stripMargin
      .update
      .withUniqueGeneratedKeys[UUID]("id")
      .attemptSql
      .transact(transactor)
  }

  override def findById(id: UUID): IO[Either[Throwable, Option[Payment]]] = {
    sql"SELECT id, user_id, amount, description, created_at FROM based.payment WHERE id = ${id.toString}::uuid"
      .query[Payment]
      .option
      .attemptSql
      .transact(transactor)
  }

  def getAll: IO[Either[Throwable, List[Payment]]] =
    sql"SELECT id, user_id, amount, description, created_at FROM based.payment"
      .query[Payment]
      .to[List]
      .attemptSql
      .transact(transactor)

  override def update(payment: Payment): IO[Either[Throwable, Unit]] =
    sql"""
         |UPDATE based.payment
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
    sql"DELETE FROM based.payment WHERE id = $id".update
      .run
      .attemptSql
      .transact(transactor)
      .map {
        case Right(_) => Right(())
        case Left(ex) => Left(ex)
      }

}