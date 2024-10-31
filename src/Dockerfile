# Utiliser une image Java 17
FROM openjdk:17-jdk-alpine

# Définir l'argument JAR_FILE avec le nom de votre jar généré
ARG JAR_FILE=target/PiSpring-0.0.1-SNAPSHOT.jar

# Copier le fichier JAR dans l'image Docker
COPY ${JAR_FILE} app.jar

# Exposer le port sur lequel votre application Spring tourne
EXPOSE 8080

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "/app.jar"]
