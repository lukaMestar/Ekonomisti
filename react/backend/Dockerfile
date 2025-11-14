# build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first to leverage Docker layer caching
# This way, Maven dependencies are only downloaded when pom.xml changes
COPY pom.xml .
# Download dependencies (this layer will be cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src
# Build the application (only this step runs when source code changes)
RUN mvn clean package -DskipTests -B

# run stage
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render sets PORT dynamically, but we expose a default for documentation
EXPOSE 9090

# Use exec form to ensure proper signal handling
# Environment variables are automatically passed by Render at runtime
CMD ["java", "-jar", "app.jar"]
