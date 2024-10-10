package tn.esprit.se.pispring.entities;

import javax.persistence.*;
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
public class CustomerTracking {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long history_id;
    private String client_firstname ;
    private String client_lastname ;
    private String history_description;
    @ElementCollection
    @Temporal(TemporalType.DATE)
    private List<Date> meeting_dates;
    @Temporal(TemporalType.DATE)
    private Date date_last_meet ;
    private String date_last_meeet ;
    private String meet_duration ;
    private String meet_subject ;
    private String meet_participant ;
    private String meet_objective ;
    private String next_step ;
    private Integer nb_meeting ;
    @OneToOne(mappedBy="customertracking",cascade = CascadeType.ALL)
    private User user;

}
