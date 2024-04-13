# Use a base image for Java 17 for building the project
FROM openjdk:17-jdk-slim as build

# Set the working directory in Docker
WORKDIR /home/app

# Copy gradlew and grant execution permissions
COPY gradlew /home/app/
COPY gradle /home/app/gradle
RUN chmod +x /home/app/gradlew

# Copy project files
COPY build.gradle settings.gradle /home/app/

# Copy application source
COPY src /home/app/src

# Build your application
RUN ./gradlew build

# Use a slim version for the runtime
FROM openjdk:17-jdk-slim

# Copy the built jar file from the build stage to the slim runtime image
COPY --from=build /home/app/build/libs/*.jar /usr/local/lib/app.jar

# Run your application
ENTRYPOINT ["java", "-jar", "/usr/local/lib/app.jar"]
