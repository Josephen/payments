package service

import cats.effect.IO
import domain.models.Payment
import domain.repositories.PaymentRepository
import utils.ImplicitsConversions._
import utils.PaymentError

import java.sql.SQLException
import java.util.UUID

class PaymentService(paymentRepo: PaymentRepository) {

  def create(payment: Payment): IO[Either[PaymentError, UUID]] = {
    if (payment.amount < 0) {
      IO.pure(Left(PaymentError.amountError))
    } else {
      paymentRepo.create(payment).map {
        case Right(id) => Right(id)
        case Left(ex: SQLException) =>Left(PaymentError.sqlExceptionHandler(ex))
        case Left(ex) => Left(PaymentError.UnknownError(ex.getMessage))
      }
    }
  }

  def getAllPayments: IO[Either[PaymentError, List[Payment]]] = {
    paymentRepo.getAll.map {
      case Right(id) => Right(id)
      case Left(ex: SQLException) => Left(PaymentError.sqlExceptionHandler(ex))
      case Left(_) => Left(PaymentError.getPaymentListError)
    }
  }
  def getPayment(id: String): IO[Either[PaymentError, Option[Payment]]] = {
    paymentRepo.findById(id).map {
      case Right(id) => Right(id)
      case Left(ex: SQLException) => Left(PaymentError.sqlExceptionHandler(ex))
      case Left(ex) => Left(PaymentError.UnknownError(ex.getMessage))
    }
  }

  def updatePayment(payment: Payment): IO[Either[PaymentError, Unit]] = {
    if (payment.amount < 0) {
      IO.pure(Left(PaymentError.amountError))
    } else {
      paymentRepo.update(payment).map {
        case Right(id) => Right(id)
        case Left(ex: SQLException) => Left(PaymentError.sqlExceptionHandler(ex))
        case Left(ex) => Left(PaymentError.UnknownError(ex.getMessage))
      }
    }
  }

  def deletePayment(id: String): IO[Either[PaymentError, Unit]] = {
    paymentRepo.delete(id).map {
      case Right(id) => Right(id)
      case Left(ex: SQLException) => Left(PaymentError.sqlExceptionHandler(ex))
      case Left(ex) => Left(PaymentError.UnknownError(ex.getMessage))
    }
  }
}

object PaymentService {
  def apply(paymentRepo: PaymentRepository): PaymentService =
    new PaymentService(paymentRepo)
}

