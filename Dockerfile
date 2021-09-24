FROM maven:3.8-jdk-11-slim AS build

WORKDIR /usr/src/app
COPY . /usr/src/app
RUN mvn install -Dmaven.test.skip=true


FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8080
COPY --from=build /usr/src/app/web/target/*.jar petclinic.jar

ENTRYPOINT ["java","-jar","/petclinic.jar"]