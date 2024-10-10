package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.Rating.LikeDislike;
import tn.esprit.se.pispring.entities.Rating.ProductRating;
import tn.esprit.se.pispring.entities.Rating.Review;
import tn.esprit.se.pispring.entities.User;

import java.util.List;

public interface LikeDislikeRepository extends JpaRepository<LikeDislike, Long> {
    LikeDislike findByProductAndUser(Product product, User user);
    LikeDislike findByReviewAndUser(Review review, User user);

    @Modifying
    @Query("DELETE FROM LikeDislike ld WHERE ld.product.productId = :productId")

    void deleteByProductProductId(Long productId);

}