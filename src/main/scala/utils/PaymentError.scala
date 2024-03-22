package utils

import java.sql.SQLException

sealed trait PaymentError extends Throwable {
  val message: String
}

object PaymentError {
  case class AmountError(message: String) extends PaymentError
  case class GetPaymentListError(message: String) extends PaymentError
  case class UnknownError(message: String) extends PaymentError

  private case class DuplicateEntryError(message: String) extends PaymentError

  private case class UnknownSQLError(message: String) extends PaymentError

  val amountError: AmountError = AmountError("Payment amount must be positive")
  val getPaymentListError: GetPaymentListError = GetPaymentListError("Can't get list of payments")

  def sqlExceptionHandler(ex: SQLException): PaymentError = {
    val errorCode = ex.getErrorCode
    val errorMsg = ex.getMessage
    val paymentError = errorCode match {
      case 23505 => PaymentError.DuplicateEntryError(errorMsg)
      case _ => PaymentError.UnknownSQLError(errorMsg) // Обработка других SQL ошибок
    }
   paymentError
  }

}