FROM openjdk:17-jdk-alpine
EXPOSE 8083
COPY target/maddouri.jar maddouri.jar
ENTRYPOINT ["java", "-jar", "maddouri.jar"]