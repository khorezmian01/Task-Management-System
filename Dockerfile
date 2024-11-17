FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/TSM-0.1.jar tsm.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "tsm.jar"]