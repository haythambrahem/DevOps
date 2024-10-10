package tn.esprit.se.pispring.Service.Review;

import tn.esprit.se.pispring.entities.Rating.Review;

import javax.transaction.Transactional;
import java.util.List;

public interface IReviewServices {
    @Transactional
    void userLikesProduct(Long productId, Long userId);

 //   @Transactional
    //void userDislikesProduct(Long productId, Long userId);

    @Transactional
    void userDislikesProduct(Long productId, Long userId);

    int numberOfLikes(Long productId);

    int numberOfDisikes(Long productId);

    @Transactional
    Review addReviewToProduct(Long userId, Long productId, Review review);

    @Transactional //okk
    void deleteReviewsByProductId(Long productId);

    List<Review> getReviewsByProductId(Long productId);
}
