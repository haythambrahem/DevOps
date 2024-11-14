package tn.esprit.se.pispring.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import tn.esprit.se.pispring.entities.Rating.LikeDislike;
import tn.esprit.se.pispring.entities.Rating.Review;

import java.util.Date;
import java.util.List;


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

    private String reference;

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


    @ManyToOne
    @JoinColumn(name = "production_id")
    private Production production;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "product")
    private List<MouvementStock> mouvementsStock;



  public void setBarcode(String s) {
  }
  public Product(Long productId, String reference, String title, String image, String description, Long stock, Float price, Long TVA, ProductType productType, Date createdAt, Cart cart, List<LikeDislike> likeDislikeProducts, List<Review> reviews, List<MouvementStock> mouvementsStock) {
    this.productId = productId;
    this.reference = reference;
    this.title = title;
    this.image = image;
    this.description = description;
    this.stock = stock;
    this.price = price;
    this.TVA = TVA;
    this.productType = productType;
    this.createdAt = createdAt;
    this.cart = cart;
    this.likeDislikeProducts = likeDislikeProducts;
    this.reviews = reviews;
    this.mouvementsStock = mouvementsStock;
  }

  public enum ProductType {
    ELECTRONICS, CLOTHING, FURNITURE,

  }

}

