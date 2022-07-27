FROM maven:3.8-jdk-11-slim AS build
EXPOSE 8080
COPY petclinic.jar petclinic.jar

ENTRYPOINT ["java","-jar","/petclinic.jar"]