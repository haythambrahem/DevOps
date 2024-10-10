package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.se.pispring.entities.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String key1, String key2);

    @Query("SELECT p.productType, AVG(p.price) FROM Product p GROUP BY p.productType")
    List<Object[]> calculateAveragePriceByType();

}