package tn.esprit.se.pispring.entities;


import lombok.*;

import javax.persistence.*;

import java.util.Date;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "meeting")
public class Meeting {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long meetId;
    @Temporal(TemporalType.DATE)
    private Date meettdate;
    private Long userId ;

    private String heure;
    private Long dureeReunion;
    @Enumerated(EnumType.STRING)
    private FirstMeet first;

    @Enumerated(EnumType.STRING)
    private MeetStatus meetStatus;

    @ManyToOne
    Consultant consultant;

}
