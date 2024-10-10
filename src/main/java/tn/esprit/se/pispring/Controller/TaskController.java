package tn.esprit.se.pispring.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.ITaskService;
import tn.esprit.se.pispring.entities.Project;
import tn.esprit.se.pispring.entities.Task;
import tn.esprit.se.pispring.entities.User;

import java.util.Date;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/Task")
public class TaskController {
    @Autowired
    ITaskService iTaskService;

    @PostMapping("/addTask")
    public Task addTask(@RequestBody Task p) {
        Task task = iTaskService.addTask(p);
        return task;
    }

    @PutMapping("/update-task")
    public Task updateTask(@RequestBody Task p) {
        Task task = iTaskService.addTask(p);
        return task;
    }
    @DeleteMapping("/remove-task/{task_id}")
    public void removetask(@PathVariable("task_id") Long task_id) {
        iTaskService.deleteTask(task_id);
    }
    @GetMapping("/retrieve-all-task")
    public List<Task> getTasks() {
        List<Task> tasks = iTaskService.getAllTask();
        return tasks;
    }

    @GetMapping("/retrieve-task/{task_id}")
    public Task retrievetask(@PathVariable("task_id") Long task_id) {
        return iTaskService.getTask(task_id);
    }

    @PostMapping("/{task_id}/assign-to-project/{projectId}")
    public ResponseEntity<Task> assignTaskToProject(@PathVariable Long task_id, @PathVariable Long projectId) {
        Task assignedTask = iTaskService.assignTaskToProject(task_id, projectId);
        if (assignedTask != null) {
            return ResponseEntity.ok(assignedTask);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{task_id}/assign-to-user/{id}")
    public ResponseEntity<Task> assignTaskToUser(@PathVariable Long task_id, @PathVariable Long id) {
        Task assignedTask = iTaskService.assignTaskToUser(task_id, id);
        if (assignedTask != null) {
            return ResponseEntity.ok(assignedTask);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{taskId}/calculate-budget")
    public ResponseEntity<Double> calculateTaskBudget(@PathVariable Long taskId) {
        Task task = iTaskService.getTask(taskId);
        if (task != null) {
            Set<User> users = task.getUsers();
            double taskBudget = iTaskService.calculateTaskBudget(task, users);
            return ResponseEntity.ok(taskBudget);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint pour trouver les tâches complétées avant une certaine date
    @GetMapping("/completed-before-enddate")
    public ResponseEntity<List<Task>> findTasksCompletedBeforeEndDate(@RequestParam Date endDate) {
        List<Task> tasks = iTaskService.findTasksCompletedBeforeEndDate(endDate);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Endpoint pour trouver les tâches complétées en retard
    @GetMapping("/completed-late")
    public ResponseEntity<List<Task>> findTasksCompletedLate() {
        List<Task> tasks = iTaskService.findTasksCompletedLate();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }
    @PostMapping("/{taskId}/assignResource/{resourceId}")
    public ResponseEntity<Task> assignTaskToResource(@PathVariable Long taskId, @PathVariable Long resourceId) {
        Task task = iTaskService.assignTaskToResource(taskId, resourceId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}