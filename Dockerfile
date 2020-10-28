FROM maven:3-jdk-11-openj9 as build

WORKDIR /

ADD . .
RUN mvn clean package

FROM openjdk:11

ARG JAR_FILE=target/*.jar
COPY --from=build ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]
ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://mongo:27017/game_statistic", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
