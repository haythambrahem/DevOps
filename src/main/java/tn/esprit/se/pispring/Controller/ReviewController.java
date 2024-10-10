package tn.esprit.se.pispring.Controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.se.pispring.Repository.ProductRepository;
import tn.esprit.se.pispring.Repository.ReviewRepository;
import tn.esprit.se.pispring.Service.Review.ReviewServices;
import tn.esprit.se.pispring.entities.Rating.Review;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/review")
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewController {
    ReviewServices reviewServices;
    ProductRepository productRepository;
    ReviewRepository reviewRepository;

    @PostMapping("/like/{userId}/{idprod}")
    public void likeProduct(@PathVariable("idprod") Long productId, @PathVariable("userId") Long userId) {
        reviewServices.userLikesProduct(productId, userId);
    }

    @PostMapping("/dislike/{userId}/{idprod}")
    public void dislikeProduct(@PathVariable("idprod") Long productId, @PathVariable("userId") Long userId) {
        reviewServices.userDislikesProduct(productId, userId);
    }

    @GetMapping("/nblike/{idprod}")
    public int numberOfLikes(@PathVariable("idprod") Long productId) {
        return reviewServices.numberOfLikes(productId);
    }

    @GetMapping("/nbdislike/{idprod}")
    public int numberOfDislikes(@PathVariable("idprod") Long productId) {
        return reviewServices.numberOfDisikes(productId);
    }

    //REVIEWW
    @PostMapping("/reviewtoproduct/{idUser}/{idprod}")
    public Review addReviewToProduct(@PathVariable("idUser") Long idUser, @PathVariable("idprod") Long productId, @RequestBody Review review) {
        return reviewServices.addReviewToProduct(idUser, productId, review);
    }

    @DeleteMapping("/reviews/{productId}")
    public ResponseEntity<String> deleteReviewByProductId(@PathVariable Long productId) {
        try {
            reviewServices.deleteReviewsByProductId(productId);
            return ResponseEntity.ok("Review deleted successfully for product ID: " + productId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete review for product ID: " + productId);
        }
    }


    @GetMapping("/{productId}")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {
        List<Review> reviews = reviewServices.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

}
