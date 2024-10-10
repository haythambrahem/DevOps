package tn.esprit.se.pispring.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Leav {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long leaveId;

    @Temporal(TemporalType.DATE)
    private Date leaveStartdate;

    @Temporal(TemporalType.DATE)
    private Date leaveEnddate;

    @Enumerated(EnumType.STRING)
    private LeaveType LeaveType;

    @Enumerated(EnumType.STRING)
    private LeaveStatus LeaveStatus;

    private String reason;

    private boolean leaveApproved;

    @Temporal(TemporalType.DATE)
    private Date requestDate;

    private String leaveApproverName;

    private String comments;

    private Integer leaveDaysLeft; // Number of leave days left for the employee



    @ManyToOne
    @JsonIgnore
    User user;

}
