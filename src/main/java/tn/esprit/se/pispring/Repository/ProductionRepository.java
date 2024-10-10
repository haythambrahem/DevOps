package tn.esprit.se.pispring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.se.pispring.entities.Production;
import tn.esprit.se.pispring.entities.ProductionStatus;

import java.util.Date;
import java.util.List;
@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {
    List<Production> findTop5ByOrderByDefectiveProductsDesc();
    List<Production> findByProductionStatusAndStartDateBeforeAndEndDateAfter(
            ProductionStatus productionStatus, Date endDate, Date startDate);
}