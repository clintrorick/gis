FROM gradle:7.2-jdk11 AS build
EXPOSE 8080
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build -x test --no-daemon

FROM openjdk:11.0.9-jre-slim

COPY --from=build /home/gradle/src/build/libs/ /app/

ENTRYPOINT ["java","-jar","/app/gis-0.0.1-SNAPSHOT.jar"]
