package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.se.pispring.entities.Task;
import tn.esprit.se.pispring.entities.TaskStatus;
import tn.esprit.se.pispring.entities.User;

import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByProjectProjectId(Long projectId);
    List<Task> findTasksByProjectProjectId(Long projectId);
    List<Task> findTasksByProjectProjectIdAndTaskStatus(Long projectId, TaskStatus taskStatus);
    List<Task> findByTaskEnddateBeforeAndTaskStatus(Date endDate, TaskStatus status);
    List<Task> findByTaskEnddateBeforeAndTaskStatusNot(Date currentDate, TaskStatus status);

    @Query("SELECT MAX(t.taskEnddate) FROM Task t WHERE t.project.projectId = :projectId")
    Date findLatestTaskEndDateByProjectId(@Param("projectId") Long projectId);

}
