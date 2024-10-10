package tn.esprit.se.pispring.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.IProjectService;
import tn.esprit.se.pispring.Service.ITaskService;
import tn.esprit.se.pispring.entities.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/Project")
public class

ProjectController {
    @Autowired
    IProjectService iProjectService;
    ITaskService iTaskService;


    @PostMapping("/addProject")
    //@PreAuthorize("hasAnyRole( 'ROLE_HR_ADMIN', 'ROLE_CRM_ADMIN', 'ROLE_PROJECT_ADMIN', 'ROLE_PRODUCT_ADMIN')")
    public Project addProject(@RequestBody Project p) {
        Project project = iProjectService.addProject(p);
        return project;
    }

    @PutMapping("/update-project")
    public Project updateProject(@RequestBody Project p) {
        Project project = iProjectService.updateProject(p);
        return project;
    }

    @DeleteMapping("/remove-project/{projectId}")
    public void removeproject(@PathVariable("projectId") Long projectId) {
        iProjectService.deleteProject(projectId);
    }

    @GetMapping("/retrieve-all-project")
    public List<Project> getProjects() {
        return iProjectService.getAllProject();
    }

    @GetMapping("/retrieve-project/{projectId}")
    public Project retrieveproject(@PathVariable("projectId") Long projectId) {
        return iProjectService.getProject(projectId);
    }


    /* @GetMapping("/projects/{projectId}/team")
     public ResponseEntity<Set<User>> getProjectTeam(@PathVariable Long projectId) {
         Set<User> projectTeam = iTaskService.getProjectTeam(projectId);
         return ResponseEntity.ok(projectTeam);
     }*/
    @GetMapping("/projects/{projectId}/team")
    public ResponseEntity<Set<User>> getProjectTeam(@PathVariable Long projectId) {
        return ResponseEntity.ok(iTaskService.getProjectTeam(projectId));
    }
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Task>> getTasksByProject(@PathVariable Long projectId) {
        List<Task> tasks = iTaskService.getTasksByProject(projectId);
        if (tasks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    @GetMapping("/{projectId}/budget")
    public ResponseEntity<Double> calculateProjectBudget(@PathVariable Long projectId) {
        double budget = iTaskService.calculateProjectBudget(projectId);
        return ResponseEntity.ok(budget);
    }

    @GetMapping("/{projectId}/completed-tasks")
    public List<Task> getCompletedTasksByProject(@PathVariable Long projectId) {
        return iTaskService.getCompletedTasksByProject(projectId);
    }

    @GetMapping("/project/{projectId}/completionPercentage")
    public ResponseEntity<Double> calculateCompletionPercentage(@PathVariable Long projectId) {
        double completionPercentage = iTaskService.calculateCompletionPercentage(projectId);
        return ResponseEntity.ok(completionPercentage);
    }

    @GetMapping("/{projectId}/status-distribution")
    public ResponseEntity<Map<TaskStatus, Long>> getTaskStatusDistribution(@PathVariable Long projectId) {
        Map<TaskStatus, Long> taskStatusDistribution = iTaskService.getTaskStatusDistribution(projectId);
        return ResponseEntity.ok(taskStatusDistribution);
    }

    @GetMapping("/{projectId}/status-percentage")
    public ResponseEntity<Map<TaskStatus, Double>> getTaskStatusPercentage(@PathVariable Long projectId) {
        Map<TaskStatus, Double> taskStatusPercentage = iTaskService.getTaskStatusPercentage(projectId);
        return ResponseEntity.ok(taskStatusPercentage);
    }

    @GetMapping("/delayed")
    public ResponseEntity<List<Project>> getDelayedProjects() {
        List<Project> delayedProjects = iProjectService.getDelayedProjects();
        return ResponseEntity.ok(delayedProjects);
    }
    @GetMapping("/completed-future")
    public ResponseEntity<List<Project>> getCompletedFutureProjects() {
        List<Project> completedFutureProjects = iProjectService.getCompletedProjects();
        return ResponseEntity.ok(completedFutureProjects);
    }
////////////MODIFIER LE ENDdATE SELON LE TASKDATE
    @PutMapping("/updateEndDates")
    public ResponseEntity<String> updateProjectEndDates() {
        try {
            iProjectService.updateAllProjectEndDates();
            return ResponseEntity.ok("Les dates de fin des projets ont été mises à jour avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur s'est produite lors de la mise à jour des dates de fin des projets.");
        }
    }
    @GetMapping("/current-user")
    public List<Project> getProjectsForCurrentUser() {
        return iProjectService.getProjectsForCurrentUser();
    }
    @GetMapping("/completed-future-percentage")
    public ResponseEntity<Double> calculateCompletedFuturePercentage() {
        double percentage = iProjectService.calculateCompletedFuturePercentage();
        return ResponseEntity.ok(percentage);
    }
}
