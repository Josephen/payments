package utils

import java.util.UUID
import scala.util.{ Failure, Success, Try }

object ImplicitsConversions {

  //todo добавить нормальную обработку ошибки
  implicit def stringToUUID(str: String): UUID = {
    Try(UUID.fromString(str)) match {
      case Success(uuid) => uuid
      case Failure(ex) =>
        println(s"Failed to parse UUID from string: $str. Error: ${ex.getMessage}")
        UUID.randomUUID()
    }

  }
}
