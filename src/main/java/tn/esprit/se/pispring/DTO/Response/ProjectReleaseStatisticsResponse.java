package tn.esprit.se.pispring.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProjectReleaseStatisticsResponse {
    private int totalProjects;
    private int completedProjects;
    private int ongoingProjects;
}
