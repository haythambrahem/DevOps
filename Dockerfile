FROM openjdk:17-jdk-alpine


EXPOSE 8089


RUN apk add --no-cache curl


RUN curl -u admin:admin -o /PiSpring-0.0.1.jar \
    http://192.168.111.128:8081/repository/maven-releases/tn/esprit/se/PiSpring/0.0.1/PiSpring-0.0.1.jar


ENTRYPOINT ["java", "-jar", "/PiSpring-0.0.1.jar"]
