#FROM ubuntu:latest
#LABEL authors="Vishal Prajapathi"
#
#ENTRYPOINT ["top", "-b"]

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/house-rental-app-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

CMD [ "java", "-jar", "app.jar" ]