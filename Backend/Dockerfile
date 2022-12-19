FROM eclipse-temurin:17-jdk-jammy
COPY .mvn/ .mvn
COPY ./mvnw ./pom.xml ./
USER root
RUN apt-get update && apt-get install -y curl
RUN ./mvnw dependency:resolve

COPY src ./src

CMD ["./mvnw", "spring-boot:run"]
