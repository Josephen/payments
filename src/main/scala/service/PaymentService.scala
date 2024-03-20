package service

import cats.effect.IO
import domain.models.Payment
import domain.repositories.PaymentRepository
import utils.ImplicitsConversions._

class PaymentService(paymentRepo: PaymentRepository) {

  def create(payment: Payment): IO[Long] =
    paymentRepo.create(payment)

  def getAllPayments(): IO[List[Payment]] =
    paymentRepo.getAll()

  def getPayment(id: String): IO[Option[Payment]] = {

    paymentRepo.findById(id)
  }

  def updatePayment(payment: Payment): IO[Unit] =
    paymentRepo.update(payment)

  def deletePayment(id: String): IO[Unit] =
    paymentRepo.delete(id)
}

object PaymentService {
  def apply(paymentRepo: PaymentRepository): PaymentService =
    new PaymentService(paymentRepo)
}

