# build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# run stage
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render sets PORT dynamically, but we expose a default for documentation
EXPOSE 9090

# Use exec form to ensure proper signal handling
# Environment variables are automatically passed by Render at runtime
CMD ["java", "-jar", "app.jar"]
