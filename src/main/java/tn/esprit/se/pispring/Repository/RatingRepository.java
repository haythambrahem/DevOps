package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.se.pispring.entities.LRating;

public interface RatingRepository extends JpaRepository<LRating,Long> {

}
