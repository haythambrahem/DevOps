FROM openjdk:17-jdk-alpine

EXPOSE 8089

# Step 2: Install curl in the Alpine image
RUN apk add --no-cache curl

# Download the JAR file from Nexus
RUN curl -u admin:admin -o /PiSpring-0.0.1.jar \
    http://192.168.159.129:8083/repository/maven-releases/tn/esprit/se/PiSpring/0.0.1/PiSpring-0.0.1.jar

# Entry command to run the JAR
ENTRYPOINT ["java", "-jar", "/PiSpring-0.0.1.jar"]