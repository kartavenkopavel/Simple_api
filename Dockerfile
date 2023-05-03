FROM openjdk:11

WORKDIR /app

COPY target/simple-api-2.7.10.jar .

CMD ["java", "-jar", "simple-api-2.7.10.jar"]