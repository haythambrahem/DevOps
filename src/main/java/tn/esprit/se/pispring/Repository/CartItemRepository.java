package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.se.pispring.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.product.productId = :productId")
    void deleteByProductId(@Param("productId") Long productId);


}