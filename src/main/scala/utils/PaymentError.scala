package utils

sealed trait PaymentError extends Throwable {
  val message: String
}

object PaymentError {
  case class AmountError(message: String) extends PaymentError
  case class GetPaymentListError(message: String) extends PaymentError
  case class DeletionError(message: String) extends PaymentError

  val amountError: AmountError = AmountError("Payment amount must be positive")
  val getPaymentListError: GetPaymentListError = GetPaymentListError("Can't get list of payments")
  val deletionError: DeletionError = DeletionError("Payment ID must not be null")
}