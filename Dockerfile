# Используем базовый образ с sbt для сборки
FROM hseeberger/scala-sbt:11.0.12_1.5.5_2.13.6 AS builder

# Копируем файлы сборки проекта
WORKDIR /app
COPY . .

# Собираем проект с использованием sbt assembly
RUN sbt assembly

# Используем другой базовый образ для запуска приложения
FROM openjdk:11

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем JAR-файл сборки
COPY --from=builder /app/target/scala-2.13/payment-infos*.jar /app/payment-infos.jar

# Команда для запуска приложения при старте контейнера
CMD ["java", "-jar", "payment-infos.jar"]
