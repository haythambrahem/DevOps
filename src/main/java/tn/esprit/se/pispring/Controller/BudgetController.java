package tn.esprit.se.pispring.Controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Service.IBudegetService;
import tn.esprit.se.pispring.Service.IProjectService;
import tn.esprit.se.pispring.Service.ITaskService;
import tn.esprit.se.pispring.entities.Budget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tn.esprit.se.pispring.entities.Project;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/Budget")
public class BudgetController {
    IBudegetService iBudegetService;
    ITaskService iTaskService;
    IProjectService iProjectService;


    private static final Logger logger = LoggerFactory.getLogger(BudgetController.class);
    @Autowired
    public BudgetController(ITaskService iTaskService, IBudegetService iBudegetService) {
        this.iTaskService = iTaskService;
        this.iBudegetService = iBudegetService;
    }
    @GetMapping("/projects/by-budget")
    public List<Project> getProjectsByBudgetId(@RequestParam Long budgetId) {
        return iProjectService.getProjectsByBudgetId(budgetId);
    }

    @GetMapping("/associated-projects")
    public ResponseEntity<List<Project>> getProjectsAssociatedWithCloseBudgets() {
        List<Project> associatedProjects = iBudegetService.getProjectsAssociatedWithCloseBudgets();
        return ResponseEntity.ok().body(associatedProjects);
    }
    @GetMapping("/variance-histogram")
    public ResponseEntity<Map<String, Integer>> getBudgetVarianceHistogram() {
        Map<String, Integer> histogram = iBudegetService.calculateBudgetVarianceHistogram();
        return new ResponseEntity<>(histogram, HttpStatus.OK);
    }

    @PostMapping("/addBudget")
    public Budget addBudget(@RequestBody Budget budget) {
        Budget budget1 = iBudegetService.addBudget(budget);
        return budget1;
    }

    @PutMapping("/update-budget")
    public Budget updateBudget(@RequestBody Budget p) {
        Budget budget = iBudegetService.updateBudget(p);
        return budget;
    }

    @DeleteMapping("/remove-budget/{budget_id}")
    public void removeBudget(@PathVariable("budget_id") Long budget_id) {
        iBudegetService.deleteBudget(budget_id);
    }

    @GetMapping("/retrieve-all-budget")
    public List<Budget> getAllBudget() {
        List<Budget> budgets = iBudegetService.getAllBudget();
        return budgets;
    }

    @GetMapping("/retrieve-budget/{budget_id}")
    public Budget retrievebudget(@PathVariable("budget_id") Long budget_id) {
        return iBudegetService.getBudget(budget_id);
    }

    @PostMapping("/assign/{budget_id}/toProject/{projectId}")
    public Budget assignBudgetToProject(@PathVariable Long budget_id, @PathVariable Long projectId) {
        return iBudegetService.assignBudgetToProject(budget_id, projectId);
    }


    @PutMapping("/{projectId}/updateBudgetReel")
    public ResponseEntity<String> updateBudgetReel(@PathVariable Long projectId) {
        try {
            iTaskService.updateBudgetReel(projectId);
            logger.info("Budget réel mis à jour avec succès pour le projet {}", projectId);
            return new ResponseEntity<>("Budget réel mis à jour avec succès pour le projet " + projectId, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du budget réel pour le projet {}", projectId, e);
            return new ResponseEntity<>("Erreur lors de la mise à jour du budget réel pour le projet " + projectId, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/calculateBudgetVariance/{budgetId}")
    public ResponseEntity<Double> calculateBudgetVariance(@PathVariable Long budgetId) {
        try {
            Double budgetVariance = iBudegetService.calculateBudgetVariance(budgetId);
            if (budgetVariance != null) {
                logger.info("Variance du budget calculée avec succès pour le budget {}", budgetId);
                return new ResponseEntity<>(budgetVariance, HttpStatus.OK);
            } else {
                logger.error("Budget introuvable pour le calcul de la variance: {}", budgetId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Erreur lors du calcul de la variance du budget {}", budgetId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-budget-variance/{budgetId}")
    public ResponseEntity<Budget> updateBudgetVariance(@PathVariable Long budgetId) {
        try {
            Budget budgetToUpdate = iBudegetService.getBudget(budgetId);
            if (budgetToUpdate != null) {
                Double budgetVariance = iBudegetService.calculateBudgetVariance(budgetId);
                if (budgetVariance != null) {
                    budgetToUpdate.setBudget_variance(budgetVariance);
                    Budget updatedBudget = iBudegetService.updateBudget(budgetToUpdate);
                    logger.info("Variance du budget mise à jour avec succès pour le budget {}", budgetId);
                    return new ResponseEntity<>(updatedBudget, HttpStatus.OK);
                } else {
                    logger.error("Impossible de calculer la variance du budget pour le budget {}", budgetId);
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                logger.error("Budget introuvable pour la mise à jour de la variance: {}", budgetId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la variance du budget {}", budgetId, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/withoutBudgets")
    public ResponseEntity<List<Project>> getProjectsWithoutBudgets() {
        List<Project> projects =iBudegetService.getProjectsWithoutBudgets();
        if (projects.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/amountGreaterThan/{amount}")
    public ResponseEntity<List<Budget>> getBudgetByAmountGreaterThan(@PathVariable Double amount) {
        List<Budget> budgets = iBudegetService.getBudgetByAmountGreaterThan(amount);
        if (budgets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(budgets);
    }





}