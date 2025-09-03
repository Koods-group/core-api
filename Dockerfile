# Build stage
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom files
COPY pom.xml .
COPY common/pom.xml ./common/
COPY auth/pom.xml ./auth/
COPY resource/pom.xml ./resource/
COPY core/pom.xml ./core/

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY . .

# Build the application (skip tests for faster build)
RUN mvn package -DskipTests -Pprod

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/core/target/*.jar app.jar

# Expose port (adjust if your app uses different port)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]