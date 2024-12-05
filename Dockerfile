# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY ./EmergencyAlertApplication.jar /app/EmergencyAlertApplication.jar

# Expose the port your application will run on
EXPOSE 8080

# Set the entry point to run your Spring Boot application
ENTRYPOINT ["java", "-jar", "EmergencyAlertApplication.jar"]
