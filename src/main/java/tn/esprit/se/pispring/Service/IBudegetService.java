package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.entities.Budget;
import tn.esprit.se.pispring.entities.Project;

import java.util.List;
import java.util.Map;

public interface IBudegetService {
    Budget addBudget(Budget budget);
    Budget updateBudget(Budget budget);
    void deleteBudget(Long budget_id);
    Budget getBudget(Long budget_id);
    public List<Budget> getAllBudget();

    Budget assignBudgetToProject(Long budget_id, Long projectId);
    Budget getBudgetByProjectId(Long projectId);


    Double calculateBudgetVariance(Long budgetId);

    Map<String, Integer> calculateBudgetVarianceHistogram();

    List<Budget> getBudgetByAmountGreaterThan(Double amount);

    List<Project> getProjectsWithoutBudgets();


    List<Project> getProjectsAssociatedWithCloseBudgets();
}
