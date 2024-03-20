package domain.repositories

import cats.effect.IO
import domain.models.Payment
import doobie.implicits._
import doobie.util.transactor.Transactor
import domain.models.Payment._

import java.util.UUID

class PaymentRepositoryImpl(transactor: Transactor[IO]) extends PaymentRepository {

  override def create(payment: Payment): IO[Long] =
    sql"""
         |INSERT INTO based.payments (amount, description)
         |VALUES (${payment.amount}, ${payment.description})
       """.stripMargin.update
      .withUniqueGeneratedKeys[Long]("id")
      .transact(transactor)

  override def findById(id: UUID): IO[Option[Payment]] =
    sql"SELECT id, amount, description FROM based.payments WHERE id = ${id.toString}::uuid"
      .query[Payment]
      .option
      .transact(transactor)

  def getAll(): IO[List[Payment]] =
    sql"SELECT id, amount, description FROM based.payments"
      .query[Payment]
      .to[List]
      .transact(transactor)

  override def update(payment: Payment): IO[Unit] =
    sql"""
         |UPDATE based.payments
         |SET amount = ${payment.amount}, description = ${payment.description}
         |WHERE id = ${payment.id}
       """.stripMargin.update.run.transact(transactor).void

  override def delete(id: UUID): IO[Unit] =
    sql"DELETE FROM payments WHERE id = $id".update.run.transact(transactor).void
}