package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.ResourceRepository;
import tn.esprit.se.pispring.Repository.TaskRepository;
import tn.esprit.se.pispring.entities.Resources;
import tn.esprit.se.pispring.entities.Task;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class ResourcesService implements IResourceService {

    private final TaskRepository taskRepository;
    private final ResourceRepository resourceRepository;
    private final ITaskService iTaskService;

    @Override
    public Resources addResource(Resources resource) {
        return resourceRepository.save(resource);
    }

    @Override
    public Resources updateResource(Resources resource) {
        return resourceRepository.save(resource);
    }

    @Override
    public void deleteResource(Long ResourceId) {
        resourceRepository.deleteById(ResourceId);

    }

    @Override
    public Resources getResource(Long resourceId) {
        return resourceRepository.findById(resourceId).get();
    }

    @Override
    public List<Resources> getAllResource() {
        return resourceRepository.findAll();
    }


    @Override
    public Resources assignResourceToTask(Long resourceId, Long taskId) {
        Resources resource = getResource(resourceId);
        Task task = iTaskService.getTask(taskId);

        if (resource != null && task != null) {
            resource.getTasks().add(task);
            task.getResourcess().add(resource);

            // Sauvegarder la ressource et la tâche dans une transaction
            resourceRepository.save(resource);
            taskRepository.save(task);
        } else {
            // Gérer les cas où la ressource ou la tâche n'existe pas
            log.error("Resource or Task not found!");
        }

        return resource;
    }

    @Override
    public Map<String, Object> getResourceStats() {
        Map<String, Object> stats = new HashMap<>();

        // Nombre total de ressources
        Long totalResources = resourceRepository.countTotalResources();
        stats.put("totalResources", totalResources);

        // Coût total des ressources
        Double totalCost = resourceRepository.sumTotalCost();
        stats.put("totalCost", totalCost);

        // Coût des ressources par projet
        List<Object[]> costByProject = resourceRepository.sumCostByProject();
        Map<String, Double> costByProjectMap = new HashMap<>();
        for (Object[] result : costByProject) {
            String projectName = (String) result[0];
            Double projectCost = (Double) result[1];
            costByProjectMap.put(projectName, projectCost);
        }
        stats.put("costByProject", costByProjectMap);

        // Nombre de ressources par type
        List<Object[]> resourcesByType = resourceRepository.countResourcesByType();
        Map<String, Long> resourcesByTypeMap = new HashMap<>();
        for (Object[] result : resourcesByType) {
            resourcesByTypeMap.put(result[0].toString(), (Long) result[1]);
        }
        stats.put("resourcesByType", resourcesByTypeMap);

        // Ressources ajoutées par mois
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        List<Object[]> resourcesAddedByMonth = resourceRepository.countResourcesAddedByMonth(currentYear);
        Map<Integer, Long> resourcesAddedByMonthMap = parseMonthlyStats(resourcesAddedByMonth);
        stats.put("resourcesAddedByMonth", resourcesAddedByMonthMap);

        // Ressources modifiées par mois
        List<Object[]> resourcesModifiedByMonth = resourceRepository.countResourcesModifiedByMonth(currentYear);
        Map<Integer, Long> resourcesModifiedByMonthMap = parseMonthlyStats(resourcesModifiedByMonth);
        stats.put("resourcesModifiedByMonth", resourcesModifiedByMonthMap);

        return stats;
    }

    private Map<Integer, Long> parseMonthlyStats(List<Object[]> results) {
        Map<Integer, Long> stats = new HashMap<>();

        for (Object[] result : results) {
            stats.put((Integer) result[0], (Long) result[1]);
        }

        return stats;
    }


    @Override
    public List<Resources> getResourcesForProject(Long projectId) {
        // Utilisez votre repository pour récupérer les ressources associées à ce projet
        return resourceRepository.findAllByTasksProjectProjectId(projectId);
    }
    @Override
    public Double calculateTotalCostForProject(Long projectId) {
        List<Resources> resources = getResourcesForProject(projectId);
        Double totalCost = 0.0;
        for (Resources resource : resources) {
            totalCost += resource.getResourcesCost();
        }
        return totalCost;
    }
}

