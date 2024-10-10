package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.DTO.Response.ProjectReleaseStatisticsResponse;
import tn.esprit.se.pispring.DTO.Response.ProjectResponse;
import tn.esprit.se.pispring.DTO.Response.UserResponse;
import tn.esprit.se.pispring.Repository.ProjectRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.Project;
import tn.esprit.se.pispring.entities.ProjectStatus;
import tn.esprit.se.pispring.entities.Task;
import tn.esprit.se.pispring.entities.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class UserStatisticsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    public Map<UserResponse, Map<ProjectResponse, Integer>> getUserTaskCountPerProject() {
        Map<UserResponse, Map<ProjectResponse, Integer>> userTaskCountPerProject = new HashMap<>();

        // Retrieve all users
        List<User> users = userRepository.findAll();

        // Iterate over each user
        for (User user : users) {
            UserResponse userResponse = new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getTelephone(),user.getRoles().stream().map(role1 -> role1.getRoleName().toString()).toList());

            // Create a map to hold project statistics for the current user

            Map<ProjectResponse, Integer> projectStats = new HashMap<>();

            // Retrieve tasks for the current user
            Set<Task> tasks = user.getTasks();

            // Iterate over each task
            for (Task task : tasks) {
                Project project = task.getProject();
                ProjectResponse projectResponse = new ProjectResponse(project.getProjectId(), project.getProject_name(),project.getProject_startdate(),project.getProjectEnddate(),project.getProject_description(), project.getProject_manager(),project.getProjectStatus());

                // Increment task count for the project
                projectStats.put(projectResponse, projectStats.getOrDefault(projectResponse, 0) + 1);
            }

            // Add the project statistics to the user map
            userTaskCountPerProject.put(userResponse, projectStats);
        }

        return userTaskCountPerProject;
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    public Map<String, Integer> getUserCountPerProject() {
        Map<String, Integer> userCountPerProject = new HashMap<>();

        // Retrieve all users
        List<User> users = userRepository.findAll();

        // Iterate over each user
        for (User user : users) {
            Set<Task> tasks = user.getTasks();

            // Iterate over each task of the user
            for (Task task : tasks) {
                Project project = task.getProject();
                String projectName = project.getProject_name();

                // Increment user count for the project
                userCountPerProject.put(projectName, userCountPerProject.getOrDefault(projectName, 0) + 1);
            }
        }

        return userCountPerProject;
    }

    public ProjectReleaseStatisticsResponse getProjectReleaseStatisticsForCurrentUser() {
        // Get the current authenticated user
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUsername);

        if (currentUser != null) {
            // Count total projects where the user is assigned as project manager
            int totalProjects = projectRepository.countByProjectManager(currentUser);

            // Count completed projects
            int completedProjects = projectRepository.countByProjectManagerAndProjectStatus(currentUser, ProjectStatus.COMPLETED);

            // Count ongoing projects
            int ongoingProjects = totalProjects - completedProjects;

            return new ProjectReleaseStatisticsResponse(totalProjects, completedProjects, ongoingProjects);
        }

        return null;
    }
}
