package domain.models

import doobie.{ Get, Meta, Read }
import io.circe.{ Decoder, Encoder }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import sttp.tapir.Schema
import doobie.implicits.javatimedrivernative._

import java.time.LocalDateTime
import java.util.UUID

case class Payment(
  id: UUID,
//  userId,
  amount: BigDecimal,
  description: String,
  createdAt: LocalDateTime
)

object Payment {
  implicit val paymentEncoder: Encoder[Payment] = deriveEncoder[Payment]
  implicit val paymentDecoder: Decoder[Payment] = deriveDecoder[Payment]

  implicit val paymentSchema: Schema[Payment] = Schema.derived

  implicit val uuidMeta: Meta[UUID] =
    Meta[String].timap(UUID.fromString)(_.toString)

  implicit val paymentRead: Read[Payment] =
    Read[(UUID, BigDecimal, String, LocalDateTime)].map {
      case (id, amount, description, createdAt) => Payment(id, amount, description, createdAt)
    }

  implicit val bigDecimalGet: Get[BigDecimal] =
    Get[BigDecimal]

  implicit val stringGet: Get[String] =
    Get[String]

}