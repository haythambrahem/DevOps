# Étape 1 : Construire l'application Spring Boot à l'aide de Maven
 # Utilise l'image Maven avec OpenJDK 17 comme environnement de construction
FROM maven:3.9.4-openjdk-17 AS build
# Définit le répertoire de travail dans le conteneur
WORKDIR /app

# Copier les fichiers du projet Maven
# Copie le fichier pom.xml dans le conteneur

COPY pom.xml .
 # Copie le répertoire source du projet dans le conteneur
COPY src ./src

# Construire l'application Spring Boot
# Exécute la commande Maven pour construire le projet, en sautant les tests
RUN mvn clean package -DskipTests

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