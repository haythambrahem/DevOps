package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.Rating.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
}