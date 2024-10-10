package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.entities.Project;
import tn.esprit.se.pispring.entities.Task;
import tn.esprit.se.pispring.entities.TaskStatus;
import tn.esprit.se.pispring.entities.User;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ITaskService {


    Task addTask (Task task);
    Task  updateTask (Task task);
    void   deleteTask (Long task_id);
    Task getTask (Long task_id);
    public List<Task > getAllTask ();

    List<Task> getTasksByProject(Long projectId);

    Task assignTaskToProject(Long projectId, Long task_id);

    Task assignTaskToUser(Long id, Long task_id);

    Set<User> getProjectTeam(Long project_id);

    double calculateTaskBudget(Task task, Set<User> users);

   double calculateProjectBudget(Long projectId);
    List<Task> getCompletedTasksByProject(Long projectId);

    void updateBudgetReel(Long projectId);

    double calculateCompletionPercentage(Long projectId);
    Map<TaskStatus, Long> getTaskStatusDistribution(Long projectId);
    Map<TaskStatus, Double> getTaskStatusPercentage(Long projectId);

    List<Task> findTasksCompletedBeforeEndDate(Date endDate);

    List<Task> findTasksCompletedLate();

    Task assignTaskToResource(Long taskId, Long resourceId);
}
