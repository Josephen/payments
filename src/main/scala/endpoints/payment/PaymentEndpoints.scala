package endpoints.payment

import domain.models.Payment
import io.circe.Json
import io.circe.yaml.parser
import sttp.tapir._
import sttp.tapir.docs.openapi._
import sttp.tapir.json.circe._
import sttp.tapir.openapi.circe.yaml.RichOpenAPI

object PaymentEndpoints {

  implicit val paymentSchema: Schema[Payment] = Schema.derived[Payment]

  private val createPayment: Endpoint[Payment, Unit, Unit, Any] =
    endpoint.post
      .in("payments")
      .in(jsonBody[Payment])

  private val getAllPayments: Endpoint[Unit, String, List[Payment], Any] =
    endpoint.get
      .in("payments")
      .errorOut(stringBody)
      .out(jsonBody[List[Payment]])

  private val getPayment: Endpoint[String, Unit, Payment, Any] =
    endpoint.get
      .in("payment" / path[String]("id"))
      .out(jsonBody[Payment])

  private val updatePayment: Endpoint[(String, Payment), Unit, Unit, Any] =
    endpoint.post
      .in("payment" / path[String]("id"))
      .in(jsonBody[Payment])

  private val deletePayment: Endpoint[String, Unit, Unit, Any] =
    endpoint.post
      .in("payment" / path[String]("id"))
      .out(emptyOutput)

  private val endpoints = List(createPayment,
    getAllPayments,
    getPayment,
    updatePayment,
    deletePayment
  )

  private val openAPIYaml: String = OpenAPIDocsInterpreter().toOpenAPI(
    endpoints,
    "Payment API",
    "1.0"
  ).toYaml

  val spec: Json = parser.parse(openAPIYaml) match {
    case Right(json) => json
    case Left(error) => Json.obj("error" -> Json.fromString(s"Error formatting YAML: $error"))
  }

}