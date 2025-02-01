# Use official Gradle image to build the application
FROM gradle:8.12.1-jdk23 AS build
WORKDIR /app

# Copy Gradle wrapper files properly
COPY gradlew gradlew.bat gradle/wrapper/ ./
RUN chmod +x gradlew  # Ensure execution permission

# Copy Gradle build scripts
COPY build.gradle settings.gradle ./

# Ensure dependencies are resolved before full build
RUN ./gradlew dependencies --no-daemon || true

# Copy the full source code
COPY . .

# Build the application
RUN ./gradlew build --no-daemon --stacktrace --info

# Use OpenJDK to run the application
FROM openjdk:23-jdk
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
