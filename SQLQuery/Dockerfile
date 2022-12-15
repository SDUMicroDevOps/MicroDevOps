FROM maven:3.8.6-amazoncorretto-11 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean compile assembly:single

FROM amazoncorretto:11
COPY --from=build /home/app/target/SQLQuery-1.0.jar /usr/local/lib/SQLQuery-1.0.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/SQLQuery-1.0.jar"]