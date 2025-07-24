# Stage 1: Build the Spring Boot application
FROM openjdk:17-jdk-slim AS build
WORKDIR /app

COPY .mvn .mvn            # includes Maven wrapper scripts and config
COPY mvnw mvnw            # Unix script
COPY mvnw.cmd mvnw.cmd    # Windows script (safe to copy)
COPY pom.xml pom.xml
COPY src ./src

RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final Docker image
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]