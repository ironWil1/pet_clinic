FROM adoptopenjdk:11-jre-hotspot
EXPOSE 8080
ARG JAR_FILE=web/target/*.jar
COPY ${JAR_FILE} petclinic.jar
ENTRYPOINT ["java","-jar","/petclinic.jar"]