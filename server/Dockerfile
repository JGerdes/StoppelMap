FROM openjdk:17
EXPOSE 8080:8080
RUN mkdir /app
COPY ./server/build/libs/*.jar /app/stoppelmap-server-all.jar
ENTRYPOINT ["java","-jar","/app/stoppelmap-server-all.jar"]
