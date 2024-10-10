package tn.esprit.se.pispring.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long budget_id;
    @Column(name = "budget_amount")
    private Double  budget_amount;
   private Double  budgetReel;
    private Double  budget_variance;

    private String dependencies;
    @OneToOne
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;
}
