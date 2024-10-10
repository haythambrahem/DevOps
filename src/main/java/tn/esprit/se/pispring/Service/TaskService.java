package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.*;
import tn.esprit.se.pispring.entities.*;

import javax.transaction.Transactional;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Slf4j
@AllArgsConstructor
public class TaskService implements ITaskService{
    private final TaskRepository taskRepository;
    private final ResourceRepository resourcesRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    BudgetRepository budgetRepository;
    PayrollRepository payrollRepository;


    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    @Override
    public Task addTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long task_id) {
        taskRepository.deleteById(task_id);
    }

    @Override
    public Task getTask(Long task_id) {
        return taskRepository.findById(task_id).get();
    }

    @Override
    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }
    @Override
    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectProjectId(projectId);
    }
    @Override
    public Task assignTaskToProject(Long task_id , Long projectId) {
        Task task = taskRepository.findById(task_id).orElse(null);
        Project project = projectRepository.findById(projectId).orElse(null);
        if (task != null && project != null) {
            task.setProject(project);
            return taskRepository.save(task);
        }
        return null;
    }
    @Transactional
    @Override
    public Task assignTaskToUser(Long id, Long task_id) {
        Task task = taskRepository.findById(task_id).orElse(null);
        User user = userRepository.findById(id).orElse(null);
        if (task != null && user != null) {
            task.getUsers().add(user);
            return taskRepository.save(task);
        }
        return null;
    }
    @Override
    @Transactional
    public Set<User> getProjectTeam(Long projectId) {
        Set<User> users= new HashSet<>();
        taskRepository.findTasksByProjectProjectId(projectId).forEach(task->users.addAll(task.getUsers()));
        return users;
    }



   @Override
    public List<Task> getCompletedTasksByProject(Long projectId) {
        return taskRepository.findTasksByProjectProjectIdAndTaskStatus(projectId, TaskStatus.COMPLETED);
    }


    @Override
    public double calculateTaskBudget(Task task, Set<User> users) {
        double taskBudget = 0.0;

        for (User user : users) {
            User userWithSalary = userRepository.findById(user.getId()).orElse(null);
            if (userWithSalary != null) {
                taskBudget += userWithSalary.getSalaire();
            }
        }
        return taskBudget;
    }

    @Override
    public double calculateProjectBudget(Long projectId) {
        List<Task> tasks = projectRepository.findTasksWithUsersByProjectId(projectId);
        if (tasks.isEmpty()) {
            return 0.0;
        }

        int projectDurationInMonths = calculateDurationInMonths(tasks.get(0).getProject().getProject_startdate(), tasks.get(0).getProject().getProjectEnddate());

        double teamSalaryCostPerMonth = 0.0;
        for (Task task : tasks) {
            for (User user : task.getUsers()) {
                List<Payroll> payrolls = payrollRepository.findByUserId(user.getId());
                for (Payroll payroll : payrolls) {
                    teamSalaryCostPerMonth += payroll.getNet_salary();
                }
            }
        }

        double totalBudget = teamSalaryCostPerMonth * projectDurationInMonths;

        List<Resources> resources = projectRepository.getResourcesForProject(projectId);
        Double totalCost = 0.0;
        for (Resources resource : resources) {
            totalCost += resource.getResourcesCost();
        }
        totalBudget += totalCost;

        return totalBudget;
    }

    @Scheduled(fixedRate = 60000) // Exécuter à minuit (00:00:00) le premier jour de chaque mois
    public void updateRealBudgets() {
        List<Budget> budgets = budgetRepository.findAll(); // Récupérer tous les budgets
        for (Budget budget : budgets) {
            Long projectId = budget.getProject().getProjectId();
              updateBudgetReel(projectId);
        }
    }
    @Override
    public void updateBudgetReel(Long projectId) {
        try {
            logger.debug("Début de la mise à jour du budget réel pour le projet {}", projectId);

            Double budgetReel = calculateProjectBudget(projectId);
            logger.debug("Budget réel calculé pour le projet {}: {}", projectId, budgetReel);

            Budget budget = budgetRepository.findByProject_ProjectId(projectId);
            if (budget != null) {
                logger.debug("Budget trouvé pour le projet {}", projectId);
                budget.setBudgetReel(budgetReel);
                budgetRepository.save(budget);
                logger.info("Budget réel mis à jour avec succès pour le projet {}", projectId);
            } else {
                logger.error("Budget introuvable pour le projet {}", projectId);
                throw new RuntimeException("Budget introuvable pour le projet " + projectId);
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du budget réel pour le projet {}", projectId, e);
            throw new RuntimeException("Erreur lors de la mise à jour du budget réel pour le projet " + projectId, e);
        }
    }



    private int calculateDurationInMonths(Date startDate, Date endDate) {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startDate);
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endDate);

        int diffYear = endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH);

        return diffMonth;
    }


    @Override
    public double calculateCompletionPercentage(Long projectId) {
        List<Task> allTasks = taskRepository.findTasksByProjectProjectId(projectId);
        int totalTasks = allTasks.size();
        List<Task> completedTasks = taskRepository.findTasksByProjectProjectIdAndTaskStatus(projectId, TaskStatus.COMPLETED);
        int completedCount = completedTasks.size();
        if (totalTasks == 0) {
            return 0.0;
        } else {
            return ((double) completedCount / totalTasks) * 100;
        }
    }

    @Override
    public Map<TaskStatus, Long> getTaskStatusDistribution(Long projectId) {
        List<Task> tasks = taskRepository.findTasksByProjectProjectId(projectId);
        Map<TaskStatus, Long> taskStatusDistribution = new HashMap<>();
        for (TaskStatus status : TaskStatus.values()) {
            long count = tasks.stream().filter(task -> task.getTaskStatus() == status).count();
            taskStatusDistribution.put(status, count);
        }
        return taskStatusDistribution;
    }
    @Override
    public Map<TaskStatus, Double> getTaskStatusPercentage(Long projectId) {
        List<Task> tasks = taskRepository.findTasksByProjectProjectId(projectId);
        Map<TaskStatus, Double> taskStatusPercentage = new HashMap<>();
        int totalTasks = tasks.size();

        for (TaskStatus status : TaskStatus.values()) {
            long count = tasks.stream().filter(task -> task.getTaskStatus() == status).count();
            double percentage = (count / (double) totalTasks) * 100;
            taskStatusPercentage.put(status, percentage);
        }

        return taskStatusPercentage;
    }

    @Override
    public List<Task> findTasksCompletedBeforeEndDate(Date endDate) {
        return taskRepository.findByTaskEnddateBeforeAndTaskStatus(endDate, TaskStatus.COMPLETED);
    }
    @Override
    public List<Task> findTasksCompletedLate() {
        Date currentDate = new Date();
        return taskRepository.findByTaskEnddateBeforeAndTaskStatusNot(currentDate, TaskStatus.COMPLETED);
    }


    @Override
    public Task assignTaskToResource(Long taskId, Long resourceId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        Resources resource = resourcesRepository.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("Resource not found with id: " + resourceId));

        task.getResourcess().add(resource);
          resource.getTasks().add(task);

        taskRepository.save(task);
        resourcesRepository.save(resource);

        return task;
    }

}