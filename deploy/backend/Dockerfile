FROM eclipse-temurin:11-jre-alpine
WORKDIR /app
COPY backend-1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
