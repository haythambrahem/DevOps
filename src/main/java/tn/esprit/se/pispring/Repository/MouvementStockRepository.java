package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.MouvementStock;
import tn.esprit.se.pispring.entities.Product;
import tn.esprit.se.pispring.entities.TypeMouvement;

import java.util.Date;
import java.util.List;

@Repository
public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {
    List<MouvementStock> findByTypeMouvement(TypeMouvement type);
    List<MouvementStock> findByTypeMouvementAndProduct(TypeMouvement typeMouvement, Product product);

    List<MouvementStock> findByProduct(Product product);


    List<MouvementStock> findByTypeMouvementAndProductAndDateMouvementBetween(TypeMouvement typeMouvement, Product product, Date startDate, Date endDate);
}