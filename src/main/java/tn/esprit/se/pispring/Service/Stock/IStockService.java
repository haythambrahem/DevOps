package tn.esprit.se.pispring.Service.Stock;

import org.springframework.transaction.annotation.Transactional;
import tn.esprit.se.pispring.entities.MouvementStock;
import tn.esprit.se.pispring.entities.TypeMouvement;

import java.util.List;

public interface IStockService {
   // MouvementStock addMvt(MouvementStock mouvementStock);

    MouvementStock updateMvt(MouvementStock mouvementStock);

    List<MouvementStock> getAllMouvements();

    MouvementStock getMouvementById(Long mvtId);

    void deleteMouvement(Long mvtId);

    List<MouvementStock> getMovementsByType(TypeMouvement type);

    @Transactional
    Long calculateCurrentStock(Long productId);

    double calculateAverageStockValue();


    //Stock moyen = (Stock initial + Stock final)/2
    double calculateAverageStockValueForPeriods(Long productId);

    //Consommation moyenne=	Somme totale des consommations / nombre total des consommations
   // double calculateAverageConsumption();

    double calculateAverageConsumptionForProduct(Long productId);





}
