package tn.esprit.se.pispring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Production {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long productionId;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Temporal(TemporalType.DATE)
    private Date endDate;
    private int productionStoppage;


    private int totalProducts;
    private int defectiveProducts;


    private double laborCost;
    private double rawMaterialCost;
    private double machineMaintenanceCost;


    @OneToMany(mappedBy = "production", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> products;

    @Enumerated(EnumType.STRING)
    private ProductionStatus productionStatus;

}
