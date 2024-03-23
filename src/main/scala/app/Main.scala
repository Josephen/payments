package app

import cats.effect.{ ExitCode, IO, IOApp }
import domain.repositories.PaymentRepositoryImpl
import endpoints.payment.PaymentRoutes
import infrastructure.database.doobie.DoobieTransactor
import org.flywaydb.core.Flyway
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.slf4j.LoggerFactory
import service.PaymentService

object Main extends IOApp {

    private val logger = LoggerFactory.getLogger(getClass)

    def run(args: List[String]): IO[ExitCode] = {
        // Загрузка конфигурации
        val config = ApplicationConfig.load()
        val dbConfig = config.dbConfig

        //Запуск миграций flyway
        val flyway = Flyway.configure.dataSource(
            dbConfig.url,
            dbConfig.username,
            dbConfig.password
        )
          .locations("filesystem:src/main/resources/migration")
          .schemas(dbConfig.schema)
          .load()
        flyway.migrate()

        // Создание транзактора Doobie
        val transactor = DoobieTransactor.create(dbConfig)

        // Создание репозитория для работы с базой данных
        val paymentRepository = new PaymentRepositoryImpl(transactor)

        // Создание сервиса для работы с платежами
        val paymentService = PaymentService(paymentRepository)

        // Создание эндпоинтов для обработки HTTP запросов
        val paymentEndpoints = new PaymentRoutes(paymentService)

        // Создание роутера HTTP4s
        val httpApp = Router(
            "/api" -> paymentEndpoints.routes
        ).orNotFound

        // Запуск HTTP сервера
        val server = BlazeServerBuilder[IO]
          .bindHttp(8080, "0.0.0.0")
          .withHttpApp(httpApp)
          .resource
          .use(_ => IO.never)
          .as(ExitCode.Success)

        logger.info("Server started")
        server
    }

}