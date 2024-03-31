package domain.models

import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import io.circe.{ Decoder, Encoder }

import java.util.UUID

case class PaymentRequest(
  userId: UUID,
  amount: BigDecimal,
  description: String,
)

object PaymentRequest {

  implicit val paymentRequestEncoder: Encoder[PaymentRequest] = deriveEncoder[PaymentRequest]
  implicit val paymentRequestDecoder: Decoder[PaymentRequest] = deriveDecoder[PaymentRequest]

}
