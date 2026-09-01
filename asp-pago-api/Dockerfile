FROM eclipse-temurin:21.0.3_9-jdk-alpine AS builder
LABEL stage=jdkbuilder

ARG profile
RUN echo "El perfil recibido fue $profile"
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw
#Agregamos la paqueteria dos2unix para darle el formato correcto de linux al archivo mvnw porque parece que da problemas
RUN apk add dos2unix
RUN dos2unix ./mvnw

COPY src ./src
RUN ./mvnw clean package "-Dspring.profiles.active=$profile"

FROM eclipse-temurin:21.0.3_9-jre-alpine
RUN addgroup usergroup; adduser  --ingroup usergroup --disabled-password user
USER user
WORKDIR /app
EXPOSE 8013
COPY --from=builder /app/target/asp-pago-api.jar /app/asp-pago-api.jar
ENTRYPOINT ["java", "-jar","-Xms128M","-Xmx2048M","/app/asp-pago-api.jar" ]
