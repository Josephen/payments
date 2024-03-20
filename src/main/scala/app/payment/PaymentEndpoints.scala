package app.endpoints

import domain.models.Payment
import sttp.tapir._
import sttp.tapir.json.circe._

object PaymentEndpoints {

  implicit val paymentSchema: Schema[Payment] = Schema.derived[Payment]

  val createPayment: Endpoint[Payment, Unit, Unit, Any] =
    endpoint.post
      .in("payments")
      .in(jsonBody[Payment])

  val getAllPayments: Endpoint[Unit, String, List[Payment], Any] =
    endpoint.get
      .in("payments")
      .errorOut(stringBody)
      .out(jsonBody[List[Payment]])

  val getPayment: Endpoint[String, Unit, Payment, Any] =
    endpoint.get
      .in("payment" / path[String]("id"))
      .out(jsonBody[Payment])

  val updatePayment: Endpoint[(String, Payment), Unit, Unit, Any] =
    endpoint.post
      .in("payment" / path[String]("id"))
      .in(jsonBody[Payment])

  val deletePayment: Endpoint[String, Unit, Unit, Any] =
    endpoint.post
      .in("payment" / path[String]("id"))
      .out(emptyOutput)


}
