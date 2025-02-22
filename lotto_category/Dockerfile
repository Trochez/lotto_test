FROM openjdk:23-jdk-slim
WORKDIR /app
COPY target/lotto_category-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
