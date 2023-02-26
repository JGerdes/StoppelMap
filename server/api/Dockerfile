FROM --platform=linux/arm64 gradle:8.0.0-jdk17 AS build
COPY . /home/src
WORKDIR /home/src
RUN ./gradlew :server:api:shadowJar --debug --no-daemon

FROM openjdk:17
EXPOSE 8181:8181
RUN mkdir /app
COPY --from=build /home/src/server/api/build/libs/*.jar /app/stoppelmap-api-all.jar
ENTRYPOINT ["java","-jar","/app/stoppelmap-api-all.jar"]
