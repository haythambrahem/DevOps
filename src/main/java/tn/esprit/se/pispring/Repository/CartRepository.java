package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.se.pispring.entities.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}