package tn.esprit.se.pispring.Service;

import com.twilio.base.Resource;
import tn.esprit.se.pispring.entities.Project;
import tn.esprit.se.pispring.entities.Resources;

import java.util.List;
import java.util.Map;

public interface IResourceService {
    Resources addResource(Resources resource);
    Resources updateResource(Resources resource);
    void deleteResource(Long resourceId);
    Resources getResource(Long resourceId);
    public List<Resources> getAllResource();

    Resources assignResourceToTask(Long resourceId, Long taskId);

    Map<String, Object> getResourceStats();

    List<Resources> getResourcesForProject(Long projectId);

    Double calculateTotalCostForProject(Long projectId);
}
