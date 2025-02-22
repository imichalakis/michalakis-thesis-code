# Use an official OpenJDK runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built WAR file into the container
COPY target/loanapproval-spring-0.1.0-SNAPSHOT.war app.war

# Expose the port your application will run on
EXPOSE 8080

# Command to run your application
ENTRYPOINT ["java", "-jar", "app.war"]