package tn.esprit.se.pispring.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.IResourceService;
import tn.esprit.se.pispring.entities.Resources;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@AllArgsConstructor
@RequestMapping("/Resource")
public class ResourcesController {
    IResourceService iResourceService;

    @PostMapping("/addResource")
    public ResponseEntity<Resources> addResource(@RequestBody Resources resource) {
        Resources newResource = iResourceService.addResource(resource);
        return new ResponseEntity<>(newResource, HttpStatus.CREATED);
    }
    @PutMapping("/update-Resource")
    public ResponseEntity<Resources> updateResource(@RequestBody Resources resource) {
        Resources updatedResource = iResourceService.updateResource(resource);
        return new ResponseEntity<>(updatedResource, HttpStatus.OK);
    }

    @DeleteMapping("/remove-resources/{resourceId}")
    public ResponseEntity<Void> removeresource(@PathVariable("resourceId") Long resourceId) {
        iResourceService.deleteResource(resourceId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/retrieve-all-Resources")
    public ResponseEntity<List<Resources>> getAllResources() {
        List<Resources> resources = iResourceService.getAllResource();
        return ResponseEntity.ok(resources);
    }
    @GetMapping("/retrieve-Resource/{resourceId}")
    public ResponseEntity<Resources> retrieveResource(@PathVariable("resourceId") Long resourceId) {
        Resources resource = iResourceService.getResource(resourceId);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
    @PostMapping("/{resourceId}/assignTask/{taskId}")
    public ResponseEntity<Resources> assignResourceToTask(@PathVariable Long resourceId, @PathVariable Long taskId) {
        Resources resource = iResourceService.assignResourceToTask(resourceId, taskId);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }




    @GetMapping("/stat")
    public ResponseEntity<Map<String, Object>> getResourceStats() {
        Map<String, Object> stats = iResourceService.getResourceStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/project/{projectId}/resources")
    public ResponseEntity<List<Resources>> getResourcesForProject(@PathVariable Long projectId) {
        List<Resources> resources = iResourceService.getResourcesForProject(projectId);
        if(resources.isEmpty()) {
            return ResponseEntity.noContent().build(); // Aucune ressource trouvée
        } else {
            return ResponseEntity.ok().body(resources); // Renvoie les ressources trouvées
        }
    }
    @GetMapping("/{projectId}/totalCost")
    public ResponseEntity<Double> getTotalCostForProject(@PathVariable Long projectId) {
        Double totalCost = iResourceService.calculateTotalCostForProject(projectId);
        return ResponseEntity.ok(totalCost);
    }
}
