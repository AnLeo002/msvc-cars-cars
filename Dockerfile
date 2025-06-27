# Stage 1: Build
FROM eclipse-temurin:21 AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre
WORKDIR /root
COPY --from=build /app/target/Cars-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9191
ENTRYPOINT ["java", "-jar", "app.jar"]