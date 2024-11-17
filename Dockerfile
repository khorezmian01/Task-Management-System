FROM openjdk:17-jre-slim

WORKDIR /app

COPY --from=build /app/target/TSM-0.1.jar /app/tsm.jar

CMD ["java", "-jar", "/app/tsm.jar"]