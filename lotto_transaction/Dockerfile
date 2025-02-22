# Usa Maven para construir la aplicación
FROM maven:3.8.6-openjdk-23 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Usa OpenJDK para ejecutar el `.jar`
FROM openjdk:23-jdk-slim
WORKDIR /app
COPY target/lotto_transaction-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
