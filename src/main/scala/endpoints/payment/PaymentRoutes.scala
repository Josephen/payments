package endpoints.payment

import cats.effect.IO
import domain.models.{ Payment, PaymentRequest }
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.{ jsonEncoderOf, jsonOf }
import org.http4s.dsl.io._
import org.http4s.headers.`Content-Type`
import org.http4s.{ EntityDecoder, EntityEncoder, HttpRoutes, MediaType }
import service.PaymentService

import java.util.UUID

class PaymentRoutes(
  paymentService: PaymentService
) {

  implicit val paymentDecoder: EntityDecoder[IO, Payment] = jsonOf[IO, Payment]
//  implicit val paymentEncoder: EntityEncoder[IO, Payment] = jsonEncoderOf[IO, Payment]

  implicit val paymentCreateRequestDecoder: EntityDecoder[IO, PaymentRequest] = jsonOf[IO, PaymentRequest]

  implicit val paymentListEncoder: EntityEncoder[IO, List[Payment]] = jsonEncoderOf[IO, List[Payment]]

  def routes: HttpRoutes[IO] = {

    HttpRoutes.of[IO] {

      case GET -> Root / "status" =>
        Ok("Server on")

      // Создание платежа
      case req @ POST -> Root / "payments" =>
        for {
          payment <- req.as[PaymentRequest]
          createdId <- paymentService.create(payment)
          resp <- createdId.fold(
            error => BadRequest(error.message),
            id => Ok(s"Payment created with id: $id")
          )
        } yield resp

      // Получение всех платежей
      case GET -> Root / "payments" =>
        paymentService.getAllPayments.flatMap {
          case Right(payments) => Ok(payments)
          case Left(error) => InternalServerError(error.message)
        }

      // Получение платежа по ID
      case GET -> Root / "payment" / id =>
        paymentService.getPayment(id).flatMap {
          case Right(payment) => Ok(payment)
          case Left(error) => InternalServerError(error.message)
        }

      // Обновление платежа
      case req @ PUT -> Root / "payment" / id =>
        for {
          payment <- req.as[Payment]
          _ <- paymentService.updatePayment(payment.copy(id = UUID.fromString(id)))
          resp <- Ok(s"Payment with id $id updated successfully")
        } yield resp

      // Удаление платежа
      case DELETE -> Root / "payment" / id =>
        for {
          _ <- paymentService.deletePayment(id)
          resp <- Ok(s"Payment with id $id deleted successfully")
        } yield resp

      //api spec
      case GET -> Root / "spec" =>
          Ok(PaymentEndpoints.spec)
          .map(_.withContentType(`Content-Type`(MediaType.application.json)))

    }
  }
}
