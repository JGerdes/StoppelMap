FROM --platform=linux/arm64 gradle:8.0.0-jdk17 AS build
COPY . /home/src
WORKDIR /home/src
RUN ./gradlew :server:shadowJar --debug --no-daemon

FROM openjdk:17
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/src/server/build/libs/*.jar /app/stoppelmap-server-all.jar
ENTRYPOINT ["java","-jar","/app/stoppelmap-server-all.jar"]
