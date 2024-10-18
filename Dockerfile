# Étape 1 : Construire l'application Spring Boot à l'aide de Maven
 # Utilise l'image Maven avec OpenJDK 17 comme environnement de construction
FROM maven:3.9.9-eclipse-temurin-17 AS build
# Définit le répertoire de travail dans le conteneur
WORKDIR /app
# Copier uniquement le fichier pom.xml pour permettre le cache des dépendances Maven
COPY pom.xml ./
RUN mvn dependency:go-offline -B


# Construire l'application Spring Boot
# Copier tout le code source et construire l'application (Jenkins gère déjà la compilation Maven, donc ce sera une étape de sécurité)
COPY src ./src
RUN mvn clean package -DskipTests
# Renommer le fichier JAR généré pour qu'il soit nommé 'DevOps.jar'
RUN mv target/*.jar target/DevOps.jar
#--------step2 with alpine os---------------


# Étape 2 : Créer une image Docker légère
# Utilise une image OpenJDK 17 légère basée sur Alpine
FROM openjdk:17-jdk-alpine
 # Définit le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier JAR généré à partir de l'étape de build
 # Copie le fichier JAR construit à partir de l'étape précédente

COPY --from=build /app/target/DevOps.jar DevOps.jar
# Exposer le port de l'application
EXPOSE 8080  # Indique que le conteneur écoute sur le port 8080

# Définir le point d'entrée pour exécuter l'application Spring Boot
# Définit la commande à exécuter lorsque le conteneur démarre

ENTRYPOINT ["java", "-jar", "DevOps.jar"]