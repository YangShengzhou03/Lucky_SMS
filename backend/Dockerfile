FROM eclipse-temurin:17-jre-alpine
ENV JAVA_OPTS="-Xms512m -Xmx1024m"
ENV TZ=Asia/Shanghai
WORKDIR /app
COPY backend-1.0-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]