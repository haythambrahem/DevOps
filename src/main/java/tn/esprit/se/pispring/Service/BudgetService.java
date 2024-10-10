package tn.esprit.se.pispring.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.se.pispring.Repository.BudgetRepository;
import tn.esprit.se.pispring.Repository.ProjectRepository;
import tn.esprit.se.pispring.entities.Budget;
import tn.esprit.se.pispring.entities.Project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class BudgetService implements IBudegetService{
    BudgetRepository budgetRepository;
    ProjectRepository projectRepository;

    @Override
    public Budget addBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public Budget updateBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    @Override
    public Budget getBudget(Long budget_id) {
        return budgetRepository.findById(budget_id).get();
    }

    @Override
    public List<Budget> getAllBudget() {
        return budgetRepository.findAll();
    }
    @Override
    public void deleteBudget(Long budget_id) {
        budgetRepository.deleteById(budget_id);
    }


    @Override
    public Budget assignBudgetToProject(Long budget_id, Long projectId) {
        Budget budget = budgetRepository.findById(budget_id).orElse(null);
        Project project = projectRepository.findById(projectId).orElse(null);

        if (budget != null && project != null) {
            project.setBudget(budget);
            projectRepository.save(project);
            return budget;
        } else {
            return null;
        }
    }



    @Override
    public Budget getBudgetByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        return project != null ? project.getBudget() : null;
    }
    @Override
    public Double calculateBudgetVariance(Long budgetId) {
        Budget budget = budgetRepository.findById(budgetId).orElse(null);
        if (budget != null) {
            return budget.getBudgetReel() - budget.getBudget_amount();
        }
        return null;
    }
    @Override
    public Map<String, Integer> calculateBudgetVarianceHistogram() {
        List<Budget> budgets = budgetRepository.findAll();

        // Filtrer les budgets avec un budget réel non nul et un montant de budget non nul
        List<Budget> validBudgets = budgets.stream()
                .filter(budget -> budget.getBudgetReel() != null && budget.getBudget_amount() != null)
                .collect(Collectors.toList());

        // Calculer les écarts budgétaires en pourcentage et construire l'histogramme
        Map<String, Integer> histogram = new HashMap<>();
        int intervalSize = 5; // Taille de l'intervalle en pourcentage

        for (Budget budget : validBudgets) {
            double budgetAmount = budget.getBudget_amount();
            double budgetReel = budget.getBudgetReel();

            // Gérer les cas où le montant du budget ou le budget réel est nul
            if (budgetAmount == 0 || budgetReel == 0) {
                // Ignorer ce budget et passer au suivant
                continue;
            }

            double variance = ((budgetReel - budgetAmount) / budgetAmount) * 100;

            String interval = getInterval(variance, intervalSize);
            histogram.put(interval, histogram.getOrDefault(interval, 0) + 1);
        }

        return histogram;
    }

    private String getInterval(double variance, int intervalSize) {
        int intervalLowerBound = ((int) (variance / intervalSize)) * intervalSize;
        int intervalUpperBound = intervalLowerBound + intervalSize;
        return "[" + intervalLowerBound + "%, " + intervalUpperBound + "%]";
}

    @Scheduled(fixedRate = 60000) // Exécute toutes les 5 minutes (300 000 millisecondes)
    public void updateBudgetVariances() {
        // Obtenez tous les budgets
        List<Budget> budgets = budgetRepository.findAll();
        for (Budget budget : budgets) {
            if (budget.getBudgetReel() != null) {
                try {
                    Double budgetVariance = calculateBudgetVariance(budget.getBudget_id());
                    if (budgetVariance != null) {
                        budget.setBudget_variance(budgetVariance);
                        updateBudget(budget);
                    }
                } catch (Exception e) {
                    // Gérer les erreurs
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public List<Budget> getBudgetByAmountGreaterThan(Double amount) {
        return budgetRepository.findByBudget_amountGreaterThan(amount);
    }

    @Override
    public List<Project> getProjectsWithoutBudgets() {
        return projectRepository.findProjectsWithoutBudgets();
    }
    @Override

    public List<Project> getProjectsAssociatedWithCloseBudgets() {
        List<Budget> budgets = budgetRepository.findAll(); // Obtenez tous les budgets

        // Filtrer les budgets dont la différence entre le montant et le budget réel est inférieure ou égale à 10%
        List<Budget> closeBudgets = budgets.stream()
                .filter(budget -> {
                    double diff = Math.abs(budget.getBudget_amount() - budget.getBudgetReel());
                    return diff <= (budget.getBudget_amount() * 0.1); // 10% du montant
                })
                .collect(Collectors.toList());

        List<Project> associatedProjects = new ArrayList<>();

        // Pour chaque budget proche, récupérez les projets associés
        for (Budget budget : closeBudgets) {
            List<Project> projects = projectRepository.findProjectsByBudgetId(budget.getBudget_id());
            associatedProjects.addAll(projects);
        }

        return associatedProjects;
    }
}
