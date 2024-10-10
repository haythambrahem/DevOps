package tn.esprit.se.pispring.entities;

import javax.persistence.*;
import lombok.*;
import java.util.Date;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Absence {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idAbsence;

    @Temporal(TemporalType.DATE)
    private Date dateAbsence;

    @Temporal(TemporalType.TIME)
    private Date startTime;

    @Temporal(TemporalType.TIME)
    private Date endTime;

    @Enumerated(EnumType.STRING)
    private AbsenceType absenceType;

    private int totalAbsenceDays; // Total absence entitlement for the employee

    private int absenceDaysLeft; // Number of absence days left for the employee

    private boolean halfDay; // Indicates if the absence is for half a day

    private String absenceCategory; // Category of absence (e.g., sick leave, vacation)

    private String attachmentUrl; // URL to any supporting documents related to the absence

    @ManyToOne
    User user;

}
