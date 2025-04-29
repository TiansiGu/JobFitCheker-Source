# Stage 1: Build the app using Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory inside the container 
WORKDIR /JobFitChecker/JobFitCheckerApp

# Copy all project files
COPY pom.xml .
COPY src ./src

# Build the jar, skip running tests
RUN mvn clean install -DskipTests


# Stage 2: Run the app, 
# We don't want Maven, source code in the final image → only the final .jar.

# Use a new official lightweight OpenJDK image
FROM openjdk:24-jdk-slim

# Set the working directory inside the container for the second stage
WORKDIR /JobFitChecker/JobFitCheckerApp

# Copy the jar from the previous stage
COPY --from=build /JobFitChecker/JobFitCheckerApp/target/*.jar app.jar

# Expose port, same as the port inside the container 
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]