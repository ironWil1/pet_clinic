FROM maven:3.8-jdk-11-slim AS build
EXPOSE 8080
COPY web/target/web-0.0.1-SNAPSHOT.jar web/web-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","web/web-0.0.1-SNAPSHOT.jar"]