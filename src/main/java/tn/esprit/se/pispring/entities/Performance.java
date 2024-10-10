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
public class Performance {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long performance_id;

    @Temporal(TemporalType.DATE)
    private Date evaluation_date;
    private String comment;
    private Integer score;
    @ManyToOne
    User user;
}
