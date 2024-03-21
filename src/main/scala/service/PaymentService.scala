package service

import cats.effect.IO
import domain.models.Payment
import domain.repositories.PaymentRepository
import org.slf4j.LoggerFactory
import utils.ImplicitsConversions._
import utils.PaymentError

class PaymentService(paymentRepo: PaymentRepository) {
  private val logger = LoggerFactory.getLogger(getClass)

  def create(payment: Payment): IO[Either[PaymentError, Long]] =
    paymentRepo.create(payment).map {
      case Right(id) => Right(id)
      case Left(_) => Left(PaymentError.amountError)
    }

  def getAllPayments: IO[Either[PaymentError, List[Payment]]] =
    paymentRepo.getAll().map {
      case Right(id) => Right(id)
      case Left(_) => Left(PaymentError.amountError)
    }

  def getPayment(id: String): IO[Either[PaymentError, Option[Payment]]] = {
    paymentRepo.findById(id).map {
      case Right(id) => Right(id)
      case Left(_) => Left(PaymentError.getPaymentListError)
    }
  }

  def updatePayment(payment: Payment): IO[Either[PaymentError,Unit]] =
    paymentRepo.update(payment).map {
      case Right(id) => Right(id)
      case Left(_) => Left(PaymentError.amountError)
    }

  def deletePayment(id: String): IO[Either[PaymentError, Unit]] =
    paymentRepo.delete(id).map {
      case Right(id) => Right(id)
      case Left(_) => Left(PaymentError.deletionError)
    }
}

object PaymentService {
  def apply(paymentRepo: PaymentRepository): PaymentService =
    new PaymentService(paymentRepo)
}

