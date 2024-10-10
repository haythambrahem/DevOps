package tn.esprit.se.pispring.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "RECRUITMENT")
public class Recruitment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long offerId;
    private String postTitle;
    private String description;
    private String requirements;
    private String hiringManager; // Name or identifier of the hiring manager responsible for the recruitment
    private Date requestDate; // Date when the recruitment request was initiated
    private String recruiter; // Name or identifier of the recruiter handling the recruitment process
    private String jobLocation; // Location of the job position
    private double salaryRangeMin; // Minimum salary range for the position
    private double salaryRangeMax; // Maximum salary range for the position
    private int numberOfOpenings; // Number of open positions for recruitment
    private boolean urgent; //
    private int ExperienceRequired; // Number of open positions for recruitment

    @Temporal(TemporalType.DATE)
    private Date recruitmentDate;
    @Enumerated(EnumType.STRING)
    private RecruitmentStatus recruitmentStatus;
    @JsonIgnore
    @ManyToOne
    User user;
    @JsonIgnore
    @OneToMany(mappedBy = "recruitment", cascade = CascadeType.ALL)
    private List<Candidate> candidates;
}
