package tn.esprit.se.pispring.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tn.esprit.se.pispring.entities.ProjectStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
    private Long project_id;
    private String project_name;
    @Temporal(TemporalType.DATE)
    private Date project_startdate;
    @Temporal(TemporalType.DATE)
    private Date project_enddate;
    private String project_description;
    private String project_manager;
    @Enumerated(EnumType.STRING)
    private ProjectStatus projectStatus;
}
