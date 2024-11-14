package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.esprit.se.pispring.entities.Task;
import tn.esprit.se.pispring.entities.TaskStatus;


import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByProjectProjectId(Long projectId);
    List<Task> findTasksByProjectProjectId(Long projectId);
    List<Task> findTasksByProjectProjectIdAndTaskStatus(Long projectId, TaskStatus taskStatus);
    List<Task> findByTaskEnddateBeforeAndTaskStatus(Date endDate, TaskStatus status);
    List<Task> findByTaskEnddateBeforeAndTaskStatusNot(Date currentDate, TaskStatus status);



}
