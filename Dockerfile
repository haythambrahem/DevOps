FROM openjdk:17-jdk-alpine
EXPOSE 8083
COPY target/DevOps.jar DevOps.jar
ENTRYPOINT ["java", "-jar", "DevOps.jar"]