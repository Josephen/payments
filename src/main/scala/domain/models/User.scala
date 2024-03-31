package domain.models

import io.circe.{ Decoder, Encoder }
import io.circe.generic.semiauto.{ deriveDecoder, deriveEncoder }
import sttp.tapir.Schema

import java.util.UUID
import java.time.LocalDateTime

case class User(
  id: UUID,
  username: String,
  password: String,
  createdAt: LocalDateTime
)

object User {
  implicit val userEncoder: Encoder[User] = deriveEncoder[User]
  implicit val userDecoder: Decoder[User] = deriveDecoder[User]

  implicit val userSchema: Schema[User] = Schema.derived
}