# Stage 1: Build the Spring Boot application
FROM eclipse-temurin:21-jre AS build
WORKDIR /app

# includes Maven wrapper scripts and config
COPY .mvn .mvn
# Unix script
COPY mvnw mvnw
# Windows script (safe to copy)
COPY mvnw.cmd mvnw.cmd
COPY pom.xml pom.xml
COPY src ./src

RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final Docker image
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]