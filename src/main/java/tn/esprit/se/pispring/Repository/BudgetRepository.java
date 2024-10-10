package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.se.pispring.entities.Budget;
import tn.esprit.se.pispring.entities.Project;

import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget,Long> {


    Budget findByProject_ProjectId(Long projectId);



    @Query("SELECT b FROM Budget b WHERE b.budget_amount > :threshold")
    List<Budget> findByBudget_amountGreaterThan(@Param("threshold") Double threshold);
}
