package tn.esprit.se.pispring.entities.Rating;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.User;

import javax.persistence.*;
import java.util.List;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    private String reviewTitle;
    private String reviewText;
    private float rating;

    @ManyToOne
    @JsonIgnore
    User user;
    @ManyToOne
    @JsonIgnore
    Product product;
    @JsonIgnore
    @OneToMany(mappedBy = "review")
    List<LikeDislike> likeDislike;
}
