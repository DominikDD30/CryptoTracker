# Etap 1: Budowanie aplikacji
FROM gradle:8.10.2-jdk21 AS build
WORKDIR /app
COPY .. .
RUN gradle clean bootJar

# Etap 2: Tworzenie obrazu uruchomieniowego
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
