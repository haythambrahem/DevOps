package tn.esprit.se.pispring.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import tn.esprit.se.pispring.entities.Rating.LikeDislike;
import tn.esprit.se.pispring.entities.Rating.Review;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long productId;
    //    private Integer quantity;
    private String reference;
  //  private String barcode;
    private String title;

    @Column(columnDefinition = "MEDIUMTEXT")

    private String image;
    private String description;
    private Long stock;
    private Float price;
    private Long TVA;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Temporal(TemporalType.DATE)
    private Date createdAt;




    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    Cart cart;
    @JsonIgnore
    @OneToMany(mappedBy = "product")
    List<LikeDislike> likeDislikeProducts;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "product")
    List<Review> reviews;

    /////////////////////////////////
    // Relation avec Production
    @ManyToOne
    @JoinColumn(name = "production_id")
    private Production production;
    //relation avec MvtStock
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "product")
    private List<MouvementStock> mouvementsStock;


  public void setBarcode(String s) {
  }
}

