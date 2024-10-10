package tn.esprit.se.pispring.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MouvementStock implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long mvtId;
    @Temporal(TemporalType.DATE)
    private Date dateMouvement;
    @Enumerated(EnumType.STRING)
    private TypeMouvement typeMouvement;
    private double quantite; // Autorise les valeurs négatives

//    @JsonBackReference
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Product produit;
@JsonBackReference
@ManyToOne
@JoinColumn(name = "product_id")
private Product product;
}
