package tn.esprit.se.pispring.entities.Rating;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.User;

import javax.persistence.*;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class LikeDislike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    ProductRating productRating;
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    User user;
    @ManyToOne
    @JsonIgnore
    Product product;
    @ManyToOne
    @JsonIgnore
    Review review;
}
