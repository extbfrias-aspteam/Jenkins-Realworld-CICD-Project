FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/ms-asp-digitalizacion-bloqueo-cuenta-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8187

ENTRYPOINT ["java", "-jar", "app.jar"]
