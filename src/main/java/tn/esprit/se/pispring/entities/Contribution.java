package tn.esprit.se.pispring.entities;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

@Builder
@Getter
@Setter
@ToString(exclude = "user")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contribution {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long contribution_id;
    private String contribution_designation;
    private Float contribution_amount;
    private String contribution_month;
    private Integer contribution_year;
    @ManyToOne
    @JsonBackReference
    User user;


}
