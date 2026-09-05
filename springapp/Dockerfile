# Build the Spring Boot application with Java 17
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -DskipTests clean package

# Run the application with a lightweight Java 17 runtime
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/springapp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
