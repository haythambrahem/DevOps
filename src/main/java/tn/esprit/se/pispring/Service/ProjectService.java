package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.ProjectRepository;
import tn.esprit.se.pispring.Repository.ResourceRepository;
import tn.esprit.se.pispring.Repository.TaskRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.Project;
import tn.esprit.se.pispring.entities.ProjectStatus;
import tn.esprit.se.pispring.entities.Task;
import tn.esprit.se.pispring.entities.User;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ProjectService implements IProjectService{
    ProjectRepository projectRepository;
    TaskRepository taskRepository;
    ResourceRepository resourceRepository;



    @Autowired
    private UserRepository userRepository;



@Override
public List<Project> getProjectsByBudgetId(Long budgetId) {
    return projectRepository.findProjectsByBudgetId(budgetId);
}
    @Override
    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public Project updateProject(Project project) {
        return  projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    @Override
    public Project getProject(Long projectId) {
        return projectRepository.findById(projectId).get();
    }

    @Override
    public List<Project> getAllProject() {
        return projectRepository.findAll();
    }


    @Override

        public List<Project> getDelayedProjects() {
            Date currentDate = new Date();
            ProjectStatus completedStatus = ProjectStatus.COMPLETED;
            return projectRepository.findByProjectEnddateBeforeAndProjectStatusNot(currentDate, completedStatus);
        }





   @Override
   public List<Project> getCompletedProjects() {
       List<Project> completedProjects = projectRepository.findByProjectStatus(ProjectStatus.COMPLETED);
       Date currentDate = new Date();
       completedProjects = completedProjects.stream()
               .filter(project -> project.getProjectEnddate().after(currentDate))
               .collect(Collectors.toList());
       return completedProjects;
   }







    @Override
    public Date findLatestTaskEndDate(Project project) {
        Date latestTaskEndDate = null;
        // Assurez-vous que la méthode getTasks() renvoie la liste des tâches associées au projet
        for (Task task : project.getTasks()) {
            if (latestTaskEndDate == null || task.getTaskEnddate().after(latestTaskEndDate)) {
                latestTaskEndDate = task.getTaskEnddate();
            }
        }
        return latestTaskEndDate;
    }

    @Transactional
    @Override
    public void updateAllProjectEndDates() {
        List<Project> projects = projectRepository.findAll();

        for (Project project : projects) {
            Date latestTaskEndDate = findLatestTaskEndDate(project);
            if (latestTaskEndDate != null && latestTaskEndDate.after(project.getProjectEnddate())) {
                project.setProjectEnddate(latestTaskEndDate);
                projectRepository.save(project);
            }
        }
    }

    @Override
    public double calculateCompletedFuturePercentage() {
        List<Project> completedFutureProjects = getCompletedProjects();
        List<Project> allProjects = getAllProject();
        if (allProjects.isEmpty()) {
            return 0.0;
        } else {
            return ((double) completedFutureProjects.size() / allProjects.size()) * 100;
        }
    }

    @Override
    public List<Project> getProjectsForCurrentUser() {
        // Obtenez l'authentification de l'utilisateur actuellement connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Obtenez le nom d'utilisateur de l'utilisateur actuellement connecté
        String currentUsername = authentication.getName();

        // Recherchez l'utilisateur dans la base de données en utilisant le nom d'utilisateur
        User currentUser = userRepository.findByEmail(currentUsername);

        // Si l'utilisateur existe, récupérez les projets associés à cet utilisateur
        if (currentUser != null) {
            return projectRepository.findByUserEmail(currentUser.getEmail());
        }

        return null;
    }
}
