package tn.esprit.se.pispring.Service;

import tn.esprit.se.pispring.entities.Project;
import tn.esprit.se.pispring.entities.ProjectStatus;
import tn.esprit.se.pispring.entities.User;

import java.util.Date;
import java.util.List;

public interface IProjectService {

    List<Project> getProjectsByBudgetId(Long budgetId);

    Project addProject(Project project);
    Project updateProject(Project project);
    void deleteProject(Long projectId);
    Project getProject(Long projectId);
    public List<Project> getAllProject();
    List<Project> getDelayedProjects();


    List<Project> getCompletedProjects();


    void updateAllProjectEndDates();

    Date findLatestTaskEndDate(Project project);

    double calculateCompletedFuturePercentage();

    List<Project> getProjectsForCurrentUser();
}
