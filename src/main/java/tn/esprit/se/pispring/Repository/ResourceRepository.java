package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.se.pispring.entities.Resources;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resources, Long>{
    @Query("SELECT COUNT(r) FROM Resources r")
    Long countTotalResources();

    @Query("SELECT SUM(r.resourcesCost) FROM Resources r")
    Double sumTotalCost();

    @Query("SELECT t.project.project_name, SUM(r.resourcesCost) " +
            "FROM Resources r " +
            "JOIN r.tasks t " +
            "GROUP BY t.project.project_name")
    List<Object[]> sumCostByProject();


    @Query("SELECT r.resourceType, COUNT(r) FROM Resources r GROUP BY r.resourceType")
    List<Object[]> countResourcesByType();

    @Query("SELECT MONTH(r.createdAt), COUNT(r) FROM Resources r WHERE YEAR(r.createdAt) = :year GROUP BY MONTH(r.createdAt)")
    List<Object[]> countResourcesAddedByMonth(@Param("year") int year);

    @Query("SELECT MONTH(r.updatedAt), COUNT(r) FROM Resources r WHERE YEAR(r.updatedAt) = :year GROUP BY MONTH(r.updatedAt)")
    List<Object[]> countResourcesModifiedByMonth(@Param("year") int year);

    @Query("SELECT  r " +
            "FROM Resources r " +
            "JOIN r.tasks t " +
            "WHERE t.project.projectId = :projectId")
    List<Resources> getResourcesForProject(@Param("projectId") Long projectId);

    List<Resources> findAllByTasksProjectProjectId(Long projectId);

}
