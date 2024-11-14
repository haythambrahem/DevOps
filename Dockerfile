FROM openjdk:17-jdk-alpine
EXPOSE 8083
COPY target/spring.jar spring.jar
ENTRYPOINT ["java", "-jar", "spring.jar"]