package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import tn.esprit.se.pispring.DTO.Response.ProjectResponse;
import tn.esprit.se.pispring.DTO.Response.UserResponse;
import tn.esprit.se.pispring.Repository.ProjectRepository;
import tn.esprit.se.pispring.Repository.UserRepository;
import tn.esprit.se.pispring.entities.Project;

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


        List<User> users = userRepository.findAll();


        for (User user : users) {
            UserResponse userResponse = new UserResponse(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getTelephone(),user.getRoles().stream().map(role1 -> role1.getRoleName().toString()).toList());


            Map<ProjectResponse, Integer> projectStats = new HashMap<>();

            Set<Task> tasks = user.getTasks();

            for (Task task : tasks) {
                Project project = task.getProject();
                ProjectResponse projectResponse = new ProjectResponse(project.getProjectId(), project.getProject_name(),project.getProject_startdate(),project.getProjectEnddate(),project.getProject_description(), project.getProject_manager(),project.getProjectStatus());

                projectStats.put(projectResponse, projectStats.getOrDefault(projectResponse, 0) + 1);
            }

            userTaskCountPerProject.put(userResponse, projectStats);
        }

        return userTaskCountPerProject;
    }
    public Map<String, Integer> getUserCountPerProject() {
        Map<String, Integer> userCountPerProject = new HashMap<>();

        List<User> users = userRepository.findAll();

        for (User user : users) {
            Set<Task> tasks = user.getTasks();

            for (Task task : tasks) {
                Project project = task.getProject();
                String projectName = project.getProject_name();

                userCountPerProject.put(projectName, userCountPerProject.getOrDefault(projectName, 0) + 1);
            }
        }

        return userCountPerProject;
    }


}
