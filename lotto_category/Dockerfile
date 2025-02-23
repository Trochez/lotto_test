# Usar OpenJDK 23 como base
FROM openjdk:23-jdk-slim AS builder

# Instalar curl
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*
    
# Instalar Maven
RUN apt update && apt install -y maven

# Configurar directorio de trabajo
WORKDIR /app

# Copiar archivos de Maven y código fuente
COPY pom.xml .
COPY src ./src

# Construir el JAR de la aplicación
RUN mvn clean package -DskipTests

# Segunda etapa: Imagen final sin Maven
FROM openjdk:23-jdk-slim

WORKDIR /app

# Copiar el JAR generado desde la fase de construcción
COPY --from=builder /app/target/lotto_category-0.0.1-SNAPSHOT.jar app.jar

# Definir el punto de entrada
ENTRYPOINT ["java", "-jar", "app.jar"]
