

# Étape Dockerfile : Créer une image Docker légère
# Utilise une image OpenJDK 17 légère basée sur Alpine
FROM alpine
RUN apk add openjdk17

 # Définit le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier JAR généré à partir de l'étape de build
 # Copie le fichier JAR construit à partir de l'étape précédente

COPY --from=build /app/target/*.jar DevOps.jar
# Exposer le port de l'application
EXPOSE 8080  # Indique que le conteneur écoute sur le port 8080

# Définir le point d'entrée pour exécuter l'application Spring Boot
# Définit la commande à exécuter lorsque le conteneur démarre

ENTRYPOINT ["java", "-jar", "DevOps.jar"]
#delivered by ingineering student
# Ajouter un health check
HEALTHCHECK --interval=30s --timeout=10s --retries=3 CMD curl --fail http://localhost:8080/health || exit 1