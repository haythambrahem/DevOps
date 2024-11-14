package tn.esprit.se.pispring.Service.Stock;

import org.springframework.transaction.annotation.Transactional;
import tn.esprit.se.pispring.entities.MouvementStock;
import tn.esprit.se.pispring.entities.TypeMouvement;

import java.util.List;

public interface IStockService {


    MouvementStock updateMvt(MouvementStock mouvementStock);

    List<MouvementStock> getAllMouvements();

    MouvementStock getMouvementById(Long mvtId);

    void deleteMouvement(Long mvtId);

    List<MouvementStock> getMovementsByType(TypeMouvement type);

    @Transactional
    Long calculateCurrentStock(Long productId);

    double calculateAverageStockValue();


    double calculateAverageStockValueForPeriods(Long productId);


    double calculateAverageConsumptionForProduct(Long productId);





}
