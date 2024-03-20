name := "payment-infos"

version := "1.0"

scalaVersion := "2.13.8"

val http4sVersion = "0.23.16"
val doobieVersion = "1.0.0-RC1"
val tapirVersion = "0.19.0-M4"
val fs2Version = "3.1.0"

libraryDependencies ++= Seq(
  // Зависимости для http4sd
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-dsl"          % http4sVersion,
  "org.http4s" %% "http4s-circe"        % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.http4s" %% "http4s-client"       % http4sVersion,

  // Зависимости для Doobie
  "org.tpolecat" %% "doobie-core"     % doobieVersion,
  "org.tpolecat" %% "doobie-hikari"   % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,

  // Зависимости для Tapir
  "com.softwaremill.sttp.tapir" %% "tapir-core"          % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe"    % tapirVersion,

  "co.fs2"        %% "fs2-core"    % fs2Version,
  "org.typelevel" %% "cats-effect" % "3.2.9",

  // Зависимости для логирования
  "org.slf4j"      % "slf4j-api"       % "1.7.32",
  "ch.qos.logback" % "logback-classic" % "1.2.6",

  "com.typesafe" % "config"      % "1.4.1",
  "org.flywaydb" % "flyway-core" % "7.15.0"
)