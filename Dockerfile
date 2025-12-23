FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /build

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline -B || true

COPY src src

RUN ./mvnw clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /build/target/*.jar app.jar

EXPOSE 8096

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=deploy"]