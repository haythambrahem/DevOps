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
public class Prime {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long prime_id;
    private String prime_designation;
    private Float value_amount;
    private String prime_month;
    private Integer prime_year;
    @ManyToOne
    @JsonBackReference
    User user;


}
