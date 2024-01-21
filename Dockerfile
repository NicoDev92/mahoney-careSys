FROM amazoncorretto:17-alpine-jdk
COPY target/nursing-app-0.0.1-SNAPSHOT.jar nicode-nursing-app.jar
ENTRYPOINT ["java", "-jar", "nicode-nursing-app.jar"]