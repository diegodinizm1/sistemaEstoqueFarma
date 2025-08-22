
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN mvn -B -DskipTests clean package


FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/estoqueFarma-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]