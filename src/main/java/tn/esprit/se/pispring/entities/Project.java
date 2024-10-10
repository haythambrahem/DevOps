package tn.esprit.se.pispring.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;
    private String project_name;
    @Temporal(TemporalType.DATE)
    private Date project_startdate;
    @Temporal(TemporalType.DATE)
    private Date projectEnddate;
    private String project_description;
    private String projectManager;

    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;
    @OneToMany(cascade = CascadeType.ALL, mappedBy="project")
    @JsonIgnore
    private Set<Task> Tasks;
    @OneToOne
    @JsonIgnore
    private Budget budget;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public Date getProject_startdate() {
        return project_startdate;
    }

    public void setProject_startdate(Date project_startdate) {
        this.project_startdate = project_startdate;
    }

    public Date getProjectEnddate() {
        return projectEnddate;
    }

    public void setProjectEnddate(Date project_enddate) {
        this.projectEnddate = project_enddate;
    }

    public String getProject_description() {
        return project_description;
    }

    public void setProject_description(String project_description) {
        this.project_description = project_description;
    }

    public String getProject_manager() {
        return projectManager;
    }

    public void setProject_manager(String project_manager) {
        this.projectManager = project_manager;
    }

    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(ProjectStatus projectStatus) {
        this.projectStatus = projectStatus;
    }

    public Set<Task> getTasks() {
        return Tasks;
    }

    public void setTasks(Set<Task> tasks) {
        Tasks = tasks;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }
}
