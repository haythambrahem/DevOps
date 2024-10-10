package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.Project;
import tn.esprit.se.pispring.entities.ProjectStatus;
import tn.esprit.se.pispring.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.se.pispring.entities.Project;
import tn.esprit.se.pispring.entities.ProjectStatus;
import tn.esprit.se.pispring.entities.Resources;
import tn.esprit.se.pispring.entities.Task;

import java.util.Date;
import java.util.List;
@Repository

public interface ProjectRepository extends JpaRepository<Project,Long> {
   // Project delete(Long projectId);
   List<Project> findByProjectEnddateBeforeAndProjectStatusNot(Date projectEnddate, ProjectStatus projectStatus);
   List<Project> findByProjectEnddateBeforeAndProjectStatusNotAndProjectEnddateBefore(Date enddate, ProjectStatus status, Date estimatedEnddate);
   List<Project> findByProjectStatus(ProjectStatus status);

   List<Project> findByProjectManager(String potfolioManager);

   int countByProjectManager(User currentUser);

   int countByProjectManagerAndProjectStatus(User currentUser, ProjectStatus projectStatus);
   @Query("SELECT  r " +
           "FROM Resources r " +
           "JOIN r.tasks t " +
           "WHERE t.project.projectId = :projectId")
   List<Resources> getResourcesForProject(@Param("projectId") Long projectId);

   @Query("SELECT t FROM Task t LEFT JOIN FETCH t.users WHERE t.project.projectId = :projectId")
   List<Task> findTasksWithUsersByProjectId(@Param("projectId") Long projectId);

   @Query(value = "SELECT * FROM Project p WHERE NOT EXISTS (SELECT 1 FROM Budget b WHERE b.project_id = p.project_id)", nativeQuery = true)
   List<Project> findProjectsWithoutBudgets();

   @Query("SELECT p FROM Project p WHERE p.projectId IN (SELECT b.project.projectId FROM Budget b WHERE b.budget_id = :budgetId)")
   List<Project> findProjectsByBudgetId(@Param("budgetId") Long budgetId);
   @Query("SELECT DISTINCT pr FROM Project pr JOIN pr.Tasks t JOIN t.users u WHERE u.email = :email")
   List<Project> findByUserEmail(@Param("email") String email);
}
