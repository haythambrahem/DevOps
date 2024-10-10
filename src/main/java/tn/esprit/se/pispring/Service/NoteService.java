package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.NoteRepository;
import tn.esprit.se.pispring.Repository.NoteUserRepository;
import tn.esprit.se.pispring.entities.Note;
import tn.esprit.se.pispring.entities.NoteUser;
import tn.esprit.se.pispring.entities.Project;
import tn.esprit.se.pispring.entities.User;

import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class NoteService implements INoteService {


    IProjectService iProjectService;
    ITaskService iTaskService;
    NoteRepository noteRepository;
    NoteUserRepository noteUserRepository;

    @Override
    public void classifyProjects() {
        List<Project> completedProjects = iProjectService.getCompletedProjects();
        log.info("Nombre de projets terminés : {}", completedProjects.size());

        // Créer une map pour stocker les utilisateurs par projet
        Map<Long, List<User>> usersByProject = new HashMap<>();

        for (Project project : completedProjects) {
            Set<User> projectUsersSet = iTaskService.getProjectTeam(project.getProjectId());
            List<User> projectUsers = new ArrayList<>(projectUsersSet);

            // Stocker les utilisateurs dans la map en utilisant l'ID du projet comme clé
            usersByProject.put(project.getProjectId(), projectUsers);
        }

        // Parcourir la map et afficher les utilisateurs par projet
        for (Map.Entry<Long, List<User>> entry : usersByProject.entrySet()) {
            Long projectId = entry.getKey();
            List<User> projectUsers = entry.getValue();

            // Afficher l'ID du projet
            log.info("ID du projet : {}", projectId);

            // Afficher la liste des utilisateurs pour le projet
            for (User user : projectUsers) {
                log.info("Utilisateur : {}", user);
            }
        }
    }

    @Override
    public Map<Long, List<User>> getCompletedProjectsAndUsers() {
        List<Project> completedProjects = iProjectService.getCompletedProjects();
        Map<Long, List<User>> projectUsersMap = new HashMap<>();

        for (Project project : completedProjects) {
            Set<User> projectUsersSet = iTaskService.getProjectTeam(project.getProjectId());
            List<User> projectUsers = new ArrayList<>(projectUsersSet);
            projectUsersMap.put(project.getProjectId(), projectUsers);

            log.info("Projet terminé: {}", project.getProject_name());
            log.info("Utilisateurs associés: {}", projectUsers);
        }

        return projectUsersMap;
    }

    @Override
    public Map<Long, List<User>> getretardProjectsAndUsers() {
        List<Project> completedProjects = iProjectService.getDelayedProjects();
        Map<Long, List<User>> projectUsersMap = new HashMap<>();

        for (Project project : completedProjects) {
            Set<User> projectUsersSet = iTaskService.getProjectTeam(project.getProjectId());
            List<User> projectUsers = new ArrayList<>(projectUsersSet);
            projectUsersMap.put(project.getProjectId(), projectUsers);

            log.info("Projet terminé: {}", project.getProject_name());
            log.info("Utilisateurs associés: {}", projectUsers);
        }

        return projectUsersMap;
    }

    @Override
    public void assignUsersToNote1() {
        Optional<Note> optionalNote = noteRepository.findById(1L);
        List<Project> projectsToAdd = new ArrayList<>();

        if (!optionalNote.isPresent()) {
            log.warn("Note avec ID 1 non trouvée.");
            return;
        }
        List<Project> completedProjects = iProjectService.getCompletedProjects();

        for (Project project : completedProjects) {
            if (!noteUserRepository.existsByProjectId(project.getProjectId())) {
                projectsToAdd.add(project);
            }
        }
        for (Project project : projectsToAdd) {
            Set<User> projectUsersSet = iTaskService.getProjectTeam(project.getProjectId());
            for (User user : projectUsersSet) {
                NoteUser noteUser = new NoteUser();
                noteUser.setNote(optionalNote.get()); // ID de la note 1
                noteUser.setUser(user); // ID de l'utilisateur
                noteUser.setProjectId(project.getProjectId()); // ID du projet
                noteUserRepository.save(noteUser); // Enregistrer dans la table NoteUser
            }
        }
        log.info("Affectation des utilisateurs à la note d'ID 1 réussie.");
    }

    @Override
    public void assignUsersToNote3() {
        Optional<Note> optionalNote = noteRepository.findById(3L);
        List<Project> projectsToAdd = new ArrayList<>();

        if (!optionalNote.isPresent()) {
            log.warn("Note avec ID 3 non trouvée.");
            return;
        }

        List<Project> delayedProjects = iProjectService.getDelayedProjects();
        for (Project project : delayedProjects) {
            if (!noteUserRepository.existsByProjectId(project.getProjectId())) {
                projectsToAdd.add(project);
            }
            for (Project projects : projectsToAdd) {
                Set<User> projectUsersSet = iTaskService.getProjectTeam(projects.getProjectId());

                for (User user : projectUsersSet) {
                    NoteUser noteUser = new NoteUser();
                    noteUser.setNote(optionalNote.get());
                    noteUser.setUser(user); // ID de l'utilisateur
                    noteUser.setProjectId(project.getProjectId()); // ID du projet
                    noteUserRepository.save(noteUser); // Enregistrer dans la table NoteUser
                }
            }
            log.info("Affectation des utilisateurs à la note d'ID 3 réussie.");
        }

    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void scheduleAssignUsersToNote1() {
        assignUsersToNote1();
    }
    @Override
    public Map<User, Long> countUserOccurrencesForNote1() {
        List<Object[]> userOccurrencesList = noteUserRepository.countUserOccurrencesForNote1();

        Map<User, Long> userOccurrencesMap = new HashMap<>();

        for (Object[] result : userOccurrencesList) {
            User user = (User) result[0];
            Long count = (Long) result[1];
            userOccurrencesMap.put(user, count);
        }

        return userOccurrencesMap;
    }
    @Override
    public Map<User, Long> countUserOccurrencesForNote3() {
        List<Object[]> userOccurrencesList = noteUserRepository.countUserOccurrencesForNote3();

        Map<User, Long> userOccurrencesMap = new HashMap<>();

        for (Object[] result : userOccurrencesList) {
            User user = (User) result[0];
            Long count = (Long) result[1];
            userOccurrencesMap.put(user, count);
        }

        return userOccurrencesMap;
    }
    @Override
    public Map<User, Map<String, Long>> countNoteOccurrencesForEachUser() {


        List<User> users = noteUserRepository.findAllUser();

        // Initialiser une carte pour stocker les informations de chaque utilisateur
        Map<User, Map<String, Long>> userNoteOccurrencesMap = new HashMap<>();

        // Pour chaque utilisateur, compter le nombre de notes 1 et 3
        for (User user : users) {
            Long countNote1 = noteUserRepository.countByUserIdAndNoteId(user.getId(), 1L);
            Long countNote3 = noteUserRepository.countByUserIdAndNoteId(user.getId(), 3L);

            // Créer une sous-carte pour stocker le nombre de chaque type de note pour cet utilisateur
            Map<String, Long> noteOccurrencesMap = new HashMap<>();
            noteOccurrencesMap.put("Note 1", countNote1);
            noteOccurrencesMap.put("Note 3", countNote3);

            // Ajouter les informations de l'utilisateur à la carte principale
            userNoteOccurrencesMap.put(user, noteOccurrencesMap);
        }

        return userNoteOccurrencesMap;
    }
}