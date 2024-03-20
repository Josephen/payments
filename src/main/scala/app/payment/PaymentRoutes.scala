package app.payment

import cats.effect.IO
import domain.models.Payment
import org.http4s.circe.{ jsonEncoderOf, jsonOf }
import org.http4s.{ EntityDecoder, EntityEncoder, HttpRoutes }
import org.http4s.dsl.io._
import service.PaymentService
import utils.ImplicitsConversions._

class PaymentRoutes(
  paymentService: PaymentService
) {

  implicit val paymentDecoder: EntityDecoder[IO, Payment] = jsonOf[IO, Payment]
  implicit val paymentEncoder: EntityEncoder[IO, Payment] = jsonEncoderOf[IO, Payment]

  implicit val paymentListEncoder: EntityEncoder[IO, List[Payment]] = jsonEncoderOf[IO, List[Payment]]

  def routes: HttpRoutes[IO] = {

    HttpRoutes.of[IO] {

      case GET ->  Root / "status" =>
      Ok(s"Server on")

      // Создание платежа
      case req @ POST -> Root / "payments" =>
        for {
          payment <- req.as[Payment]
          createdId <- paymentService.create(payment)
          resp <- Ok(s"Payment created with id: $createdId")
        } yield resp

      // Получение всех платежей
      case GET -> Root / "payments" =>
        for {
          payments <- paymentService.getAllPayments()
          resp <- Ok(payments)
        } yield resp

      // Получение платежа по ID
      case GET -> Root / "payment" / id =>
        for {
          payment <- paymentService.getPayment(id)
          resp <- payment.fold(NotFound(s"Payment with id $id not found"))(p => Ok(p))
        } yield resp

      // Обновление платежа
      case req @ PUT -> Root / "payment" / id =>
        for {
          payment <- req.as[Payment]
          _ <- paymentService.updatePayment(payment.copy(id = id))
          resp <- Ok(s"Payment with id $id updated successfully")
        } yield resp

      // Удаление платежа
      case DELETE -> Root / "payment" / id =>
        for {
          _ <- paymentService.deletePayment(id)
          resp <- Ok(s"Payment with id $id deleted successfully")
        } yield resp
    }
  }
}
